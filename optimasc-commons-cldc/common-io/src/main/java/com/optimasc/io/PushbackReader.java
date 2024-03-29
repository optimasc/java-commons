/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.io;

import java.io.IOException;
import java.io.Reader;

    /**
     * Wraps an existing {@link Reader} and adds functionality to "push back"
     * characters that have been read, so that they can be read again. Parsers may
     * find this useful. The number of characters which may be pushed back can be
     * specified during construction. If the buffer of pushed back bytes is empty,
     * characters are read from the underlying reader.
     */
public class PushbackReader {

        /**
         * The {@code char} array containing the chars to read.
         */
        char[] buf;

        /**
         * The current position within the char array {@code buf}. A value
         * equal to buf.length indicates no chars available. A value of 0 indicates
         * the buffer is full.
         */
        int pos;

        protected Reader in;

        /**
         * Constructs a new {@code PushbackReader} with the specified reader as
         * source. The size of the pushback buffer is set to the default value of 1
         * character.
         *
         * @param in
         *            the source reader.
         */
        protected  PushbackReader(Reader in)
        {
            this.in = in;
            buf = new char[1];
            pos = 1;
        }

        /**
         * Constructs a new {@code PushbackReader} with {@code in} as source reader.
         * The size of the pushback buffer is set to {@code size}.
         *
         * @param in
         *            the source reader.
         * @param size
         *            the size of the pushback buffer.
         * @throws IllegalArgumentException
         *             if {@code size} is negative.
         */
        public PushbackReader(Reader in, int size) {
            this.in = in;
            buf = new char[1];
            pos = 1;
            if (size <= 0) {
                throw new IllegalArgumentException("Invalid size argument.");
            }
            buf = new char[size];
            pos = size;
        }

        /**
         * Closes this reader. This implementation closes the source reader
         * and releases the pushback buffer.
         *
         * @throws IOException
         *             if an error occurs while closing this reader.
         */
        public void close() throws IOException {
                buf = null;
                in.close();
        }

        /**
         * Marks the current position in this stream. Setting a mark is not
         * supported in this class; this implementation always throws an
         * {@code IOException}.
         *
         * @param readAheadLimit
         *            the number of character that can be read from this reader
         *            before the mark is invalidated; this parameter is ignored.
         * @throws IOException
         *             if this method is called.
         */
        public void mark(int readAheadLimit) throws IOException {
            throw new IOException("Mark not supported");
        }

        /**
         * Indicates whether this reader supports the {@code mark(int)} and
         * {@code reset()} methods. {@code PushbackReader} does not support them, so
         * it returns {@code false}.
         *
         * @return always {@code false}.
         * @see #mark(int)
         * @see #reset()
         */
        public boolean markSupported() {
            return false;
        }

        /**
         * Reads a single character from this reader and returns it as an integer
         * with the two higher-order bytes set to 0. Returns -1 if the end of the
         * reader has been reached. If the pushback buffer does not contain any
         * available characters then a character from the source reader is returned.
         * Blocks until one character has been read, the end of the source reader is
         * detected or an exception is thrown.
         *
         * @return the character read or -1 if the end of the source reader has been
         *         reached.
         * @throws IOException
         *             if this reader is closed or an I/O error occurs while reading
         *             from this reader.
         */
        public synchronized int read() throws IOException {
                if (buf == null) {
                    throw new IOException("Invalid buffer");
                }
                /* Is there a pushback character available? */
                if (pos < buf.length) {
                    return buf[pos++];
                }
                /**
                 * Assume read() in the InputStream will return 2 lowest-order bytes
                 * or -1 if end of stream.
                 */
                return in.read();
        }

        /**
         * Reads at most {@code length} bytes from this reader and stores them in
         * byte array {@code buffer} starting at {@code offset}. Characters are
         * read from the pushback buffer first, then from the source reader if more
         * bytes are required. Blocks until {@code count} characters have been read,
         * the end of the source reader is detected or an exception is thrown.
         *
         * @param buffer
         *            the array in which to store the characters read from this
         *            reader.
         * @param offset
         *            the initial position in {@code buffer} to store the characters
         *            read from this reader.
         * @param count
         *            the maximum number of bytes to store in {@code buffer}.
         * @return the number of bytes read or -1 if the end of the source reader
         *         has been reached.
         * @throws IndexOutOfBoundsException
         *             if {@code offset < 0} or {@code count < 0}, or if
         *             {@code offset + count} is greater than the length of
         *             {@code buffer}.
         * @throws IOException
         *             if this reader is closed or another I/O error occurs while
         *             reading from this reader.
         */
        public synchronized int read(char[] buffer, int offset, int count) throws IOException {
                if (null == buf) {
                    throw new IOException("Invalid buffer");
                }
                // avoid int overflow
                // BEGIN android-changed
                // Exception priorities (in case of multiple errors) differ from
                // RI, but are spec-compliant.
                // made implicit null check explicit, used (offset | count) < 0
                // instead of (offset < 0) || (count < 0) to safe one operation
                if (buffer == null) {
                    throw new NullPointerException("");
                }
                if ((offset | count) < 0 || offset > buffer.length - count) {
                    throw new IndexOutOfBoundsException("");
                }
                // END android-changed

                int copiedChars = 0;
                int copyLength = 0;
                int newOffset = offset;
                /* Are there pushback chars available? */
                if (pos < buf.length) {
                    copyLength = (buf.length - pos >= count) ? count : buf.length
                            - pos;
                    System.arraycopy(buf, pos, buffer, newOffset, copyLength);
                    newOffset += copyLength;
                    copiedChars += copyLength;
                    /* Use up the chars in the local buffer */
                    pos += copyLength;
                }
                /* Have we copied enough? */
                if (copyLength == count) {
                    return count;
                }
                int inCopied = in.read(buffer, newOffset, count - copiedChars);
                if (inCopied > 0) {
                    return inCopied + copiedChars;
                }
                if (copiedChars == 0) {
                    return inCopied;
                }
                return copiedChars;
        }

        /**
         * Indicates whether this reader is ready to be read without blocking.
         * Returns {@code true} if this reader will not block when {@code read} is
         * called, {@code false} if unknown or blocking will occur.
         *
         * @return {@code true} if the receiver will not block when
         *         {@code read()} is called, {@code false} if unknown
         *         or blocking will occur.
         * @throws IOException
         *             if this reader is closed or some other I/O error occurs.
         * @see #read()
         * @see #read(char[], int, int)
         */
        public synchronized boolean ready() throws IOException {
                if (buf == null) {
                    throw new IOException("Invalid buffer");
                }
                return (buf.length - pos > 0 || in.ready());
        }

        /**
         * Resets this reader to the last marked position. Resetting the reader is
         * not supported in this class; this implementation always throws an
         * {@code IOException}.
         *
         * @throws IOException
         *             if this method is called.
         */
        public void reset() throws IOException {
            throw new IOException("Not supported operation.");
        }

        /**
         * Pushes all the characters in {@code buffer} back to this reader. The
         * characters are pushed back in such a way that the next character read
         * from this reader is buffer[0], then buffer[1] and so on.
         * <p>
         * If this reader's internal pushback buffer cannot store the entire
         * contents of {@code buffer}, an {@code IOException} is thrown. Parts of
         * {@code buffer} may have already been copied to the pushback buffer when
         * the exception is thrown.
         *
         * @param buffer
         *            the buffer containing the characters to push back to this
         *            reader.
         * @throws IOException
         *             if this reader is closed or the free space in the internal
         *             pushback buffer is not sufficient to store the contents of
         *             {@code buffer}.
         */
        public void unread(char[] buffer) throws IOException {
            unread(buffer, 0, buffer.length);
        }

        /**
         * Pushes a subset of the characters in {@code buffer} back to this reader.
         * The subset is defined by the start position {@code offset} within
         * {@code buffer} and the number of characters specified by {@code length}.
         * The bytes are pushed back in such a way that the next byte read from this
         * stream is {@code buffer[offset]}, then {@code buffer[1]} and so on.
         * <p>
         * If this stream's internal pushback buffer cannot store the selected
         * subset of {@code buffer}, an {@code IOException} is thrown. Parts of
         * {@code buffer} may have already been copied to the pushback buffer when
         * the exception is thrown.
         *
         * @param buffer
         *            the buffer containing the characters to push back to this
         *            reader.
         * @param offset
         *            the index of the first byte in {@code buffer} to push back.
         * @param length
         *            the number of bytes to push back.
         * @throws IndexOutOfBoundsException
         *             if {@code offset < 0} or {@code count < 0}, or if
         *             {@code offset + count} is greater than the length of
         *             {@code buffer}.
         * @throws IOException
         *             if this reader is closed or the free space in the internal
         *             pushback buffer is not sufficient to store the selected
         *             contents of {@code buffer}.
         * @throws NullPointerException
         *             if {@code buffer} is {@code null}.
         */
        public synchronized void unread(char[] buffer, int offset, int length) throws IOException {
                if (buf == null) {
                    // K0059=Stream is closed
                    throw new IOException("Invalud buffer");
                }
                if (length > pos) {
                    // K007e=Pushback buffer full
                    throw new IOException("Pushback buffer full");
                }
                // Force buffer null check first!
                if (offset > buffer.length - length || offset < 0) {
                    // K002e=Offset out of bounds \: {0}
                    throw new ArrayIndexOutOfBoundsException("offset out of bounds");
                }
                if (length < 0) {
                    // K0031=Length out of bounds \: {0}
                    throw new ArrayIndexOutOfBoundsException("length out of bounds");
                }

                for (int i = offset + length - 1; i >= offset; i--) {
                    unread(buffer[i]);
                }
        }

        /**
         * Pushes the specified character {@code oneChar} back to this reader. This
         * is done in such a way that the next character read from this reader is
         * {@code (char) oneChar}.
         * <p>
         * If this reader's internal pushback buffer cannot store the character, an
         * {@code IOException} is thrown.
         *
         * @param oneChar
         *            the character to push back to this stream.
         * @throws IOException
         *             if this reader is closed or the internal pushback buffer is
         *             full.
         */
        public synchronized void unread(int oneChar) throws IOException {
                if (buf == null) {
                    throw new IOException("Invalid buffer");
                }
                if (pos == 0) {
                    throw new IOException("Invalid position");
                }
                buf[--pos] = (char) oneChar;
        }

        /**
         * Skips {@code count} characters in this reader. This implementation skips
         * characters in the pushback buffer first and then in the source reader if
         * necessary.
         *
         * @param count
         *            the number of characters to skip.
         * @return the number of characters actually skipped.
         * @throws IllegalArgumentException if {@code count < 0}.
         * @throws IOException
         *             if this reader is closed or another I/O error occurs.
         */
        public synchronized long skip(long count) throws IOException {
            if (count < 0) {
                throw new IllegalArgumentException();
            }
                if (buf == null) {
                    throw new IOException("Invalid buffer");
                }
                if (count == 0) {
                    return 0;
                }
                long inSkipped;
                int availableFromBuffer = buf.length - pos;
                if (availableFromBuffer > 0) {
                    long requiredFromIn = count - availableFromBuffer;
                    if (requiredFromIn <= 0) {
                        pos += count;
                        return count;
                    }
                    pos += availableFromBuffer;
                    inSkipped = in.skip(requiredFromIn);
                } else {
                    inSkipped = in.skip(count);
                }
                return inSkipped + availableFromBuffer;
            }
}
