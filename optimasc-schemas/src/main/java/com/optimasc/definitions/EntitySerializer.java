package com.optimasc.definitions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Interface to save and load named entities  from a stream. */
public interface EntitySerializer
{
  
  /** Writes the specified entity registry to the
   *  specified stream.
   * 
   * @param registry [in] The entity registry to output.
   * @param out [in] The output stream to write to.
   * @throws IOException Thrown in case of I/O Error
   */
  public void write(EntityRegistry registry, OutputStream out) throws IOException;
  /** Loads one or more several entities from a stream. 
   * 
   * @param in [in] The stream to read from.
   * @return An instance of the registry with populated 
   *   entries
   * @throws IOException Thrown in case of I/O Error
   */
  public EntityRegistry load(InputStream in) throws IOException;
}
