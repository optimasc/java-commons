package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.Locale;
import java.util.StringTokenizer;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.utils.VisualAltLangMap;
import com.optimasc.datatypes.utils.VisualMap;

/** Represents a map type that contains a default value. This is mainly
 *  used to represent a Lang Alt map used by the XMP Specification.
 *  
 *  The {@link #parse(String)} method accepts different formats for
 *  the parsing. If there is only one value with no key separator
 *  then it will be correctly be parsed and will be considered
 *  the value with the x-default key. 
 *  
 * @author Carl Eric Codere
 *
 */
public class AltLangMapType extends MapType
{
  /** The x-default string for localized properties */
  String X_DEFAULT = "x-default";
  
  public AltLangMapType()
  {
      super();
      setKeyDatatype(new LanguageType());
  }

  @Override
  public Object parse(String value) throws ParseException
  {
    VisualAltLangMap visualMap = new VisualAltLangMap();
    StringTokenizer st = new StringTokenizer(value,entrySeparatorChar);
    while (st.hasMoreTokens()) 
    {
        String entryString = st.nextToken();
        if (entryString.indexOf(keySeparatorChar)!=-1)
        {
          String entry[] = entryString.split(keySeparatorChar);
          visualMap.put((Locale)keyDatatype.parse(entry[0]),valueDatatype.parse(entry[1]).toString());
        } else
        {
          visualMap.put(new Locale(X_DEFAULT),valueDatatype.parse(entryString).toString());
        }
    }
    try
    {
      validate(visualMap);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Error while parsing list items.",0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Error while parsing list items.",0);
    }
    return visualMap;
  }
  
  

  
}
