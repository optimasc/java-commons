package com.optimasc.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/** Abstract class that is used to read information and get streams on
 *  data from a resource file.
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class ArchiveFileReader
{

    protected InputStream is;
    /** The position of stream.
     *
     */
    private long position;
    private byte w[]; // work array for buffering input

    /**
     * 
     * @param is InputStream for the resource. 
     * @throws IllegalArgumentException
     *       Thrown if mark is not supported in the InputStream
     * @throws IOException
     */
    public ArchiveFileReader(InputStream is) throws IllegalArgumentException, IOException
    {
        if (is.markSupported() == false)
        {
            throw new IllegalArgumentException("Mark support is required for InputStream!");
        }
        if (is != null)
        {
            is.mark(is.available());
        }
        this.is = is;
        w = new byte[8];
    }

    /** Closes the archive file.
     *
     */
    public abstract void close();

    /** Returns a vector containing all the entries in this
     *  resource.
     * 
     * @return
     */
    public abstract Vector entries();
    

    /** Returns the resource entry for the specified name, or null if not found.
     *
     * @param name The name to look for.
     * @return
     */
    public ArchiveEntry getEntry(String name)
    {
        Vector v = entries();
        ArchiveEntry entry;
        for (int i = 0; i < v.size(); i++)
        {
            entry = (ArchiveEntry) v.elementAt(i);
            if (entry.getName().equals(name))
            {
                return entry;
            }
        }
        return null;
    }

    /** Returns an input stream for reading the contents of the specified archive entry.
     *
     * @param entry
     * @return
     */
    public abstract InputStream getInputStream(ArchiveEntry entry)  throws IOException, IllegalArgumentException;

//  String getName() 
//  Returns the path name of the ZIP file.
    /** Returns the number of entries in the archive file.
     *
     * @return
     */
    public abstract int size();

    /** Returns the comment associated with this archive, if this information
     *  can be stored in the archive file or null if there are no comments
     *  or this feature is not supported.
     *
     */
    public abstract String getComment();

    
    protected final short readShort() throws IOException
    {
        read(w, 0, 2);
        position += 2;
        return (short) ((w[1] & 0xff) << 8 | (w[0] & 0xff));
    }

    /**
     * Note, returns int even though it reads a short.
     */
    protected final int readUnsignedShort() throws IOException
    {
        read(w, 0, 2);
        position += 2;
        return ((w[1] & 0xff) << 8 | (w[0] & 0xff));
    }

    /**
     * like DataInputStream.readChar except little endian.
     */
    protected final char readChar() throws IOException
    {
        read(w, 0, 2);
        position += 2;
        return (char) ((w[1] & 0xff) << 8 | (w[0] & 0xff));
    }

    /**
     * like DataInputStream.readInt except little endian.
     */
    protected final int readInt() throws IOException
    {
        read(w, 0, 4);
        position += 4;
        return (w[3]) << 24 | (w[2] & 0xff) << 16 | (w[1] & 0xff) << 8
                | (w[0] & 0xff);
    }

    /**
     * like DataInputStream.readLong except little endian.
     */
    protected final long readLong() throws IOException
    {
        read(w, 0, 8);
        position += 8;
        return (long) (w[7]) << 56 | (long) (w[6] & 0xff) << 48
                | (long) (w[5] & 0xff) << 40 | (long) (w[4] & 0xff) << 32
                | (long) (w[3] & 0xff) << 24 | (long) (w[2] & 0xff) << 16
                | (long) (w[1] & 0xff) << 8 | (long) (w[0] & 0xff);
    }

    protected final long readUnsignedInt() throws IOException
    {
        return (long) (readInt() & 0xFFFFFFFFL);
    }

    protected final void read(byte[] buffer, int off, int len) throws IOException
    {
        is.read(buffer, off, len);
        position += len;
    }

    protected final void skip(long offset) throws IOException
    {
        long skipped = is.skip(offset);
        offset = offset - skipped;
        while (offset > 0)
        {
            skipped = is.skip(offset);
            offset = offset - skipped;
        }
        position += offset;
    }
}
