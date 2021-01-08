package com.chrissyx.jfix.modules;

import com.chrissyx.jfix.common.error.JFixError;
import com.chrissyx.jfix.common.error.JFixFtpAuthError;
import com.chrissyx.jfix.modules.util.CryptUtils;
import com.chrissyx.jfix.plugins.FiletimeFixer;
import com.enterprisedt.net.ftp.EventListener;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FileTransferClient;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.LoggerFactory;

/**
 * Manages FTP access.
 *
 * @author Chrissyx
 * @see <a href="http://www.enterprisedt.com/products/edtftpj/overview.html" target="_blank">edtFTPj/Free Homepage</a>
 */
public final class FtpController implements Observer
{
    /**
     * Singleton instance of this class.
     */
    private static FtpController ftpController = new FtpController();

    /**
     * The FTP client being worked with.
     */
    private FileTransferClient ftpClient;

    /**
     * Instance of filetime fixing plug-in to use.
     */
    private FiletimeFixer filetimeFixer;

    /**
     * Comparator to list remote directories before files.
     */
    private Comparator<FTPFile> ftpFileComparator = new Comparator<FTPFile>()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(final FTPFile file1, final FTPFile file2)
        {
            return (file1.isDir() && file2.isDir()) || (!file1.isDir() && !file2.isDir()) ? 0
                    : (file1.isDir() && !file2.isDir() ? -1 : 1);
        }
    };

    /**
     * Fix filetimes recursively.
     */
    private boolean recursive;

    /**
     * Initializes the FTP client module.
     */
    private FtpController()
    {
        ConfigController.getInstance().addObserver(this);
        LoggerFactory.getLogger(FtpController.class).debug("Setting up FTP client...");
        this.ftpClient = new FileTransferClient();
        this.update(null, null);
        this.ftpClient.setEventListener(new EventListener()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void commandSent(final String connId, final String cmd)
            {
                LoggerFactory.getLogger(EventListener.class).trace("<Connection ID: {}> Command '{}' sent", connId, cmd);
                GuiController.getInstance().appendLogEntry(cmd);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void replyReceived(final String connId, final String reply)
            {
                LoggerFactory.getLogger(EventListener.class).trace("<Connection ID: {}> Reply '{}' received", connId, reply);
                GuiController.getInstance().appendLogEntry(reply);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void bytesTransferred(final String connId, final String remoteFilename, final long bytes)
            {
                LoggerFactory.getLogger(EventListener.class).trace("<Connection ID: {}> Transferred {} bytes for file '{}'", new Object[]
                        {
                            connId, bytes, remoteFilename
                        });
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void downloadStarted(final String connId, final String remoteFilename)
            {
                LoggerFactory.getLogger(EventListener.class).trace("<Connection ID: {}> Started download for file '{}'...", connId, remoteFilename);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void downloadCompleted(final String connId, final String remoteFilename)
            {
                LoggerFactory.getLogger(EventListener.class).trace("<Connection ID: {}> Finished download for file '{}'", connId, remoteFilename);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void uploadStarted(final String connId, final String remoteFilename)
            {
                LoggerFactory.getLogger(EventListener.class).trace("<Connection ID: {}> Started upload for file '{}'...", connId, remoteFilename);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void uploadCompleted(final String connId, final String remoteFilename)
            {
                LoggerFactory.getLogger(EventListener.class).trace("<Connection ID: {}> Finished upload for file '{}'", connId, remoteFilename);
            }
        });
        //http://www.enterprisedt.com/products/edtftpj/examples/howto/monitor_transfers_and_commands/MonitorTransfersCommands.java.html
        // the transfer notify interval must be greater than buffer
        this.ftpClient.getAdvancedSettings().setTransferBufferSize(500);
        this.ftpClient.getAdvancedSettings().setTransferNotifyInterval(1000);
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return Instance of this class
     */
    public static FtpController getInstance()
    {
        return FtpController.ftpController;
    }

    /**
     * Changes current remote working directory.
     *
     * @param filename Directory to change to
     * @throws JFixError If changing remote folder failed
     */
    public void changeDir(final String filename) throws JFixError
    {
        try
        {
            this.ftpClient.changeDirectory(filename);
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't change remote directory to '" + filename + "'!", e);
        }
        catch(final IOException e)
        {
            throw new JFixError(e);
        }
    }

    /**
     * Connects to FTP host.
     *
     * @throws JFixError If connecting failed
     */
    public void connect() throws JFixError
    {
        LoggerFactory.getLogger(FtpController.class).debug("Connecting to server...");
        try
        {
            this.ftpClient.connect();
        }
        catch(final FTPException e)
        {
            if(e.getReplyCode() == 530)
                throw new JFixFtpAuthError("Login rejected from server!", e);
            else
                throw new JFixError("Can't connect to FTP host!", e);
        }
        catch(final IOException e)
        {
            throw new JFixError(e);
        }
        LoggerFactory.getLogger(FtpController.class).info("Connected to {}!", this.ftpClient.getRemoteHost());
    }

    /**
     * Disconnects from FTP host.
     *
     * @throws JFixError If disconnecting failed
     */
    public void disconnect() throws JFixError
    {
        LoggerFactory.getLogger(FtpController.class).debug("Disconnecting from server...");
        try
        {
            this.ftpClient.disconnect();
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't disconnect from FTP host!", e);
        }
        catch(final IOException e)
        {
            throw new JFixError(e);
        }
        LoggerFactory.getLogger(FtpController.class).info("Disconnected from {}!", this.ftpClient.getRemoteHost());
    }

    /**
     * Fixes filetimes of files in stated local dir with the ones in current remote working directory.
     *
     * @param localDir Local directory with files and folders to get filetimes from
     */
    public void fixFiletimes(final File localDir) throws JFixError
    {
        LoggerFactory.getLogger(FtpController.class).info("Fixing filetimes for '{}'...", localDir.getName());
        for(final File curFile : localDir.listFiles())
            if(this.recursive && curFile.isDirectory())
            {
                try
                {
                    this.changeDir(curFile.getName());
                }
                catch(final JFixError e)
                {
                    e.warn(FtpController.class, true);
                    continue;
                }
                this.fixFiletimes(curFile);
                this.changeDir("../");
            }
            else
                try
                {
                    this.ftpClient.executeCommand(this.filetimeFixer.getCommand(curFile.getName(), curFile.lastModified()));
                }
                catch(final FTPException e)
                {
                    throw new JFixError("Can't execute command!", e);
                }
                catch(final IOException e)
                {
                    throw new JFixError(e);
                }
        LoggerFactory.getLogger(FtpController.class).info("Filetimes fixed!");
    }

    /**
     * Returns sorted file list of current remote working directory.
     *
     * @return Remote file list
     * @throws JFixError If getting remote file list failed
     */
    public String[] getFileList() throws JFixError
    {
        try
        {
            //Get original file list and sort it
            final FTPFile[] fileList = this.ftpClient.directoryList();
            Arrays.sort(fileList, this.ftpFileComparator);
            //Convert to name list only
            final LinkedList<String> nameList = new LinkedList<String>();
            for(final FTPFile curFile : fileList)
                nameList.add(curFile.getName());
            //Add relative paths
            if(!nameList.contains(".."))
                nameList.addFirst("..");
            if(!nameList.contains("."))
                nameList.addFirst(".");
            return nameList.toArray(new String[0]);
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't get remote directory file list!", e);
        }
        catch(final ParseException e)
        {
            throw new JFixError(e);
        }
        catch(final IOException e)
        {
            throw new JFixError(e);
        }
    }

    /**
     * Returns if a FTP connection is currently active.
     *
     * @return Connection state
     */
    public boolean isConnected()
    {
        return this.ftpClient.isConnected();
    }

    /**
     * Sets host name or IP address of FTP server.
     *
     * @param remoteHost Name or IP address
     * @throws JFixError If name or IP address cannot be set
     */
    public void setHost(final String remoteHost) throws JFixError
    {
        try
        {
            this.ftpClient.setRemoteHost(remoteHost);
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't set host address '" + remoteHost + "'!", e);
        }
    }

    /**
     * Sets the password to authenticate with the FTP server.
     *
     * @param password Password to use
     * @throws JFixError If password cannot be set
     */
    public void setPassword(final String password) throws JFixError
    {
        try
        {
            this.ftpClient.setPassword(password);
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't set password!", e);
        }
    }

    /**
     * Sets port number for connection.
     *
     * @param remotePort Port to use
     * @throws JFixError If port number cannot be set
     */
    public void setPort(final int remotePort) throws JFixError
    {
        try
        {
            this.ftpClient.setRemotePort(remotePort);
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't set port number to " + remotePort + "!", e);
        }
    }

    /**
     * Sets the user name to authenticate with the FTP server.
     *
     * @param userName User name to use
     * @throws JFixError If user name cannot be set
     */
    public void setUserName(final String userName) throws JFixError
    {
        try
        {
            this.ftpClient.setUserName(userName);
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't set user name '" + userName + "'!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Observable o, final Object arg)
    {
        try
        {
            this.setHost(ConfigController.getInstance().getCfgVal("host"));
            this.setPort(Integer.parseInt(ConfigController.getInstance().getCfgVal("port")));
            this.setUserName(ConfigController.getInstance().getCfgVal("user"));
            this.setPassword(CryptUtils.decode(ConfigController.getInstance().getCfgVal("pass")));
            this.filetimeFixer = (FiletimeFixer) Class.forName("com.chrissyx.jfix.plugins." + ConfigController.getInstance().getCfgVal("plugIn")).newInstance();
            this.recursive = Boolean.parseBoolean(ConfigController.getInstance().getCfgVal("recursive"));
        }
        catch(final JFixError e)
        {
            e.error(FtpController.class);
        }
        catch(final ClassNotFoundException e)
        {
            new JFixError("Can't find plugin '" + ConfigController.getInstance().getCfgVal("plugIn") + "'!", e).error(FtpController.class);
        }
        catch(final InstantiationException e)
        {
            new JFixError(e).error(FtpController.class);
        }
        catch(final IllegalAccessException e)
        {
            new JFixError(e).error(FtpController.class);
        }
    }
}
