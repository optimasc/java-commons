package com.optimasc.io;

import java.io.DataOutput;
import java.io.IOException;

/** Extended DataOutput interface that also permits writing
 *  unsigned primitive values. This is used when the underlying
 *  stream distinguishes between signed and unsigned formats.
 *
 * @author carl
 */
public interface DataOutputEx extends DataOutput
{
  public void writeUnsignedInt(long v) throws IOException;
  public void writeUnsignedByte(int v) throws IOException;
  public void writeUnsignedShort(int v) throws IOException;
}
