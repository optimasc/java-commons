package com.optimasc.datatypes.aggregate;

/** Represents an ordered list of elements, each of the elements is separated by
 *  the others by a space and a semi-colon. This the same separator used from the
 *  Adobe XMP Java implementation.
 *  
 * @author Carl Eric Codere
 *
 */
public class SequenceType extends ListType
{

  public SequenceType()
  {
    super();
    setOrdered(true);
    setDelimiter(";");
  }

}
