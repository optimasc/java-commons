package com.optimasc.archive;

import java.util.Calendar;
import java.util.Date;

public class ZipEntry extends AbstractArchiveEntry
{

    /**
     * Compression method for uncompressed entries.
     */
    public static final int COMPRESSION_STORED = 0;
    /**
     * Compression method for shrunk entries.
     */
    public static final int COMPRESSION_SHRUNK = 1;
    /**
     * Compression method for reduced entries with compression factor 1
     */
    public static final int COMPRESSION_REDUCED_1 = 2;
    /**
     * Compression method for reduced entries with compression factor 2
     */
    public static final int COMPRESSION_REDUCED_2 = 3;
    /**
     * Compression method for reduced entries with compression factor 3
     */
    public static final int COMPRESSION_REDUCED_3 = 4;
    /**
     * Compression method for reduced entries with compression factor 4
     */
    public static final int COMPRESSION_REDUCED_4 = 5;
    /**
     * Compression method for impldoed entries
     */
    public static final int COMPRESSION_IMPLODED = 6;
    /**
     * Compression method for compressed (deflated) entries.
     */
    public static final int COMPRESSION_DEFLATED = 8;
    /**
     * Compression method for compressed (deflated64) entries.
     */
    public static final int COMPRESSION_DEFLATED64 = 9;
    /**
     * Compression method for bzip2 compressed entries.
     */
    public static final int COMPRESSION_BZIP2 = 12;
    /**
     * Compression method for LZMA compressed entries.
     */
    public static final int COMPRESSION_LZMA = 14;
    /**
     * Compression method for IBM TERSE compressed entries.
     */
    public static final int COMPRESSION_IBM_TERSE = 18;
    /**
     * Compression method for IBM LZ77 Z compressed entries.
     */
    public static final int COMPRESSION_IBM_Z = 19;
    /**
     * Compression method for Wavpack compressed entries.
     */
    public static final int COMPRESSION_WAVPACK = 97;
    /**
     * Compression method for PPMd Version I, Rev 1 compressed entries.
     */
    public static final int COMPRESSION_PPMd = 98;


    /** Offset to this file header from the start of the stream */
    protected long offset;
    long crc = -1; // crc-32 of entry data

    /**
     * Sets the uncompressed size of the entry data.
     *
     * @param size
     *          the uncompressed size in bytes
     * @exception IllegalArgumentException
     *              if the specified size is less than 0 or greater than
     *              0xFFFFFFFF bytes
     * @see #getSize()
     */
    public void setSize(long size)
    {
        if (size < 0 || size > 0xFFFFFFFFL)
        {
            throw new IllegalArgumentException("invalid entry size");
        }
        this.size = size;
    }

    /**
     * Sets the size of the compressed entry data.
     *
     * @param csize
     *          the compressed size to set to
     * @see #getCompressedSize()
     */
    public void setCompressedSize(long csize)
    {
        this.compressedSize = csize;
    }

    /**
     * Sets the CRC-32 checksum of the uncompressed entry data.
     *
     * @param crc
     *          the CRC-32 value
     * @exception IllegalArgumentException
     *              if the specified CRC-32 value is less than 0 or greater than
     *              0xFFFFFFFF
     * @see #setCrc(long)
     */
    public void setCrc(long crc)
    {
        if (crc < 0 || crc > 0xFFFFFFFFL)
        {
            throw new IllegalArgumentException("invalid entry crc-32");
        }
        this.crc = crc;
    }

    /**
     * Sets the compression method for the entry.
     *
     * @param method
     *          the compression method, either STORED or DEFLATED
     * @exception IllegalArgumentException
     *              if the specified compression method is invalid
     * @see #getMethod()
     */
    public void setMethod(int method)
    {

        switch (method)
        {
            case COMPRESSION_STORED:
            case COMPRESSION_SHRUNK:
            case COMPRESSION_REDUCED_1:
            case COMPRESSION_REDUCED_2:
            case COMPRESSION_REDUCED_3:
            case COMPRESSION_REDUCED_4:
            case COMPRESSION_IMPLODED:
            case COMPRESSION_DEFLATED:
            case COMPRESSION_DEFLATED64:
            case COMPRESSION_BZIP2:
            case COMPRESSION_LZMA:
            case COMPRESSION_IBM_TERSE:
            case COMPRESSION_IBM_Z:
            case COMPRESSION_WAVPACK:
            case COMPRESSION_PPMd:
                this.method = method;
                break;
            default:
                throw new IllegalArgumentException("invalid compression method");
        }
    }

    /**
     * Sets the optional extra field data for the entry.
     *
     * @param extra
     *          the extra field data bytes
     * @exception IllegalArgumentException
     *              if the length of the specified extra field data is greater
     *              than 0xFFFF bytes
     * @see #getExtra()
     */
    public void setExtra(byte[] extra)
    {
        if (extra != null && extra.length > 0xFFFF)
        {
            throw new IllegalArgumentException("invalid extra field length");
        }
        this.extra = extra;
    }

    /**
     * Sets the optional comment string for the entry.
     *
     * @param comment
     *          the comment string
     * @exception IllegalArgumentException
     *              if the length of the specified comment string is greater than
     *              0xFFFF bytes
     * @see #getComment()
     */
    public void setComment(String comment)
    {
        if (comment != null && comment.length() > 0xffff / 3)
        {
            throw new IllegalArgumentException("invalid entry comment length");
        }
        this.comment = comment;
    }

    /**
     * Returns true if this is a directory entry. A directory entry is defined to
     * be one whose name ends with a '/'.
     *
     * @return true if this is a directory entry
     */
    public boolean isDirectory()
    {
        return name.endsWith("/");
    }

    public void setName(String name)
    {
        this.name = name;
    }


  public void setModificationTime(Date cal)
  {
    modificationTime = cal;
  }

  public void setCreationTime(Date cal)
  {
    creationTime = cal;
  }

  public void setLastAccessTime(Date cal)
  {
    accessTime = cal;
  }

}
