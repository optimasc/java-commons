package com.optimasc.io;

import java.io.IOException;
import java.io.Reader;

/** Class that implements a simple text line reader. Portable
 *  across different java editions, and allows reading all different
 *  types of text files.
 *  
 *  Taken from stackoverflow.com
 *
 */
public class LineReader
{
    private Reader in;
    private int bucket=-1;

    public LineReader(Reader in){
        this.in=in;
    }

    public boolean hasLine() throws IOException{
        if(bucket!=-1)return true;
        bucket=in.read();
        return bucket!=-1;
    }

    //Read a line, removing any /r and /n. Buffers the string
    public String readLine() throws IOException{
        int tmp;
        StringBuffer out=new StringBuffer();
        //Read in data
        while(true){
            //Check the bucket first. If empty read from the input stream
            if(bucket!=-1){
                tmp=bucket;
                bucket=-1;
            }else{
                tmp=in.read();
                if(tmp==-1)break;
            }
            //If new line, then discard it. If we get a \r, we need to look ahead so can use bucket
            if(tmp=='\r'){
                int nextChar=in.read();
                if(tmp!='\n')bucket=nextChar;//Ignores \r\n, but not \r\r
                break;
            }else if(tmp=='\n'){
                break;
            }else{
                //Otherwise just append the character
                out.append((char) tmp);
            }
        }
        return out.toString();
    }

}
