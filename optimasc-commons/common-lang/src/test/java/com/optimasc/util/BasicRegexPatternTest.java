package com.optimasc.util;


import java.text.ParsePosition;

import com.optimasc.util.SetExpressionFormatter.SelectingExpression;

import junit.framework.TestCase;

public class BasicRegexPatternTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
/*  public void testMatchChars()
  {
     ParsePosition pos = new ParsePosition(0);
     BasicPattern pattern = new BasicPattern("A?B",'.','*');
     
     assertEquals(true,DefaultPatternMatcher.matchChars((SelectingExpression) pattern.localPatternItems.get(0), 'B', pos));
     assertEquals(0,pos.getIndex());
     
     pos = new ParsePosition(0);
     assertEquals(true,DefaultPatternMatcher.matchChars((SelectingExpression) pattern.localPatternItems.get(0), 'A', pos));
     assertEquals(1,pos.getIndex());
     
     pos = new ParsePosition(0);
     assertEquals(true,DefaultPatternMatcher.matchChars((SelectingExpression) pattern.localPatternItems.get(0), 'C', pos));
     assertEquals(0,pos.getIndex());
     
     pos = new ParsePosition(0);
     pattern = new BasicPattern("A",'.','*');
     assertEquals(false,DefaultPatternMatcher.matchChars((SelectingExpression) pattern.localPatternItems.get(0), 'B', pos));
     assertEquals(0,pos.getIndex());
     
     pos = new ParsePosition(0);
     assertEquals(true,DefaultPatternMatcher.matchChars((SelectingExpression) pattern.localPatternItems.get(0), 'A', pos));
     assertEquals(1,pos.getIndex());
     
     pos = new ParsePosition(0);
     assertEquals(false,DefaultPatternMatcher.matchChars((SelectingExpression) pattern.localPatternItems.get(0), 'C', pos));
     assertEquals(0,pos.getIndex());
     
  } */
  
  public void testPatternLiteral()
  {
    BasicRegexPattern pattern = new BasicRegexPattern("literal");
    assertEquals(7,pattern.getMinLength());
    assertEquals(7,pattern.getMaxLength());

    Pattern matcher = pattern;
    assertEquals(false,matcher.matches(""));
    assertEquals(true,matcher.matches("literal"));
    
    pattern = new BasicRegexPattern("");
    assertEquals(0,pattern.getMinLength());
    assertEquals(0,pattern.getMaxLength());
    
    matcher = pattern;
    assertEquals(true,matcher.matches(""));
    assertEquals(false,matcher.matches("literal"));
    
    pattern = new BasicRegexPattern("A");
    assertEquals(1,pattern.getMinLength());
    assertEquals(1,pattern.getMaxLength());
    
    matcher = pattern;
    assertEquals(true,matcher.matches("A"));
    assertEquals(false,matcher.matches("X"));
    assertEquals(false,matcher.matches("literal"));
  }
  
  
  public void testPatternAnyChar()
  {
    Pattern matcher;
    BasicRegexPattern pattern = new BasicRegexPattern("l.teral");
    assertEquals(7,pattern.getMinLength());
    assertEquals(7,pattern.getMaxLength());
    
    matcher = pattern;

    assertEquals(false,matcher.matches(""));
    assertEquals(true,matcher.matches("literal"));
    assertEquals(true,matcher.matches("lZteral"));
    
    pattern = new BasicRegexPattern("");
    matcher = pattern;
    
    assertEquals(true,matcher.matches(""));
    assertEquals(false,matcher.matches("literal"));
    
    pattern = new BasicRegexPattern(".");
    assertEquals(1,pattern.getMinLength());
    assertEquals(1,pattern.getMaxLength());
    matcher = pattern;
    
    assertEquals(true,matcher.matches("A"));
    assertEquals(true,matcher.matches("X"));
    assertEquals(false,matcher.matches("literal"));
  }
  
  
  public void testPatternWildchar()
  {
    Pattern matcher;
    BasicRegexPattern pattern = new BasicRegexPattern("l.*l");
    assertEquals(2,pattern.getMinLength());
    assertEquals(Integer.MAX_VALUE,pattern.getMaxLength());
    matcher = pattern;

    assertEquals(false,matcher.matches(""));
    assertEquals(true,matcher.matches("ll"));
    assertEquals(true,matcher.matches("literal"));
    assertEquals(true,matcher.matches("lZteral"));
    
    pattern = new BasicRegexPattern("");
    matcher = pattern;

    assertEquals(true,matcher.matches(""));
    assertEquals(false,matcher.matches("literal"));
    
    pattern = new BasicRegexPattern(".*");
    assertEquals(0,pattern.getMinLength());
    assertEquals(Integer.MAX_VALUE,pattern.getMaxLength());
    matcher = pattern;
    
    assertEquals(true,matcher.matches("A"));
    assertEquals(true,matcher.matches(""));
    assertEquals(true,matcher.matches("X"));
    assertEquals(true,matcher.matches("literal"));
    
    pattern = new BasicRegexPattern(".*.*");
    assertEquals(0,pattern.getMinLength());
    assertEquals(Integer.MAX_VALUE,pattern.getMaxLength());
    matcher = pattern;
    
    assertEquals(true,matcher.matches("A"));
    assertEquals(true,matcher.matches("X"));
    assertEquals(true,matcher.matches("literal"));
    
  }
  
  
  public void testPatternComplex()
  {
    Pattern matcher;
    BasicRegexPattern pattern = new BasicRegexPattern("[-+]?[0-9][0-9]");
    assertEquals(2,pattern.getMinLength());
    assertEquals(3,pattern.getMaxLength());
    matcher = pattern;

    assertEquals(false,matcher.matches(""));
    assertEquals(false,matcher.matches("literal"));
    assertEquals(true,matcher.matches("09"));
    assertEquals(true,matcher.matches("-09"));
    assertEquals(true,matcher.matches("+09"));
    
    
  }
  
  
}
