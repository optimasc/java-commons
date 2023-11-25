package com.optimasc.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.optimasc.nio.charset.CharSets;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

// TODO:
//  - Filesystem attributes setting (NTFS, MS-DOS)
// Limitations:
//  - Does not support ZIP64 format
//  - Does not support encrypted file formats (encryption headers)
//

/** This class implements a small ZIP file archive reader which
 *  follows strictly the PKWare application note.
 *
 *  It is better than the Java standard one, as it supports
 *  decoding UTF-8 filenames as supported by both PKWARE and
 *  INFO-ZIP. It also correctly extracts comments from the archive
 *  and each file within the archive. It also extracts the file
 *  attributes as used in the ExtraField attributes.
 *
 * @author Carl Eric Codere
 */
public class ZIPFileReader extends ArchiveFileReader
{

    private static final int LOCAL_HEADER_SIGNATURE = 0x04034b50;
    private static final int DATA_DESCRIPTOR_SIGNATURE = 0x08074b50;
    private static final int CENTRAL_HEADER_SIGNATURE = 0x02014b50;
    private static final int ARCHIVE_EXTRA_DATA_SIGNATURE = 0x08064b50;
    private static final int CENTRAL_END_HEADER_SIGNATURE = 0x06054b50;


    /* Private chunk identifiers in ExtraData field */
    private static final int CHUNK_UNICODE_PATH = 0x7075;
    private static final int CHUNK_UNICODE_COMMENT = 0x6375;
    private static final int CHUNK_NTFS_ATTRIBUTES = 0x000A;
    private static final int CHUNK_UNIX_TIMESTAMP = 0x5455;

    /**
     * Indicates that the size, compressed size and uncompressed sizes are zero,
     * and a data descriptor immediately follows the file data.
     */
    private int FLAGS_DATA_DESCRIPTOR = 1 << 3;
    /**
     * If this flag is set, it indicates that the filename and comments are stored
     * in UTF-8 format.
     *
     */
    private int FLAGS_UTF8_NAMES = 1 << 11;
    /**
     * Temporary buffer used for reading comments or filenames.
     *
     */
    private byte[] buffer;
    /**
     * Temporary buffer for converting string characters.
     *
     */
    private StringBuffer stringBuffer;
    /** The read entries of the archive.
     *
     */
    private Vector entries;

    /** This is the comment archive */
    private String archiveComment;

    public ZIPFileReader(InputStream is) throws IOException, IllegalArgumentException
    {
        super(is);
        buffer = new byte[65535];
        stringBuffer = new StringBuffer(8192);
        entries = readEntries();
    }

    protected Vector readEntries() throws IOException
    {
        ZipEntry entry;
        String filename;
        int i;
        int length;
        long signature;
        String comment = null;
        byte[] extraDataBuffer = null;
        Vector v = new Vector();
        Date internalmodificationTime;
        Date internalCreationTime;
        Date internalAccessTime;

        // Local header signature
        signature = readUnsignedInt();
        if (signature != LOCAL_HEADER_SIGNATURE)
        {
            throw new IllegalArgumentException("This is not a valid zip file!");
        }

        do
        {
            int version = readUnsignedShort();
            int flags = readUnsignedShort();
            int method = readUnsignedShort();
            long tstamp = readUnsignedInt();
            internalmodificationTime = DOSDateTimeToCalendar(tstamp).getTime();
            long crc32 = readUnsignedInt();
            long compressedSize = readUnsignedInt();
            long uncompressedSize = readUnsignedInt();
            int nameLength = readUnsignedShort();
            int extraLength = readUnsignedShort();

            // Read the filename length.
            read(buffer, 0, nameLength);
            // Read the extra data
            if (extraLength > 0)
            {
                extraDataBuffer = new byte[extraLength];
                read(extraDataBuffer, 0, extraLength);
            }
            // Skip the compressed data
            skip(compressedSize);
            // Data is stored at this location instead
            if ((flags & FLAGS_DATA_DESCRIPTOR) == FLAGS_DATA_DESCRIPTOR)
            {
                crc32 = readUnsignedInt();
                // The value contained a data descriptor signature
                if (crc32 == DATA_DESCRIPTOR_SIGNATURE)
                {
                    crc32 = readUnsignedInt();
                }
                compressedSize = readUnsignedInt();
                uncompressedSize = readUnsignedInt();
            }
            signature = readUnsignedInt();
        } while (signature == LOCAL_HEADER_SIGNATURE);

        if (signature != CENTRAL_HEADER_SIGNATURE)
        {
            if (signature == ARCHIVE_EXTRA_DATA_SIGNATURE)
            {
                length = readUnsignedShort();
                skip(length);
                signature = readUnsignedInt();
            } else
            {
                throw new IllegalArgumentException("Unknown or corrupt ZIP file!");
            }
        }

        if (signature != CENTRAL_HEADER_SIGNATURE)
        {
            throw new IllegalArgumentException("Unknown or corrupt ZIP file!");
        }
        do
        {
            comment = null;
            filename = null;
            internalAccessTime = null;
            internalCreationTime = null;
            int version = readUnsignedShort();
            version = readUnsignedShort();
            int flags = readUnsignedShort();
            int method = readUnsignedShort();
            long tstamp = readUnsignedInt();
            internalmodificationTime = DOSDateTimeToCalendar(tstamp).getTime();
            long crc32 = readUnsignedInt();
            long compressedSize = readUnsignedInt();
            long uncompressedSize = readUnsignedInt();
            int nameLength = readUnsignedShort();
            int extraLength = readUnsignedShort();
            int commentLength = readUnsignedShort();
            int diskNumberStart = readUnsignedShort();
            int internalFileAttributes = readUnsignedShort();
            long externalFileAttributes = readUnsignedInt();
            long relativeOffset = readUnsignedInt();


            // Read the filename length.
            read(buffer, 0, nameLength);
            // Convert to correct character set
            if ((flags & FLAGS_UTF8_NAMES) == FLAGS_UTF8_NAMES)
            {
                filename = new String(buffer, 0, nameLength, "UTF-8");
            } else
            // Code Page 437 - Convert to UCS-2 format
            {
                stringBuffer.ensureCapacity(nameLength);
                stringBuffer.setLength(0);
                for (i = 0; i < nameLength; i++)
                {
                    stringBuffer.append(CharSets.CodePage437ToUCS2[buffer[i] & 0xff]);
                }
                filename = stringBuffer.toString();
            }


            // Read the extra data
            if (extraLength > 0)
            {
                extraDataBuffer = new byte[extraLength];
                read(extraDataBuffer, 0, extraLength);



                //------------- Check if we have unicode filename
                int offset = findTag(CHUNK_UNICODE_PATH,extraDataBuffer,0,extraLength);
                if (offset != -1)
                {
                    int size = getShortLittle(extraDataBuffer,offset) & 0xFFFF;
                    size = size - 5; // Remove version and CRC-32 value
                    filename = new String(extraDataBuffer,offset+2+5,size, "UTF-8");
                }
                //------------- Check if we have unicode comment
                offset = findTag(CHUNK_UNICODE_COMMENT,extraDataBuffer,0,extraLength);
                if (offset != -1)
                {
                    int size = getShortLittle(extraDataBuffer,offset) & 0xFFFF;
                    size = size - 5; // Remove version and CRC-32 value
                    comment = new String(extraDataBuffer,offset+2+5,size, "UTF-8");
                }
                //------------- Check if we have NTFS attributes
                offset = findTag(CHUNK_NTFS_ATTRIBUTES,extraDataBuffer,0,extraLength);
                if (offset != -1)
                {
                    int size = getShortLittle(extraDataBuffer,offset) & 0xFFFF;
                    offset += 2;
                    // Check if we have tag #0001 which contains the NTFS timestamps
                    int internalOffset = findTag(0x0001,extraDataBuffer,offset,extraLength);
                    if ((internalOffset <= (offset + size)) && (internalOffset != -1))
                    {
                        size = getShortLittle(extraDataBuffer,internalOffset) & 0xFFFF;
                        internalOffset += 2;
                        // 3 x 8 byte fields
                        if (size == 24)
                        {
                            long mtime = getLongLittle(extraDataBuffer,internalOffset);
                            internalOffset += 8;
                            internalmodificationTime =  FiletimeToCalendar(mtime).getTime();
                            long atime = getLongLittle(extraDataBuffer,internalOffset);
                            internalOffset += 8;
                            internalAccessTime =  FiletimeToCalendar(atime).getTime();
                            long ctime = getLongLittle(extraDataBuffer,internalOffset);
                            internalOffset += 8;
                            internalCreationTime =  FiletimeToCalendar(ctime).getTime();
                        }
                    }

                }

                offset = findTag(CHUNK_UNIX_TIMESTAMP,extraDataBuffer,0,extraLength);
                if (offset != -1)
                {
                    int size = getShortLittle(extraDataBuffer,offset) & 0xFFFF;
                    offset += 2;
                    int flag = extraDataBuffer[offset] & 0xFF;
                    offset++;
                    long mtime = getIntLittle(extraDataBuffer,offset);
                    Date d = new Date();
                    d.setTime(mtime * 1000);
                    internalmodificationTime = d;
                }


            }
            // If the comment is already read-in in the UTF-8 block, you
            // do not need to read it in again
            if ((commentLength > 0) && (comment == null))
            {
                read(buffer, 0, commentLength);
                // Convert to correct character set
                if ((flags & FLAGS_UTF8_NAMES) == FLAGS_UTF8_NAMES)
                {
                    comment = new String(buffer, 0, commentLength, "UTF-8");
                } else
                // Code Page 437 - Convert to UCS-2 format
                {
                    stringBuffer.ensureCapacity(commentLength);
                    stringBuffer.setLength(0);
                    for (i = 0; i < commentLength; i++)
                    {
                        stringBuffer.append(CharSets.CodePage437ToUCS2[buffer[i]]);
                    }
                    comment = stringBuffer.toString();
                }
            }


            entry = new ZipEntry();
            entry.setMethod(method);
            entry.setCrc(crc32);

            entry.offset = relativeOffset;
            entry.setModificationTime(internalmodificationTime);
            entry.setCreationTime(internalCreationTime);
            entry.setLastAccessTime(internalCreationTime);
            entry.setExtra(extraDataBuffer);
            entry.setCompressedSize(compressedSize);
            entry.setSize(uncompressedSize);
            entry.setCrc(crc32);
            entry.setCompressedSize(compressedSize);
            entry.setSize(uncompressedSize);
            entry.setName(filename);
            v.addElement(entry);

            signature = readUnsignedInt();
        } while (signature == CENTRAL_HEADER_SIGNATURE);

        // Get the comment number
        if (signature == CENTRAL_END_HEADER_SIGNATURE)
        {
            int diskNumber = readUnsignedShort();
            int diskStartCentral = readUnsignedShort();
            int dirEntries = readUnsignedShort();
            int dirEntries2 = readUnsignedShort();
            long dirSize = readUnsignedInt();
            long diskOffset = readUnsignedInt();
            int commentLength = readUnsignedShort();

            // Code Page 437 - Convert to UCS-2 format
            if (commentLength > 0)
            {
                read(buffer, 0, commentLength);
                stringBuffer.ensureCapacity(commentLength);
                stringBuffer.setLength(0);
                for (i = 0; i < commentLength; i++)
                {
                    stringBuffer.append(CharSets.CodePage437ToUCS2[buffer[i] & 0xFF]);
                }
                archiveComment = stringBuffer.toString();
            }
        }
        return v;
    }

    public void close()
    {
        // TODO Auto-generated method stub
    }

    public Vector entries()
    {
        return entries;
    }

    public InputStream getInputStream(ArchiveEntry entry) throws IOException, IllegalArgumentException
    {
        ZipEntry z = (ZipEntry)entry;
        /** Offset to the zip file data is as follows:
         *    sizeof(local file header) + length(file name length) + length(extra field length)
         */
        is.reset();
        skip(z.offset);
        long signature = readUnsignedInt();
        int version = readUnsignedShort();
        int flags = readUnsignedShort();
        int method = readUnsignedShort();
        long tstamp = readUnsignedInt();
        long crc32 = readUnsignedInt();
        long compressedSize = readUnsignedInt();
        long uncompressedSize = readUnsignedInt();
        int nameLength = readUnsignedShort();
        int extraLength = readUnsignedShort();
        skip(nameLength + extraLength);
        return is;
    }

    public int size()
    {
        return entries.size();
    }


    public String getComment()
    {
        return archiveComment;
    }



    /** Searches for the specified tag and returns in the buffer
     *  to the tag data (just after this tag header)
     *
     * @param tag The tag to search for
     * @param buffer The buffer to search in
     * @param off The offset in the buffer to search for
     * @param len The length of the buffer
     * @return the Offset in the buffer, or -1 if not found.
     */
    protected int findTag(int tag, byte[] buffer, int off, int len)
    {
        int sTag = 0;
        int sLength = 0;
        if (len == 0)
            return -1;

        while (off < len)
        {
            sTag = getShortLittle(buffer,off) & 0xFFFF;
            off = off + 2;
            if (sTag == tag)
            {
                return off;
            }
            sLength = getShortLittle(buffer,off) & 0xFFFF;
            off += 2;
            off += sLength;
        }
        return -1;
    }


    public static short getShortLittle(byte[] array, int offset)
    {
        return (short)((array[1+offset] & 0xff) << 8 | (array[offset] & 0xff));
    }

    public static int getIntLittle(byte[] w , int offset)
    {
        return (w[3+offset]) << 24 | (w[2+offset] & 0xff) << 16 | (w[1+offset] & 0xff) << 8
                | (w[offset] & 0xff);

    }

  public static long getLongLittle(byte[] array, int offset) {
    return
      ((long)(array[offset+7]   & 0xff) << 56) |
      ((long)(array[offset+6] & 0xff) << 48) |
      ((long)(array[offset+5] & 0xff) << 40) |
      ((long)(array[offset+4] & 0xff) << 32) |
      ((long)(array[offset+3] & 0xff) << 24) |
      ((long)(array[offset+2] & 0xff) << 16) |
      ((long)(array[offset+1] & 0xff) << 8) |
      ((long)(array[offset] & 0xff));
  }



    /**
     * Smallest supported DOS date/time value in a ZIP file,
     * which is January 1<sup>st</sup>, 1980 AD 00:00:00 local time.
     */
    static final long MIN_DOS_TIME = (1 << 21) | (1 << 16); // 0x210000;

    /** Converts an MS-DOS encoded Date and Time ime to
     *  a Gregorian Calendar representation. Since the
     *  MS-DOS Datetime does not contain any timezone information,
     *  the TZ shall be set to GMT by default.
     *
     * @return
     */
    protected static Calendar DOSDateTimeToCalendar(long datetime)
    {
        if (datetime <= 0)
        {
            datetime = MIN_DOS_TIME;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, (int) (1980 + ((datetime >> 25) & 0x7f)));
        cal.set(Calendar.MONTH, (int) ((datetime >> 21) & 0x0f) - 1);
        cal.set(Calendar.DAY_OF_MONTH, (int) (datetime >> 16) & 0x1f);
        cal.set(Calendar.HOUR_OF_DAY, (int) (datetime >> 11) & 0x1f);
        cal.set(Calendar.MINUTE, (int) (datetime >> 5) & 0x3f);
        cal.set(Calendar.SECOND, (int) (datetime << 1) & 0x3e);
        return cal;
    }


    /** Difference between Filetime epoch and Unix epoch (in ms). */
    private static final long FILETIME_EPOCH_DIFF = 11644473600000L;
    /** One millisecond expressed in units of 100s of nanoseconds. */
    private static final long FILETIME_ONE_MILLISECOND = 10 * 1000;


    /** Converts a FILETIME encoded Date and Time ime to
     *  a Gregorian Calendar representation.
     *
     * @return
     */
    public static Calendar FiletimeToCalendar(long filetime)
    {
       Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
       Date d = new Date();
       d.setTime((filetime / FILETIME_ONE_MILLISECOND) - FILETIME_EPOCH_DIFF);
       cal.setTime(d);
       return cal;
    }

}
