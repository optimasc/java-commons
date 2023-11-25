package com.optimasc.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/** This class represents a key-value pair. It is fully portable across the different
 *  platforms. It also contains utilities for converting a property to its binary
 *  representation. The binary representation of a property is as follows:
 *    
 *  [L1][V1][L2][V2] where the key is first stored follows by the actual value. 
 *  L1, L2 are actual number of bytes in the following values V1 and V2 stored
 *  as a big-endian 16-bit value. The Values are encoded as UTF-8 strings.
 *  
 *  
 * @author Carl Eric Codere
 *
 */
public class Property {
    private String key;
    private String value;
    private Hashtable attributes;
    
    public Property(String key, String value) {
        super();
        this.key = key;
        this.value = value;
        this.attributes = null;
    }
    
    public Property(String key, String value, Hashtable attributes)
    {
      super();
      this.key = key;
      this.value = value;
      this.attributes = attributes;
    }

    /** Returns the key associated with this property.  
     * 
     * @return The key associated with this property.
     */
    public String getKey() {
        return key;
    }

    /** Returns the value of this property.
     * 
     * @return The value of this property.
     */
    public String getValue() {
        return value;
    }
    
    /** Returns the specified attribute.
     * 
     * @param key The attribute to search for
     * @return The actual attribute value as an object
     *   or null if there are no attributes or if this
     *   key does not exist.
     */
    public Object getAttribute(Object key)
    {
      if (attributes!=null)
      {
        return attributes.get(key);
      }
      return null;
    }
    
    /** From an array of properties, search for the specified property */
    public static String getPropertyValue(Property[] props, String key) {
        for (int i = 0; i < props.length; i++) {
            if (props[i].getKey().equals(key)) {
                return props[i].getValue();
            }
        }
        return "";
    }
    
    
    /** From a binary representation of a property, returns its instance */
    public static Property propertyFromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream is = new DataInputStream(bais);

        String key = is.readUTF();
        String value = is.readUTF();

        bais.close();
        is.close();

        return new Property(key, value);
    }

    /** Convert a property value to a binary representation */
    public static byte[] toByteArray(Property prop) throws IOException {
        byte[] output = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);

        os.writeUTF(prop.getKey());
        os.writeUTF(prop.getValue());
        os.close();
        output = baos.toByteArray();
        baos.close();
        return output;
    }

    /** Writes out the visual representation of a property. The string 
     *  representation is the actual key=value string. */
    public String toString()
    {
      return key+"="+value;
    }

}
