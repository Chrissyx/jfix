package com.chrissyx.jfix.modules.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some helping methods for GUI classes.
 *
 * @author Chrissyx
 * @since 0.14
 */
public class GuiUtils
{
    /**
     * Hidden constructor to prevent instances of this class.
     */
    private GuiUtils()
    {
    }

    /**
     * Returns user-friendly plug-in names of given class names, e.g. "SiteUtime" converts to "SITE UTIME".
     *
     * @param classList List with plug-in class names
     * @return Display names of provided list
     */
    public static String[] getPlugInNames(final String[] classList)
    {
        String curPlugInName;
        Matcher curMatcher;
        for(int i = 0; i < classList.length; i++)
        {
            curPlugInName = "";
            curMatcher = Pattern.compile("([A-Z0-9][a-z0-9]+)").matcher(classList[i]);
            while(curMatcher.find())
                curPlugInName += curMatcher.group().toUpperCase() + " ";
            if(!curPlugInName.isEmpty())
                classList[i] = curPlugInName.trim();
        }
        return classList;
    }
}
