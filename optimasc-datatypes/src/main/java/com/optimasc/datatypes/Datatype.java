package com.optimasc.datatypes;

import java.text.ParseException;

import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a generic datatype. This is the base class for all possible
 *  datatypes. It has several important properties:
 *  
 *  <ul>
 *  <li>{@link #getClassType()} returns the class associated with the value of this type. </li>
 *  </ul>
 * 
 * @author Carl Eric Codere
 */
public abstract class Datatype extends Type
{
  public Datatype(boolean ordered)
  {
    super(ordered);
  }

  public Datatype(String comment, boolean ordered)
  {
    super(ordered);
    this.comment = comment;
  }

}
