package com.optimasc.text;

/** Standard pre-defined converters for different time and date formats. */
public class StandardDateFormatters
{
  // Resolution: year
  public static final String PATTERN_ISO8601_YEAR = "yyyy";
  // Resolution: month
  public static final String PATTERN_ISO8601_YEAR_MONTH = "yyyy-MM";
  // Resolution day
  public static final String PATTERN_ISO8601_DATE = "yyyy-MM-dd";
  public static final String PATTERN_ISO8601_TIME = "HH:mm:ss.SX";
  public static final String PATTERN_ISO8601_DATETIME = "yyyy-MM-ddTHH:mm:ss";
  
  /** ISO 8601 Time with or without timezone */
  public static final DateConverter ISO8601_TIME = new DateConverter(
   new String[]{   
       "HH:mm:ss.SX",
       "HH:mm:ss.S",
       "HH:mm:ssX",
       "HH:mm:ss",
       "'T'HH:mm:ss.SX",
       "'T'HH:mm:ss.S",
       "'T'HH:mm:ssX",
       "'T'HH:mm:ss"});
  
  
  /** Date/time pattern from IETF RFC 822 / IETF RFC 2822 standard */
  public static final DateConverter IETF_RFC_822_DATETIME = new DateConverter(
      new String[] {
          "EEE, dd MMM yyyy HH:mm:ss Z"});  
  
  /** Date/time pattern from ISO/IEC 8824 and PDF properties */
  public static final DateConverter DATE_ISO_8824 = new DateConverter(
      new String[]{
          "yyyyMMddHHmmssZ"}
      );
  
  /** Date/time pattern from EXIF specification */
  public static final DateConverter DATE_EXIF = new DateConverter(
      new String[]{
       "yyyy:MM:dd HH:mm:ss"}
  );
  
  /** Date/time pattern for ISO 8601 standard date and local time (YYYY-MM-DDThh:mm:ss) */
  public static final DateConverter ISO8601_DATETIME = new DateConverter(
      new String[]{
          PATTERN_ISO8601_DATETIME}
  );
  
  
  /** Date formatter for ISO 8601 standard date format (YYYY-MM-DD) */
  public static class ISO8601DateConverter extends DateConverter
  {
    
    private static DataConverter instance;
    
    public ISO8601DateConverter()
    {
      super(new String[]{PATTERN_ISO8601_DATE});
    }
    
    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (ISO8601DateConverter.class)
        {
          if (instance == null)
            instance = new ISO8601DateConverter();
        }
      }
      return instance;
    }
  }
  

  /** Date-time formatter for ASN1 GeneralizedTime date-time format. */
  public static class GeneralizedTimeConverter extends DateConverter
  {
    private static DataConverter instance;
    
    public GeneralizedTimeConverter()
    {
      super(
          new String[]{
              /* With timezone */
              "yyyyMMddHHmmss.SSSx",  
              "yyyyMMddHHmmssx",
              "yyyyMMddHHmmx",
              "yyyyMMddHHx",
              /* Local time */  
              "yyyyMMddHH",
              "yyyyMMddHHmm",
              "yyyyMMddHHmmss",
              "yyyyMMddHHmmss.SSS"});
    }
    
    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (GeneralizedTimeConverter.class)
        {
          if (instance == null)
            instance = new GeneralizedTimeConverter();
        }
      }
      return instance;
    }
  }  
  
  /** Date-time formatter W3C NOTE-datetime - Date and Time Formats */
  public static class W3CDateTimeConverter extends DateConverter
  {
    private static DataConverter instance;
    
    public W3CDateTimeConverter()
    {
      super(
          new String[]{   
              "yyyy",
              "yyyy-MM-dd'T'HH:mm:ss.SX",
              "yyyy-MM-dd'T'HH:mm:ssX",
              "yyyy-MM-dd'T'HH:mmX",
              "yyyy-MM-dd",
              "yyyy-MM"});
    }
    
    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (W3CDateTimeConverter.class)
        {
          if (instance == null)
            instance = new W3CDateTimeConverter();
        }
      }
      return instance;
    }
  }  
  
      
}

