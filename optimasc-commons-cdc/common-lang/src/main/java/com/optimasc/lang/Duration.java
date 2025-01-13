/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimasc.lang;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * <p>Immutable representation of a time span as defined in
 * the W3C XML Schema 1.0 specification.</p>
 * 
 * <p>A Duration object represents a period of Gregorian time,
 * which consists of six fields (years, months, days, hours,
 * minutes, and seconds) plus a sign (+/-) field.</p>
 * 
 * <p>The first five fields have non-negative (>=0) integers or null
 * (which represents that the field is not set),
 * and the seconds field has a non-negative decimal or null.
 * A negative sign indicates a negative duration.</p> 
 * 
 * <p>This class provides a number of methods that make it easy
 * to use for the duration datatype of XML Schema 1.0 with
 * the errata.</p>
 * 
 * <h2>Order relationship</h2>
 * <p>Duration objects only have partial order, where two values A and B
 * maybe either:</p>
 * <ol>
 *  <li>A&lt;B (A is shorter than B)
 *  <li>A&gt;B (A is longer than B)
 *  <li>A==B   (A and B are of the same duration)
 *  <li>A&lt;>B (Comparison between A and B is indeterminate)
 * </ol>
 * <p>For example, 30 days cannot be meaningfully compared to one month.
 * The {@link #compare(javax.xml.datatype.Duration)} method implements this
 * relationship.</p>
 * 
 * <p>See the {@link #isLongerThan(javax.xml.datatype.Duration)} method for details about
 * the order relationship among {@link javax.xml.datatype.Duration} objects.</p>
 * 
 * 
 * 
 * <h2>Operations over Duration</h2>
 * <p>This class provides a set of basic arithmetic operations, such
 * as addition, subtraction and multiplication.
 * Because durations don't have total order, an operation could
 * fail for some combinations of operations. For example, you cannot
 * subtract 15 days from 1 month. See the javadoc of those methods
 * for detailed conditions where this could happen.</p>
 * 
 * <p>Also, division of a duration by a number is not provided because
 * the {@link javax.xml.datatype.Duration} class can only deal with finite precision
 * decimal numbers. For example, one cannot represent 1 sec divided by 3.</p> 
 * 
 * <p>However, you could substitute a division by 3 with multiplying
 * by numbers such as 0.3 or 0.333.</p>
 *
 *
 * 
 * <h2>Range of allowed values</h2>
 * <p>
 * Because some operations of {@link javax.xml.datatype.Duration} rely on {@link java.util.Calendar}
 * even though {@link javax.xml.datatype.Duration} can hold very large or very small values,
 * some of the methods may not work correctly on such {@link javax.xml.datatype.Duration}s.
 * The impacted methods document their dependency on {@link java.util.Calendar}.
 * 
 *  
 * @author <a href="mailto:Kohsuke.Kawaguchi@Sun.com">Kohsuke Kawaguchi</a>
 * @author <a href="mailto:Joseph.Fialli@Sun.com">Joseph Fialli</a>
 * @version $Id: DurationImpl.java 327940 2005-10-24 03:22:51Z mrglavas $
 * @see javax.xml.datatype.GregorianDateTime#add(javax.xml.datatype.Duration)
 */
public class Duration implements Serializable, DateTimeConstants {
    
    /**
     * <p>Number of Fields.</p>
     */
    private static final int FIELD_NUM = 6;
    
    /**
     * <p>Internal array of value Fields.</p>
     */
	private static final int FIELDS[] = new int[]{
			YEARS,
			MONTHS,
			DAYS,
			HOURS,
			MINUTES,
			SECONDS
		};


    /**
     * <p>Indicates the sign. -1, 0 or 1 if the duration is negative,
     * zero, or positive.</p>
     */
    private final int signum;
    
    /**
     * <p>Years of this <code>Duration</code>.</p>
     */
    private final long years;
    
    /**
     * <p>Months of this <code>Duration</code>.</p>
     */
    private final long months;
    
    /**
     * <p>Days of this <code>Duration</code>.</p>
     */
    private final long days;
    
    /**
     * <p>Hours of this <code>Duration</code>.</p>
     */
    private final long hours;
    
    /**
     * <p>Minutes of this <code>Duration</code>.</p>
     */
    private final long minutes;
    
    /**
     * <p>Seconds of this <code>Duration</code>.</p>
     */
    private final long seconds;

	/**
	 * Returns the sign of this duration in -1,0, or 1.
	 * 
	 * @return
	 *      -1 if this duration is negative, 0 if the duration is zero,
	 *      and 1 if the duration is postive.
	 */
	public int getSign() {
		
		return signum;
	}

	/**
	 * TODO: Javadoc
	 * @param isPositive Sign.
	 * 
	 * @return 1 if positive, else -1.
	 */	        
    private int calcSignum(boolean isPositive) {
        if ((years == 0)
            && (months == 0)
            && (days == 0)
            && (hours == 0)
            && (minutes == 0)
            && (seconds == 0)) 
            {
            return 0;
            }

            if (isPositive) {
                return 1;
            } else {
                return -1;
            }

    }
    
    
    /**
     * <p>Makes sure that the given number is non-negative. If it is not,
     * throw {@link IllegalArgumentException}.</p>
     * 
     * @param n Number to test.
     * @param f Field to test.
     */
    private static void testNonNegative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Values are negative.");
        }
    }
    
    /**
     * <p>Makes sure that the given number is non-negative. If it is not,
     * throw {@link IllegalArgumentException}.</p>
     * 
     * @param n Number to test.
     * @param f Field to test.
     */
    private static void testNonNegative(BigDecimal n) {
        if (n != null && n.signum() < 0) {
                throw new IllegalArgumentException("Values are negative.");
        }
    }
    
    /**
     * <p>Constructs a new Duration object by specifying each field
     * individually.</p>
     * 
     * <p>This method is functionally equivalent to
     * invoking another constructor by wrapping
     * all non-zero parameters into {@link java.math.BigInteger} and {@link java.math.BigDecimal}.
     * Zero value of int parameter is equivalent of null value of
     * the corresponding field.</p> 
     * 
     * @see #DurationImpl(boolean, java.math.BigInteger, java.math.BigInteger, java.math.BigInteger, java.math.BigInteger,
     *   java.math.BigInteger, java.math.BigDecimal)
     */
    public Duration(
        final boolean isPositive,
        final int years,
        final int months,
        final int days,
        final int hours,
        final int minutes,
        final int seconds) 
    {
      
        this.years = years;
        this.months = months;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;

        this.signum = calcSignum(isPositive);

    }

    /**
     * <p>Constructs a new Duration object by specifying the duration
     * in milliseconds.</p>
     * 
     * <p>The DAYS, HOURS, MINUTES and SECONDS fields are used to
     * represent the specified duration in a reasonable way.
     * That is, the constructed object <code>x</code> satisfies
     * the following conditions:</p>
     * <ul>
     *  <li>x.getHours()&lt;24
     *  <li>x.getMinutes()&lt;60
     *  <li>x.getSeconds()&lt;60 
     * </ul>
     * 
     * @param durationInMilliSeconds
     *      The length of the duration in milliseconds.
     */
    public Duration(final long durationInMilliSeconds) {
        
        boolean is0x8000000000000000L = false;
        long l = durationInMilliSeconds;
        
        if (l > 0) {
            signum = 1;
        } else if (l < 0) {
            signum = -1;
            if (l == 0x8000000000000000L) {
                // negating 0x8000000000000000L causes an overflow
                l++;
                is0x8000000000000000L = true;
            }
            l *= -1;
        } else {
            signum = 0;
        }
        
        this.years = 0;
        this.months = 0;
        
        this.seconds = (long)((l % 60000L) + (is0x8000000000000000L ? 1 : 0));
        
        l /= 60000L;
        this.minutes = (int) ((l == 0) ? 0 : l % 60L);

        l /= 60L;
        this.hours = (int) ((l == 0) ? 0 : l % 24L);
        
        l /= 24L;
        this.days = (int) ((l == 0) ? 0 : l);
    }
    
    
    /**
     * Constructs a new Duration object by
     * parsing its string representation
     * "PnYnMnDTnHnMnS" as defined in XML Schema 1.0 section 3.2.6.1.
     * 
     * <p>
     * The string representation may not have any leading
     * and trailing whitespaces.
     * 
     * <p>
     * For example, this method parses strings like
     * "P1D" (1 day), "-PT100S" (-100 sec.), "P1DT12H" (1 days and 12 hours).
     *  
     * <p>
     * The parsing is done field by field so that   
     * the following holds for any lexically correct string x:
     * <pre>
     * new Duration(x).toString().equals(x)
     * </pre>
     * 
     * Returns a non-null valid duration object that holds the value
     * indicated by the lexicalRepresentation parameter.
     *
     * @param lexicalRepresentation
     *      Lexical representation of a duration.
     * @throws IllegalArgumentException
     *      If the given string does not conform to the aforementioned
     *      specification.
     * @throws NullPointerException
     *      If the given string is null.
     */
    public Duration(String lexicalRepresentation)
        throws IllegalArgumentException {
        // only if I could use the JDK1.4 regular expression ....
        
        final String s = lexicalRepresentation;
        boolean positive;
        int[] idx = new int[1];
	int length = s.length();
	boolean timeRequired = false;

	if (lexicalRepresentation == null) {
	    throw new NullPointerException();
	}
        
	idx[0] = 0;
	if (length != idx[0] && s.charAt(idx[0]) == '-') {
	    idx[0]++;
	    positive = false;
	} else {
	    positive = true;
	}
        
	if (length != idx[0] && s.charAt(idx[0]++) != 'P') {
	    throw new IllegalArgumentException(s); //,idx[0]-1);
	}

        
        // phase 1: chop the string into chunks
        // (where a chunk is '<number><a symbol>'
        //--------------------------------------
        int dateLen = 0;
        String[] dateParts = new String[3];
        int[] datePartsIndex = new int[3];
        while (length != idx[0]
            && isDigit(s.charAt(idx[0]))
            && dateLen < 3) {
            datePartsIndex[dateLen] = idx[0];
            dateParts[dateLen++] = parsePiece(s, idx);
        }
        
	if (length != idx[0]) {
	    if (s.charAt(idx[0]++) == 'T') {
		timeRequired = true;
	    } else {
		throw new IllegalArgumentException(s); // ,idx[0]-1);
	    }
	}
        
        int timeLen = 0;
        String[] timeParts = new String[3];
        int[] timePartsIndex = new int[3];
        while (length != idx[0]
            && isDigitOrPeriod(s.charAt(idx[0]))
            && timeLen < 3) {
            timePartsIndex[timeLen] = idx[0];
            timeParts[timeLen++] = parsePiece(s, idx);
        }
        
	if (timeRequired && timeLen == 0) {
            throw new IllegalArgumentException(s); // ,idx[0]);
	}

        if (length != idx[0]) {
            throw new IllegalArgumentException(s); // ,idx[0]);
        }
        if (dateLen == 0 && timeLen == 0) {
            throw new IllegalArgumentException(s); // ,idx[0]);
        }
        
        // phase 2: check the ordering of chunks
        //--------------------------------------
        organizeParts(s, dateParts, datePartsIndex, dateLen, "YMD");
        organizeParts(s, timeParts, timePartsIndex, timeLen, "HMS");
        
        // parse into numbers
        years = parseInteger(s, dateParts[0], datePartsIndex[0]);
        months = parseInteger(s, dateParts[1], datePartsIndex[1]);
        days = parseInteger(s, dateParts[2], datePartsIndex[2]);
        hours = parseInteger(s, timeParts[0], timePartsIndex[0]);
        minutes = parseInteger(s, timeParts[1], timePartsIndex[1]);
        seconds = parseInteger(s, timeParts[2], timePartsIndex[2]);
        signum = calcSignum(positive);
    }
        
     
    /**
     * TODO: Javadoc
     * 
     * @param ch char to test.
     * 
     * @return true if ch is a digit, else false.
     */
    private static boolean isDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }
    
    /**
     * TODO: Javadoc
     * 
     * @param ch to test.
     * 
     * @return true if ch is a digit or a period, else false.
     */
    private static boolean isDigitOrPeriod(char ch) {
        return isDigit(ch) || ch == '.';
    }
    
    /**
     * TODO: Javadoc
     * 
     * @param whole String to parse.
     * @param idx TODO: ???
     * 
     * @return Result of parsing.
     * 
     * @throws IllegalArgumentException If whole cannot be parsed.
     */
    private static String parsePiece(String whole, int[] idx)
        throws IllegalArgumentException {
        int start = idx[0];
        while (idx[0] < whole.length()
            && isDigitOrPeriod(whole.charAt(idx[0]))) {
            idx[0]++;
            }
        if (idx[0] == whole.length()) {
            throw new IllegalArgumentException(whole); // ,idx[0]);
        }

        idx[0]++;

        return whole.substring(start, idx[0]);
    }
    
    /**
     * TODO: Javadoc.
     * 
     * @param whole TODO: ???
     * @param parts TODO: ???
     * @param partsIndex TODO: ???
     * @param len TODO: ???
     * @param tokens TODO: ???
     * 
     * @throws IllegalArgumentException TODO: ???
     */
    private static void organizeParts(
        String whole,
        String[] parts,
        int[] partsIndex,
        int len,
        String tokens)
        throws IllegalArgumentException {

        int idx = tokens.length();
        for (int i = len - 1; i >= 0; i--) {
            int nidx =
                tokens.lastIndexOf(
                    parts[i].charAt(parts[i].length() - 1),
                    idx - 1);
            if (nidx == -1) {
                throw new IllegalArgumentException(whole);
                // ,partsIndex[i]+parts[i].length()-1);
            }

            for (int j = nidx + 1; j < idx; j++) {
                parts[j] = null;
            }
            idx = nidx;
            parts[idx] = parts[i];
            partsIndex[idx] = partsIndex[i];
        }
        for (idx--; idx >= 0; idx--) {
            parts[idx] = null;
        }
    }
    
    /**
     * TODO: Javadoc
     * 
     * @param whole TODO: ???.
     * @param part TODO: ???.
     * @param index TODO: ???.
     * 
     * @return TODO: ???.
     * 
     * @throws IllegalArgumentException TODO: ???.
     */
    private static int parseInteger(
        String whole,
        String part,
        int index)
        throws IllegalArgumentException {
        if (part == null) {
            return 0;
        }
        part = part.substring(0, part.length() - 1);
        //        try {
        return Integer.parseInt(part);
        //        } catch( NumberFormatException e ) {
        //            throw new ParseException( whole, index );
        //        }
    }
    
    /**
     * TODO: Javadoc.
     * 
     * @param whole TODO: ???.
     * @param part TODO: ???.
     * @param index TODO: ???.
     * 
     * @return TODO: ???.
     * 
     * @throws IllegalArgumentException TODO: ???.
     */
    private static double parseBigDecimal(
        String whole,
        String part,
        int index)
        throws IllegalArgumentException {
        if (part == null) {
            return 0;
        }
        part = part.substring(0, part.length() - 1);
        // NumberFormatException is IllegalArgumentException
        //        try {
        return Double.parseDouble(part);
        //        } catch( NumberFormatException e ) {
        //            throw new ParseException( whole, index );
        //        }
    }
    
    /**
     * <p>Four constants defined for the comparison of durations.</p>
     */
    private static final GregorianDateTime[] TEST_POINTS = new GregorianDateTime[] {
        GregorianDateTime.parse("1696-09-01T00:00:00Z"),
        GregorianDateTime.parse("1697-02-01T00:00:00Z"),
        GregorianDateTime.parse("1903-03-01T00:00:00Z"),
        GregorianDateTime.parse("1903-07-01T00:00:00Z")
    };
    
	/**
	 * <p>Partial order relation comparison with this <code>Duration</code> instance.</p>
	 * 
	 * <p>Comparison result must be in accordance with
	 * <a href="http://www.w3.org/TR/xmlschema-2/#duration-order">W3C XML Schema 1.0 Part 2, Section 3.2.7.6.2,
	 * <i>Order relation on duration</i></a>.</p>
	 * 
	 * <p>Return:</p>
	 * <ul>
	 *   <li>{@link javax.xml.datatype.DatatypeConstants#LESSER} if this <code>Duration</code> is shorter than <code>duration</code> parameter</li>
	 *   <li>{@link javax.xml.datatype.DatatypeConstants#EQUAL} if this <code>Duration</code> is equal to <code>duration</code> parameter</li>
	 *   <li>{@link javax.xml.datatype.DatatypeConstants#GREATER} if this <code>Duration</code> is longer than <code>duration</code> parameter</li>
	 *   <li>{@link javax.xml.datatype.DatatypeConstants#INDETERMINATE} if a conclusive partial order relation cannot be determined</li>
	 * </ul>
	 *
	 * @param duration to compare
	 * 
	 * @return the relationship between <code>this</code> <code>Duration</code>and <code>duration</code> parameter as
	 *   {@link javax.xml.datatype.DatatypeConstants#LESSER}, {@link javax.xml.datatype.DatatypeConstants#EQUAL}, {@link javax.xml.datatype.DatatypeConstants#GREATER}
	 *   or {@link javax.xml.datatype.DatatypeConstants#INDETERMINATE}.
	 * 
	 * @throws UnsupportedOperationException If the underlying implementation
	 *   cannot reasonably process the request, e.g. W3C XML Schema allows for
	 *   arbitrarily large/small/precise values, the request may be beyond the
	 *   implementations capability.
	 * @throws NullPointerException if <code>duration</code> is <code>null</code>. 
	 *
	 * @see #isShorterThan(javax.xml.datatype.Duration)
	 * @see #isLongerThan(javax.xml.datatype.Duration)
	 */
    public int compare(Duration rhs) {
    	
    	long maxintAsBigInteger = Integer.MAX_VALUE;
    	long minintAsBigInteger = Integer.MIN_VALUE;

    	// check for fields that are too large in this Duration
    	if (years != 0 && years > maxintAsBigInteger) {    		
            throw new UnsupportedOperationException("years value is greater than "+maxintAsBigInteger);
    	}
    	if (months != 0 && months > maxintAsBigInteger) {
          throw new UnsupportedOperationException("months values is greater than "+maxintAsBigInteger);
    	}
    	if (days != 0 && days > maxintAsBigInteger) {
          throw new UnsupportedOperationException("days value is greater than "+maxintAsBigInteger);
    	}
    	if (hours != 0 && hours > maxintAsBigInteger) {
          throw new UnsupportedOperationException("hours value is greater than "+maxintAsBigInteger);
    	}
    	if (minutes != 0 && minutes > maxintAsBigInteger) {
          throw new UnsupportedOperationException("minutes value is greater than "+maxintAsBigInteger);
    	}
    	if (seconds != 0 && seconds > maxintAsBigInteger) {
          throw new UnsupportedOperationException("seconds value is greater than "+maxintAsBigInteger);
    	}
    	
    	// check for fields that are too large in rhs Duration
    	long rhsYears = rhs.years;
    	if (rhsYears != 0 && rhsYears > maxintAsBigInteger) {
          throw new UnsupportedOperationException("years value is greater than "+maxintAsBigInteger);
    	}
    	long rhsMonths = rhs.months;
    	if (rhsMonths != 0 && rhsMonths > maxintAsBigInteger) {
          throw new UnsupportedOperationException("months values is greater than "+maxintAsBigInteger);
    	}
    	long rhsDays = rhs.days;
    	if (rhsDays != 0 && rhsDays > maxintAsBigInteger) {
          throw new UnsupportedOperationException("days value is greater than "+maxintAsBigInteger);
    	}
    	long rhsHours = rhs.hours;
    	if (rhsHours != 0 && rhsHours > maxintAsBigInteger) {
          throw new UnsupportedOperationException("hours value is greater than "+maxintAsBigInteger);
    	}
    	long rhsMinutes = rhs.minutes;
    	if (rhsMinutes != 0 && rhsMinutes > maxintAsBigInteger) {
          throw new UnsupportedOperationException("minutes value is greater than "+maxintAsBigInteger);
    	}
    	double rhsSecondsAsBigDecimal = rhs.seconds;
    	long rhsSeconds = (long)rhsSecondsAsBigDecimal;
    	if (rhsSeconds != 0 && rhsSeconds > maxintAsBigInteger) {
          throw new UnsupportedOperationException("seconds value is greater than "+maxintAsBigInteger);
    	}

    	// turn this Duration into a GregorianCalendar
    	GregorianCalendar lhsCalendar = new GregorianCalendar(
    			1970,
				1,
				1,
				0,
				0,
				0);
   		lhsCalendar.add(GregorianCalendar.YEAR, getYears() * getSign());
   		lhsCalendar.add(GregorianCalendar.MONTH, getMonths() * getSign());
   		lhsCalendar.add(GregorianCalendar.DAY_OF_YEAR, getDays() * getSign());
   		lhsCalendar.add(GregorianCalendar.HOUR_OF_DAY, getHours() * getSign());
   		lhsCalendar.add(GregorianCalendar.MINUTE, getMinutes() * getSign());
   		lhsCalendar.add(GregorianCalendar.SECOND, getSeconds() * getSign());
   		
   		// turn compare Duration into a GregorianCalendar
    	GregorianCalendar rhsCalendar = new GregorianCalendar(
				1970,
				1,
				1,
				0,
				0,
				0);
   		rhsCalendar.add(GregorianCalendar.YEAR, rhs.getYears() * rhs.getSign());
   		rhsCalendar.add(GregorianCalendar.MONTH, rhs.getMonths() * rhs.getSign());
   		rhsCalendar.add(GregorianCalendar.DAY_OF_YEAR, rhs.getDays() * rhs.getSign());
   		rhsCalendar.add(GregorianCalendar.HOUR_OF_DAY, rhs.getHours() * rhs.getSign());
   		rhsCalendar.add(GregorianCalendar.MINUTE, rhs.getMinutes() * rhs.getSign());
   		rhsCalendar.add(GregorianCalendar.SECOND, rhs.getSeconds() * rhs.getSign());
   	
   		
   		if (lhsCalendar.equals(rhsCalendar)) {
   			return 0;
   		}

   		  try
        {
          return compareDates(this, rhs);
        } catch (CloneNotSupportedException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
          return DateTimeConstants.INDETERMINATE;
        }
    }
    
    /**
     * Compares 2 given durations. (refer to W3C Schema Datatypes "3.2.6 duration")
     *
     * @param duration1  Unnormalized duration
     * @param duration2  Unnormalized duration
     * @return INDETERMINATE if the order relationship between date1 and date2 is indeterminate.
     * EQUAL if the order relation between date1 and date2 is EQUAL.
     * If the strict parameter is true, return LESS_THAN if date1 is less than date2 and
     * return GREATER_THAN if date1 is greater than date2.
     * If the strict parameter is false, return LESS_THAN if date1 is less than OR equal to date2 and
     * return GREATER_THAN if date1 is greater than OR equal to date2
     * @throws CloneNotSupportedException 
     */
    private int compareDates(Duration duration1, Duration duration2) throws CloneNotSupportedException {
        
        int resultA = INDETERMINATE; 
        int resultB = INDETERMINATE;
        
        GregorianDateTime tempA = (GregorianDateTime)TEST_POINTS[0].clone();
        GregorianDateTime tempB = (GregorianDateTime)TEST_POINTS[0].clone();
        
        //long comparison algorithm is required
        tempA.add(duration1);
        tempB.add(duration2);
        resultA =  tempA.compare(tempB);
        if ( resultA == INDETERMINATE ) {
            return INDETERMINATE;
        }

        tempA = (GregorianDateTime)TEST_POINTS[1].clone();
        tempB = (GregorianDateTime)TEST_POINTS[1].clone();
        
        tempA.add(duration1);
        tempB.add(duration2);
        resultB = tempA.compare(tempB);
        resultA = compareResults(resultA, resultB);
        if (resultA == INDETERMINATE) {
            return INDETERMINATE;
        }

        tempA = (GregorianDateTime)TEST_POINTS[2].clone();
        tempB = (GregorianDateTime)TEST_POINTS[2].clone();
        
        tempA.add(duration1);
        tempB.add(duration2);
        resultB = tempA.compare(tempB);
        resultA = compareResults(resultA, resultB);
        if (resultA == INDETERMINATE) {
            return INDETERMINATE;
        }

        tempA = (GregorianDateTime)TEST_POINTS[3].clone();
        tempB = (GregorianDateTime)TEST_POINTS[3].clone();
        
        tempA.add(duration1);
        tempB.add(duration2);
        resultB = tempA.compare(tempB);
        resultA = compareResults(resultA, resultB);

        return resultA;
    }

    private int compareResults(int resultA, int resultB){

      if ( resultB == INDETERMINATE ) {
            return INDETERMINATE;
        }
        else if ( resultA!=resultB) {
            return INDETERMINATE;
        }
        return resultA;
    }
    
    /**
     * Returns a hash code consistent with the definition of the equals method.
     * 
     * @see Object#hashCode() 
     */
    public int hashCode() {
        // component wise hash is not correct because 1day = 24hours
	Calendar cal = TEST_POINTS[0].toGregorianCalendar();
	this.addTo(cal);
	return (int) getCalendarTimeInMillis(cal);
    }
    
    /**
     * Returns a string representation of this duration object.
     * 
     * <p>
     * The result is formatter according to the XML Schema 1.0
     * spec and can be always parsed back later into the
     * equivalent duration object by
     * the {@link #DurationImpl(String)} constructor.
     * 
     * <p>
     * Formally, the following holds for any {@link javax.xml.datatype.Duration}
     * object x. 
     * <pre>
     * new Duration(x.toString()).equals(x)
     * </pre>
     * 
     * @return
     *      Always return a non-null valid String object.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (signum < 0) {
            buf.append('-');
        }
        buf.append('P');
        
        if (years != 0) {
            buf.append(years).append('Y');
        }
        if (months != 0) {
            buf.append(months).append('M');
        }
        if (days != 0) {
            buf.append(days).append('D');
        }

        if (hours != 0 || minutes != 0 || seconds != 0) {
            buf.append('T');
            if (hours != 0) {
                buf.append(hours).append('H');
            }
            if (minutes != 0) {
                buf.append(minutes).append('M');
            }
            if (seconds != 0) {
                buf.append(seconds).append('S');
            }
        }
        
        return buf.toString();
    }

    /**
     * <p>Turns {@link java.math.BigDecimal} to a string representation.</p>
     * 
     * <p>Due to a behavior change in the {@link java.math.BigDecimal#toString()}
     * method in JDK1.5, this had to be implemented here.</p>
     * 
     * @param bd <code>BigDecimal</code> to format as a <code>String</code>
     * 
     * @return  <code>String</code> representation of <code>BigDecimal</code> 
     */
    private String toString(BigDecimal bd) {
        String intString = bd.unscaledValue().toString();
        int scale = bd.scale();

        if (scale == 0) {
            return intString;
        }

        /* Insert decimal point */
        StringBuffer buf;
        int insertionPoint = intString.length() - scale;
        if (insertionPoint == 0) { /* Point goes right before intVal */
            return "0." + intString;
        } else if (insertionPoint > 0) { /* Point goes inside intVal */
            buf = new StringBuffer(intString);
            buf.insert(insertionPoint, '.');
        } else { /* We must insert zeros between point and intVal */
            buf = new StringBuffer(3 - insertionPoint + intString.length());
            buf.append("0.");
            for (int i = 0; i < -insertionPoint; i++) {
                buf.append('0');
            }
            buf.append(intString);
        }
        return buf.toString();
    }

    
    /**
     * Gets the value of a field. 
     * 
     * Fields of a duration object may contain arbitrary large value.
     * Therefore this method is designed to return a {@link Number} object.
     * 
     * In case of YEARS, MONTHS, DAYS, HOURS, and MINUTES, the returned
     * number will be a non-negative integer. In case of seconds,
     * the returned number may be a non-negative decimal value.
     * 
     * @param field
     *      one of the six Field constants (YEARS,MONTHS,DAYS,HOURS,
     *      MINUTES, or SECONDS.)
     * @return
     *      If the specified field is present, this method returns
     *      a non-null non-negative {@link Number} object that
     *      represents its value. If it is not present, return null.
     *      For YEARS, MONTHS, DAYS, HOURS, and MINUTES, this method
     *      returns a {@link java.math.BigInteger} object. For SECONDS, this
     *      method returns a {@link java.math.BigDecimal}.
     * 
     * @throws NullPointerException
     *      If the field parameter is null.
     */
    public long getField(int field) {

		if (field == YEARS) {
			return years;
		}

		if (field == MONTHS) {
			return months;
		}

		if (field == DAYS) {
			return days;
		}

		if (field == HOURS) {
			return hours;
		}

		if (field == MINUTES) {
			return minutes;
		}
		
		if (field == SECONDS) {
			return (long)seconds;
		}
        
        throw new IllegalArgumentException("Unknown field +"+field);			
    }
    
    /**
     * Obtains the value of the YEARS field as an integer value,
     * or 0 if not present.
     * 
     * <p>
     * 
     * <p>
     * Note that since this method returns <tt>int</tt>, this
     * method will return an incorrect value for {@link javax.xml.datatype.Duration}s
     * with the year field that goes beyond the range of <tt>int</tt>.
     * Use <code>getField(YEARS)</code> to avoid possible loss of precision.</p>
     * 
     * @return
     *      If the YEARS field is present, return
     *      its value as an integer by using the {@link Number#intValue()}
     *      method. If the YEARS field is not present, return 0.
     */
    public int getYears() {
        return (int) years;
    }
    
    /**
     * Obtains the value of the MONTHS field as an integer value,
     * or 0 if not present.
     * 
     * This method works just like {@link #getYears()} except
     * that this method works on the MONTHS field.
     * 
     * @return Months of this <code>Duration</code>.
     */
    public int getMonths() {
        return (int) months;
    }
    
    /**
     * Obtains the value of the DAYS field as an integer value,
     * or 0 if not present.
     * 
     * This method works just like {@link #getYears()} except
     * that this method works on the DAYS field.
     * 
     * @return Days of this <code>Duration</code>.
     */
    public int getDays() {
        return (int) days;
    }
    
    /**
     * Obtains the value of the HOURS field as an integer value,
     * or 0 if not present.
     * 
     * This method works just like {@link #getYears()} except
     * that this method works on the HOURS field.
     * 
     * @return Hours of this <code>Duration</code>.
     * 
     */
    public int getHours() {
        return (int) hours;
    }
    
    /**
     * Obtains the value of the MINUTES field as an integer value,
     * or 0 if not present.
     * 
     * This method works just like {@link #getYears()} except
     * that this method works on the MINUTES field.
     * 
     * @return Minutes of this <code>Duration</code>.
     * 
     */
    public int getMinutes() {
        return (int) minutes;
    }
    
    /**
     * Obtains the value of the SECONDS field as an integer value,
     * or 0 if not present.
     * 
     * This method works just like {@link #getYears()} except
     * that this method works on the SECONDS field.
     * 
     * @return seconds in the integer value. The fraction of seconds
     *   will be discarded (for example, if the actual value is 2.5,
     *   this method returns 2)
     */
    public int getSeconds() {
        return (int) seconds;
    }
    

        
    /**
     * <p>Returns the length of the duration in milli-seconds.</p>
     * 
     * <p>If the seconds field carries more digits than milli-second order,
     * those will be simply discarded (or in other words, rounded to zero.)  
     * For example, for any Calendar value <code>x<code>,</p>
     * <pre>
     * <code>new Duration("PT10.00099S").getTimeInMills(x) == 10000</code>.
     * <code>new Duration("-PT10.00099S").getTimeInMills(x) == -10000</code>.
     * </pre>
     * 
     * <p>
     * Note that this method uses the {@link #addTo(java.util.Calendar)} method,
     * which may work incorectly with {@link javax.xml.datatype.Duration} objects with
     * very large values in its fields. See the {@link #addTo(java.util.Calendar)}
     * method for details.
     * 
     * @param startInstant
     *      The length of a month/year varies. The <code>startInstant</code> is
     *      used to disambiguate this variance. Specifically, this method
     *      returns the difference between <code>startInstant</code> and
     *      <code>startInstant+duration</code>
     * 
     * @return milliseconds between <code>startInstant</code> and
     *   <code>startInstant</code> plus this <code>Duration</code>
     *
     * @throws NullPointerException if <code>startInstant</code> parameter 
     * is null.
     * 
     */
    public long getTimeInMillis(final Calendar startInstant) {
        Calendar cal = (Calendar) startInstant.clone();
        addTo(cal);
        return getCalendarTimeInMillis(cal)
                    - getCalendarTimeInMillis(startInstant);
    }
    
    /**
     * <p>Returns the length of the duration in milli-seconds.</p>
     * 
     * <p>If the seconds field carries more digits than milli-second order,
     * those will be simply discarded (or in other words, rounded to zero.)
     * For example, for any <code>Date</code> value <code>x<code>,</p>   
     * <pre>
     * <code>new Duration("PT10.00099S").getTimeInMills(x) == 10000</code>.
     * <code>new Duration("-PT10.00099S").getTimeInMills(x) == -10000</code>.
     * </pre>
     * 
     * <p>
     * Note that this method uses the {@link #addTo(java.util.Date)} method,
     * which may work incorectly with {@link javax.xml.datatype.Duration} objects with
     * very large values in its fields. See the {@link #addTo(java.util.Date)}
     * method for details.
     * 
     * @param startInstant
     *      The length of a month/year varies. The <code>startInstant</code> is
     *      used to disambiguate this variance. Specifically, this method
     *      returns the difference between <code>startInstant</code> and
     *      <code>startInstant+duration</code>.
     * 
     * @throws NullPointerException
     *      If the startInstant parameter is null.
     * 
     * @return milliseconds between <code>startInstant</code> and
     *   <code>startInstant</code> plus this <code>Duration</code>
     *
     * @see #getTimeInMillis(java.util.Calendar)
     */
    public long getTimeInMillis(final Date startInstant) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(startInstant);
        this.addTo(cal);
        return getCalendarTimeInMillis(cal) - startInstant.getTime();
    }
    
    
    /**
     * <p>Converts the years and months fields into the days field
     * by using a specific time instant as the reference point.</p>
     * 
     * <p>For example, duration of one month normalizes to 31 days
     * given the start time instance "July 8th 2003, 17:40:32".</p>
     * 
     * <p>Formally, the computation is done as follows:</p>
     * <ol>
     *  <li>The given Calendar object is cloned.
     *  <li>The years, months and days fields will be added to
     *      the {@link java.util.Calendar} object
     *      by using the {@link java.util.Calendar#add(int,int)} method.
     *  <li>The difference between two Calendars are computed in terms of days.
     *  <li>The computed days, along with the hours, minutes and seconds
     *      fields of this duration object is used to construct a new
     *      Duration object.
     * </ol>
     * 
     * <p>Note that since the Calendar class uses <code>int</code> to
     * hold the value of year and month, this method may produce
     * an unexpected result if this duration object holds
     * a very large value in the years or months fields.</p>
     *
     * @param startTimeInstant <code>Calendar</code> reference point.
     *  
     * @return <code>Duration</code> of years and months of this <code>Duration</code> as days.
     * 
     * @throws NullPointerException If the startTimeInstant parameter is null.
     */
    public Duration normalizeWith(Calendar startTimeInstant) {
        
        Calendar c = (Calendar) startTimeInstant.clone();
        
        // using int may cause overflow, but 
        // Calendar internally treats value as int anyways.
   	    c.add(Calendar.YEAR, getYears() * signum);
        c.add(Calendar.MONTH, getMonths() * signum);
        c.add(Calendar.DAY_OF_MONTH, getDays() * signum);
        
        // obtain the difference in terms of days
        long diff = getCalendarTimeInMillis(c) - getCalendarTimeInMillis(startTimeInstant);
        int days = (int) (diff / (1000L * 60L * 60L * 24L));
        
        return new Duration(
            days >= 0,
            0,
            0,
            days,
            (int)hours,
            (int)minutes,
            (int)seconds);
    }
    
    
    /**
     * <p>Computes a new duration whose value is <code>factor</code> times
     * longer than the value of this duration.</p>
     * 
     * <p>This method is provided for the convenience.
     * It is functionally equivalent to the following code:</p>
     * <pre>
     * multiply(new BigDecimal(String.valueOf(factor)))
     * </pre>
     * 
     * @param factor Factor times longer of new <code>Duration</code> to create.
     * 
     * @return New <code>Duration</code> that is <code>factor</code>times longer than this <code>Duration</code>.
     * 
     * @see #multiply(java.math.BigDecimal)
     */
    
    public Duration multiply(int factor) {
        int carry = 0;
        int factorSign = signum(factor);
        factor = Math.abs(factor);
        
        long[] buf = new long[6];
        
        for (int i = 0; i < 5; i++) 
        {
            long bd = getField(FIELDS[i]);
            bd = (bd * factor) + carry;
            
            bd = bd - buf[i];
            if (i == 1) {
                if (signum((int)bd) != 0) {
                    throw new IllegalStateException(); // illegal carry-down
                } else {
                    carry = 0;
                }
            } else {
                carry = (int) (bd * FACTORS[i]);
            }
        }
        
        if (seconds != 0) {
            buf[5] = ((long)seconds * factor) + carry;
        } else {
            buf[5] = carry;
        }
                
        return new Duration(
            this.signum * factorSign >= 0,
            (int)buf[0],
            (int)buf[1],
            (int)buf[2],
            (int)buf[3],
            (int)buf[4],
            (int)((signum(buf[5]) == 0 && seconds == 0) ? 0 : buf[5]));
    }
    
    /**
     * <p>BigInteger value of BigDecimal value.</p>
     * 
     * @param value Value to convert.
     * @param canBeNull Can returned value be null?
     * 
     * @return BigInteger value of BigDecimal, possibly null.
     */
    private static BigInteger toBigInteger(
        BigDecimal value,
        boolean canBeNull) {
        if (canBeNull && value.signum() == 0) {
            return null;
        } else {
            return value.unscaledValue();
        }
    }
    
    /**
     * 1 unit of FIELDS[i] is equivalent to <code>FACTORS[i]</code> unit of
     * FIELDS[i+1].
     */
    private static final int[] FACTORS = new int[]{
        12,
        0/*undefined*/,
        24,
        60,
        60
    };    
    
    /**
     * <p>Computes a new duration whose value is <code>this+rhs</code>.</p>
     * 
     * <p>For example,</p>
     * <pre>
     * "1 day" + "-3 days" = "-2 days"
     * "1 year" + "1 day" = "1 year and 1 day"
     * "-(1 hour,50 minutes)" + "-20 minutes" = "-(1 hours,70 minutes)"
     * "15 hours" + "-3 days" = "-(2 days,9 hours)"
     * "1 year" + "-1 day" = IllegalStateException
     * </pre>
     * 
     * <p>Since there's no way to meaningfully subtract 1 day from 1 month,
     * there are cases where the operation fails in
     * {@link IllegalStateException}.</p> 
     * 
     * <p>
     * Formally, the computation is defined as follows.</p>
     * <p>
     * Firstly, we can assume that two {@link javax.xml.datatype.Duration}s to be added
     * are both positive without losing generality (i.e.,
     * <code>(-X)+Y=Y-X</code>, <code>X+(-Y)=X-Y</code>,
     * <code>(-X)+(-Y)=-(X+Y)</code>)
     * 
     * <p>
     * Addition of two positive {@link javax.xml.datatype.Duration}s are simply defined as
     * field by field addition where missing fields are treated as 0.
     * <p>
     * A field of the resulting {@link javax.xml.datatype.Duration} will be unset if and
     * only if respective fields of two input {@link javax.xml.datatype.Duration}s are unset.
     * <p>
     * Note that <code>lhs.add(rhs)</code> will be always successful if
     * <code>lhs.signum()*rhs.signum()!=-1</code> or both of them are
     * normalized.</p>
     * 
     * @param rhs <code>Duration</code> to add to this <code>Duration</code>
     * 
     * @return
     *      non-null valid Duration object.
     * 
     * @throws NullPointerException
     *      If the rhs parameter is null.
     * @throws IllegalStateException
     *      If two durations cannot be meaningfully added. For
     *      example, adding negative one day to one month causes
     *      this exception.
     * 
     * 
     * @see #subtract(javax.xml.datatype.Duration)
     */
    public Duration add(final Duration rhs) {
        Duration lhs = this;
        int[] buf = new int[6];
        
        buf[0] = sanitize((int)years,lhs.getSign())+sanitize((int)years,  rhs.getSign());
        buf[1] = sanitize((int)months,lhs.getSign())+sanitize((int)months,  rhs.getSign());
        buf[2] = sanitize((int)days,lhs.getSign())+sanitize((int)days,  rhs.getSign());
        buf[3] = sanitize((int)hours,lhs.getSign())+sanitize((int)hours,  rhs.getSign());
        buf[4] = sanitize((int)minutes,lhs.getSign())+sanitize((int)minutes,  rhs.getSign());
        buf[5] = sanitize((int)seconds,lhs.getSign())+sanitize((int)seconds,  rhs.getSign());
        
        // align sign
        alignSigns(buf, 0, 2); // Y,M
        alignSigns(buf, 2, 6); // D,h,m,s
        
        // make sure that the sign bit is consistent across all 6 fields.
        int s = 0;
        for (int i = 0; i < 6; i++) {
            if (s * signum(buf[i]) < 0) {
                throw new IllegalStateException();
            }
            if (s == 0) {
                s = signum(buf[i]);
            }
        }
        
        return new Duration(
            s >= 0,
            sanitize(buf[0], s),
            sanitize(buf[1], s),
            sanitize(buf[2], s),
            sanitize(buf[3], s),
            sanitize(buf[4], s),
             (signum(buf[5]) == 0
             ) ? 0 : sanitize(buf[5], s));
    }
    
    private static int signum(long value)
    {
      if (value < 0)
        return -1;
      else
      if (value == 0)
        return 0;
      return 1;
    }
    
    private static void alignSigns(int[] buf, int start, int end) {
        // align sign
        boolean touched;
        
        do { // repeat until all the sign bits become consistent
            touched = false;
            int s = 0; // sign of the left fields

            for (int i = start; i < end; i++) {
                if (s * buf[i] < 0) {
                    // this field has different sign than its left field.
                    touched = true;

                    // compute the number of unit that needs to be borrowed.
                    long borrow = Math.abs(buf[i]) / FACTORS[i-1];
                    if (signum(buf[i]) > 0) {
                        borrow = -borrow;
                    }

                    // update values
                    buf[i - 1] = (int) (buf[i - 1]-borrow);
                    buf[i] = (int) (buf[i]+(borrow*FACTORS[i - 1]));
                }
                if (signum(buf[i]) != 0) {
                    s = signum(buf[i]);
                }
            }
        } while (touched);
    }
    
    /**
     * Compute <code>value*signum</code> where value==null is treated as
     * value==0.
     * @param value Value to sanitize.
     * @param signum 0 to sanitize to 0, > 0 to sanitize to <code>value</code>, < 0 to sanitize to negative <code>value</code>.
     *
     * @return non-null {@link java.math.BigDecimal}.
     */
    static int sanitize(int value, int signum) {
        if (signum == 0) {
            return 0;
        }
        if (signum > 0) {
            return value;
        }
        return -value;
    }
        

    /**
     * <p>Computes a new duration whose value is <code>this-rhs</code>.</p>
     * 
     * <p>For example:</p>
     * <pre>
     * "1 day" - "-3 days" = "4 days"
     * "1 year" - "1 day" = IllegalStateException
     * "-(1 hour,50 minutes)" - "-20 minutes" = "-(1hours,30 minutes)"
     * "15 hours" - "-3 days" = "3 days and 15 hours"
     * "1 year" - "-1 day" = "1 year and 1 day"
     * </pre>
     * 
     * <p>Since there's no way to meaningfully subtract 1 day from 1 month,
     * there are cases where the operation fails in {@link IllegalStateException}.</p> 
     * 
     * <p>Formally the computation is defined as follows.
     * First, we can assume that two {@link javax.xml.datatype.Duration}s are both positive
     * without losing generality.  (i.e.,
     * <code>(-X)-Y=-(X+Y)</code>, <code>X-(-Y)=X+Y</code>,
     * <code>(-X)-(-Y)=-(X-Y)</code>)</p>
     *  
     * <p>Then two durations are subtracted field by field.
     * If the sign of any non-zero field <tt>F</tt> is different from
     * the sign of the most significant field,
     * 1 (if <tt>F</tt> is negative) or -1 (otherwise)
     * will be borrowed from the next bigger unit of <tt>F</tt>.</p>
     * 
     * <p>This process is repeated until all the non-zero fields have
     * the same sign.</p> 
     * 
     * <p>If a borrow occurs in the days field (in other words, if
     * the computation needs to borrow 1 or -1 month to compensate
     * days), then the computation fails by throwing an
     * {@link IllegalStateException}.</p>
     * 
     * @param rhs <code>Duration</code> to substract from this <code>Duration</code>.
     *  
     * @return New <code>Duration</code> created from subtracting <code>rhs</code> from this <code>Duration</code>.
     * 
     * @throws IllegalStateException
     *      If two durations cannot be meaningfully subtracted. For
     *      example, subtracting one day from one month causes
     *      this exception.
     * 
     * @throws NullPointerException
     *      If the rhs parameter is null.
     * 
     * @see #add(javax.xml.datatype.Duration)
     */
    public Duration subtract(final Duration rhs) {
        return add(rhs.negate());
    }
    
    /**
     * Returns a new {@link Duration} object whose value is <code>-this</code>.
     * 
     * <p>
     * Since the {@link javax.xml.datatype.Duration} class is immutable, this method
     * doesn't change the value of this object. It simply computes
     * a new Duration object and returns it.
     * 
     * @return
     *      always return a non-null valid {@link javax.xml.datatype.Duration} object.
     */
    public Duration negate() {
        return new Duration(
            signum <= 0,
            (int)years,
            (int)months,
            (int)days,
            (int)hours,
            (int)minutes,
            (int)seconds);
    }
    
    /**
     * Returns the sign of this duration in -1,0, or 1.
     * 
     * @return
     *      -1 if this duration is negative, 0 if the duration is zero,
     *      and 1 if the duration is postive.
     */
    public int signum() {
        return signum;
    }
    
    
    /**
     * Adds this duration to a {@link java.util.Calendar} object.
     * 
     * <p>
     * Calls {@link java.util.Calendar#add(int,int)} in the
     * order of YEARS, MONTHS, DAYS, HOURS, MINUTES, SECONDS, and MILLISECONDS
     * if those fields are present. Because the {@link java.util.Calendar} class
     * uses int to hold values, there are cases where this method
     * won't work correctly (for example if values of fields
     * exceed the range of int.) 
     * </p>
     * 
     * <p>
     * Also, since this duration class is a Gregorian duration, this
     * method will not work correctly if the given {@link java.util.Calendar}
     * object is based on some other calendar systems. 
     * </p>
     * 
     * <p>
     * Any fractional parts of this {@link javax.xml.datatype.Duration} object
     * beyond milliseconds will be simply ignored. For example, if
     * this duration is "P1.23456S", then 1 is added to SECONDS,
     * 234 is added to MILLISECONDS, and the rest will be unused. 
     * </p>
     * 
     * <p>
     * Note that because {@link java.util.Calendar#add(int, int)} is using
     * <tt>int</tt>, {@link javax.xml.datatype.Duration} with values beyond the
     * range of <tt>int</tt> in its fields
     * will cause overflow/underflow to the given {@link java.util.Calendar}.
     * {@link javax.xml.datatype.GregorianDateTime#add(javax.xml.datatype.Duration)} provides the same
     * basic operation as this method while avoiding
     * the overflow/underflow issues.
     * 
     * @param calendar
     *      A calendar object whose value will be modified.
     * @throws NullPointerException
     *      if the calendar parameter is null.
     */
    public void addTo(Calendar calendar) {
        calendar.add(Calendar.YEAR, getYears() * signum);
        calendar.add(Calendar.MONTH, getMonths() * signum);
        calendar.add(Calendar.DAY_OF_MONTH, getDays() * signum);
        calendar.add(Calendar.HOUR, getHours() * signum);
        calendar.add(Calendar.MINUTE, getMinutes() * signum);
        calendar.add(Calendar.SECOND, getSeconds() * signum);
    }
    
    /**
     * Adds this duration to a {@link java.util.Date} object.
     * 
     * <p>
     * The given date is first converted into
     * a {@link java.util.GregorianCalendar}, then the duration
     * is added exactly like the {@link #addTo(java.util.Calendar)} method.
     * 
     * <p>
     * The updated time instant is then converted back into a
     * {@link java.util.Date} object and used to update the given {@link java.util.Date} object.
     * 
     * <p>
     * This somewhat redundant computation is necessary to unambiguously
     * determine the duration of months and years.
     * 
     * @param date
     *      A date object whose value will be modified.
     * @throws NullPointerException
     *      if the date parameter is null.
     */
    public void addTo(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date); // this will throw NPE if date==null
        this.addTo(cal);
        date.setTime(getCalendarTimeInMillis(cal));
    }
    
    /**
     * <p>Stream Unique Identifier.</p>
     * 
     * <p>TODO: Serialization should use the XML string representation as
     * the serialization format to ensure future compatibility.</p>
     */
    private static final long serialVersionUID = 1L; 
    
    /**
     * Writes {@link javax.xml.datatype.Duration} as a lexical representation
     * for maximum future compatibility.
     * 
     * @return
     *      An object that encapsulates the string
     *      returned by <code>this.toString()</code>.
     */
    private Object writeReplace() throws IOException {
        return new DurationStream(this.toString());
    }
    
    /**
     * Representation of {@link javax.xml.datatype.Duration} in the object stream.
     * 
     * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
     */
    private static class DurationStream implements Serializable {
        private final String lexical;

        private DurationStream(String _lexical) {
            this.lexical = _lexical;
        }
        
        private Object readResolve() throws ObjectStreamException {
            //            try {
            return new Duration(lexical);
            //            } catch( ParseException e ) {
            //                throw new StreamCorruptedException("unable to parse "+lexical+" as duration");
            //            }
        }
        
        private static final long serialVersionUID = 1L; 
    }
    
    /**
     * Calls the {@link java.util.Calendar#getTimeInMillis} method.
     * Prior to JDK1.4, this method was protected and therefore
     * cannot be invoked directly.
     * 
     * In future, this should be replaced by
     * <code>cal.getTimeInMillis()</code>
     */
    private static long getCalendarTimeInMillis(Calendar cal) {
        return cal.getTime().getTime();
    }

    
}

