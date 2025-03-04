/*
 * 
 * Copyright (c) 2004 Optima SC Inc. All rights reserved.
 * 
 * Redistribution and use in source and binary forms,
 * with or without modification in commercial and
 * non-commercial packages/software, are permitted
 * provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the
 * following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the
 * redistribution, if any, must include the following
 * acknowledgment:
 * 
 * "This product includes software developed by
 * Carl Eric Codere of Optima SC Inc."
 * 
 * Alternately, this acknowledgment may appear in the
 * software itself, if and wherever such third-party
 * acknowledgments normally appear.
 * 
 * 4. The names "Optima SC Inc." and "Carl Eric Codere" must
 * not be used to endorse or promote products derived from
 * this software without prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * OPTIMA SC INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package com.optimasc.text;

import java.io.IOException;

import junit.framework.TestCase;

/**
 *
 * @author carl
 */
public class StringUtilitiesTest extends TestCase {
    
    public StringUtilitiesTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of wildcardMatch method, of class StringUtilities.
     */
    public void testWildcardMatch()
    {
        System.out.println("wildcardMatch");
        String N1 = "";
        String N2 = "";
        boolean result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "";
        N2 = "A";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(false, result);

        N1 = "A";
        N2 = "";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(false, result);

        N1 = "ABCDEFG";
        N2 = "A";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(false, result);

        N1 = "A";
        N2 = "ABCDEFG";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(false, result);

        N1 = "ABCDEFG";
        N2 = "ABC";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(false, result);

        N1 = "ABC";
        N2 = "ABCDEFG";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(false, result);


        N1 = "A";
        N2 = "A";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "ABCDEFG";
        N2 = "ABCDEFG";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "?B?DEF?";
        N2 = "ABCDEFG";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N2 = "?B?DEF?";
        N1 = "ABCDEFG";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "*";
        N2 = "T";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "*";
        N2 = "AR";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);


        N1 = "*TH";
        N2 = "ARTH";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N2 = "*TH";
        N1 = "ARTH";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "*PAS";
        N2 = "HELLOPAS";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "*P?S";
        N2 = "HELLOPAS";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "TO??";
        N2 = "TOTO";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "TO?";
        N2 = "TOT";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "*TOTO";
        N2 = "ROTOTO";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);

        N1 = "T*";
        N2 = "TheSimpleLife";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);


        N2 = "T*";
        N1 = "TheSimpleLife";
        result = StringUtilities.wildcardMatch(N1, N2);
        assertEquals(true, result);


    }
    
    /** Testing of dumpData routine */
    public void testDumpData()
    {
      final byte[] buffer = {0x00,0x20,0x30,0x31,0x32,0x33,0x34,0x36,0x37,0x38};
      
      try
      {
        StringUtilities.dumpData(System.out, buffer, buffer.length, 8);
      } catch (IOException e)
      {
        fail();
      }
      
    }

}
