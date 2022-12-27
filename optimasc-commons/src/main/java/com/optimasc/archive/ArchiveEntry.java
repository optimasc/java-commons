package com.optimasc.archive;

import java.util.Calendar;

/** Represents an abstract representation of an entry in an 
 *  archive or disk file, containing all the attributes
 *  associated with this archive.
 *  
 *  Each implementation should override this class and 
 *  implement the appropriate methods.
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class ArchiveEntry
{

  String name = null; // entry name
  Calendar modificationTime = null; // Last Modification time
  Calendar creationTime = null; // Creation time 
  Calendar accessTime = null; // Access time

  long compressedSize = -1;
  long uncompressedSize = -1;

  String comment; // Comment associated with this entry;

  long crc = -1; // crc-32 of entry data
  long size = -1; // uncompressed size of entry data
  int method = -1; // compression method
  byte[] extra; // optional extra field data for entry
  // The following flags are used only by Zip{Input,Output}Stream
  int flag; // bit flags
  int version; // version needed to extract

  
  /**
   * Returns the name of the entry in the archive. 
   * 
   * @return the name of the entry
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the uncompressed size of the entry data.
   * 
   * @param size
   *          the uncompressed size in bytes of the resource.
   * @exception IllegalArgumentException
   *              if the specified size is less than 0 or greater than
   *              than the limit supported by the archive format.
   * @see #getSize()
   */
  public abstract void setSize(long size);

  
  /**
   * Returns the uncompressed size of the entry data, or -1 if not known.
   * 
   * @return the uncompressed size of the entry data, or -1 if not known
   * @see #setSize(long)
   */
  public long getSize()
  {
    return size;
  }

  /**
   * Returns the size of the compressed entry data, or -1 if not known. 
   * 
   * @return the size of the compressed entry data, or -1 if not known
   * @see #setCompressedSize(long)
   */
  public long getCompressedSize()
  {
    return compressedSize;
  }

  /**
   * Sets the size of the compressed entry data.
   * 
   * @param csize
   *          the compressed size to set to
   * @exception IllegalArgumentException
   *              if the specified size is not valid for this archive format. 
   * @see #getCompressedSize()
   */
  public abstract void setCompressedSize(long csize);

  
  /**
   * Sets the checksum of the uncompressed entry data.
   * 
   * @param checksum
   *          the checksum value
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
   * Returns the CRC-32 checksum of the uncompressed entry data, or -1 if not
   * known.
   * 
   * @return the CRC-32 checksum of the uncompressed entry data, or -1 if not
   *         known
   * @see #getCrc()
   */
  public long getCrc()
  {
    return crc;
  }

  /**
   * Sets the compression method for the entry.
   * 
   * @param method
   *          the compression method used internally in the archive,
   *          this is archive specific. 
   * @exception IllegalArgumentException
   *              if the specified compression method is invalid
   * @see #getMethod()
   */
  public abstract void setMethod(int method);

  
  /**
   * Returns the compression method of the entry, or -1 if not specified.
   * 
   * @return the compression method of the entry, or -1 if not specified
   * @see #setMethod(int)
   */
  public int getMethod()
  {
    return method;
  }

  /**
   * Sets the optional extra field data for the entry.
   * 
   * @param extra
   *          the extra field data bytes. This data is archive specific.
   * @exception IllegalArgumentException
   *              if the length of the specified extra field data is greater
   *              than allowed for this archive format.
   * @see #getExtra()
   */
  public abstract void setExtra(byte[] extra);

  /**
   * Returns the extra field data for the entry, or null if none is
   * present or it is not supported.
   * 
   * @return the extra field data for the entry, or null if none
   * @see #setExtra(byte[])
   */
  public byte[] getExtra()
  {
    return extra;
  }

  /**
   * Sets the optional comment string for the entry.
   * 
   * @param comment
   *          the comment string
   * @exception IllegalArgumentException
   *              if the length of the specified comment string is greater than
   *              allowed by the archive format or if the comment is not
   *              supported in this archive format.
   * @see #getComment()
   */
  public abstract void setComment(String comment);

  /**
   * Returns the comment string for the entry, or null if none.
   * 
   * @return the comment string for the entry, or null if none
   * @see #setComment(String)
   */
  public String getComment()
  {
    return comment;
  }

  /** Returns if this entry is a directory entry.
   * 
   * @return true if this entry represents a directory and not a file
   */
  public abstract boolean isDirectory();

  /**
   * Returns a string representation of the archive entry.
   */
  public String toString()
  {
    return getName();
  }

  /** Returns the modification time of the entry as found on the
   *  original file system, or null if unknown.
   *  
   * @return
   */
  public Calendar getModificationTime()
  {
    return modificationTime;
  }

  /** Returns the creation time of the entry as found on
   *  the original file system, or null if unknown.
   * 
   *  
   * @return
   */
  public Calendar getCreationTime()
  {
    return creationTime;
  }

  /** Returns the last access time of the entry as found
   *  on the original file system, or null if unknown.
   * 
   *  
   * @return
   */
  public Calendar getAccessTime()
  {
    return accessTime;
  }
  
  /** Sets the name associated with this entry.
   * 
   * @param name The name associated with this entry.
   */
  public abstract void setName(String name);
  
  
  

}
