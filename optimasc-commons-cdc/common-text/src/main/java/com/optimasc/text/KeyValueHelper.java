package com.optimasc.text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Helper class for working with key-value pairs.
 * 
 * <p>Key value pairs are stored as String arrays in the format
 * "context=value" (e.g., "en=Hello", "fr=Bonjour"). This class provides
 * utilities to convert between array and Map representations and to
 * efficiently lookup values by context.</p>
 * 
 * @author Carl Eric Codere
 */
public class KeyValueHelper
{
  
  /** Separator character between context and value */
  private static final char SEPARATOR = '=';
  
  /**
   * Parses a key-value pair in the format "context=value".
   * 
   * @param keyValue [in] The string to parse (e.g., "en=Hello")
   * @return Array with two elements: [0]=key, [1]=value
   * @throws IllegalArgumentException if the format is invalid
   */
  public static String[] parseKeyValue(String keyValue) 
  {
    if (keyValue == null) {
      throw new IllegalArgumentException("Key value must not be null");
    }
    
    int idx = keyValue.indexOf(SEPARATOR);
    if (idx == -1) {
      throw new IllegalArgumentException(
          "Invalid key value format: '" + keyValue + 
          "'. Expected format is 'key=value'");
    }
    
    if (idx == 0) {
      throw new IllegalArgumentException(
          "Invalid key value format: '" + keyValue + 
          "'. Key part is empty");
    }
    
    return new String[] {
      keyValue.substring(0, idx).trim(),        // key
      keyValue.substring(idx + 1)               // value (may be empty)
    };
  }
  
  /**
   * Creates a key-value pair in the format "key=value".
   * 
   * @param key [in] The key identifier (e.g., "en", "fr")
   * @param value [in] The value object (will be converted to String)
   * @return The formatted string "key=value"
   * @throws IllegalArgumentException if key is null or empty
   */
  public static String createKeyValue(String key, Object value) 
  {
    if (key == null || key.length() == 0) {
      throw new IllegalArgumentException("Key must not be null or empty");
    }
    
    String valueStr = (value == null) ? "" : value.toString();
    return key + SEPARATOR + valueStr;
  }
  
  /**
   * Converts an array of key-value pairs to a Map for efficient lookups.
   * 
   * <p>The array should contain strings in the format "key=value".
   * Invalid entries are skipped with a warning.</p>
   * 
   * @param keyValues [in] Array of "context=value" strings
   * @return Map where keys are contexts and values are the corresponding values.
   *         Returns empty Map if input is null or empty.
   */
  public static Map toKeyValueMap(Object[] keyValues) {
    Map contextMap = new HashMap();
    
    if (keyValues == null || keyValues.length == 0) {
      return contextMap;
    }
    
    for (int i = 0; i < keyValues.length; i++) {
      if (keyValues[i] instanceof String) {
        try {
          String[] parts = parseKeyValue((String) keyValues[i]);
          contextMap.put(parts[0], parts[1]);
        } catch (IllegalArgumentException e) {
          // Skip invalid entries
          System.err.println("Warning: Skipping invalid alt value at index " + 
              i + ": " + e.getMessage());
        }
      }
    }
    
    return contextMap;
  }
  
  /**
   * Converts a Map to an array of key-values for storage.
   * 
   * @param keyValueMap [in] Map where keys are keys and values are the values
   * @return Array of "key=value" strings
   */
  public static String[] fromKeyValueMap(Map keyValueMap) {
    if (keyValueMap == null || keyValueMap.isEmpty()) {
      return new String[0];
    }
    
    String[] result = new String[keyValueMap.size()];
    Iterator it = keyValueMap.entrySet().iterator();
    int idx = 0;
    
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      result[idx++] = createKeyValue(
          entry.getKey().toString(), 
          entry.getValue());
    }
    
    return result;
  }
  
  /**
   * Gets the value for a specific key-value from an array of alternative values.
   * 
   * <p>This performs a linear search through the array. For frequent lookups,
   * consider converting to a Map using {@link #toKeyValueMap(Object[])}.</p>
   * 
   * @param keyValues [in] Array of "context=value" strings
   * @param key [in] The context to search for
   * @return The value for that key, or null if not found
   */
  public static Object getValueForContext(Object[] keyValues, String key) {
    if (keyValues == null || key == null) {
      return null;
    }
    
    for (int i = 0; i < keyValues.length; i++) {
      if (keyValues[i] instanceof String) {
        try {
          String[] parts = parseKeyValue((String) keyValues[i]);
          if (parts[0].equals(key)) {
            return parts[1];
          }
        } catch (IllegalArgumentException e) {
          // Skip invalid entries
          continue;
        }
      }
    }
    
    return null;
  }
  
  /**
   * Checks if an array contains key values in the correct format.
   * 
   * @param keyValues [in] Array to validate
   * @return true if all elements are valid "key=value" strings
   */
  public static boolean isValidKeyValueArray(Object[] keyValues) {
    if (keyValues == null || keyValues.length == 0) {
      return false;
    }
    
    for (int i = 0; i < keyValues.length; i++) {
      if (!(keyValues[i] instanceof String)) {
        return false;
      }
      try {
        parseKeyValue((String) keyValues[i]);
      } catch (IllegalArgumentException e) {
        return false;
      }
    }
    
    return true;
  }
}