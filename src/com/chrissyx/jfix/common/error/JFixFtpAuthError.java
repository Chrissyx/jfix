package com.chrissyx.jfix.common.error;

/**
 * Problems with authentication of remote server.
 *
 * @author Chrissyx
 * @since 0.2
 */
public class JFixFtpAuthError extends JFixError
{
    /**
     * Creates an exception with a custom log message and original error.
     *
     * @param sLogMessage Custom log message
     * @param e Original exception
     */
    public JFixFtpAuthError(final String sLogMessage, final Throwable e)
    {
        super(sLogMessage, e);
    }
}
