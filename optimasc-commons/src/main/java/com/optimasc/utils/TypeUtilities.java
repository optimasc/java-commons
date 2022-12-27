package com.optimasc.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** Class that is used to convert from binary ot other type representation. */
public class TypeUtilities {


    /** Converts a string to its binary encoded representation, the format
     *  being [L1][V1]..[Ln][Vn] where earch L is a length encoded as a
     *  16-bit signed big endian number. Each value is encoded using the 
     *  UTF-8 encoding format.
     *  
     * 
     * @param values The String array to represent as a binary value.
     * @return The asllocated byte array representing the full string
     *  values.
     * @throws IOException
     */
    public static byte[] toByteArray(String[] values) throws IOException {
        byte[] output = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);

        for (int i = 0; i < values.length; i++) {
            os.writeUTF(values[i]);
        }

        os.close();
        output = baos.toByteArray();
        baos.close();
        return output;
    }
    
    
    


}
