package com.chrissyx.jfix.modules;

import com.chrissyx.jfix.JFix;
import com.chrissyx.jfix.common.error.JFixError;
import com.chrissyx.jfix.common.error.JFixFtpAuthError;
import com.chrissyx.jfix.gui.MainView;
import com.chrissyx.jfix.gui.OptionsView;
import com.chrissyx.jfix.gui.impl.MainFrame;
import com.chrissyx.jfix.gui.impl.OptionsDialog;
import com.chrissyx.jfix.modules.util.CryptUtils;
import com.chrissyx.jfix.modules.util.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.slf4j.LoggerFactory;

/**
 * Creates GUIs and handles their events.
 *
 * @author Chrissyx
 */
public final class GuiController
{
    /**
     * Singleton instance of this class.
     */
    private static GuiController guiController = new GuiController();

    /**
     * The main frame to handle.
     */
    private MainView mainView;

    /**
     * The options frame to handle.
     */
    private OptionsView optionsView;

    /**
     * Current local working directory for file browsing.
     */
    private File cwdLocal;

    /**
     * Current remote working directory for file browsing.
     */
    private String cwdRemote;

    /**
     * Creates and shows main frame.
     */
    private GuiController()
    {
        LoggerFactory.getLogger(GuiController.class).debug("Setting system L&F...");
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(final Exception e)
        {
            LoggerFactory.getLogger(GuiController.class).warn("Cannot set system L&F!", e);
        }
        LoggerFactory.getLogger(GuiController.class).debug("Creating main frame...");
        this.mainView = new MainFrame();
        this.mainView.setLocalFileList(FileUtils.getFileListing(this.cwdLocal = new File(ConfigController.getInstance().getCfgVal("localStartDir")), true));
        this.mainView.showFrame();
        this.appendLogEntry(LangController.getInstance().getString("jfix_version_x_ready", JFix.VERSION));
        this.appendLogEntry("© 2011 by Chrissyx");
        this.appendLogEntry("http://www.chrissyx.de(.vu)/");
        this.appendLogEntry("http://www.chrissyx.com/");
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return Instance of this class
     */
    public static GuiController getInstance()
    {
        return GuiController.guiController;
    }

    /**
     * Displays given log message in console text area.
     *
     * @param logMessage Message to log
     */
    public void appendLogEntry(final String logMessage)
    {
        this.mainView.appendLogEntry(logMessage);
    }

    /**
     * Changes file list of selected local directory.
     *
     * @param filename Name of local directory
     */
    public void onChangeLocalDir(final String filename)
    {
        LoggerFactory.getLogger(GuiController.class).debug("Browsing locally to '{}' from '{}'...", filename, this.cwdLocal);
        try
        {
            final File newLocation = filename.contentEquals("..") ? this.cwdLocal.getParentFile() : new File(this.cwdLocal.getCanonicalPath().concat(File.separator).concat(filename));
            if(newLocation.isDirectory())
                this.cwdLocal = newLocation;
        }
        catch(final IOException e)
        {
            new JFixError(e).error(GuiController.class);
        }
        this.mainView.setLocalFileList(FileUtils.getFileListing(this.cwdLocal, true));
    }

    /**
     * Changes file list of selected remote directory via {@link FtpController}.
     *
     * @param filename Name of remote directory
     */
    public void onChangeRemoteDir(final String filename)
    {
        LoggerFactory.getLogger(GuiController.class).debug("Browsing remotely to '{}' from '{}'...", filename, this.cwdRemote);
        if(!FtpController.getInstance().isConnected())
            return;
        final String oldRemoteDir = this.cwdRemote; //Back up current dir in case of error (file selected, ...)
        if(!filename.contentEquals("."))
            this.cwdRemote = filename.contentEquals("..") ? this.getRemoteParentDir() : this.cwdRemote.concat(filename).concat("/");
        try
        {
            FtpController.getInstance().changeDir(this.cwdRemote);
            this.mainView.setRemoteFileList(FtpController.getInstance().getFileList());
        }
        catch(final JFixError e)
        {
            this.cwdRemote = oldRemoteDir;
            e.error(FtpController.class);
        }
    }

    /**
     * Returns the parent remote working directory from the current one.
     *
     * @return Remote working directory path down to parent of current one
     */
    private String getRemoteParentDir()
    {
        if(this.cwdRemote.length() == 1)
            return this.cwdRemote;
        final StringBuilder stringBuilder = new StringBuilder();
        final String[] remoteDirs = this.cwdRemote.split("/");
        for(int i = 0; i < remoteDirs.length - 1; i++)
            stringBuilder.append(remoteDirs[i]).append("/");
        return stringBuilder.toString();
    }

    /**
     * Closes all frames and exits application.
     */
    public void onClose()
    {
        LoggerFactory.getLogger(GuiController.class).info("Exiting JFix...");
        this.onDisconnect();
        this.mainView.closeFrame();
        System.exit(0);
    }

    /**
     * Connects to FTP server via {@link FtpController} and changes to preset remote directory.
     *
     * @return State of connect / change dir operation
     */
    public boolean onConnect()
    {
        this.mainView.clearLogConsole();
        try
        {
            FtpController.getInstance().connect();
            FtpController.getInstance().changeDir(this.cwdRemote = ConfigController.getInstance().getCfgVal("remoteStartDir"));
            this.mainView.setRemoteFileList(FtpController.getInstance().getFileList());
            return true;
        }
        catch(final JFixFtpAuthError e)
        {
            e.warn(FtpController.class, false);
            this.showWarningDialog(LangController.getInstance().getString("text_login_failed"), LangController.getInstance().getString("title_login_failed"));
        }
        catch(final JFixError e)
        {
            e.error(FtpController.class);
        }
        this.onDisconnect();
        return false;
    }

    /**
     * Disconnects (if connected) from FTP server via {@link FtpController}.
     *
     * @return State of disconnect operation
     */
    public boolean onDisconnect()
    {
        if(FtpController.getInstance().isConnected())
            try
            {
                FtpController.getInstance().disconnect();
                return true;
            }
            catch(final JFixError e)
            {
                e.error(FtpController.class);
            }
        return false;
    }

    /**
     * Starts synchronizing remote filetimes with local ones via {@link FtpController}.
     */
    public void onFixFiletimes()
    {
        if(FtpController.getInstance().isConnected())
            try
            {
                FtpController.getInstance().fixFiletimes(this.cwdLocal);
            }
            catch(final JFixError e)
            {
                e.error(FtpController.class);
            }
        else
            this.showWarningDialog(LangController.getInstance().getString("text_connect_first"), LangController.getInstance().getString("title_connect_first"));
    }

    /**
     * Checks and saves settings to file.
     */
    public void onSaveOptions()
    {
        if(!this.optionsView.getOptionRemoteStartDir().endsWith("/"))
        {
            this.showWarningDialog(LangController.getInstance().getString("text_trailing_slash_needed"), LangController.getInstance().getString("title_trailing_slash_needed"));
            return;
        }
        ConfigController.getInstance().setCfgVal("host", this.optionsView.getOptionHost());
        ConfigController.getInstance().setCfgVal("port", this.optionsView.getOptionPort());
        ConfigController.getInstance().setCfgVal("user", this.optionsView.getOptionUser());
        ConfigController.getInstance().setCfgVal("pass", CryptUtils.encode(this.optionsView.getOptionPass()));
        ConfigController.getInstance().setCfgVal("language", this.optionsView.getOptionLanguage());
        ConfigController.getInstance().setCfgVal("localStartDir", this.optionsView.getOptionLocalStartDir());
        ConfigController.getInstance().setCfgVal("remoteStartDir", this.optionsView.getOptionRemoteStartDir());
        ConfigController.getInstance().setCfgVal("plugIn", this.optionsView.getOptionPlugIn());
        ConfigController.getInstance().setCfgVal("recursive", Boolean.toString(this.optionsView.getOptionRecursive()));
        try
        {
            ConfigController.getInstance().save();
            this.optionsView.closeFrame();
        }
        catch(final JFixError e)
        {
            e.error(ConfigController.class);
        }
    }

    /**
     * Shows the options frame.
     */
    public void onShowOptions()
    {
        if(this.optionsView == null)
        {
            LoggerFactory.getLogger(GuiController.class).debug("Creating options frame...");
            this.optionsView = new OptionsDialog((JFrame) this.mainView, true);
        }
//        ((JFrame) this.optionsView).addWindowListener(new java.awt.event.WindowAdapter()
//        {
//            @Override
//            public void windowClosing(final java.awt.event.WindowEvent e)
//            {
//                System.exit(0);
//            }
//        });
        this.optionsView.showFrame();
    }

    /**
     * Displays given message in an error dialog.
     *
     * @param message Message text to display
     */
    public void showErrorDialog(final String message)
    {
        this.showErrorDialog(message, LangController.getInstance().getString("error"));
    }

    /**
     * Displays given message and title in an error dialog.
     *
     * @param message Message text to display
     * @param title Title of error dialog
     */
    private void showErrorDialog(final String message, final String title)
    {
        JOptionPane.showMessageDialog((JFrame) this.mainView, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays given message in a warning dialog.
     *
     * @param message Message text to display
     */
    public void showWarningDialog(final String message)
    {
        this.showWarningDialog(message, LangController.getInstance().getString("warning"));
    }

    /**
     * Displays given message and title in a warning dialog.
     *
     * @param message Message text to display
     * @param title Title of warning dialog
     */
    public void showWarningDialog(final String message, final String title)
    {
        JOptionPane.showMessageDialog((JFrame) this.mainView, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
