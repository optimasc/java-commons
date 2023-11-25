/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.archive;

import com.optimasc.text.PrintfFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import junit.framework.TestCase;

/**
 *
 * @author Carl
 */
public class ZIPFileReaderTest extends TestCase {
    
    public ZIPFileReaderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public static final int ENTRIES_COUNT = 21;
    public static final String ENTRIES_FILENAMES[] =
    {"calgary/",
     "calgary/bib",
     "calgary/book1",
     "calgary/book2",
     "calgary/geo",
     "calgary/news",
     "calgary/obj1",
     "calgary/obj2",
     "calgary/paper1",
     "calgary/paper2",
     "calgary/paper3",
     "calgary/paper4",
     "calgary/paper5",
     "calgary/paper6",
     "calgary/pic",
     "calgary/progc",
     "calgary/progl",
     "calgary/progp",
     "calgary/trans",
     "test\u00e9.txt",
     "\u81ea\u8ff0.txt"
    };

    public static final int ENTRIES_UNCOMPRESSED_SIZE[] =
    {0,
     111261,
     768771,
     610856,
     102400,
     // news
     377109,
      21504,
     246814,
      53161,
      82199,
      46526,
      13286,
      11954,
      38105,
     513216,
      39611,
      71646,
      49379,
      93695,
         56,
         66
    };

    public static final long ENTRIES_MTIME[] =
    {
       // "calgary/
        Date.UTC(2013-1900,07-1,12,14,35,31),
       // "calgary/bib"
        Date.UTC(1991-1900,07-1,15,05,55,46),
      // "calgary/book1"
        Date.UTC(1991-1900,07-1,15,05,56,0),
      // "calgary/book2"
        Date.UTC(1991-1900,07-1,15,05,56,10),
      // "calgary/geo"
        Date.UTC(1991-1900,07-1,15,05,56,12),
      // "calgary/news"
        Date.UTC(1991-1900,07-1,15,05,56,16),
      // "calgary/obj1"
        Date.UTC(1991-1900,07-1,15,05,56,18),
      // "calgary/obj2"
        Date.UTC(1991-1900,07-1,15,05,56,26),
      // "calgary/paper1"
        Date.UTC(1991-1900,07-1,15,05,56,28),
      // "calgary/paper2"
        Date.UTC(1991-1900,07-1,15,05,56,30),
      // "calgary/paper3"
        Date.UTC(1991-1900,07-1,15,05,56,30),
      // "calgary/paper4"
        Date.UTC(1991-1900,07-1,15,05,56,32),
      // "calgary/paper5"
        Date.UTC(1991-1900,07-1,15,05,56,32),
      // "calgary/paper6"
        Date.UTC(1991-1900,07-1,15,05,56,36),
      // "calgary/pic"
        Date.UTC(1991-1900,07-1,15,05,57,04),
      // "calgary/progc"
        Date.UTC(1991-1900,07-1,15,05,57,04),
      // "calgary/progl"
        Date.UTC(1991-1900,07-1,15,05,57,06),
      // "calgary/progp"
        Date.UTC(1991-1900,07-1,15,05,57,8),
      // "calgary/trans"
        Date.UTC(1991-1900,07-1,15,05,57,10),
      //"test\u00e9.txt"
        Date.UTC(2013-1900,07-1,12,14,39,03),
      // "\u81ea\u8ff0.txt"
        Date.UTC(2013-1900,07-1,12,14,38,8)
    };

  /**
   * Converts a calendar object to an ISO 8601 string conformant representation.
   * This shall be represented as an ISO 8601 representation, depending on
   * the parameters passed.
   *
   * @param cal
   *          The calendar to use for the conversion
   * @param useTime
   *          Set to true if the time information should be added.
   * @param useTimezone
   *          Set to true if the timezone information should be added.
   * @return The ISO 8601 String representation of this date
   * @throws java.lang.IllegalArgumentException
   */
  public static String toCompactString(Calendar cal, boolean useTime,
      boolean useTimezone) throws IllegalArgumentException
  {
    PrintfFormat TwoDigitFormat;
    PrintfFormat YearFormat;
    String s = null;
    Object o[];

    TwoDigitFormat = new PrintfFormat("%02d");
    YearFormat = new PrintfFormat("%04d");

    if (cal == null)
    {
      throw new IllegalArgumentException("Calendar parameter is null.");
    }
    s = YearFormat.sprintf(cal.get(Calendar.YEAR));

    // Verify if month is set
    s = s + TwoDigitFormat.sprintf(cal.get(Calendar.MONTH) + 1);
    s = s + TwoDigitFormat.sprintf(cal.get(Calendar.DAY_OF_MONTH));
    if (useTime == true)
    {
      s = s + "T" + TwoDigitFormat.sprintf(cal.get(Calendar.HOUR_OF_DAY))
          + TwoDigitFormat.sprintf(cal.get(Calendar.MINUTE));
      s = s + TwoDigitFormat.sprintf(cal.get(Calendar.SECOND));
      // Timezone offset
      TimeZone tz = cal.getTimeZone();
      if (useTimezone == true)
      {
        int tzoffset = tz.getRawOffset();
        if (tzoffset == 0)
          s = s + "Z";
        else
        {
          int tzminutes = tzoffset / (1000 * 60);
          int tzhour = tzminutes / 60;
          tzminutes = tzminutes % 60;
          if (tzminutes > 0)
          {
            s = s + "+";
          }
          s = s + TwoDigitFormat.sprintf(tzhour) + ":"
              + TwoDigitFormat.sprintf(tzminutes);
        }

      }
    }

    return s;
  }




    /**
     * Test of readEntries method, of class ZIPFileReader.
     */
    public void testReadEntries() throws Exception
    {
        ZIPFileReader instance = null;
        ArchiveEntry entry;
        Vector v;
        int i;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        System.out.println("readEntries");
        instance = new ZIPFileReader(getClass().getResourceAsStream("/res/calgary-winrar.zip"));

        assertEquals(instance.getComment(),"This is the archive comment.");
        assertEquals(instance.entries().size(),ENTRIES_COUNT);
        v = instance.entries();
        for (i = 0; i < ENTRIES_COUNT; i++)
        {
            entry = instance.getEntry(ENTRIES_FILENAMES[i]);
            assertEquals(entry.getName(),ENTRIES_FILENAMES[i]);
            assertEquals(entry.getSize(),ENTRIES_UNCOMPRESSED_SIZE[i]);
            assertEquals(true,compareDates(new Date(ENTRIES_MTIME[i]),entry.getLastModifiedDate()));
        }


        instance = new ZIPFileReader(getClass().getResourceAsStream("/res/calgary-7z.zip"));

        assertEquals(instance.getComment(),null);
        assertEquals(instance.entries().size(),ENTRIES_COUNT);
        v = instance.entries();
        for (i = 0; i < ENTRIES_COUNT; i++)
        {
            entry = instance.getEntry(ENTRIES_FILENAMES[i]);
            assertEquals(entry.getName(),ENTRIES_FILENAMES[i]);
            assertEquals(entry.getSize(),ENTRIES_UNCOMPRESSED_SIZE[i]);
            assertEquals(true,compareDates(new Date(ENTRIES_MTIME[i]),entry.getLastModifiedDate()));

        }

        instance = new ZIPFileReader(getClass().getResourceAsStream("/res/calgary-infozip.zip"));

        assertEquals(instance.getComment(),null);
        assertEquals(instance.entries().size(),ENTRIES_COUNT);
        v = instance.entries();
        for (i = 0; i < ENTRIES_COUNT; i++)
        {
            entry = instance.getEntry(ENTRIES_FILENAMES[i]);
            assertEquals(entry.getName(),ENTRIES_FILENAMES[i]);
            assertEquals(entry.getSize(),ENTRIES_UNCOMPRESSED_SIZE[i]);
            assertEquals(true,compareDates(new Date(ENTRIES_MTIME[i]),entry.getLastModifiedDate()));
        }


        // Special case with old zip file -
        //  -> The initial directory is not stored
        //  -> The last filename is stored under another filenames since it does not fit in CP437
        //  -> It uses the internal DOS timestamp - therefore we must adjust the times

        instance = new ZIPFileReader(getClass().getResourceAsStream("/res/calgary-pkzip1.zip"));

        assertEquals(instance.getComment(),null);
        v = instance.entries();
        for (i = 1; i < ENTRIES_COUNT-1; i++)
        {
            entry = instance.getEntry(ENTRIES_FILENAMES[i].toUpperCase());
            assertEquals(entry.getName().toUpperCase(),ENTRIES_FILENAMES[i].toUpperCase());
            assertEquals(entry.getSize(),ENTRIES_UNCOMPRESSED_SIZE[i]);
            cal.clear();
            // 8 hour difference when this zip file was created on the local filesystem
            cal.setTimeInMillis(ENTRIES_MTIME[i]+(8*60*60*1000));
            assertEquals(true,compareDates(cal.getTime(),entry.getLastModifiedDate()));
            if (entry.getName().equals(ENTRIES_FILENAMES[20].toUpperCase()))
            {
               assertEquals(((ZipEntry)entry).getComment(),"This is a comment");
            }
        }

        // Special case with info-zip file from linux
        //  -> The last entries are not included because of filename limitations

        instance = new ZIPFileReader(getClass().getResourceAsStream("/res/calgary-infozip-linux.zip"));
        assertEquals(instance.getComment(),null);
        v = instance.entries();
        for (i = 0; i < ENTRIES_COUNT-2; i++)
        {
            entry = instance.getEntry(ENTRIES_FILENAMES[i]);
            assertEquals(entry.getName(),ENTRIES_FILENAMES[i]);
            assertEquals(entry.getSize(),ENTRIES_UNCOMPRESSED_SIZE[i]);
        }

        
    }
    
    
    public static boolean compareDates(Date expected, Date value)
    {
      // Millisecond precision is removed. and last second also.
      long expectedValue = expected.getTime() / 10000;
      long actualValue = value.getTime() / 10000;
      if (expectedValue != actualValue)
        return false;
      return true;
      
    }


}
