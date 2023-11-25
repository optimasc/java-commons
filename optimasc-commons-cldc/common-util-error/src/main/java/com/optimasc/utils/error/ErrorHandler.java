package com.optimasc.utils.error;

public interface ErrorHandler
{
  /** Receive notification of a recoverable error. */
  public void error(Exception exception);
  /** Receive notification of a warning. */
  public void warning(Exception exception); 
}
