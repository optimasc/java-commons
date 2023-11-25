/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.utils;

import java.util.NoSuchElementException;
import junit.framework.TestCase;

/**
 *
 * @author Carl
 */
public class StringTokenizerTest extends TestCase {
    
    public StringTokenizerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of nextToken method, of class StringTokenizer.
     */
    public void testTokenizer01() {


        System.out.println("StringTokenizer() test with normal cases");

        StringTokenizer st = new StringTokenizer("this is a test"," ");
        assertEquals(true, st.hasMoreTokens());
        assertEquals("this",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals("is",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals("a",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals("test",st.nextToken());
        assertEquals(false, st.hasMoreTokens());
        try
        {
           String s= st.nextToken();
           fail("Exception now thrown");
        } catch (NoSuchElementException e)
        {

        }
    }

    /**
     * Test of nextToken method, of class StringTokenizer.
     */
    public void testTokenizer02() {


        System.out.println("StringTokenizer() test with include delimiters");

        StringTokenizer st = new StringTokenizer("this is a test"," ",true);
        assertEquals(true, st.hasMoreTokens());
        assertEquals("this",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals(" ",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals("is",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals(" ",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals("a",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals(" ",st.nextToken());
        assertEquals(true, st.hasMoreTokens());
        assertEquals("test",st.nextToken());
        assertEquals(false, st.hasMoreTokens());
        try
        {
           String s= st.nextToken();
           fail("Exception now thrown");
        } catch (NoSuchElementException e)
        {

        }
    }


}
