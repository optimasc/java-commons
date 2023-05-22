package com.optimasc.dataset;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Calendar;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.derived.DateType;
import com.optimasc.datatypes.primitives.BinaryType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.primitives.StringType;
import com.optimasc.datatypes.primitives.TimeType;

public class BaseIndexableDataset implements IndexableDataset
{
  protected DatasetDriver   driver;

  // Used internally as temporary buffer;
  protected StringBuffer    strBuffer;
  
  // Default buffer size
  private static final int BUFFER_SIZE = 255;
  
  public BaseIndexableDataset(DatasetDriver driver)
  {
    strBuffer = new StringBuffer(BUFFER_SIZE);
    this.driver = driver;
    
  }

  public Class getType(String dataItemName) throws DatasetException
  {
    return driver.getMetaData().getDatatype(findColumn(dataItemName)).getClassType();
  }

  public Object getValue(String dataItemName) throws DatasetException, IllegalArgumentException, IOException
  {
    int columnIndex = findColumn(dataItemName);
    return getObject(columnIndex);
  }

  public String getAsString(String dataItemName) throws DatasetException, IllegalArgumentException, IOException
  {
    int columnIndex = findColumn(dataItemName);
    return getObject(columnIndex).toString();
  }

  public boolean isReadOnly(String dataItemName) throws DatasetException
  {
    int column = findColumn(dataItemName);
    return driver.getMetaData().isReadOnly(column);
  }

  public DatasetMetaData getMetaData() throws DatasetException
  {
    return driver.getMetaData();
  }

  public void setAsString(String dataItemName, String value)
      throws DatasetException
  {
  }

  public void setValue(String dataItemName, Object value)
      throws DatasetException
  {
    int columnIndex = findColumn(dataItemName);
    updateObject(columnIndex, value);
  }

  public boolean first()  throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException, IOException
  {
      return driver.first();
  }

  public boolean next() throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException, IOException
  {
      return driver.next();
  }

  public boolean absolute(int row) throws IOException, IllegalArgumentException,
      DatasetException, UnsupportedOperationException, IOException
  {
      return driver.absolute(row);
  }


  public boolean wasNull() throws DatasetException, IllegalArgumentException, IOException
  {
    return driver.wasNull();
  }

  public boolean getBoolean(int columnIndex) throws DatasetException,
      IllegalArgumentException, IOException
  {
    int type = driver.getMetaData().getDatatype(columnIndex).getType();
    columnIndex--;
    if (type != Datatype.BOOLEAN)
    {
      throw new IllegalArgumentException(
          "Trying to read a boolean value from a non-boolean field.");
    }
    return driver.getBoolean(columnIndex);
  }

  public InputStream getBinaryStream(int columnIndex) throws DatasetException,
      IllegalArgumentException
  {
    columnIndex--;
    // TODO Auto-generated method stub
    return null;
  }

  /* This calls the internal calls, which actually decrement the column index
   * values, so this should not do it!
   * 
   * @see com.optimasc.dataset.IndexableDataset#getObject(int)
   */
  public Object getObject(int columnIndex) throws DatasetException,
      IllegalArgumentException, IOException
  {
    int type = driver.getMetaData().getDatatype(columnIndex).getType();
    switch (type)
    {
    case Datatype.ARRAY:
        break;
    case Datatype.BIGINT:
        break;
    case Datatype.BINARY:
    case Datatype.LONGVARBINARY:
    case Datatype.VARBINARY:
        break;
    case Datatype.BIT:
        break;
    case Datatype.BLOB:
        break;
    case Datatype.BOOLEAN:
      return new Boolean(getBoolean(columnIndex));
    case Datatype.DATALINK:
      break;
    case Datatype.DATE:
      break;
    case Datatype.DECIMAL:
      break;
    case Datatype.DISTINCT:
      break;
    case Datatype.REAL:
    case Datatype.DOUBLE:
      return new Double(getReal(columnIndex));
    case Datatype.FLOAT:
      return new Float((float)getReal(columnIndex));
    case Datatype.INTEGER:
      return new Integer(getInteger(columnIndex));
    case Datatype.JAVA_OBJECT:
      break;
    case Datatype.NULL:
      break;
    case Datatype.NUMERIC:
      break;
    case Datatype.OTHER:
      break;
    case Datatype.REF:
      break;
    case Datatype.SMALLINT:
      return new Short((short)getInteger(columnIndex));
    case Datatype.STRUCT:
      break;
    case Datatype.TIME:
      break;
    case Datatype.TIMESTAMP:
      break;
    case Datatype.TINYINT:
      return new Byte((byte)getInteger(columnIndex));
    case Datatype.VARCHAR:
    case Datatype.LONGVARCHAR:
    case Datatype.CHAR:
    case Datatype.CLOB:
      int length = getString(strBuffer,columnIndex);
      return new String(strBuffer);
    case Datatype.ENUM:
      break;
      
    }
    return null;
  }

  public int findColumn(String columnName) throws DatasetException,
      IllegalArgumentException
  {
    DatasetMetaData tableMetadata =driver.getMetaData(); 
    int count = tableMetadata.getCount();
    for (int i = 1; i <= count; i++)
    {
      if (columnName.equals(tableMetadata.getName(i)))
      {
        return i;
      }
    }
    throw new DatasetException("Column with specified name does not exist.");
  }


  public int getRow() throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub
    return 0;
  }

  public boolean rowUpdated() throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean rowInserted() throws DatasetException,
      IllegalArgumentException
  {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean rowDeleted() throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub
    return false;
  }

  public void updateNull(int columnIndex) throws DatasetException,
      IllegalArgumentException, IOException
  {
    driver.updateNull(columnIndex);
  }

  public void updateBoolean(int columnIndex, boolean x)
      throws DatasetException, IllegalArgumentException
  {
    int type = driver.getMetaData().getDatatype(columnIndex).getType();
    if (type != Datatype.BOOLEAN)
    {
      throw new IllegalArgumentException(
          "Trying to write a boolean value from a non-boolean field.");
    }
  }

  public void updateString(int columnIndex, String x) throws DatasetException,
      IllegalArgumentException
  {
    DatasetMetaData tableMetadata = driver.getMetaData(); 
    int type = tableMetadata.getType(columnIndex);
    if (((type == Datatype.CLOB) || (type == Datatype.CHAR)
        || (type == Datatype.VARCHAR) || (type == Datatype.LONGVARCHAR))==false)
    {
      throw new IllegalArgumentException(
          "Trying to read a binary value from a non-binary field.");
    }
    StringType strType = (StringType) tableMetadata
        .getDatatype(columnIndex);
    try
    {
      strType.validate(x);
    } catch (DatatypeException e)
    {
      throw new IllegalArgumentException(
      "Trying to write a string with an invalid constraint - check type definition.");
    }
  }

  public void updateBytes(int columnIndex, byte[] x) throws DatasetException,
      IllegalArgumentException
  {
    DatasetMetaData tableMetadata = driver.getMetaData();
    int type = tableMetadata.getType(columnIndex);
    if (((type == Datatype.BLOB) || (type == Datatype.BINARY) || (type == Datatype.VARBINARY))==false)
    {
      throw new IllegalArgumentException(
          "Trying to read a binary value from a non-binary field.");
    }
    BinaryType binType = (BinaryType) tableMetadata.getDatatype(columnIndex);
    try
    {
      binType.validate(x);
    } catch (DatatypeException e)
    {
      throw new IllegalArgumentException(
      "Trying to write binary data with invalid constraints - check type definition.");
    }
  }

  public void updateBinaryStream(int columnIndex, InputStream x, int length)
      throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub

  }

  public void updateCharacterStream(int columnIndex, Reader x, int length)
      throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub

  }

  public void updateObject(int columnIndex, Object x) throws DatasetException,
      IllegalArgumentException
  {
        // TODO Auto-generated method stub
  }

  public void insertRow() throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub

  }

  public void updateRow() throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub

  }

  public void deleteRow() throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub

  }

  public void refreshRow() throws DatasetException, IllegalArgumentException
  {
    // TODO Auto-generated method stub

  }

  public int getInteger(int columnIndex) throws DatasetException,
      IllegalArgumentException, IOException
  {
    int type = driver.getMetaData().getType(columnIndex);
    columnIndex--;
    switch (type)
    {
    case Datatype.INTEGER:
        return driver.getInteger(columnIndex);
    case Datatype.SMALLINT:
      return driver.getShort(columnIndex);
    case Datatype.TINYINT:
        return driver.getByte(columnIndex);
    default:
      throw new IllegalArgumentException(
          "Trying to read an integer value from a non-integer field.");
    }
    
  }

  public double getReal(int columnIndex) throws DatasetException,
      IllegalArgumentException, IOException
  {
    int type = driver.getMetaData().getType(columnIndex);
    columnIndex--;
    switch (type)
    {
        case Datatype.REAL: 
        case Datatype.DOUBLE:
            return driver.getDouble(columnIndex);
        case Datatype.FLOAT:
            return driver.getFloat(columnIndex);
        default:
          throw new IllegalArgumentException(
          "Trying to read a real value to a non-real field.");
    }
    
  }

  public Calendar getDate(int columnIndex) throws DatasetException,
      IllegalArgumentException, IOException
  {
    int type = driver.getMetaData().getType(columnIndex);
    columnIndex--;
    if (type != Datatype.DATE)
    {
      throw new IllegalArgumentException(
          "Trying to read a date value from a non-date field.");
    }
    return driver.getDate(columnIndex);
  }

  public Calendar getTime(int columnIndex) throws DatasetException,
      IllegalArgumentException, IOException
  {
    int type = driver.getMetaData().getType(columnIndex);
    columnIndex--;
    if (type != Datatype.TIME)
    {
      throw new IllegalArgumentException(
          "Trying to read a time value from a non-time field.");
    }
    return driver.getTime(columnIndex);
  }

  public Calendar getTimestamp(int columnIndex) throws DatasetException,
      IllegalArgumentException, IOException
  {
    int type = driver.getMetaData().getType(columnIndex);
    columnIndex--;
    if (type != Datatype.TIMESTAMP)
    {
      throw new IllegalArgumentException(
          "Trying to read a timestamp value from a non-timestamp field.");
    }
    return driver.getTimestamp(columnIndex);
  }

  public void updateInteger(int columnIndex, int x) throws DatasetException,
      IllegalArgumentException, IOException
  {
    DatasetMetaData tableMetadata = driver.getMetaData();
    int type = tableMetadata.getType(columnIndex);
    if (!((type == Datatype.BIGINT) || (type == Datatype.INTEGER)
        || (type == Datatype.SMALLINT) || (type == Datatype.TINYINT)))
    {
      throw new IllegalArgumentException(
          "Trying to write an integer value to a non-integer field.");
    }
    IntegralType intType = (IntegralType) tableMetadata.getDatatype(columnIndex);
    try
    {
      intType.validate(x);
    } catch (DatatypeException e)
    {
      throw new IllegalArgumentException(
      "Trying to write an integer with an invalid range - check type definition.");
    }
    
    columnIndex--;
    switch (type)
    {
        case Datatype.INTEGER: 
        case Datatype.DOUBLE:
            driver.updateInteger(columnIndex, x);
        case Datatype.SMALLINT:
            driver.updateShort(columnIndex, (short)x);
        case Datatype.TINYINT:
            driver.updateByte(columnIndex, (byte)x);
    }
    
  }

  public void updateReal(int columnIndex, double x) throws DatasetException,
      IllegalArgumentException, IOException
  {
    DatasetMetaData tableMetadata = driver.getMetaData();
    int type = tableMetadata.getType(columnIndex);
    if (!((type == Datatype.DOUBLE) || (type == Datatype.FLOAT) || (type == Datatype.REAL)))
    {
      throw new IllegalArgumentException(
          "Trying to write a real value to a non-real field.");
    }
    RealType realType = (RealType) tableMetadata.getDatatype(columnIndex);
    try
    {
      realType.validate(new Double(x));
    } catch (DatatypeException e)
    {
      throw new IllegalArgumentException(
      "Trying to write a real value with an invalid constraint - check type definition.");
    }
    columnIndex--;
    switch (type)
    {
        case Datatype.REAL: 
        case Datatype.DOUBLE:
            driver.updateDouble(columnIndex, x);
        case Datatype.FLOAT:
            driver.updateFloat(columnIndex, (float)x);
    }
    
  }

  public void updateDate(int columnIndex, Calendar x) throws DatasetException,
      IllegalArgumentException, IOException
  {
    DatasetMetaData tableMetadata = driver.getMetaData();
    int type = tableMetadata.getType(columnIndex);
    if (type != Datatype.DATE)
    {
      throw new IllegalArgumentException(
          "Trying to write a date value to a non-date field.");
    }
    DateType dateType = (DateType) tableMetadata.getDatatype(columnIndex);
    ///dateType.validate(x);
    driver.updateDate(columnIndex, x);
  }

  public void updateTime(int columnIndex, Calendar x) throws DatasetException,
      IllegalArgumentException, IOException
  {
    DatasetMetaData tableMetadata = driver.getMetaData();
    int type = tableMetadata.getType(columnIndex);
    if (type != Datatype.TIME)
    {
      throw new IllegalArgumentException(
          "Trying to write a time value to a non-time field.");
    }
    TimeType timeType = (TimeType) tableMetadata.getDatatype(columnIndex);
    try
    {
      timeType.validate(x);
    } catch (DatatypeException e)
    {
      throw new IllegalArgumentException(
      "Trying to write a date with an invalid range - check type definition.");
    }
    driver.updateTime(columnIndex, x);
  }

  public void updateTimestamp(int columnIndex, Calendar x)
      throws DatasetException, IllegalArgumentException, IOException
  {
    DatasetMetaData tableMetadata = driver.getMetaData();
    int type = tableMetadata.getType(columnIndex);
    if (type != Datatype.TIMESTAMP)
    {
      throw new IllegalArgumentException(
          "Trying to write a timestamp value to a non-timestamp field.");
    }
    // Nothing to validate in this case
    driver.updateTimestamp(columnIndex, x);
  }

  public int getSize() throws DatasetException, IllegalArgumentException
  {
    try
    {
      return driver.getRecordCount();
    } catch (IOException e)
    {
      throw new DatasetException("I/O Error in reading the data.");
    }
  }

  public int getBytes(int columnIndex, byte[] buffer, int offset)
      throws DatasetException, IllegalArgumentException
  {
    DatasetMetaData tableMetadata = driver.getMetaData();
    int type = tableMetadata.getType(columnIndex);
    columnIndex--;
    switch (type)
    {
    case Datatype.BINARY:
    case Datatype.VARBINARY:
      return driver.getBytes(columnIndex, buffer, offset);
    case Datatype.BLOB:
      try
      {
        return driver.getBLOB(columnIndex, buffer, offset);
      } catch (IOException e)
      {
        throw new DatasetException("I/O Error in reading the data.");
      }
      
    default:
      throw new IllegalArgumentException(
          "Trying to read a binary value from a non-bytes or non-blob field.");
    }
  }

  public int getString(StringBuffer outBuffer, int columnIndex)
      throws DatasetException, IllegalArgumentException, IOException
  {
    DatasetMetaData tableMetadata = driver.getMetaData();
    int type = tableMetadata.getType(columnIndex);
    columnIndex--;
    switch (type)
    {
    case Datatype.CHAR:
    case Datatype.VARCHAR:
      return driver.getString(columnIndex, outBuffer);
    case Datatype.CLOB:
      try
      {
        return driver.getCLOB(columnIndex, outBuffer);
      } catch (IOException e)
      {
        throw new DatasetException("I/O Error in reading the data.");
      } 
    default:
      throw new IllegalArgumentException(
          "Trying to read a string value from a non-memo or non-alpha field.");
    }
  }
    

}



