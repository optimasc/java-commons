package com.optimasc.utils;

/** Represents an attribute value. 
 * 
 * @author Carl
 *
 */
public class Attribute
{
   /** The namespace of this attribute */
   public String namespaceURI;
   /** The local name of this attribute */ 
   public String localName;
   /** The value of this attribute */
   public String value;
   
   public Attribute(String nameSpaceURI, String localName, String value)
   {
     this.namespaceURI = nameSpaceURI;
     this.localName = localName;
     this.value = value;
   }
   
   public Attribute()
   {
   }  
   
   public String toString()
   {
     String suffix = "";
     String value = "";
     if ((localName!=null) && (localName.length()!=0))
     {
       suffix = localName;
     }
     if ((value!=null) && (value.length()!=0))
     {
       suffix = localName;
     }
     return suffix+"="+value;
   }
}
