/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.io;

import java.io.DataInput;
import java.io.IOException;

/** Extended DataInput interface that also permits reading
 *  int unsigned values.
 *
 * @author carl
 */
public interface DataInputEx extends DataInput
{
    /**
     * Reads four input bytes and returns
     * an <code>long</code> value in the range <code>0</code>
     * through <code>2^32</code>.
     *
     *
     * @return     the unsigned 32-bit value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
    public long readUnsignedInt() throws IOException;


}
