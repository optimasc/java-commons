package com.optimasc.datatypes.utils;

import java.util.Locale;

/** Overrides the toString() to only return the x-default value./
 * 
 * @author Carl Eric Codere
 *
 */
public class VisualAltLangMap extends VisualMap<Locale,String>
{
  /** The x-default string for localized properties */
  public static final String X_DEFAULT = "x-default";
  public static final Locale X_DEFAULT_LOCALE = new Locale(X_DEFAULT); 
  
  public VisualAltLangMap()
  {
    super();
  }

  
  @Override
  public synchronized String toString()
  {
    Object value = get(X_DEFAULT_LOCALE);
    if ((value!=null))
    {
      return value.toString();
    }
    return super.toString();
  }

}
