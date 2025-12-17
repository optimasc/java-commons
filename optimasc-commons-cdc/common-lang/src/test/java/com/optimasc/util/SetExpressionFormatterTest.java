package com.optimasc.util;

import java.text.ParsePosition;
import java.util.List;

import com.optimasc.util.BasicRegexPattern;
import com.optimasc.util.SetExpressionFormatter;
import com.optimasc.util.SetExpressionFormatter.SelectingExpression;
import com.optimasc.lang.OrdinalSelectItem;
import com.optimasc.lang.OrdinalSelectItems.OrdinalSelectRange;
import com.optimasc.lang.OrdinalSelectItems.OrdinalSelectValue;
import com.optimasc.lang.OrdinalSelectItem.*;

import junit.framework.TestCase;

public class SetExpressionFormatterTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public void testBracketExpression()
  {
    ParsePosition pos = new ParsePosition(0);
    SetExpressionFormatter formatter = new SetExpressionFormatter();
    
    // Standard selecting value
    SelectingExpression selections = (SelectingExpression) formatter.parseObject("[012]",pos);

    assertEquals(3,selections.size());
    OrdinalSelectValue v = (OrdinalSelectValue) selections.get(0);
    assertEquals('0',v.getValue().intValue());
    v = (OrdinalSelectValue) selections.get(1);
    assertEquals('1',v.getValue().intValue());
    v = (OrdinalSelectValue) selections.get(2);
    assertEquals('2',v.getValue().intValue());
    assertEquals(5,pos.getIndex());
    assertEquals(-1,pos.getErrorIndex());
    
    // Range value
    pos.setIndex(0);
    selections = (SelectingExpression) formatter.parseObject("[0-2]",pos);
    assertEquals(1,selections.size());
    OrdinalSelectRange range = (OrdinalSelectRange) selections.get(0);
    assertEquals('0',range.getMinInclusive().intValue());
    assertEquals('2',range.getMaxInclusive().intValue());
    assertEquals(5,pos.getIndex());
    assertEquals(-1,pos.getErrorIndex());
    
    // Range value
    pos.setIndex(0);
    selections = (SelectingExpression) formatter.parseObject("[A-Z]",pos);
    assertEquals(1,selections.size());
    range = (OrdinalSelectRange) selections.get(0);
    assertEquals('A',range.getMinInclusive().intValue());
    assertEquals('Z',range.getMaxInclusive().intValue());
    assertEquals(5,pos.getIndex());
    assertEquals(-1,pos.getErrorIndex());
    
    // Range values
    pos.setIndex(0);
    selections = (SelectingExpression) formatter.parseObject("[A-Za-z]",pos);
    assertEquals(2,selections.size());
    range = (OrdinalSelectRange) selections.get(0);
    assertEquals('A',range.getMinInclusive().intValue());
    assertEquals('Z',range.getMaxInclusive().intValue());
    range = (OrdinalSelectRange) selections.get(1);
    assertEquals('a',range.getMinInclusive().intValue());
    assertEquals('z',range.getMaxInclusive().intValue());
    assertEquals(8,pos.getIndex());
    assertEquals(-1,pos.getErrorIndex());
    
    // Mixed values
    pos.setIndex(0);
    selections = (SelectingExpression) formatter.parseObject("[A-Z0a-z]",pos);
    assertEquals(3,selections.size());
    range = (OrdinalSelectRange) selections.get(0);
    assertEquals('A',range.getMinInclusive().intValue());
    assertEquals('Z',range.getMaxInclusive().intValue());
    v = (OrdinalSelectValue) selections.get(1);
    assertEquals('0',v.getValue().intValue());
    range = (OrdinalSelectRange) selections.get(2);
    assertEquals('a',range.getMinInclusive().intValue());
    assertEquals('z',range.getMaxInclusive().intValue());
    assertEquals(9,pos.getIndex());
    assertEquals(-1,pos.getErrorIndex());
    
    // This is not a legal value according to Java, PHP, Go and Python
    pos.setIndex(0);
    selections = (SelectingExpression) formatter.parseObject("[]",pos);
    assertEquals(null,selections);
    assertEquals(1,pos.getErrorIndex());
    
  }

}
