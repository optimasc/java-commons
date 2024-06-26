package com.optimasc.datatypes.aggregate;

import java.awt.List;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.Vector;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.derived.UCS2StringType;
import com.optimasc.datatypes.generated.StringTypeEx;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.utils.VisualList;
import com.optimasc.datatypes.visitor.TypeVisitor;


/** This datatype represents a sequence datatype consisting
 *  of a set of ordered items in a specified order. Internally
 *  this is represented in the Java language as a <code>Vector</code>. 
 *  Several items with the same value are allowed.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>list</code> type in XMLSchema</li>
 *   <li><code>Seq</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>SEQUENCE</code> ASN.1 datatype</li>
 *  </ul>
 * 
 * @author Carl Eric Codere
 */
public abstract class ListType extends Datatype implements LengthFacet, ConstructedSimple
{
  protected LengthHelper lengthHelper;
  protected TypeReference datatypeReference;
  protected Type datatype;
  /* Delimiter between the list items, by default the space character. */
  protected String delimiter = " ";
  
  public ListType()
  {
      super(Datatype.OTHER,true);
      lengthHelper = new LengthHelper();
      setMinLength(0);
      setMaxLength(Integer.MAX_VALUE);
      setBaseTypeReference(UCS2StringType.TYPE_REFERENCE);
  }
  
  
  public Class getClassType()
  {
    return Vector.class;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    int i;
    checkClass(value);
    Vector v = (Vector)value;
    int size = v.size();
    if ((size < getMinLength()) || (size > getMaxLength()))
    {
      DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE, "Sequence has an invalid length.");
    }
    /* Check each value in the list. */
    for (i = 0; i < size; i++)
    {
      Object item = v.elementAt(i);
      if (datatypeReference.getType() instanceof Datatype)
      {
        ((Datatype)datatypeReference.getType()).validate(item);
      }
    }
  }

  public Object parse(String value) throws ParseException
  {
    Vector<Object> v = new VisualList<Object>();
    StringTokenizer tokenizer = new StringTokenizer(value, delimiter);
    while (tokenizer.hasMoreTokens()) 
    {
      String itemValue = tokenizer.nextToken();
      Object o = itemValue;
      if (datatype instanceof Parseable)
      {
         o = ((Parseable)datatype).parseObject(itemValue);
      }
      v.addElement(o);
    }
    try
    {
      validate(v);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Error while parsing list items.",0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Error while parsing list items.",0);
    }
    return v;
  }

  public TypeReference getBaseTypeReference()
  {
    return datatypeReference;
  }

  public void setBaseTypeReference(TypeReference value)
  {
    datatypeReference = value;
    datatype = value.getType();
  }
  

  public void setMinLength(int value)
  {
    lengthHelper.setMinLength(value);
  }


  public void setMaxLength(int value)
  {
    lengthHelper.setMaxLength(value);
  }


  public int getMinLength()
  {
    return lengthHelper.getMinLength();
  }


  public int getMaxLength()
  {
    return lengthHelper.getMaxLength();
  }

  /** Return the list lexical delimiter when parsing a list from a string. */
  public String getDelimiter()
  {
    return delimiter;
  }
  
  /** Sets the list lexical delimiter when parsing a list from a string. */
  public void setDelimiter(String delimiter)
  {
    if (delimiter.length()>1)
    {
      throw new IllegalArgumentException("The delimiter must be one character in length.");
    }
    this.delimiter = delimiter; 
  }


  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }
    ListType seqType;
    if (!(obj instanceof ListType))
    {
      return false;
    }
    seqType = (ListType) obj;
    if (this.delimiter.equals(seqType.delimiter)==false)
    {
      return false;
    }
    if (this.datatypeReference.equals(seqType.datatypeReference)==false)
    {
      return false;
    }
    if (this.getMinLength()!=seqType.getMinLength())
    {
      return false;
    }
    if (this.getMaxLength()!=seqType.getMaxLength())
    {
      return false;
    }
    return true;
  }


  public boolean isOrdered()
  {
    return ordered;
  }


  public void setOrdered(boolean ordered)
  {
    this.ordered = ordered;
  }
  
  
  
}
