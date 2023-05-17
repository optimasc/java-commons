package com.optimasc.datatypes;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.UnknownType;

/** Base class that represents an expression.
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class AbstractExpression
{ 
  protected static final Type UNKNOWN_TYPE = new UnknownType();
  
  /** The defined type associated with this expression. */
  protected Type expressionType;
  
  /** Creates an instance of an expression that 
   *  represents the specified type.
   * 
   * @param expressionTyp The type information
   */
  public AbstractExpression(Type expressionTyp)
  {
    this.expressionType = expressionTyp;
  }
  
  public Type getExpressionType()
  {
    return expressionType;
  }
  
  
  
  
}
