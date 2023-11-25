package com.optimasc.date;

import java.util.Calendar;

public class ParadoxDateTime
{
  public static double CalendarToTimeStamp(Calendar cal)
  {
    double value = ParadoxDate.converter.encode(cal);
    value = (value * 86400.0D + cal.get(11) * 3600 + cal.get(12) * 60 + cal.get(13)) * 1000.0D + cal.get(14);
    return value;
  }

  public static Calendar ParadoxTimestampToCalendar(double data)
  {
    double secvalues = data / 1000.0D;
    int days = (int)(secvalues / 86400.0D);
    int secs = (int)(secvalues % 86400.0D);
    Calendar cal = ParadoxDate.converter.decode(days);
    cal.set(11, secs / 3600);
    cal.set(12, secs / 60 % 60);
    cal.set(13, secs % 60);
    cal.set(14, (int)(data % 1000.0D));
    return cal;
  }  

}
