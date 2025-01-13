package com.optimasc.lang;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Class that represents a Media type as defined in IETF RFC 4288. Matching is
 * always case-insensitive as defined in IETF RFC 2045. The Media Type is
 * composed of a primary type (called the Media Type in IETF), its sub-type and
 * optional attributes (called parameters in EITF). Both primary type and
 * sub-type are mandatory and need to be specified.
 * 
 * @author Carl Eric Codere
 *
 */
public class MediaType
{
  /**
   * The maximum number of characters allowed for each part of the media type.
   */
  public static int MAX_LENGTH = 127;

  protected static char TYPE_SEPARATOR = '/';

  public static final boolean ALLOWED_CHARACTERS[] = { false, /* 32    (Space) */
  true, /* 33  ! (Exclamation mark) */
  false, /* 34  " (Quotation mark ; quotes) */
  true, /* 35  # (Number sign) */
  true, /* 36  $ (Dollar sign) */
  false, /* 37  % (Percent sign) */
  true, /* 38  & (Ampersand) */
  false, /* 39  ' (Apostrophe) */
  false, /* 40  ( (round brackets or parentheses) */
  false, /* 41  ) (round brackets or parentheses) */
  false, /* 42  * (Asterisk) */
  true, /* 43  + (Plus sign) */
  false, /* 44  , (Comma) */
  true, /* 45  - (Hyphen) */
  true, /* 46  . (Dot , full stop) */
  false, /* 47  / (Slash) */
  true, /* 48  0 (number zero) */
  true, /* 49  1 (number one) */
  true, /* 50  2 (number two) */
  true, /* 51  3 (number three) */
  true, /* 52  4 (number four) */
  true, /* 53  5 (number five) */
  true, /* 54  6 (number six) */
  true, /* 55  7 (number seven) */
  true, /* 56  8 (number eight) */
  true, /* 57  9 (number nine) */
  false, /* 58  : (Colon) */
  false, /* 59  ; (Semicolon) */
  false, /* 60  < (Less-than sign) */
  false, /* 61  = (Equals sign) */
  false, /* 62  > (Greater-than sign ; Inequality)  */
  false, /* 63  ? (Question mark) */
  false, /* 64  @ (At sign) */
  true, /* 65  A (Capital A) */
  true, /* 66  B (Capital B) */
  true, /* 67  C (Capital C) */
  true, /* 68  D (Capital D) */
  true, /* 69  E (Capital E) */
  true, /* 70  F (Capital F) */
  true, /* 71  G (Capital G) */
  true, /* 72  H (Capital H) */
  true, /* 73  I (Capital I) */
  true, /* 74  J (Capital J) */
  true, /* 75  K (Capital K) */
  true, /* 76  L (Capital L) */
  true, /* 77  M (Capital M) */
  true, /* 78  N (Capital N) */
  true, /* 79  O (Capital O) */
  true, /* 80  P (Capital P) */
  true, /* 81  Q (Capital Q) */
  true, /* 82  R (Capital R) */
  true, /* 83  S (Capital S) */
  true, /* 84  T (Capital T) */
  true, /* 85  U (Capital U) */
  true, /* 86  V (Capital V) */
  true, /* 87  W (Capital W) */
  true, /* 88  X (Capital X) */
  true, /* 89  Y (Capital Y) */
  true, /* 90  Z (Capital Z) */
  false, /* 91  [ (square brackets or box brackets) */
  false, /* 92  \ (Backslash) */
  false, /* 93  ] (square brackets or box brackets) */
  true, /* 94  ^ (Caret or circumflex accent) */
  true, /* 95  _ (underscore , understrike , underbar or low line) */
  false, /* 96  ` (Grave accent) */
  true, /* 97  a (Lowercase  a ) */
  true, /* 98  b (Lowercase  b ) */
  true, /* 99  c (Lowercase  c ) */
  true, /* 100 d (Lowercase  d ) */
  true, /* 101 e (Lowercase  e ) */
  true, /* 102 f (Lowercase  f ) */
  true, /* 103 g (Lowercase  g ) */
  true, /* 104 h (Lowercase  h ) */
  true, /* 105 i (Lowercase  i ) */
  true, /* 106 j (Lowercase  j ) */
  true, /* 107 k (Lowercase  k ) */
  true, /* 108 l (Lowercase  l ) */
  true, /* 109 m (Lowercase  m ) */
  true, /* 110 n (Lowercase  n ) */
  true, /* 111 o (Lowercase  o ) */
  true, /* 112 p (Lowercase  p ) */
  true, /* 113 q (Lowercase  q ) */
  true, /* 114 r (Lowercase  r ) */
  true, /* 115 s (Lowercase  s ) */
  true, /* 116 t (Lowercase  t ) */
  true, /* 117 u (Lowercase  u ) */
  true, /* 118 v (Lowercase  v ) */
  true, /* 119 w (Lowercase  w ) */
  true, /* 120 x (Lowercase  x ) */
  true, /* 121 y (Lowercase  y ) */
  true, /* 122 z (Lowercase  z ) */
  false, /* 123 { (curly brackets or braces) */
  false, /* 124 | (vertical-bar, vbar, vertical line or vertical slash) */
  false, /* 125 } (curly brackets or braces) */
  false, /* 126 ~ (Tilde ; swung dash) */
  false /* 127 DEL (Delete) */
  };

  /**
   * IANA Registered primary media types as defined in IANA Media type registry.
   */
  protected static final String[] REGISTERED_TYPES = { "application", "audio", "font",
      "example", "image", "message", "model", "multipart", "text", "video" };

  protected String primaryType;
  protected String subType;
  /** Map of parameters. Case-insensitive search. */
  protected TreeMap parameters;

  /**
   * Parses a media type string with optional attributes. The parsers tries to
   * parse and fill up internal data structures as much as possible before
   * throwing errors. There is currently a limitation when parsing the syntax of
   * parameters, any errors in parameter syntax will always point to the start
   * of parameters index in {@link java.text.ParseException}.
   * 
   * @param rawdata
   *          [in] The Media type string
   * @throws ParseException
   *           Thrown if the syntax of the media type does not conform to the
   *           IETF RFC 2045 specification.
   */
  public MediaType(String rawdata) throws ParseException
  {
    parameters = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    // Parser exception, when parsing parameters.
    ParseException parameterException = null;
    StringTokenizer tokenizer;
    String attributeValue;
    String attributeName;
    int subTypeIndex = rawdata.indexOf(TYPE_SEPARATOR);
    if (subTypeIndex == -1)
    {
      throw new ParseException(
          "Missing type / subtype separator, is this a valid Media type", 0);
    }
    // Check syntax of primary type
    primaryType = rawdata.substring(0, subTypeIndex);
    primaryType = MediaType.parseElement(primaryType, 0);
    // Search for parameters
    int parameterIndex = rawdata.indexOf(';', subTypeIndex + 1);
    if (parameterIndex == -1)
    {
      subType = rawdata.substring(subTypeIndex + 1);
    }
    else
    {
      // Parse parameters
      subType = rawdata.substring(subTypeIndex + 1, parameterIndex);
      parameterException = parseParameters(rawdata,parameterIndex,parameters);
    }
    subType = MediaType.parseElement(subType, subTypeIndex + 1);
    if (parameterException != null)
      throw parameterException;
  }

  public MediaType(String primaryType, String subType) throws ParseException
  {
    parameters = new TreeMap(String.CASE_INSENSITIVE_ORDER);
    this.primaryType = MediaType.parseElement(primaryType, 0);
    this.subType = MediaType.parseElement(subType, 0);
  }
  
  protected static String toString(CharSequence sequence)
  {
    StringBuffer buffer = new StringBuffer(sequence.length()); 
    for (int i=0; i < sequence.length(); i++)
    {
      buffer.append(sequence.charAt(i));
    }
    return buffer.toString();
  }
  
  /** Parse parameters.
   * 
   * @param input [in] Input character seqeunce
   * @param fromIndex [in] Location where parameter information is found.
   * @param params [in,out] Map that will be filled up with the found parameters
   * @return Error condition, otherwise <code>null</code>
   */
  protected static ParseException parseParameters(CharSequence input, int fromIndex, TreeMap params)
  {
    StringTokenizer tokenizer;
    String attributeName;
    String attributeValue;
    // Search for parameters
    int parameterIndex = indexOf(input,';', fromIndex);
    // No parameters
    if (parameterIndex == -1)
    {
      return null;
    }
    else
    {
      // Parse parameters
      String parameterString = toString(input.subSequence(parameterIndex,input.length()));
      // Only ; character with nothing else ? length == 1 then error
      // otherwise continue parsing parameters
      if (parameterString.length() > 1)
      {
        tokenizer = new StringTokenizer(parameterString, ";");
        while (tokenizer.hasMoreTokens())
        {
          String token = tokenizer.nextToken();
          int index = token.indexOf('=');
          if (index == -1)
          {
            return new ParseException(
                "Parameter name syntax does not contain the '=' character.",
                parameterIndex);
          }
          attributeName = token.substring(0, index);
          attributeValue = token.substring(index + 1);
          if (attributeValue.length() == 0)
          {
            return new ParseException(
                "One of the parameter value is empty.", parameterIndex);
          }
          try
          {
            attributeName = MediaType.parseElement(attributeName, parameterIndex);
          } catch (ParseException e)
          {
            return e;
          }
          params.put(attributeName, attributeValue);
        }
      }
      else
      {
        return new ParseException(
            "Parameter separator is present, but with no actual parameters.",
            parameterIndex);
      }
    }
    return null;
  }

  /**
   * Returns the list of attributes associated with this media type.
   * 
   * @return List of attributes, always non-null.
   */
  public Set getParameters()
  {
    return parameters.keySet();
  }

  /**
   * Verifies the syntax of a type, or parameter name according to IETF
   * definition.
   */
  private static String parseElement(CharSequence value, int shiftIndex)
      throws ParseException
  {
    char c;
    int length = value.length();
    if ((length > MAX_LENGTH) || (length == 0))
    {
      throw new ParseException("Length of element must be between 1 and " + MAX_LENGTH
          + " characters", shiftIndex + 0);
    }
    for (int i = 0; i < length; i++)
    {
      c = value.charAt(i);
      if (c < 32)
      {
        throw new ParseException("Control characters not allowed", i + shiftIndex);
      }
      int index = c - 32;
      if (ALLOWED_CHARACTERS[index] == false)
      {
        throw new ParseException("Illegal character '" + c + "'", i + shiftIndex);
      }
    }
    return value.toString();
  }

  /**
   * Retrieves the primary type of this media type.
   * 
   * @return The primary type of this media type.
   */
  public String getPrimaryType()
  {
    return primaryType;
  }

  /**
   * Sets the primary type of this media type. This only verifies that the
   * syntax is valid and does not verify if the primary type is actually in the
   * list of registered types or not.
   * 
   * @param primaryType
   *          [in] The primary type.
   * @throws ParseException
   *           Thrown if the syntax does not conform to IETF RFC 4288
   *           <code>type-name</code> syntax.
   */
  public void setPrimaryType(String primaryType) throws ParseException
  {
    this.primaryType = MediaType.parseElement(primaryType, 0);
  }

  /**
   * Retrieves the sub-type of this media type.
   * 
   * @return The sub-type type of this media type.
   */
  public String getSubType()
  {
    return subType;
  }

  /**
   * Sets the sub-type of this media type.
   * 
   * @param primaryType
   *          [in] The subtype type.
   * @throws ParseException
   *           Thrown if the syntax does not conform to IETF RFC 4288
   *           <code>subtype-name</code> syntax.
   */
  public void setSubType(String subType) throws ParseException
  {
    this.subType = MediaType.parseElement(subType, 0);
  }

  /**
   * Return a String representation of this Media type, including its Media type
   * and sub-type, but excluding any attributes.
   * 
   * @return The MIME Media type and sub-type
   */
  public String getBaseType()
  {
    return primaryType + TYPE_SEPARATOR + subType;
  }

  /**
   * Returns the list of the IANA Mime primary-types. This list is current as
   * when this library was released, and may have been superseded.
   * 
   * @return The list of IANA registered primary Media types.
   */
  public String[] getRegisteredPrimaryTypes()
  {
    return REGISTERED_TYPES;
  }

  /**
   * Sets the attribute name and its associated value replacing any existing
   * parameter with same name.
   * 
   * @param name
   *          [in] The attribute name
   * @param value
   *          [in] The attribute value
   * @throws ParseException
   *           Thrown if the syntax does not conform to IETF RFC 4288
   *           <code>parameter-name</code> syntax.
   */
  public void setParameter(String name, String value) throws ParseException
  {
    name = MediaType.parseElement(name, 0);
    parameters.put(name, value);
  }

  /**
   * Retrieve the attribute value associated with the given name, or null if
   * there is no current association.
   * 
   * @param name
   *          [in] The name of the attribute.
   * @return The associated attribute value or <code>null</code> if not found.
   */
  public String getParameter(String name)
  {
    return (String) parameters.get(name);
  }

  /** Removes the specified parameter. */
  public void removeParameter(String name)
  {
    parameters.remove(name);
  }

  /**
   * Checks whether or not the specified MediaType matches this MediaType.
   * Matches only compares the primary type and sub-type for case-insensitive
   * equality, and if equal, verifies for equality for attributes that are
   * defined in the specified MediaType, unspecified attributes are ignored.
   * 
   * @param type
   *          [in] The media type to compare with.
   * @return true if both media types match.
   */
  public boolean matches(MediaType type)
  {
    if (primaryType.equalsIgnoreCase(type.primaryType) == false)
    {
      return false;
    }
    if (subType.equalsIgnoreCase(type.subType) == false)
    {
      return false;
    }
    // The specified media type does not contain any attributes
    // hence no more processing required, both media type matches.
    if (type.parameters.size() == 0)
    {
      return true;
    }

    // Check case of each attribute name and attribute
    // value
    Iterator props = type.parameters.keySet().iterator();
    while (props.hasNext())
    {
      String name = (String) props.next();
      Object otherValue = parameters.get(name);
      if (otherValue == null)
      {
        return false;
      }
      else
      {
        if (type.parameters.get(name).equals(parameters.get(name)) == false)
        {
          return false;
        }
      }
    } // end while
    return true;
  }

  protected static int indexOf(CharSequence input, char c, int fromIndex)
  {
    int inLength = input.length();
    if (fromIndex < inLength)
    {
      if (fromIndex < 0)
      {
        fromIndex = 0;
      }
      if (c >= 0 && c <= Character.MAX_VALUE)
      {
        for (int i = fromIndex; i < inLength; i++)
        {
          if (input.charAt(i) == c)
          {
            return i;
          }
        }
      }
    }
    return -1;
  }
  
  
  protected static int indexOfIgnoreCase(CharSequence input, char c, int fromIndex)
  {
    int inLength = input.length();
    c = Character.toUpperCase(c);
    if (fromIndex < inLength)
    {
      if (fromIndex < 0)
      {
        fromIndex = 0;
      }
      if (c >= 0 && c <= Character.MAX_VALUE)
      {
        for (int i = fromIndex; i < inLength; i++)
        {
          if (Character.toUpperCase(input.charAt(i)) == c)
          {
            return i;
          }
        }
      }
    }
    return -1;
  }
  

  /**
   * Search a sequence of characters in another sequence of characters and
   * returns the character index where it is found or -1 if not found.
   * 
   * @param input
   *          [in] The character sequence to search in.
   * @param toMatch
   *          [in] The character sequence to search for.
   * @param fromIndex
   *          [in] Start index in input to start search for.
   * @return The position where the first match was not found or -1 if not
   *         found.
   */
  protected static int indexOf(CharSequence input, CharSequence subString, int fromIndex)
  {
    int inLength = input.length();
    if (fromIndex < 0)
    {
      fromIndex = 0;
    }
    int subCount = subString.length();
    if (subCount > 0)
    {
      if (subCount + fromIndex > inLength)
      {
        return -1;
      }
      int subOffset = 0;
      char firstChar = subString.charAt(subOffset);
      int end = subOffset + subCount;
      while (true)
      {
        int i = MediaType.indexOf(input,firstChar, fromIndex);
        if (i == -1 || subCount + i > inLength)
        {
          return -1; // handles subCount > count || start >= count
        }
        int o1 = 0 + i, o2 = subOffset;
        while (++o2 < end && input.charAt(++o1) == subString.charAt(o2))
        {
          // Intentionally empty
        }
        if (o2 == end)
        {
          return i;
        }
        fromIndex = i + 1;
      }
    }
    return fromIndex < inLength ? fromIndex : inLength;
  }
  
  
  /**
   * Search a sequence of characters in another sequence of characters and
   * returns the character index where it is found or -1 if not found.
   * 
   * @param input
   *          [in] The character sequence to search in.
   * @param toMatch
   *          [in] The character sequence to search for.
   * @param fromIndex
   *          [in] Start index in input to start search for.
   * @return The position where the first match was not found or -1 if not
   *         found.
   */
  protected static int indexOfIgnoreCase(CharSequence input, CharSequence subString, int fromIndex)
  {
    int inLength = input.length();
    if (fromIndex < 0)
    {
      fromIndex = 0;
    }
    int subCount = subString.length();
    if (subCount > 0)
    {
      if (subCount + fromIndex > inLength)
      {
        return -1;
      }
      int subOffset = 0;
      char firstChar = subString.charAt(subOffset);
      int end = subOffset + subCount;
      while (true)
      {
        int i = MediaType.indexOfIgnoreCase(input,firstChar, fromIndex);
        if (i == -1 || subCount + i > inLength)
        {
          return -1; // handles subCount > count || start >= count
        }
        int o1 = 0 + i, 
        o2 = subOffset;
        while (++o2 < end && Character.toUpperCase(input.charAt(++o1)) == Character.toUpperCase(subString.charAt(o2)))
        {
          // Intentionally empty
        }
        if (o2 == end)
        {
          return i;
        }
        fromIndex = i + 1;
      }
    }
    return fromIndex < inLength ? fromIndex : inLength;
  }
  

  /**
   * Checks whether or not the specified media type signature matches this
   * MediaType. Matches only compares the primary type and sub-type for
   * case-insensitive equality, and if equal, verifies for equality for
   * attributes that are defined in the specified MediaType, unspecified
   * attributes are ignored.
   * 
   * @param type
   *          [in] The media type string to compare with.
   * @return true if both media types match.
   */
  public boolean matches(CharSequence rawdata)
  {
    int inLength = 0;
    int cmpLength = 0;
    int inOffset = 0;
    char c1;
    char c2;
    inLength = rawdata.length();

    // Check primary type tag
    int offset = indexOfIgnoreCase(rawdata,primaryType,0);
    if (offset == -1)
    {
      return false;
    }
    if (offset+1 > inLength)
    {
      return false;
    }
    offset = offset + primaryType.length();
    if (rawdata.charAt(offset)!=TYPE_SEPARATOR)
    {
      return false;
    }
    offset = indexOfIgnoreCase(rawdata,subType,offset);
    if (offset == -1)
    {
      return false;
    }
    if (offset+1 > inLength)
    {
      return false;
    }
    
    // Both primary and subtype are equal, point to any parameters
    inOffset = offset + subType.length();
    
    // No more characters, then there must absolutely be a match
    if (inOffset == rawdata.length())
    {
      return true;
    }
    // Invalid attribute information
    if (rawdata.charAt(inOffset)!=';')
    {
      return false;
    }
    TreeMap params = new TreeMap();
    ParseException e = parseParameters(rawdata,inOffset,params);
    // Check case of each attribute name and attribute
    // value
    Iterator props = params.keySet().iterator();
    while (props.hasNext())
    {
      String name = (String) props.next();
      Object otherValue = parameters.get(name);
      if (otherValue == null)
      {
        return false;
      }
      else
      {
        if (params.get(name).equals(parameters.get(name)) == false)
        {
          return false;
        }
      }
    } // end while
    return true;
  }

  /**
   * Extracts suffix if present from the sub-type. Suffixes define the
   * underlying structured format and is generally defined in IETF RFC 6839.
   * 
   * @return The structured suffix for this format or an empty string, if there
   *         is no suffix in this sub-type.
   */
  public String getSuffix()
  {
    int index = subType.lastIndexOf('+');
    if (index == -1)
    {
      return "";
    }
    return subType.substring(index + 1);
  }

  /**
   * Returns the full string representation of this media type. The
   * representation includes parameters. The order of parameters are ordered by
   * the string comparator, and not original ordering of the parameters.
   * 
   */
  public String toString()
  {
    String out = primaryType + TYPE_SEPARATOR + subType;
    if (parameters.size() > 0)
    {
      Iterator it = parameters.keySet().iterator();
      while (it.hasNext())
      {
        String name = (String) it.next();
        out = out + ";" + name + "=" + parameters.get(name).toString();
      }
    }
    return out;
  }

  /**
   * Returns true if the passed <code>MediaType</code> and this object have the
   * same primary and sub-types (case insensitive check) and they're the same
   * type and all of their parameters are equal.
   * 
   */
  public boolean equals(Object obj)
  {
    if (obj == this)
    {
      return true;
    }
    if ((obj instanceof MediaType) == false)
    {
      return false;
    }
    MediaType type = (MediaType) obj;
    if (primaryType.equalsIgnoreCase(type.primaryType) == false)
    {
      return false;
    }
    if (subType.equalsIgnoreCase(type.subType) == false)
    {
      return false;
    }

    if (parameters.size() != type.parameters.size())
    {
      return false;
    }

    // Check case of each attribute name and attribute
    // value. case insensitive match on name
    Iterator props = parameters.keySet().iterator();
    while (props.hasNext())
    {
      String name = (String) props.next();
      Object otherValue = type.parameters.get(name);
      if (otherValue == null)
      {
        return false;
      }
      else
      {
        if (type.parameters.get(name).equals(parameters.get(name)) == false)
        {
          return false;
        }
      }
    } // end while
    return true;
  }
}
