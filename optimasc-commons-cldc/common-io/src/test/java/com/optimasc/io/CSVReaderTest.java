package com.optimasc.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

/**
 * Unit tests for {@link CSVReader}.
 *
 * <p>Each test method covers one distinct behaviour or boundary condition.
 * Tests use {@link StringReader} to feed input without requiring any file
 * system access, keeping the suite self-contained and runnable on CLDC 1.1
 * emulators as well as standard JVMs.</p>
 */
public class CSVReaderTest extends TestCase
{

  /* ======================================================================
   *  HELPERS
   * ====================================================================== */

  /**
   * Convenience: parse every record from {@code csv} and return them as a
   * two-dimensional array.  The outer index is the record number (0-based)
   * and the inner index is the field number.
   */
  private static String[][] parseAll(String csv) throws IOException
  {
    Reader r = new StringReader(csv);
    java.util.Vector records = new java.util.Vector();
    String[] record;
    while ((record = CSVReader.parseCSVRecord(r, ',')) != null)
    {
      records.addElement(record);
    }
    String[][] result = new String[records.size()][];
    for (int i = 0; i < records.size(); i++)
    {
      result[i] = (String[]) records.elementAt(i);
    }
    return result;
  }

  /** Asserts that a single-record parse of {@code csv} yields {@code expected}. */
  private static void assertRecord(String[] expected, String csv)
      throws IOException
  {
    String[] actual = CSVReader.parseCSVRecord(new StringReader(csv), ',');
    assertNotNull("Expected a record but got null.", actual);
    assertEquals("Field count mismatch.", expected.length, actual.length);
    for (int i = 0; i < expected.length; i++)
    {
      assertEquals("Field " + i + " mismatch.", expected[i], actual[i]);
    }
  }

  /* ======================================================================
   *  BASIC PARSING
   * ====================================================================== */

  /** A single unquoted field. */
  public void testSingleField() throws IOException
  {
    assertRecord(new String[]{ "hello" }, "hello");
  }

  /** Two unquoted fields separated by the delimiter. */
  public void testTwoUnquotedFields() throws IOException
  {
    assertRecord(new String[]{ "foo", "bar" }, "foo,bar");
  }

  /** Three unquoted fields. */
  public void testThreeUnquotedFields() throws IOException
  {
    assertRecord(new String[]{ "a", "b", "c" }, "a,b,c");
  }

  /** Empty fields at start, middle, and end. */
  public void testEmptyFields() throws IOException
  {
    assertRecord(new String[]{ "", "b", "" }, ",b,");
  }

  /** All fields empty (two delimiters, three empty fields). */
  public void testAllEmptyFields() throws IOException
  {
    assertRecord(new String[]{ "", "", "" }, ",,");
  }

  /* ======================================================================
   *  QUOTED FIELDS
   * ====================================================================== */

  /** A quoted field containing no special characters. */
  public void testSimpleQuotedField() throws IOException
  {
    assertRecord(new String[]{ "hello" }, "\"hello\"");
  }

  /** A quoted field containing the delimiter character. */
  public void testQuotedFieldWithDelimiter() throws IOException
  {
    assertRecord(new String[]{ "hello,world" }, "\"hello,world\"");
  }

  /** Two quoted fields. */
  public void testTwoQuotedFields() throws IOException
  {
    assertRecord(new String[]{ "foo,bar", "baz" }, "\"foo,bar\",baz");
  }

  /** RFC 4180 escaped double-quote: {@code ""} inside a quoted field. */
  public void testEscapedQuoteInsideQuotedField() throws IOException
  {
    assertRecord(new String[]{ "say \"hello\"" }, "\"say \"\"hello\"\"\"");
  }

  /** A quoted field that is completely empty. */
  public void testEmptyQuotedField() throws IOException
  {
    assertRecord(new String[]{ "" }, "\"\"");
  }

  /** Mixed quoted and unquoted fields in one record. */
  public void testMixedQuotedAndUnquoted() throws IOException
  {
    assertRecord(new String[]{ "plain", "quo,ted", "plain2" },
        "plain,\"quo,ted\",plain2");
  }

  /* ======================================================================
   *  EMBEDDED NEWLINES (the original bug)
   * ====================================================================== */

  /** A quoted field containing an embedded LF — must stay in one record. */
  public void testEmbeddedNewlineInQuotedField() throws IOException
  {
    String csv = "\"line1\nline2\",next";
    assertRecord(new String[]{ "line1\nline2", "next" }, csv);
  }

  /** A quoted field containing an embedded CRLF. */
  public void testEmbeddedCRLFInQuotedField() throws IOException
  {
    String csv = "\"line1\r\nline2\",next";
    String[] record = CSVReader.parseCSVRecord(new StringReader(csv), ',');
    assertNotNull(record);
    assertEquals(2, record.length);
    /* The CRLF inside the quote is preserved verbatim. */
    assertEquals("line1\r\nline2", record[0]);
    assertEquals("next", record[1]);
  }

  /** Two records where the first contains an embedded newline. */
  public void testMultiRecordWithEmbeddedNewline() throws IOException
  {
    String csv = "\"a\nb\",c\nd,e";
    String[][] records = parseAll(csv);
    assertEquals("Expected 2 records.", 2, records.length);
    assertEquals(2, records[0].length);
    assertEquals("a\nb", records[0][0]);
    assertEquals("c",    records[0][1]);
    assertEquals(2, records[1].length);
    assertEquals("d",    records[1][0]);
    assertEquals("e",    records[1][1]);
  }

  /* ======================================================================
   *  MULTIPLE RECORDS
   * ====================================================================== */

  /** Two simple records separated by LF. */
  public void testTwoRecords() throws IOException
  {
    String[][] records = parseAll("a,b\nc,d");
    assertEquals(2, records.length);
    assertEquals("a", records[0][0]);
    assertEquals("b", records[0][1]);
    assertEquals("c", records[1][0]);
    assertEquals("d", records[1][1]);
  }

  /** Three records. */
  public void testThreeRecords() throws IOException
  {
    String[][] records = parseAll("1,2\n3,4\n5,6");
    assertEquals(3, records.length);
    assertEquals("1", records[0][0]);
    assertEquals("4", records[1][1]);
    assertEquals("5", records[2][0]);
  }

  /** Records separated by CRLF. */
  public void testCRLFRecordSeparator() throws IOException
  {
    String[][] records = parseAll("a,b\r\nc,d");
    assertEquals(2, records.length);
    assertEquals("a", records[0][0]);
    assertEquals("b", records[0][1]);
    assertEquals("c", records[1][0]);
    assertEquals("d", records[1][1]);
  }

  /** A trailing newline after the last record does not produce a ghost record.
   *  RFC 4180 section 2 rule 2 says the last record MAY have a trailing CRLF.
   *  We accept both: trailing newline produces one extra empty record, which
   *  the caller can discard.  This test documents the actual behaviour. */
  public void testTrailingNewlineProducesEmptyRecord() throws IOException
  {
    String[][] records = parseAll("a,b\n");
    /*
     * The LF ends record 0; the parser then reads the next character which
     * is EOF, returning null — so we get exactly one record.
     */
    assertEquals("Trailing LF should yield exactly 1 record.", 1, records.length);
    assertEquals("a", records[0][0]);
    assertEquals("b", records[0][1]);
  }

  /* ======================================================================
   *  EDGE CASES
   * ====================================================================== */

  /** Empty stream returns null immediately. */
  public void testEmptyStreamReturnsNull() throws IOException
  {
    String[] record = CSVReader.parseCSVRecord(new StringReader(""), ',');
    assertNull("Empty stream must return null.", record);
  }

  /** A single empty line (bare LF) returns one record with one empty field. */
  public void testSingleEmptyLine() throws IOException
  {
    String[] record = CSVReader.parseCSVRecord(new StringReader("\n"), ',');
    assertNotNull(record);
    assertEquals(1, record.length);
    assertEquals("", record[0]);
  }

  /** Numbers and digits are passed through verbatim. */
  public void testNumericFields() throws IOException
  {
    assertRecord(new String[]{ "123", "45.67", "-8" }, "123,45.67,-8");
  }

  /** A quoted field immediately followed by another quoted field. */
  public void testAdjacentQuotedFields() throws IOException
  {
    assertRecord(new String[]{ "a", "b" }, "\"a\",\"b\"");
  }

  /** A field value consisting of only spaces. */
  public void testSpacesInField() throws IOException
  {
    assertRecord(new String[]{ "   " }, "   ");
  }

  /** Spaces are preserved inside quoted fields. */
  public void testSpacesInsideQuotedField() throws IOException
  {
    assertRecord(new String[]{ "  hello  " }, "\"  hello  \"");
  }

  /** Semi-colon delimiter instead of comma. */
  public void testSemicolonDelimiter() throws IOException
  {
    Reader r = new StringReader("a;b;c");
    String[] record = CSVReader.parseCSVRecord(r, ';');
    assertNotNull(record);
    assertEquals(3, record.length);
    assertEquals("a", record[0]);
    assertEquals("b", record[1]);
    assertEquals("c", record[2]);
  }

  /** Tab delimiter. */
  public void testTabDelimiter() throws IOException
  {
    Reader r = new StringReader("col1\tcol2\tcol3");
    String[] record = CSVReader.parseCSVRecord(r, '\t');
    assertNotNull(record);
    assertEquals(3, record.length);
    assertEquals("col1", record[0]);
    assertEquals("col2", record[1]);
    assertEquals("col3", record[2]);
  }

  /* ======================================================================
   *  ERROR CASES
   * ====================================================================== */

  /** Null reader must throw IllegalArgumentException. */
  public void testNullReaderThrows() throws IOException
  {
    try
    {
      CSVReader.parseCSVRecord(null, ',');
      fail("Expected IllegalArgumentException for null reader.");
    }
    catch (IllegalArgumentException e) { /* expected */ }
  }

  /** Quote character as delimiter must throw IllegalArgumentException. */
  public void testQuoteAsDelimiterThrows() throws IOException
  {
    try
    {
      CSVReader.parseCSVRecord(new StringReader("a"), '"');
      fail("Expected IllegalArgumentException for quote delimiter.");
    }
    catch (IllegalArgumentException e) { /* expected */ }
  }

  /** LF as delimiter must throw IllegalArgumentException. */
  public void testLFAsDelimiterThrows() throws IOException
  {
    try
    {
      CSVReader.parseCSVRecord(new StringReader("a"), '\n');
      fail("Expected IllegalArgumentException for LF delimiter.");
    }
    catch (IllegalArgumentException e) { /* expected */ }
  }

  /** CR as delimiter must throw IllegalArgumentException. */
  public void testCRAsDelimiterThrows() throws IOException
  {
    try
    {
      CSVReader.parseCSVRecord(new StringReader("a"), '\r');
      fail("Expected IllegalArgumentException for CR delimiter.");
    }
    catch (IllegalArgumentException e) { /* expected */ }
  }

  /** An unclosed quoted field must throw IOException. */
  public void testUnclosedQuoteThrowsIOException()
  {
    try
    {
      CSVReader.parseCSVRecord(new StringReader("\"unclosed"), ',');
      fail("Expected IOException for unclosed quoted field.");
    }
    catch (IOException e)   { /* expected */ }
    catch (Exception e)
    {
      fail("Expected IOException but got: " + e.getClass().getName());
    }
  }

  /** An unclosed quote spanning multiple lines must throw IOException. */
  public void testUnclosedQuoteMultilineThrowsIOException()
  {
    try
    {
      CSVReader.parseCSVRecord(new StringReader("\"line1\nline2"), ',');
      fail("Expected IOException for unclosed multi-line quoted field.");
    }
    catch (IOException e)   { /* expected */ }
    catch (Exception e)
    {
      fail("Expected IOException but got: " + e.getClass().getName());
    }
  }
}
