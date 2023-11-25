package com.optimasc.archive;

import java.util.Date;

/** Represents an abstract representation of an entry in an 
 *  archive or disk file, containing the basic attributes
 *  common to all formats.
 *  
 *  Each implementation should override this class and 
 *  implement the appropriate methods and additional
 *  methods as needed.
 *  
 *  The implementation is equal to the Apache Commons
 *  <code>org.apache.commons.compress.archivers.ArchiveEntry
 *  interface.</code>
 * 
 * @author Carl Eric Codere
 *
 */

public interface ArchiveEntry
{
  /** Returned to indicate that the size of the file/uncompressed
   *  file is unknown.
   */
  public static final long UNKNOWN_SIZE = -1;
  
  /** Returns the last modification date of this entry. Normally
   *  this value is always non-null.
   * 
   *  @return The last modification date of this archive entry.
   */
  public Date getLastModifiedDate();
  
  /** Returns the raw name of this archive entry. */  
  public String getName();
  
  /** Returns the uncompressed size of this entry. If
   *  the size is unknown, the value {@link #UNKNOWN_SIZE}
   *  will be returned.
   * 
   * @return The size of the uncompressed data associated 
   *   with this entry.
   */
  public long getSize();
  
  /** Returns true if this entry refers to a directory. */
  public boolean isDirectory();
  
}
