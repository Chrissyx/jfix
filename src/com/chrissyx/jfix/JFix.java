package com.chrissyx.jfix;

import com.chrissyx.jfix.modules.GuiController;

import javax.swing.SwingUtilities;

import org.slf4j.LoggerFactory;

/**
 * Main class and application entry point of JFix.
 *
 * <b>J</b>ava <b>F</b>TP F<b>i</b>letime Fi<b>x</b> manages FTP access and fixes timestamps of remote files.
 * Filetimes are set with FTP commands defined by the bundled plug-ins, e.g. SITE UTIME instead of unsupported MDTM or MFMT server commands.
 *
 * @author Chrissyx
 * @version 1.0
 */
public class JFix
{
    /**
     * Current version number of JFix.
     */
    public static final String VERSION = "1.0";

    /**
     * Hidden constructor to prevent instances of this class.
     */
    private JFix()
    {
    }

    /**
     * Starts application with GUI by calling {@link GuiController}.
     *
     * @param args Unused arguments
     */
    public static void main(final String[] args)
    {
        LoggerFactory.getLogger(JFix.class).info("Starting JFix {}...", JFix.VERSION);
        LoggerFactory.getLogger(JFix.class).info("Running on {} {} with Java {}", new Object[]
                {
                    System.getProperty("os.name"),
                    System.getProperty("os.version"),
                    System.getProperty("java.version")
                });
        SwingUtilities.invokeLater(new Runnable()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void run()
            {
                GuiController.getInstance();
            }
        });
    }
}
