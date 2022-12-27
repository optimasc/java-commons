/*
 * 
 * See License.txt for more information on the licensing terms
 * for this source code.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * OPTIMA SC INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.optimasc.io;

import java.io.IOException;

/**
 *
 * @author carl
 */
public class LittleEndianDataInput extends BasicDataInput
{

    public LittleEndianDataInput(InputStreamInterface in)
    {
        super(in);
        w = new byte[8];
    }

    public int available() throws IOException
    {
        return in.available();
    }

    public final short readShort() throws IOException
    {
        readFully(w, 0, 2);
        return (short) ((w[1] & 0xff) << 8
                | (w[0] & 0xff));
    }

    /**
     * Note, returns int even though it reads a short.
     */
    public final int readUnsignedShort() throws IOException
    {
        readFully(w, 0, 2);
        return ((w[1] & 0xff) << 8
                | (w[0] & 0xff));
    }

    /**
     * like DataInputStream.readChar except little endian.
     */
    public final char readChar() throws IOException
    {
        readFully(w, 0, 2);
        return (char) ((w[1] & 0xff) << 8
                | (w[0] & 0xff));
    }

    /**
     * like DataInputStream.readInt except little endian.
     */
    public final int readInt() throws IOException
    {
        readFully(w, 0, 4);
        return (w[3]) << 24
                | (w[2] & 0xff) << 16
                | (w[1] & 0xff) << 8
                | (w[0] & 0xff);
    }

    /**
     * like DataInputStream.readLong except little endian.
     */
    public final long readLong() throws IOException
    {
        readFully(w, 0, 8);
        return (long) (w[7]) << 56
                | (long) (w[6] & 0xff) << 48
                | (long) (w[5] & 0xff) << 40
                | (long) (w[4] & 0xff) << 32
                | (long) (w[3] & 0xff) << 24
                | (long) (w[2] & 0xff) << 16
                | (long) (w[1] & 0xff) << 8
                | (long) (w[0] & 0xff);
    }



    public long readUnsignedInt() throws IOException
    {
        return (long) (readInt() & 0xFFFFFFFFL);
    }

    public String readLine() throws IOException
    {
        throw new IllegalArgumentException("Not supported yet.");
    }
    // InputStream
    private byte w[]; // work array for buffering input
}
