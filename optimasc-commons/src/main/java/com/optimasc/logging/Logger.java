package com.optimasc.logging;

public abstract class Logger
{
  /** This indicates the maximum logging level supported */
  private int logLevel;
  
  /** This is the application/activity/library name to use for logging purposes */
  protected String appName;

  private static final int INFO_MASK = 0x01;
  private static final int WARN_MASK = 0x02;
  private static final int ERROR_MASK = 0x04;
  
  /** This is the log level that implicate outputting all <code>LEVEL_ERROR, 
   *  LEVEL_WARN</code> and  <code>LEVEL_INFO</code> logs. This is the most
   *  verbose mode available for this log mechanism. 
   */
  public static final int LEVEL_INFO = (INFO_MASK | WARN_MASK | ERROR_MASK);

  
  /** This is the log level that implicate outputting all <code>LEVEL_ERROR, 
   *  and  <code>LEVEL_WARN</code> logs.  
   */
  public static final int LEVEL_WARN = (WARN_MASK | ERROR_MASK);

  
  /** This is the log level that implicate outputting all <code>LEVEL_ERROR</code> 
   *  logs.  
   */
  public static final int LEVEL_ERROR = (ERROR_MASK);
  
  
  public static final String STRING_ERROR = "[ERROR]: ";
  public static final String STRING_INFO = "[INFO]: ";
  public static final String STRING_WARN = "[WARN]: ";
  
  
  public static final String LibraryName = "optimasc.com event loggger"; 
  
  /** Indicates the level of logging to use */
  public Logger(int level, String applicationName)
  {
    super();
    logLevel = level;
    appName = applicationName;
  }
  
  /**
  Log a message object with the {@link Level#WARN WARN} Level.

  <p>This method first checks if this category is <code>WARN</code>
  enabled by comparing the level of this category with {@link
  Level#WARN WARN} Level. If the category is <code>WARN</code>
  enabled, then it converts the message object passed as parameter
  to a string by invoking the appropriate method (usring toString()). 

  <p><b>WARNING</b> Note that passing a {@link Throwable} to this
  method will print the name of the Throwable but no stack trace. To
  print a stack trace use the {@link #warn(Object, Throwable)} form
  instead.  <p>

  @param message the message object to log.  */
  public void warn(java.lang.Object msg)
  {
     if ((logLevel & WARN_MASK)==WARN_MASK)
     {
       log(LEVEL_WARN,msg,null);
     }
  }

  /**
  Log a message with the <code>WARN</code> level including the
  stack trace of the {@link Throwable} <code>t</code> passed as
  parameter.

  <p>See {@link #warn(Object)} for more detailed information.

  @param message the message object to log.
  @param t the exception to log, including its stack trace.  */
  public void warn(java.lang.Object msg, java.lang.Throwable ex)
  {
    if ((logLevel & WARN_MASK)==WARN_MASK)
    {
      log(LEVEL_WARN, msg,ex);
    }
    
  }
  
  
  /**
  Log a message object with the {@link Level#ERROR ERROR} Level.

  <p>This method first checks if this category is <code>ERROR</code>
  enabled by comparing the level of this category with {@link
  Level#ERROR ERROR} Level. If this category is <code>ERROR</code>
  enabled, then it converts the message object passed as parameter
  to a string by invoking the appropriate method (usually toString()). 

  <p><b>WARNING</b> Note that passing a {@link Throwable} to this
  method will print the name of the <code>Throwable</code> but no
  stack trace. To print a stack trace use the {@link #error(Object,
  Throwable)} form instead.

  @param message the message object to log */
  public void error(java.lang.Object msg)
  {
    if ((logLevel & ERROR_MASK)==ERROR_MASK)
    {
      log(LEVEL_ERROR, msg,null);
    }
    
  }
  
  /**
  Log a message object with the <code>ERROR</code> level including
  the stack trace of the {@link Throwable} <code>t</code> passed as
  parameter.

  <p>See {@link #error(Object)} form for more detailed information.

  @param message the message object to log.
  @param t the exception to log, including its stack trace.  */
  public void error(java.lang.Object msg, java.lang.Throwable ex)
  {
    if ((logLevel & ERROR_MASK)==ERROR_MASK)
    {
      log(LEVEL_ERROR, msg,ex);
    }
  }
  
  /**
  Log a message object with the {@link Level#INFO INFO} Level.

  <p>This method first checks if this category is <code>INFO</code>
  enabled by comparing the level of this category with {@link
  Level#INFO INFO} Level. If the category is <code>INFO</code>
  enabled, then it converts the message object passed as parameter
  to a string by invoking the appropriate method (usually toString()).

  <p><b>WARNING</b> Note that passing a {@link Throwable} to this
  method will print the name of the Throwable but no stack trace. To
  print a stack trace use the {@link #info(Object, Throwable)} form
  instead.

  @param message the message object to log */
  public void info(java.lang.Object msg)
  {
    if ((logLevel & INFO_MASK)==INFO_MASK)
    {
      log(LEVEL_INFO, msg,null);
    }
  }
  

  /** Internal method that does the actual output. This must
   *  be implemented for each platform specific implementation. The
   *  verification if the value must be output or not is already 
   *  checked before calling this method. 
   *  
   * @param msg This is the message to output (as-is) 
   * @param ex This parameter can be null, otherwise it points to 
   *   a throwable value.
   */
  protected abstract void log(int level, java.lang.Object msg, java.lang.Throwable ex);
  
}
 