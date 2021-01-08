package com.chrissyx.jfix;

import com.chrissyx.jfix.common.error.JFixError;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Perfoms one run of JFix with logging.
 *
 * @author Chrissyx
 * @version 0.1
 * @see <a href="http://www.chrissyx.de.vu/" target="_blank">Chrissyx Homepage</a>
 */
public class JFixMain
{
    /**
     * Static instance of the logger.
     */
    private static Logger logger;

    /**
     * Hidden constructor to prevent instances of this class.
     */
    private JFixMain()
    {
    }

    /**
     * Creates JFix instance and performs sync of filetimes.
     *
     * @param args Contains host, user and pass
     */
    public static void main(final String[] args)
    {
        if(args.length < 4)
            System.out.println(
                "Usage: CHSJFix <host> <user> <pass> <localPath> [<remoteDir>]");
        else
        {
            try
            {
                final JFix jFix = new JFix(args[0], args[1], args[2]);
                jFix.connect();
                jFix.fixFiletimes(args[3], args.length < 5 ? null : args[4]);
                jFix.disconnect();
            }
            catch(final JFixError e)
            {
                JFixMain.logger.severe(e.getLogMessage());
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    /**
     * Returns an instance of the logger. Loglevel is hard-coded.
     *
     * @return Logger instance
     * @see java.util.logging.Logger
     */
    public static Logger getLogger()
    {
        if(JFixMain.logger == null)
        {
            JFixMain.logger = Logger.getLogger(JFixMain.class.getName());
            try
            {
                //Log in TEMP folder
                final FileHandler fh = new FileHandler("%t/jfix.log");
                fh.setFormatter(new SimpleFormatter());
                JFixMain.logger.addHandler(fh);
            }
            catch(final IOException e)
            {
                //Only console available...
                JFixMain.logger.log(Level.WARNING, e.getMessage(), e);
            }
            JFixMain.logger.setLevel(Level.ALL); //Set level here
            JFixMain.logger.fine("Logger initialized!");
        }
        return JFixMain.logger;
    }
}
