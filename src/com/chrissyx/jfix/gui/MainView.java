package com.chrissyx.jfix.gui;

/**
 * The main frame.
 *
 * @author Chrissyx
 */
public interface MainView extends BaseView
{
    /**
     * Appends a log entry to text area console.
     *
     * @param logMessage Log entry to append
     */
    void appendLogEntry(final String logMessage);

    /**
     * Clears all log entries from text area console.
     */
    void clearLogConsole();

    /**
     * Sets local filenames as items to listbox.
     *
     * @param filenames Filenames to set
     */
    void setLocalFileList(final String[] filenames);

    /**
     * Sets remote filenames as items to listbox.
     *
     * @param filenames Filenames to set
     */
    void setRemoteFileList(final String[] filenames);
}
