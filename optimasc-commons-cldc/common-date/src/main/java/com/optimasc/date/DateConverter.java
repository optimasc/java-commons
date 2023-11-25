package com.optimasc.date;

import java.util.Calendar;

/** Abstract class used to convert between numeric formats and {@link java.util.Calendar} formats */ 
public abstract class DateConverter
{
  public abstract long toLong(final Calendar cal);
  public abstract Calendar toCalendar(final long timestamp);
}
