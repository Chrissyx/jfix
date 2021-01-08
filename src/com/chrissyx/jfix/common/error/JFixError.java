package com.chrissyx.jfix.common.error;

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
    private final String logMessage;

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
}
