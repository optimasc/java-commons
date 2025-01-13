package com.optimasc.text;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import com.optimasc.lang.GregorianDatetimeCalendar;

import junit.framework.TestCase;

public class DateConverterTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testApplyPattern()
  {
    DateConverter converter = new DateConverter();
    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
  }

  public void testParseDatesInvalid_ISO8601ExtendedTZ()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    boolean fail;

    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ssX");
    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1975-01-19T23:59:58-00:00");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(19, e.getErrorOffset());
    }
    assertFalse(fail);

    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1975-01-19T23:59:58-0800");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(19, e.getErrorOffset());
    }
    assertFalse(fail);

    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1975-01-19T23:59:58-08");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(19, e.getErrorOffset());
    }
    assertFalse(fail);

    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1975-01-19T23:59:58+A000");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(20, e.getErrorOffset());
    }
    assertFalse(fail);

    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1975-01-19T23:59:58>08:00");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(19, e.getErrorOffset());
    }
    assertFalse(fail);

  }

  public void testParseDatesValid_ISO8601ExtendedTZ()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;

    // ISO 8601 Expanded format
    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ssX");

    try
    {
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1975-01-19T01:01:12Z");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1975, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      assertEquals(19, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(01, calendar.get(Calendar.MINUTE));
      assertEquals(12, calendar.get(Calendar.SECOND));
      assertEquals(0, calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1975-01-19T01:01:12+08:30");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1975, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      assertEquals(19, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(01, calendar.get(Calendar.MINUTE));
      assertEquals(12, calendar.get(Calendar.SECOND));
      assertEquals(8 * 60 * 60 * 1000 + 30 * 60 * 1000,
          calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

    // ISO 8601 Expanded format with milliseconds.
    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SX");

    try
    {
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1975-01-19T01:01:12.979+00:00");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(true, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1975, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      assertEquals(19, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(01, calendar.get(Calendar.MINUTE));
      assertEquals(12, calendar.get(Calendar.SECOND));
      assertEquals(979, calendar.get(Calendar.MILLISECOND));
      assertEquals(0, calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

  }

  public void testParseDatesValid_ISO8601BasicTZ()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;

    // ISO 8601:2004 Basic format
    converter.applyPattern("'T'HHmmssx");

    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("T010112Z");
      assertEquals(false, calendar.isUserSet(Calendar.YEAR));
      assertEquals(false, calendar.isUserSet(Calendar.MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(01, calendar.get(Calendar.MINUTE));
      assertEquals(12, calendar.get(Calendar.SECOND));
      assertEquals(0, calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("T010112+08");
      assertEquals(false, calendar.isUserSet(Calendar.YEAR));
      assertEquals(false, calendar.isUserSet(Calendar.MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(01, calendar.get(Calendar.MINUTE));
      assertEquals(12, calendar.get(Calendar.SECOND));
      assertEquals(8 * 60 * 60 * 1000, calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("T010112-0830");
      assertEquals(false, calendar.isUserSet(Calendar.YEAR));
      assertEquals(false, calendar.isUserSet(Calendar.MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(01, calendar.get(Calendar.MINUTE));
      assertEquals(12, calendar.get(Calendar.SECOND));
      assertEquals(-8 * 60 * 60 * 1000 - 30 * 60 * 1000,
          calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

  }

  public void testParseDatesInvalid_RFC822TZ()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    boolean fail;

    // IETF RFC 822 Date-time
    converter.applyPattern("d MMM yy HH:mm:ss Z");

    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1 Feb 89 01:45:59 ZZ");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(18, e.getErrorOffset());
    }
    assertFalse(fail);

    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1 Feb 89 01:45:59 -08");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(21, e.getErrorOffset());
    }
    assertFalse(fail);

  }

  public void testParseDatesValid_RFC822TZ()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;

    // IETF RFC 822 Date-time
    converter.applyPattern("d MMM yy HH:mm:ss Z");

    try
    {
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1 Feb 89 01:45:59 UT");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1989, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
      assertEquals(01, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(45, calendar.get(Calendar.MINUTE));
      assertEquals(59, calendar.get(Calendar.SECOND));
      assertEquals(0, calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1 Feb 89 01:45:59 -0830");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1989, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
      assertEquals(01, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(45, calendar.get(Calendar.MINUTE));
      assertEquals(59, calendar.get(Calendar.SECOND));
      assertEquals(-((8 * 60 * 60) + (30 * 60)) * 1000,
          calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      // This is considered a local timezone
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1 Feb 89 01:45:59 -0000");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1989, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
      assertEquals(01, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(45, calendar.get(Calendar.MINUTE));
      assertEquals(59, calendar.get(Calendar.SECOND));
    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      // This is considered as UTC
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1 Feb 89 01:45:59 +0000");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1989, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
      assertEquals(01, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(45, calendar.get(Calendar.MINUTE));
      assertEquals(59, calendar.get(Calendar.SECOND));
      assertEquals(0, calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      // This is considered as -0400
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1 Feb 89 01:45:59 EST");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(true, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1989, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
      assertEquals(01, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(01, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(45, calendar.get(Calendar.MINUTE));
      assertEquals(59, calendar.get(Calendar.SECOND));
      assertEquals(-5 * 60 * 60 * 1000, calendar.get(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      fail();
    }
  }

  public void testParseDatesValid()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    // ISO 8601 local dateTime
    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("1999-01-31T23:59:58");
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1999, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(59, calendar.get(Calendar.MINUTE));
      assertEquals(58, calendar.get(Calendar.SECOND));

      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

    } catch (ParseException e)
    {
      fail();
    }

    // ISO 8601 extended datetime
    converter.applyPattern("-yyyyy-MM-dd'T'HH:mm:ss");
    try
    {
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("-1999-01-31T23:59:58");

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.BC, calendar.get(Calendar.ERA));
      assertEquals(2000, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(59, calendar.get(Calendar.MINUTE));
      assertEquals(58, calendar.get(Calendar.SECOND));

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("19999-01-31T23:59:58");

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(19999, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(59, calendar.get(Calendar.MINUTE));
      assertEquals(58, calendar.get(Calendar.SECOND));

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

    } catch (ParseException e)
    {
      fail();
    }

    // Time with fractional value

    converter.applyPattern("yyyyMMddHHmmss.S");
    // Single digit precision
    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("19011011125958.1");

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(true, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1901, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.OCTOBER, calendar.get(Calendar.MONTH));
      assertEquals(11, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(59, calendar.get(Calendar.MINUTE));
      assertEquals(58, calendar.get(Calendar.SECOND));
      assertEquals(100, calendar.get(Calendar.MILLISECOND));

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(true, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

    } catch (ParseException e)
    {
      fail();
    }

    // Multiple digit precision
    converter.applyPattern("yyyyMMddHHmmss.SSS");
    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("19011011125958.999");

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(true, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1901, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.OCTOBER, calendar.get(Calendar.MONTH));
      assertEquals(11, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(12, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(59, calendar.get(Calendar.MINUTE));
      assertEquals(58, calendar.get(Calendar.SECOND));
      assertEquals(999, calendar.get(Calendar.MILLISECOND));

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(true, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(true, calendar.isUserSet(Calendar.SECOND));
      assertEquals(true, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

    } catch (ParseException e)
    {
      fail();
    }

    // IETF RFC 822 Datetime
    converter.applyPattern("d MMM yy");
    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("20 Jun 82");

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(false, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(false, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1982, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
      assertEquals(20, calendar.get(Calendar.DAY_OF_MONTH));

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(false, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(false, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

    } catch (ParseException e)
    {
      fail();
    }

    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("1 Jan 82");

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(false, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(false, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1982, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      assertEquals(01, calendar.get(Calendar.DAY_OF_MONTH));

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(true, calendar.isUserSet(Calendar.MONTH));
      assertEquals(true, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(false, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(false, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

    } catch (ParseException e)
    {
      fail();
    }

  }

  public void testParseDatesInvalid()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    boolean fail;

    // Not enough number of digits in year.
    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter.parseObject("1 Jan 82");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(1, e.getErrorOffset());
    }
    assertFalse(fail);

    // Missing millieconds 
    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.S");
    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter
          .parseObject("1999-01-31T23:59:58.");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(20, e.getErrorOffset());
    }
    assertFalse(fail);

  }

  public void testFormatDatesValid_RFC822TZ()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    String result;

    // IETF RFC 822 Date-time
    converter.applyPattern("d MMM yy HH:mm:ss Z");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1989);
    calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 01);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 45);
    calendar.set(Calendar.SECOND, 59);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);

    result = converter.format(calendar);
    assertEquals("1 Feb 89 01:45:59 GMT", result);

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1989);
    calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 01);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 45);
    calendar.set(Calendar.SECOND, 59);
    calendar.setTimeZone(new SimpleTimeZone(-((8 * 60 * 60) + (30 * 60)) * 1000, "TST"));

    result = converter.format(calendar);
    assertEquals("1 Feb 89 01:45:59 -0830", result);

  }

  public void testFormatDatesValid_ISO8601ExtendedTZ()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    String result;

    // ISO 8601 Expanded format
    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ssX");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);

    result = converter.format(calendar);
    assertEquals("1975-01-19T01:01:12Z", result);

    //------------------------------

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(new SimpleTimeZone(8 * 60 * 60 * 1000 + 30 * 60 * 1000, "RWA"));

    result = converter.format(calendar);
    assertEquals("1975-01-19T01:01:12+08:30", result);

    //------------------------------

    // ISO 8601 Expanded format with milliseconds.
    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);
    calendar.set(Calendar.MILLISECOND, 979);

    result = converter.format(calendar);
    assertEquals("1975-01-19T01:01:12.979Z", result);

    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SX");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);
    calendar.set(Calendar.MILLISECOND, 979);

    result = converter.format(calendar);
    assertEquals("1975-01-19T01:01:12.9Z", result);

    converter.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SX");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);
    calendar.set(Calendar.MILLISECOND, 455);

    result = converter.format(calendar);
    assertEquals("1975-01-19T01:01:12.5Z", result);

  }

  public void testFormatDatesValid_ISO8601BasicTZ()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    String result;

    // ISO 8601 Basic format
    converter.applyPattern("yyyyMMddHHmmssx");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);

    result = converter.format(calendar);
    assertEquals("19750119010112Z", result);

    //------------------------------

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(new SimpleTimeZone(8 * 60 * 60 * 1000 + 30 * 60 * 1000, "RWA"));

    result = converter.format(calendar);
    assertEquals("19750119010112+0830", result);

    //------------------------------

    // ISO 8601 Basic format with milliseconds.
    converter.applyPattern("yyyyMMddHHmmss.SSSx");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);
    calendar.set(Calendar.MILLISECOND, 979);

    result = converter.format(calendar);
    assertEquals("19750119010112.979Z", result);

    converter.applyPattern("yyyyMMddHHmmssSx");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);
    calendar.set(Calendar.MILLISECOND, 979);

    result = converter.format(calendar);
    assertEquals("197501190101129Z", result);

    converter.applyPattern("yyyyMMddHHmmss.Sx");

    calendar = new GregorianDatetimeCalendar();
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    calendar.set(Calendar.YEAR, 1975);
    calendar.set(Calendar.MONTH, Calendar.JANUARY);
    calendar.set(Calendar.DAY_OF_MONTH, 19);
    calendar.set(Calendar.HOUR_OF_DAY, 01);
    calendar.set(Calendar.MINUTE, 01);
    calendar.set(Calendar.SECOND, 12);
    calendar.setTimeZone(GregorianDatetimeCalendar.ZULU);
    calendar.set(Calendar.MILLISECOND, 455);

    result = converter.format(calendar);
    assertEquals("19750119010112.5Z", result);

  }

  public void testParseDatesValid_OpenDate()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    String result;
    
    String patterns[] = new String[]
    {
     "yyyy",
     "yyyy-MM-dd'T'HH:mm:ss.SX",
     "yyyy-MM-dd'T'HH:mm:ssX",
     "yyyy-MM-dd'T'HH:mmX",
     "yyyy-MM-dd",
     "yyyy-MM",    
    };    
    converter.applyPatterns(patterns);
    
    
    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("1999");

      assertEquals(true, calendar.isUserSet(Calendar.ERA));
      assertEquals(true, calendar.isUserSet(Calendar.YEAR));
      assertEquals(false, calendar.isUserSet(Calendar.MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.DAY_OF_MONTH));
      assertEquals(false, calendar.isUserSet(Calendar.HOUR_OF_DAY));
      assertEquals(false, calendar.isUserSet(Calendar.MINUTE));
      assertEquals(false, calendar.isUserSet(Calendar.SECOND));
      assertEquals(false, calendar.isUserSet(Calendar.MILLISECOND));
      assertEquals(false, calendar.isUserSet(Calendar.ZONE_OFFSET));

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1999, calendar.get(Calendar.YEAR));

    } catch (ParseException e)
    {
      fail();
    }
    
    try
    {
      calendar = (GregorianDatetimeCalendar) converter.parseObject("1999-01-31T23:59:58Z");

      assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
      assertEquals(1999, calendar.get(Calendar.YEAR));
      assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
      assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
      assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
      assertEquals(59, calendar.get(Calendar.MINUTE));
      assertEquals(58, calendar.get(Calendar.SECOND));

    } catch (ParseException e)
    {
      fail();
    }
    
  }
  
  public void testParseDatesInvalid_OpenDate()
  {
    DateConverter converter = new DateConverter();
    GregorianDatetimeCalendar calendar;
    boolean fail;

    
    String patterns[] = new String[]
    {
     "yyyy",
     "yyyyMM",    
    };    
    converter.applyPatterns(patterns);
    
    
    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter.parseObject("199901-");
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(6, e.getErrorOffset());
    }
    assertFalse(fail);
    
    try
    {
      fail = true;
      calendar = (GregorianDatetimeCalendar) converter.parseObject("19990131235958Z");

    } catch (ParseException e)
    {
      fail = false;
      assertEquals(6, e.getErrorOffset());
    }
    assertFalse(fail);
    
  }
  
}
