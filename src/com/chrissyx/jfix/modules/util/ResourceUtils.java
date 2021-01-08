package com.chrissyx.jfix.modules.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.LoggerFactory;

/**
 * Utilities for resources.
 *
 * @author Chrissyx
 */
public class ResourceUtils
{
    /**
     * Hidden constructor to prevent instances of this class.
     */
    private ResourceUtils()
    {
    }

    /**
     * Loads and returns image from the resource directory.
     *
     * @param filename Name of image file
     * @return Loaded image
     */
    public static BufferedImage getImage(final String filename)
    {
        try
        {
            return ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(ResourceUtils.getPath("icon.png")));
        }
        catch(final IllegalArgumentException e)
        {
            LoggerFactory.getLogger(ResourceUtils.class).warn("Cannot find application icon!", e);
        }
        catch(final IOException e)
        {
            LoggerFactory.getLogger(ResourceUtils.class).warn("Cannot get application icon!", e);
        }
        return null;
    }

    /**
     * Returns path to a file in the resource directory.
     *
     * @param filename Name of file in resource folder
     * @return Path to file in resource folder
     */
    public static String getPath(final String filename)
    {
        return "com/chrissyx/jfix/resources/" + filename;
    }
}
