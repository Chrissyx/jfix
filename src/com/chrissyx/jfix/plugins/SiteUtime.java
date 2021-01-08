package com.chrissyx.jfix.plugins;

import java.util.Date;

/**
 * Filetime fixing plug-in for {@code SITE UTIME} command.
 *
 * @author Chrissyx
 * @since 0.9
 */
public class SiteUtime extends FiletimeFixer
{
    /**
     * Sets date pattern.
     */
    public SiteUtime()
    {
        super("yyyyMMddHHmmss");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCommand(final String filename, final long timestamp)
    {
        final String curTime = this.dateFormatter.format(new Date(timestamp));
        return "SITE UTIME " + filename + " " + curTime + " " + curTime + " " + curTime + " UTC";
    }
}
