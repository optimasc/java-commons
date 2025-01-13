package com.optimasc.lang;

/** Class representing the type of transfer encoding of the 
 *  characters, based on Unicode character repertoire.
 * 
 */
public class TransferEncoding
{
  /** UTF-8 transfer encoding Object identifier according to ISO/IEC 10646 */  
  protected static final String UTF8_OID = "1.0.10646.1.0.8";
  /** UTF-8 transfer encoding IANA Character set registration name */  
  protected static final String UTF8_IANA_NAME = "UTF-8";
  
  /** UCS-2 transfer encoding Object identifier according to ISO/IEC 10646.
   *  It either includes a BOM or if not present, it is in network byte
   *  order (big-endian) */  
  protected static final String UCS2_OID = "1.0.10646.1.0.2";
  /** UCS2 transfer encoding IANA Character set registration name */  
  protected static final String UCS2_IANA_NAME = "ISO-10646-UCS-2";
  
  /** UTF-16 transfer encoding Object identifier according to ISO/IEC 10646.
   *  It either includes a BOM or if not present, it is in network byte
   *  order (big-endian) */  
  protected static final String UTF16_OID = "1.0.10646.1.0.5";
  /** UCS2 transfer encoding IANA Character set registration name */  
  protected static final String UTF16_IANA_NAME = "UTF-16";
  
  /** UTF-32 transfer encoding Object identifier according to ISO/IEC 10646.
   *  It either includes a BOM or if not present, it is in network byte
   *  order (big-endian) */  
  protected static final String UTF32_OID = "1.0.10646.1.0.4";
  /** UCS2 transfer encoding IANA Character set registration name */  
  protected static final String UTF32_IANA_NAME = "UTF-32";
  
  /** UTF-8 transfer encoding according to ISO/IEC 10646 */  
  public static final TransferEncoding UTF8 = new TransferEncoding(UTF8_OID,UTF8_IANA_NAME);
  /** UCS-2 transfer encoding according to ISO/IEC 10646.
   *  It either includes a BOM or if not present, it is in network byte
   *  order (big-endian) */  
  public static final TransferEncoding UCS2 = new TransferEncoding(UCS2_OID,UCS2_IANA_NAME);
  /** UTF-16 transfer encoding according to ISO/IEC 10646.
   *  It either includes a BOM or if not present, it is in network byte
   *  order (big-endian) */  
  public static final TransferEncoding UTF16 = new TransferEncoding(UTF16_OID,UTF16_IANA_NAME);
  /** UTF-32 transfer encoding according to ISO/IEC 10646.
   *  It either includes a BOM or if not present, it is in network byte
   *  order (big-endian) */  
  public static final TransferEncoding UTF32 = new TransferEncoding(UTF32_OID,UTF32_IANA_NAME);
  
  /** Object identifier associated with the transfer encoding. */
  protected final String oid;
  /** IANA Character set encoding name.*/
  protected final String name;

  
  /** Creates a new transfer encoding record.
   * 
   * @param oid [in] The Object identifier associated with 
   *  this character transfer encoding. This value should
   *  be composed of non-negative integer values separated
   *  by a '.' character, such as 1.2.3.4.
   * @param name [in] The IANA Character set associated
   *  with this character encoding. This value may
   *  be set to <code>null</code> if there is no IANA
   *  registry entry. 
   */
  public TransferEncoding(String oid, String name)
  {
    super();
    this.oid = oid;
    this.name = name;
  }

  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((oid == null) ? 0 : oid.hashCode());
    return result;
  }
  
  
  
  /** Return the OBJECT IDENTIFIER associated with this
   *  transfer character encoding.
   *  
   * @return The transfer encoding object identifier
   *  as a string of non-negative integer's separated
   *  by a dot.
   */
  public String getOid()
  {
    return oid;
  }

  /** Return the IANA Character set encoding
   *  name.
   * 
   * @return The IANA character set name 
   *   representing this encoding, or
   *   <code>null</code> if there are none.
   */
  public String getName()
  {
    return name;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if ((obj instanceof TransferEncoding)==false)
      return false;
    TransferEncoding other = (TransferEncoding) obj;
    if (name == null)
    {
      if (other.name != null)
        return false;
    }
    else if (!name.equals(other.name))
      return false;
    if (oid == null)
    {
      if (other.oid != null)
        return false;
    }
    else if (!oid.equals(other.oid))
      return false;
    return true;
  }

}
