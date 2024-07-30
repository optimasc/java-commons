package com.optimasc.lang;

import com.optimasc.lang.OrdinalSelecting.OrdinalSelectRange;

/** Represents a character set repertoire used 
 *  to identify character sets. A character set
 *  repertoire is identified by its Object Identifier,
 *  and optionally by its IANA Character set name
 *  and its MIBEnum. 
 * 
 * @author Carl Eric Codere
 *
 */
public class CharacterSet
{
  /**
   * The minimum value of a supplementary code point, {@code U+010000}.
   */
  public static final int MIN_SUPPLEMENTARY_CODE_POINT = 0x10000;

  /**
   * The minimum code point value, {@code U+0000}.
   */
  public static final int MIN_CODE_POINT = 0x000000;

  /**
   * The maximum code point value, {@code U+10FFFF}.
   */
  public static final int MAX_CODE_POINT = 0x10FFFF;
  
  
  public static class Blocks
  {
    /** Control characters group C0 defined by ISO 6429.*/
    public static OrdinalSelecting CONTROL_CHARACTERS_C0 = 
        new OrdinalSelectRange(0x0000,0x001F);
    /** Control characters group C1 defined by ISO 6429.*/
    public static OrdinalSelecting CONTROL_CHARACTERS_C1 = 
        new OrdinalSelectRange(0x007F,0x009F);
    
    /** Graphic characters defined by ISO-646:IRV. The
     *  block name is the one defined in the Unicode standard */
    public static OrdinalSelecting BASIC_LATIN =
        new OrdinalSelectRange(0x0020,0x007F);
    
    /** Extended Latin characters defined by Unicode. */
    public static OrdinalSelecting LATIN_1_SUPPLEMENT = 
        new OrdinalSelectRange(0x00A0,0x00FF);

    /** Unicode basic multilanguage plane definition */
    public static OrdinalSelecting BMP[] = new OrdinalSelecting[]
    {
      new OrdinalSelectRange(0x0000,0xD7FF),
      new OrdinalSelectRange(0xE000,0xFFFD),
    };    
    
    /** Full Unicode  definition */
    public static OrdinalSelecting UNICODE[] = new OrdinalSelecting[]
    {    
        new OrdinalSelectRange(0x0000,0xFDCF),
        new OrdinalSelectRange(0xFDF0,0xFFFD),
        new OrdinalSelectRange(0x10000,0x1FFFD),
        new OrdinalSelectRange(0x10000,0x1FFFD),
        new OrdinalSelectRange(0x20000,0x2FFFD),
        new OrdinalSelectRange(0x30000,0x3FFFD),        
        new OrdinalSelectRange(0x40000,0x4FFFD),        
        new OrdinalSelectRange(0x50000,0x5FFFD),        
        new OrdinalSelectRange(0x60000,0x6FFFD),
        new OrdinalSelectRange(0x70000,0x7FFFD),        
        new OrdinalSelectRange(0x80000,0x8FFFD),        
        new OrdinalSelectRange(0x90000,0x9FFFD),        
        new OrdinalSelectRange(0xA0000,0xAFFFD),        
        new OrdinalSelectRange(0xB0000,0xBFFFD),        
        new OrdinalSelectRange(0xC0000,0xCFFFD),        
        new OrdinalSelectRange(0xD0000,0xDFFFD),        
        new OrdinalSelectRange(0xE0000,0xEFFFD),        
        new OrdinalSelectRange(0xF0000,0xFFFFD),        
        new OrdinalSelectRange(0x100000,0x10FFFD)        
    };   
  }
  
  
  /** Object identifier for US-ASCII Character set repertoire, 
   *  which includes control characters. The OID is taken from
   *  the LDAP specification. This is equivalent to ASN.1
   *  <code>IA5String</code> datatype.
   */
  protected static final String US_ASCII_OID = "1.3.6.1.4.1.1466.115.121.1.26";
  /** IANA Character set name for US-ASCII Character set, 
   *  which includes control characters.
   */
  protected static final String US_ASCII_IANA_NAME = "US-ASCII";
  /** IANA Character set enum ID for US-ASCII character set repertoire, 
   *  which includes control characters.
   */
  protected static final Integer US_ASCII_ID = new Integer(3);
  
  
  /** Object identifier for ASN.1 PrintableString string
   *  type, which is a subset of US-ASCII. It permits
   *  the following <code>[A-Za-z0-9] '()+,-./:=?<code> 
   *  characters.
   */
  protected static final String PRINTABLE_STRING_OID = "2.1.0.1.1"; 

  /** Object identifier for ASN.1 NumericString string
   *  type, which is a subset of US-ASCII. It permits
   *  the following <code>[0-9 ]<code> characters.
   */
  protected static final String NUMERIC_STRING_OID = "2.1.0.1.0";
  
  /** Object identifier for ISO-8859-1 Character set repertoire, 
   *  which excludes control characters. The OID is based 
   *  on the rules defined in ISO/IEC 10646 and contains the allowed
   *  collections.
   */
  protected static final String LATIN1_OID = "1.0.10646.0.3.1.1.2";
  /** IANA Character set name for ISO-8859-1 Character set, 
   *  which excludes control characters.
   */
  protected static final String LATIN1_IANA_NAME = "ISO-10646-Unicode-Latin1";
  /** IANA Character set enum ID for LATIN1 character set repertoire, 
   *  which excludes control characters.
   */
  protected static final Integer LATIN1_ID = new Integer(1003);
  
  
  /** Object identifier for ISO-8859-1 Character set repertoire, 
   *  which also includes control characters. The OID is based 
   *  on the rules defined in ISO/IEC 10646 and contains the allowed
   *  collections.
   */
  protected static final String ISO_8859_1_OID = "1.0.10646.0.3.1.1.2.208";
  /** IANA Character set name for ISO-8859-1 Character set, 
   *  which includes control characters.
   */
  protected static final String ISO_8859_1_IANA_NAME = "ISO-8859-1";
  /** IANA Character set enum ID for ISO-8859-1 character set repertoire, 
   *  which includes control characters.
   */
  protected static final Integer ISO_8859_1_ID = new Integer(4);
  
  /** Object identifier for printable ASCII Character set repertoire 
   *  (0020-007E). The OID is based  on the rules defined in ISO/IEC 10646 
   *  and contains the allowed collections. This value is equivalent
   *  to the VisibleString ASN.1 datatype.
   */
  protected static final String VISIBLE_STRING_OID = "1.0.10646.0.3.1.1";
  /** IANA Character set name for printable ASCII character set 
   *  repertoire (0020-007E).
   */
  protected static final String VISIBLE_STRING_NAME = "ISO-10646-UCS-Basic";
  /** IANA Character set enum ID for Visible Ascii string character set repertoire, 
   *  that does not include control characters. It is based on the Unicode
   *  definition and usage of Collection 1.
   */
  protected static final Integer VISIBLE_STRING_ID = new Integer(1002);
  
  
  /** Object identifier for Basic multilanguage plane Character set repertoire 
   *  which also contains control characters. The OID is based  on the rules defined in ISO/IEC 10646 
   *  and contains the allowed collections. This value is equivalent
   *  to the BMPString ASN.1 datatype.
   */
  protected static final String BMP_STRING_OID = "1.0.10646.0.3.1.208.302";
  
  /** Object identifier for Full Unicode Character set repertoire 
   *  which also contains control characters. The OID is based  on the 
   *  rules defined in ISO/IEC 10646  and contains the allowed collections. 
   *  This value is equivalent to the UniveralString ASN.1 datatype.
   */
  protected static final String UNIVERSAL_STRING_OID = "1.0.10646.0";
  /** IANA Character set name for Full Unicode Character set repertoire, 
   *  which includes control characters.
   */
  protected static final String UNIVERSAL_STRING_NAME = "ISO-10646-UCS-4";
  /** IANA Character set enum ID for Full Unicode Character set repertoire, 
   *  which includes control characters.
   */
  protected static final Integer UNIVERSAL_ID = new Integer(1001);
  
  
  /** The object identifier associated with this character repertoire identifier. */
  public String oid;
  /** The parent character set repertoire identifier, or <code>null</code> if empty. */
  public CharacterSet parent;
  /** IANA Character set name, or null if not known. */
  public String name;
  /** IANA MIBenum value, or <code>null</code> if not known. */
  public Integer id;
  
  /** The full Unicode character set repertoire. Root of all character set
   *  repertoire identified here. This is equivalent to ASN.1 <code>UniversalString<code>. */ 
  public static final CharacterSet UNICODE = new CharacterSet(UNIVERSAL_STRING_OID,null,
      UNIVERSAL_STRING_NAME, UNIVERSAL_ID,Blocks.UNICODE);
  
  /** The Basic Multilingual Plane character set repertoire of Unicode, including
   *  control codes (C0,C1). This is equivalent to ASN.1 <code>BMPString</code>.
   * 
   */
  public static final CharacterSet BMP = new CharacterSet(BMP_STRING_OID,UNICODE,
      null, null,Blocks.BMP);
  
  /** ISO 8859-1 character set repertoire, with the control characters (C0,C1). This is equivalent
   *  to the <code>ISO8BIT</code> SQL2003 character repertoire. */ 
  public static final CharacterSet ISO8BIT = new CharacterSet(ISO_8859_1_OID,BMP,ISO_8859_1_IANA_NAME,
      ISO_8859_1_ID,new OrdinalSelecting[]{Blocks.CONTROL_CHARACTERS_C0,Blocks.BASIC_LATIN, Blocks.CONTROL_CHARACTERS_C1,
      Blocks.LATIN_1_SUPPLEMENT});
  
  
  /** ISO 8859-1 character set repertoire, without the control characters (C0,C1). This is equivalent
   *  to the ISO/IEC 8859-1 standard and <code>LATIN1<code> SQL2003 character repertoire. */ 
  public static final CharacterSet LATIN1 = new CharacterSet(LATIN1_OID,ISO8BIT,LATIN1_IANA_NAME,
      LATIN1_ID,new OrdinalSelecting[]{Blocks.BASIC_LATIN, Blocks.LATIN_1_SUPPLEMENT});
  
  
  /** US-ASCII based on ISO 646 character set repertoire, with control characters (C0,C1).
   *  This is equivalent to ASN.1 <code>IA5String</code>
   */
  public static final CharacterSet ASCII = new CharacterSet(US_ASCII_OID,LATIN1,US_ASCII_IANA_NAME,
      US_ASCII_ID, new OrdinalSelecting[]{Blocks.CONTROL_CHARACTERS_C0,Blocks.BASIC_LATIN});
  
  /** ISO 646 IRV / US-ASCII character set repertoire, without the control characters.
   *  This is equivalent to ASN.1 <code>VisibleString</code> and SQL2003 <code>GRAPHIC_IRV</code>
   *  character repertoire.
   */
  public static final CharacterSet GRAPHIC_IRV = new CharacterSet(VISIBLE_STRING_OID,ASCII,VISIBLE_STRING_NAME,
      VISIBLE_STRING_ID,new OrdinalSelecting[]{Blocks.BASIC_LATIN});
  
  protected OrdinalSelecting[] selectingItems;
  
  public CharacterSet(String oid, CharacterSet parent, String name, Integer id, OrdinalSelecting[] items)
  {
    super();
    this.oid = oid;
    this.parent = parent;
    this.name = name;
    this.id = id;
    this.selectingItems = items;
  }
  
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((oid == null) ? 0 : oid.hashCode());
    result = prime * result + ((parent == null) ? 0 : parent.hashCode());
    return result;
  }
  
  /** Returns true if this character set definition
   *  is included in the character set repertoire of the
   *  one passed in as parameter. 
   * 
   * @param charSet [in] The parent character subset.
   * @return true if this character set is a subset 
   *  of the specified character set.
   */
  public boolean isRestrictionOf(CharacterSet charSet)
  {
    CharacterSet p = parent; 
    while (p != null)
    {
      if (charSet.equals(p))
      {
        return true;
      }
      p = p.parent;
    }
    return false;
  }
  
  /** Verifies if this codepoint is valid in this character
   *  set repertoire.
   * 
   * @param codePoint [in] The unicode codepoint to verify.
   * @return <code>true</code> if this codepoint can be represented 
   *  in the character repertoire, otherwise <code>false</code>.
   */
  public boolean isValid(long codePoint)
  {
    return OrdinalSelecting.isValid(selectingItems, codePoint);
  }


  /** Character set repertoire are equal 
   *  if both Object identifiers are equal.
   *  The other parameters are not compared.
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if ((obj instanceof CharacterSet)==false)
      return false;
    CharacterSet other = (CharacterSet) obj;
    if (id == null)
    {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    return true;
  }


  /**
   * Indicates whether {@code codePoint} is a valid Unicode code point.
   *
   * @param codePoint
   *            the code point to test.
   * @return {@code true} if {@code codePoint} is a valid Unicode code point;
   *         {@code false} otherwise.
   */
  public static boolean isValidCodePoint(int codePoint) {
      return (MIN_CODE_POINT <= codePoint && MAX_CODE_POINT >= codePoint);
  }

  /**
   * Indicates whether {@code codePoint} is within the supplementary code
   * point range.
   *
   * @param codePoint
   *            the code point to test.
   * @return {@code true} if {@code codePoint} is within the supplementary
   *         code point range; {@code false} otherwise.
   */
  public static boolean isSupplementaryCodePoint(int codePoint) {
      return (MIN_SUPPLEMENTARY_CODE_POINT <= codePoint && MAX_CODE_POINT >= codePoint);
  }


  /**
   * Converts the specified Unicode code point into a UTF-16 encoded sequence
   * and returns it as a char array.
   * 
   * @param codePoint
   *            the Unicode code point to encode.
   * @return the UTF-16 encoded char sequence. If {@code codePoint} is a
   *         {@link #isSupplementaryCodePoint(int) supplementary code point},
   *         then the returned array contains two characters, otherwise it
   *         contains just one character.
   * @throws IllegalArgumentException
   *             if {@code codePoint} is not a valid Unicode code point.
   */
  public static char[] toChars(int codePoint) 
  {
      if (!isValidCodePoint(codePoint)) 
      {
          throw new IllegalArgumentException();
      }

      if (isSupplementaryCodePoint(codePoint)) {
          int cpPrime = codePoint - 0x10000;
          int high = 0xD800 | ((cpPrime >> 10) & 0x3FF);
          int low = 0xDC00 | (cpPrime & 0x3FF);
          return new char[] { (char) high, (char) low };
      }
      return new char[] { (char) codePoint };
  }
  
  /** Returns the lowest range of the allowed codepoint
   *  for this datatype.
   * 
   * @return
   */
  public long getMinInclusive()
  {
    return OrdinalSelecting.getMinInclusive(selectingItems);
  }
  
  /** Returns the highest range of the allowed codepoint
   *  for this datatype.
   * 
   * @return
   */
  public long getMaxInclusive()
  {
    return OrdinalSelecting.getMaxInclusive(selectingItems);
  }
  


}
