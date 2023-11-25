package com.optimasc.locale;

import java.io.IOException;
import java.io.InputStream;

import com.optimasc.utils.PropertyList;

/** This class is used to retrieve strings data from the specific configuration data
 *  for making applications language neutral. The properties file should be located
 *  in /res/conf/ and named strings.properties and should contain the strings
 *  associated with the application in the correct language.
 * 
 * @author Carl Eric Codere
 *
 */
public class Locale
{
   protected static final String STRING_DATA_PATH = "/res/conf/strings.properties";
   
   protected static Locale locale;
   
   protected static PropertyList localeData;
   
   /** Singleton to get one instance of the Local manager.
    * 
    * @return
    */
   public static Locale getInstance()
   {
     if (locale == null)
     {
       locale = new Locale();
     } 
     return locale;
   }
   
   Locale()
   {
     try {
     InputStream is = getClass().getResourceAsStream(STRING_DATA_PATH);
     localeData = new PropertyList();
     localeData.load(is);
    } catch (IOException e)
    {
    }
   }

   /** Returns the string associated with the specified key or null
    *  if this key value does not exist.
    * 
    * @param key The key value to search for.
    * @return The string associated with this key, or null if there
    *   is none.
    */
   public String getString(String key)
   {
     return localeData.getString(key);
   }
}
