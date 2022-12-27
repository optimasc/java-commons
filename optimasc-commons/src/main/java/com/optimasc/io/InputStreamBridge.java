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
import java.io.InputStream;

/**
 *
 * @author carl
 */
public class InputStreamBridge implements InputStreamInterface
{

    private InputStream is;

    public InputStreamBridge(InputStream input)
    {
        this.is = input;
    }

    public int available() throws IOException
    {
        return is.available();
    }

    public void close() throws IOException
    {
        is.close();
    }

    public void mark(int readlimit) throws IOException
    {
        is.mark(readlimit);
    }

    public boolean markSupported()
    {
        return is.markSupported();
    }

    public int read() throws IOException
    {
        return is.read();
    }

    public int read(byte[] b) throws IOException
    {
       return is.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
       return is.read(b,off,len);
    }

    public void reset() throws IOException
    {
       is.reset();
    }

    public long skip(long n) throws IOException
    {
        return is.skip(n);
    }

}
