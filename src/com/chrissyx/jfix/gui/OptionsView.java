package com.chrissyx.jfix.gui;

/**
 * The options frame.
 *
 * @author Chrissyx
 */
public interface OptionsView extends BaseView
{
    /**
     * Returns option value for host.
     *
     * @return Host option value
     */
    String getOptionHost();

    /**
     * Returns option value for port number.
     *
     * @return Port number option value
     */
    String getOptionPort();

    /**
     * Returns option value for user name.
     *
     * @return User name option value
     */
    String getOptionUser();

    /**
     * Returns option value for password.
     *
     * @return Password option value
     */
    String getOptionPass();

    /**
     * Returns option value for selected language.
     *
     * @return New language to use
     */
    String getOptionLanguage();

    /**
     * Returns option value for local start directory.
     *
     * @return Local start directory option value
     */
    String getOptionLocalStartDir();

    /**
     * Returns option value for remote start directory.
     *
     * @return Remote start directory option value
     */
    String getOptionRemoteStartDir();

    /**
     * Returns option value for selected plug-in.
     *
     * @return Plug-in to use
     */
    String getOptionPlugIn();

    /**
     * Returns option value for fecursive fixing of filetimes.
     *
     * @return Fix filetimes recursively
     */
    boolean getOptionRecursive();
}
