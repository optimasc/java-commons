package com.optimasc.datatypes.aggregate;

/** Represents an unordered list of elements, each of the elements is separated by
 *  the others by a space and a semi-colon. This the same separator used from the
 *  Adobe XMP Java implementation.
 *  
 * @author Carl Eric Codere
 *
 */
public class BagType extends ListType
{
  public BagType()
  {
    super();
    setOrdered(true);
    setDelimiter(";");
  }
}
