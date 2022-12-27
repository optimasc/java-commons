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
import java.io.OutputStream;

/**
 *
 * @author Carl Eric Codere <carl.codere@optimasc.com>
 */
public class OutputStreamBridge implements OutputStreamInterface
{
    private OutputStream os;

    public OutputStreamBridge(OutputStream input)
    {
        this.os = input;
    }

    public void close() throws IOException
    {
        os.close();
    }

    public void flush() throws IOException
    {
        os.flush();
    }

    public void write(int b) throws IOException
    {
        os.write(b);
    }

    public void write(byte[] b) throws IOException
    {
        os.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException
    {
        os.write(b,off,len);
    }

}
