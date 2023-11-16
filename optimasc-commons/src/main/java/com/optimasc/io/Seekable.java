package com.optimasc.io;

import java.io.IOException;

/** Implemented by some streams so the read/write position can be adjusted.
 *  The API are similar to a subset of the {@link javax.imageio.stream.ImageInputStream}
 *  interface.
 * 
 * @author Carl Eric Codere
 *
 */
public interface Seekable
{
  /** Returns the current byte position of the stream.
   *  The next read will take place starting at this offset.
   *
   * @return The current position of the stream
   * @throws IOException
   */
  public long getStreamPosition() throws IOException;
  /** Sets the current stream position to the desired location.
   *  The next read will occur at this location. The bit offset is set to 0.
   *
   *  It is legal to seek past the end of the file; an EOFException will be thrown only if a read is performed.
   *
   * @param pos the desired stream pointer position in bytes.
   * @throws IOException
   */
  public void seek(long pos) throws IOException;
  /** Returns the total length of the stream, if known.
   *  Otherwise, -1 is returned.
   *
   * @return the length of the stream in bytes, if known, or else -1.
   * @throws IOException
   */
  public long length() throws IOException;
}
