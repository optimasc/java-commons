package com.optimasc.utils.error;

/** Interface to be implemented by classes that wish to be notified of errors. */
public interface ErrorHandler
{
  /** Receive notification of a recoverable error. */
  public void error(Exception exception);
  /** Receive notification of a warning. */
  public void warning(Exception exception); 
}
