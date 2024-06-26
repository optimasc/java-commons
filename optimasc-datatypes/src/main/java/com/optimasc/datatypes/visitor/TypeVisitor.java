/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.visitor;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.UnknownType;
import com.optimasc.datatypes.aggregate.ArrayType;
import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.aggregate.InterfaceType;
import com.optimasc.datatypes.aggregate.RecordType;
import com.optimasc.datatypes.aggregate.SetType;
import com.optimasc.datatypes.aggregate.TableType;
import com.optimasc.datatypes.defined.ASCIICharType;
import com.optimasc.datatypes.defined.BinaryType;
import com.optimasc.datatypes.defined.ByteType;
import com.optimasc.datatypes.defined.DoubleType;
import com.optimasc.datatypes.defined.LatinCharType;
import com.optimasc.datatypes.defined.LongType;
import com.optimasc.datatypes.defined.ObjectIdentifierType;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.defined.UnsignedByteType;
import com.optimasc.datatypes.derived.CurrencyType;
import com.optimasc.datatypes.derived.DateType;
import com.optimasc.datatypes.derived.IntType;
import com.optimasc.datatypes.derived.LatinStringType;
import com.optimasc.datatypes.derived.ShortType;
import com.optimasc.datatypes.derived.SingleType;
import com.optimasc.datatypes.derived.TimestampType;
import com.optimasc.datatypes.derived.UCS2CharType;
import com.optimasc.datatypes.derived.UCS2StringType;
import com.optimasc.datatypes.derived.UnsignedIntType;
import com.optimasc.datatypes.derived.UnsignedShortType;
import com.optimasc.datatypes.derived.VisibleCharType;
import com.optimasc.datatypes.generated.FormalParameterType;
import com.optimasc.datatypes.generated.PointerType;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.generated.ReferenceType;
import com.optimasc.datatypes.generated.UnionType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.primitives.DecimalType;
import com.optimasc.datatypes.primitives.DurationType;
import com.optimasc.datatypes.primitives.EnumType;
import com.optimasc.datatypes.primitives.ExceptionType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.NameSpaceType;
import com.optimasc.datatypes.primitives.PrimitiveType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.primitives.TimeType;
import com.optimasc.datatypes.primitives.VoidType;

/**
 *
 * @author Carl
 */
public interface TypeVisitor {
  
  public Object visit(ASCIICharType n, Object arg);
  
  public Object visit(ArrayType n, Object arg);

  public Object visit(BinaryType n, Object arg);
  
  public Object visit(BooleanType n, Object arg);
  
  public Object visit(ByteType n, Object arg);
  
  public Object visit(ClassType n, Object arg);

  public Object visit(CurrencyType n, Object arg);
  
  public Object visit(DateType n, Object arg);
  
  public Object visit(DecimalType n, Object arg);
  
  public Object visit(DoubleType n, Object arg);
  
  public Object visit(DurationType n, Object arg);
  
  public Object visit(EnumType n, Object arg);
  
  public Object visit(ExceptionType n, Object arg);
  
  public Object visit(FormalParameterType n, Object arg);
  
  public Object visit(IntegralType n, Object arg);

  public Object visit(InterfaceType n, Object arg);
  
  public Object visit(IntType n, Object arg);

  public Object visit(LatinCharType n, Object arg);
  
  public Object visit(LatinStringType n, Object arg);
  
  public Object visit(LongType n, Object arg);
  
  public Object visit(NamedTypeReference n, Object arg);
  
  public Object visit(NameSpaceType n, Object arg);
  
  public Object visit(ObjectIdentifierType n, Object arg);
  
  public Object visit(PointerType n, Object arg);
  
  public Object visit(ProcedureType n, Object arg);

  public Object visit(RealType n, Object arg);

  public Object visit(RecordType n, Object arg);

  public Object visit(ReferenceType n, Object arg);

  public Object visit(SetType n, Object arg);

    public Object visit(ShortType n, Object arg);

    public Object visit(SingleType n, Object arg);
    
    public Object visit(TableType n, Object arg);

    public Object visit(TimestampType n, Object arg);

    public Object visit(TimeType n, Object arg);

    public Object visit(UCS2CharType n, Object arg);

    public Object visit(UCS2StringType n, Object arg);

    public Object visit(UnionType n, Object arg);

    public Object visit(UnknownType n, Object arg);
    
    public Object visit(UnnamedTypeReference n, Object arg);

    public Object visit(UnsignedByteType n, Object arg);
    
    public Object visit(UnsignedIntType n, Object arg);
    
    public Object visit(UnsignedShortType n, Object arg);
    
    public Object visit(VisibleCharType n, Object arg);
    
    public Object visit(VoidType n, Object arg);
    
}
