package com.optimasc.utils.error;

/** Interface to be implemented by all classes
 *  that wish to manage errors.
 *  
 * @author Carl Eric Codere <carl-eric.codere@optimasc.com>
 *
 */
public interface ErrorHandlerManager
{
  /**
   * Allow an application to register an error event handler.
   *
   * <p>If the application does not register an error handler, all
   * error events reported by the may be silently
   * ignored except for a fatal error,  however, normal processing may not continue.  It is
   * highly recommended that all applications implement an
   * error handler to avoid unexpected bugs if this interface is implemented.</p>
   *
   * <p>Applications may register a new or different handler at any
   * time and the handler will immediately be used.</p>
   *
   * @param handler The error handler.
   * @see #getErrorHandler
   */
  public void setErrorHandler (ErrorHandler handler); 
  
  
  /**
   * Return the current error handler.
   *
   * @return The current error handler, or null if none
   *         has been registered.
   * @see #setErrorHandler
   */
  public ErrorHandler getErrorHandler ();
  
}
