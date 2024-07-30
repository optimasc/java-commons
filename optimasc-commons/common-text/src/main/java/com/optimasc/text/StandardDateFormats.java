package com.optimasc.text;

/** Standard pre-defined converters for different time and date formats. */
public class StandardDateFormats
{
  // Resolution: year
  public static final String PATTERN_ISO8601_YEAR = "yyyy";
  // Resolution: month
  public static final String PATTERN_ISO8601_YEAR_MONTH = "yyyy-MM";
  // Resolution day
  public static final String PATTERN_ISO8601_DATE = "yyyy-MM-dd";
  public static final String PATTERN_ISO8601_TIME = "HH:mm:ss.SX";
  
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
  
  /** ISO 8601 Datetime with or without timezone */
//;  public static final DateConverter ISO8601_DATETIME = new DateConverter(
  
  
  /** W3C NOTE-datetime - Date and Time Formats */
  public static final DateConverter W3C_DATETIME = new DateConverter(
   new String[]{   
       "yyyy",
       "yyyy-MM-dd'T'HH:mm:ss.SX",
       "yyyy-MM-dd'T'HH:mm:ssX",
       "yyyy-MM-dd'T'HH:mmX",
       "yyyy-MM-dd",
       "yyyy-MM"});
  

  /** ASN1 GeneralizedTime type */
  public static final DateConverter ASN1_GENERALIZEDTIME = new DateConverter(
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
      
}

