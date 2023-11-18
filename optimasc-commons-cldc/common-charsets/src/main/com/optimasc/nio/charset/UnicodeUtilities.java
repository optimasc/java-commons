/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.optimasc.text;

/**
 *
 * @author Carl
 */
public class UnicodeUtilities
{

    private static final int trailingBytesForUTF8[] =
    {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5
    };
    private static final int offsetsFromUTF8[] =
    {
        0x00000000, 0x00003080, 0x000E2080,
        0x03C82080, 0xFA082080, 0x82082080
    };
    /** When the character is not supported in UCS-2 it is replaced
     *  by this character.
     */
    private static final int UNI_REPLACEMENT_CHAR = 0x0000FFFD;
    /** Maximum value of an UCS-2 character.
     *
     */
    private static final int UNI_MAX_UCS2 = 0x0000FFFF;

    /** Converts a byte array of characters encoded in UTF-8 and converts
     *  it to a StringBuffer.
     *
     * @param inBuffer
     * @param off
     * @param len
     * @param outBuffer
     */
    public static void UTF8Decode(byte inBuffer[], int off, int len, StringBuffer outBuffer)
    {
        int i = off;
        int ch = 0;
        int extraBytesToRead;
        int currentIndex;
        int maxlen = len;
        byte[] src = inBuffer;
        outBuffer.setLength(0);
        outBuffer.ensureCapacity(len*2);
        while (i < maxlen)
        {
            ch = 0;
            extraBytesToRead = trailingBytesForUTF8[src[i] & 0xff];
            currentIndex = extraBytesToRead;
            if (currentIndex == 5)
            {
                ch = ch + (src[i] & 0xff);
                i++;
                ch = ch << 6;
                currentIndex--;
            }
            if (currentIndex == 4)
            {
                ch = ch + (src[i] & 0xff);
                i++;
                ch = ch << 6;
                currentIndex--;
            }
            if (currentIndex == 3)
            {
                ch = ch + (src[i] & 0xff);
                i++;
                ch = ch << 6;

            }
            if (currentIndex == 2)
            {
                ch = ch + (src[i] & 0xff);
                i++;
                ch = ch << 6;
                currentIndex--;
            }
            if (currentIndex == 1)
            {
                ch = ch + (src[i] & 0xff);
                i++;
                ch = ch << 6;
                currentIndex--;
            }
            if (currentIndex == 0)
            {
                ch = ch + (src[i] & 0xff);
                i++;
            }
            ch = ch - offsetsFromUTF8[extraBytesToRead];
            if (ch <= UNI_MAX_UCS2)
            {
                outBuffer.append((char)ch);
            } else
            {
                outBuffer.append((char) UNI_REPLACEMENT_CHAR);
            }
        }
    }
}
