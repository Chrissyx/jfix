package com.chrissyx.jfix.plugins;

import java.text.SimpleDateFormat;

/**
 * General template for implementing plug-ins to specify individual FTP command patterns.
 *
 * @author Chrissyx
 * @since 0.9
 */
public abstract class FiletimeFixer
{
    /**
     * Date formatter to prepare the filetimes.
     */
    protected final SimpleDateFormat dateFormatter = new SimpleDateFormat();

    /**
     * Applies date pattern to the provided internal date formatter.
     *
     * @param datePattern Pattern to format FTP command specific date
     */
    protected FiletimeFixer(final String datePattern)
    {
        this.dateFormatter.applyPattern(datePattern);
    }

    /**
     * Returns ready-to-use FTP command to fix remote filetime of stated local file with given timestamp.
     *
     * @param filename Name of local and remote file to fix its filetime
     * @param timestamp Timestamp to apply
     * @return FTP command for execution
     */
    public abstract String getCommand(final String filename, final long timestamp);
}
