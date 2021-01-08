package com.chrissyx.jfix;

import com.chrissyx.jfix.common.error.JFixError;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * <b>J</b>ava <b>F</b>TP F<b>i</b>letime Fi<b>x</b> manages FTP access and
 * fixes filetimes not possible to set with MDTM or MFMT with SITE UTIME.
 * Uses edtFTPj/Free as FTP client.
 *
 * @author Chrissyx
 * @see <a href="http://www.enterprisedt.com/products/edtftpj/overview.html" target="_blank">edtFTPj/Free Homepage</a>
 */
public class JFix
{
    /**
     * The FTP client being worked with.
     */
    private FileTransferClient ftpClient;

    /**
     * Date formatter to prepare the filetimes.
     */
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * Initializes the FTP client module.
     *
     * @param host Host address to conenct to
     * @param user User name for authentication
     * @param pass Password of user
     * @throws JFixError If setting up FTP client failed
     */
    public JFix(final String host, final String user, final String pass) throws
        JFixError
    {
        this.ftpClient = new FileTransferClient();
        try
        {
            this.ftpClient.setRemoteHost(host);
            this.ftpClient.setUserName(user);
            this.ftpClient.setPassword(pass);
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't setup FTP client!", e);
        }
        JFixMain.getLogger().info("JFix initialized!");
    }

    /**
     * Connects to FTP host.
     *
     * @throws JFixError If connecting failed
     */
    public void connect() throws JFixError
    {
        try
        {
            this.ftpClient.connect();
        }
        catch(final FTPException e)
        {
            throw new JFixError("Can't connect to FTP host!", e);
        }
        catch(final IOException e)
        {
            throw new JFixError(e);
        }
        JFixMain.getLogger().info(
            "Connected to " + this.ftpClient.getRemoteHost() + "!");
    }

    /**
     * Disconnects from FTP host.
     *
     * @throws JFixError If disconnecting failed
     */
    public void disconnect() throws JFixError
    {
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
        JFixMain.getLogger().info("Disconnected from " + this.ftpClient.
            getRemoteHost() + "!");
    }

    /**
     * Fixes the filestimes of current local files with the ones from the host.
     *
     * @param localDir Locale directory to gather the files' timestamp
     * @param remoteDir Optional remote directory to change to first
     * @throws JFixError If executing FTP command failed
     */
    public void fixFiletimes(final String localDir, final String remoteDir)
        throws JFixError
    {
        if(remoteDir != null)
            try
            {
                this.ftpClient.changeDirectory(remoteDir);
            }
            catch(final FTPException e)
            {
                throw new JFixError(
                    "Can't change remote directory to '" + remoteDir + "'!", e);
            }
            catch(final IOException e)
            {
                throw new JFixError(e);
            }
        final LinkedList<String> commands = new LinkedList<String>();
        //Create command list
        for(final File curFile : new File(localDir).listFiles())
        {
            final String curTime = this.sdf.format(new Date(
                curFile.lastModified()));
            commands.add("SITE UTIME " + curFile.getName() + " " + curTime + " "
                + curTime + " " + curTime + " UTC");
        }
        //Execute commands
        for(final String curCommand : commands)
            try
            {
                JFixMain.getLogger().info("Executing '" + curCommand + "'...\n"
                    + this.ftpClient.executeCommand(curCommand));
            }
            catch(final FTPException e)
            {
                throw new JFixError(
                    "Can't execute '" + curCommand + "' command!", e);
            }
            catch(final IOException e)
            {
                throw new JFixError(e);
            }
        JFixMain.getLogger().info("Executed " + commands.size() + " commands!");
    }
}
