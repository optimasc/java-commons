package com.optimasc.dataset;

import java.io.IOException;
import java.util.Calendar;

/** Interface to be implemented by each dataset driver.
 *  Some information concerning the implementation, the actual checking to see if this field's
 *  datatype can be read is already done by the wrapper. It should just simply return the data
 *  without any actual validation.
 *  
 *  Even though in the public API the column indexes the column indexes start at 1, the
 *  driver interface assumes they start at index 0.  
 *  
 */  
public interface DatasetDriver
{
  
  public DatasetMetaData getMetaData();

  public int getBytes(int columnIndex, byte[] outBuffer, int outOffset)
      throws IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public double getReal(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  public double getDouble(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  public float getFloat(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  /**
   * 
   * @param columnIndex
   * @param outBuffer
   * @return The number of bytes actually read.
   * @throws IOException
   * @throws IllegalArgumentException
   * @throws DatasetException
   * @throws UnsupportedOperationException
   */
  public int getString(int columnIndex, StringBuffer outBuffer)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public int getInteger(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  public byte getByte(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;;

  public boolean getBoolean(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;;

  public short getShort(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  public Calendar getDate(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  public int getAutoInc(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  public Calendar getTime(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  public Calendar getTimestamp(int columnIndex)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public int getCLOB(int columnIndex, StringBuffer outBuffer)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public int getBLOB(int columnIndex, byte[] outBuffer, int outOffset)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  /*--------------------------- Update routines ------------------------------*/

  public void updateByte(int columnIndex, byte value)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateBytes(int columnIndex, byte[] inBuffer, int inOffset,
      int inLength) throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateReal(int columnIndex, double value)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateFloat(int columnIndex, float value)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateDouble(int columnIndex, double value)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateString(int columnIndex, StringBuffer inBuffer)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateInteger(int columnIndex, int value)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateBoolean(int columnIndex, boolean value)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateShort(int columnIndex, short value)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateDate(int columnIndex, Calendar cal)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateTime(int columnIndex, Calendar cal)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateTimestamp(int columnIndex, Calendar cal)
      throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

  public void updateNull(int columnIndex) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  /*-------------------------- Utility routines ---------------------------------*/

  public int getRecordCount() throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException;

  public boolean first() throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException, IOException;

  public boolean next() throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException, IOException;

  public boolean absolute(int row) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException, IOException;

  public boolean wasNull() throws IOException, IllegalArgumentException,
  DatasetException, UnsupportedOperationException;
  
  public int getCodePage() throws IOException, IllegalArgumentException, DatasetException,
      UnsupportedOperationException;

}
