package com.chrissyx.jfix.common.error;

import com.chrissyx.jfix.modules.GuiController;

import org.slf4j.LoggerFactory;

/**
 * General JFix error.
 *
 * @author Chrissyx
 */
public class JFixError extends Exception
{
    /**
     * Custom log message.
     */
    protected final String logMessage;

    /**
     * Creates an exception.
     */
    public JFixError()
    {
        super();
        this.logMessage = null;
    }

    /**
     * Creates an exception with a custom log message.
     *
     * @param sLogMessage Custom log message
     */
    public JFixError(final String sLogMessage)
    {
        super();
        this.logMessage = sLogMessage;
    }

    /**
     * Creates an exception with a custom log message and original error.
     *
     * @param sLogMessage Custom log message
     * @param e Original exception
     */
    public JFixError(final String sLogMessage, final Throwable e)
    {
        super(e.getMessage(), e);
        this.logMessage = sLogMessage;
    }

    /**
     * Creates an exception with the original error.
     *
     * @param e Original exception
     */
    public JFixError(final Throwable e)
    {
        super(e.getMessage(), e);
        this.logMessage = null;
    }

    /**
     * Returns the custom log message.
     *
     * @return Custom log message
     */
    public String getLogMessage()
    {
        return this.logMessage;
    }

    /**
     * Logs this error for stated class with a error severity and displays it in an error dialog.
     *
     * @param clazz Class this error was thrown from
     */
    public void error(final Class clazz)
    {
        final String curLogMessage = this.logMessage == null ? super.getMessage() : this.logMessage;
        LoggerFactory.getLogger(clazz).error(curLogMessage, super.getCause());
        GuiController.getInstance().showErrorDialog(curLogMessage);
    }

    /**
     * Logs this error for stated class with a warning severity.
     *
     * @param clazz Class this error was thrown from
     * @param showDialog Show this error in a warning dialog
     */
    public void warn(final Class clazz, final boolean showDialog)
    {
        final String curLogMessage = this.logMessage == null ? super.getMessage() : this.logMessage;
        LoggerFactory.getLogger(clazz).warn(curLogMessage, super.getCause());
        if(showDialog)
            GuiController.getInstance().showWarningDialog(curLogMessage);
    }
}
