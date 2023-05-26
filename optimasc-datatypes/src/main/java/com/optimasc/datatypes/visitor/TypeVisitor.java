/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.visitor;

import com.optimasc.datatypes.UnknownType;
import com.optimasc.datatypes.VariableInstance;
import com.optimasc.datatypes.aggregate.ArrayType;
import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.aggregate.RecordType;
import com.optimasc.datatypes.aggregate.SetType;
import com.optimasc.datatypes.derived.ByteType;
import com.optimasc.datatypes.derived.CurrencyType;
import com.optimasc.datatypes.derived.DateType;
import com.optimasc.datatypes.derived.DoubleType;
import com.optimasc.datatypes.derived.IntType;
import com.optimasc.datatypes.derived.LatinCharType;
import com.optimasc.datatypes.derived.LongType;
import com.optimasc.datatypes.derived.ShortType;
import com.optimasc.datatypes.derived.SingleType;
import com.optimasc.datatypes.derived.TimestampType;
import com.optimasc.datatypes.derived.UCS2CharType;
import com.optimasc.datatypes.derived.UnsignedByteType;
import com.optimasc.datatypes.derived.UnsignedIntType;
import com.optimasc.datatypes.derived.UnsignedShortType;
import com.optimasc.datatypes.generated.PointerType;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.generated.UnionType;
import com.optimasc.datatypes.primitives.BinaryType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.datatypes.primitives.DurationType;
import com.optimasc.datatypes.primitives.EnumType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.PrimitiveType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.primitives.StringType;
import com.optimasc.datatypes.primitives.TimeType;
import com.optimasc.datatypes.primitives.VoidType;

/**
 *
 * @author Carl
 */
public interface TypeVisitor {
  
  /********************** Primitive types *************************/
  public Object visit(BinaryType n, Object arg);

  public Object visit(BooleanType n, Object arg);
  
  public Object visit(CharacterType n, Object arg);
  
  public Object visit(DateTimeType n, Object arg);
  
  public Object visit(DurationType n, Object arg);
  
  public Object visit(EnumType n, Object arg);

  public Object visit(IntegralType n, Object arg);
  
  public Object visit(RealType n, Object arg);
  
  public Object visit(StringType n, Object arg);
  
  public Object visit(TimeType n, Object arg);
  
  public Object visit(VoidType n, Object arg);
  
  
  /********************** Aggregate types *************************/
  
  public Object visit(ArrayType n, Object arg);

  public Object visit(ClassType n, Object arg);

  public Object visit(RecordType n, Object arg);
  
  /********************** Generated types *************************/
  
  public Object visit(PointerType n, Object arg);
  
  public Object visit(ProcedureType n, Object arg);
  
  public Object visit(UnionType n, Object arg);

  /********************** Derived types *************************/
  
  public Object visit(DateType n, Object arg);
  
  public Object visit(LatinCharType n, Object arg);

  public Object visit(TimestampType n, Object arg);

  public Object visit(UCS2CharType n, Object arg);

  public Object visit(UnknownType n, Object arg);

  public Object visit(VariableInstance n, Object arg);

  /************************* Derived datatypes ***************************/

    public Object visit(ByteType n, Object arg);

    public Object visit(DoubleType n, Object arg);

    public Object visit(IntType n, Object arg);

    public Object visit(LongType n, Object arg);

    public Object visit(ShortType n, Object arg);

    public Object visit(SingleType n, Object arg);

    public Object visit(UnsignedByteType n, Object arg);

    public Object visit(UnsignedIntType n, Object arg);

    public Object visit(UnsignedShortType n, Object arg);
    
    public Object visit(CurrencyType n, Object arg);
    
    public Object visit(SetType n, Object arg);
    
    
}
