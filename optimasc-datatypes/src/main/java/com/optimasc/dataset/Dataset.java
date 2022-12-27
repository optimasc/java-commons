package com.optimasc.dataset;

import java.io.IOException;

/**  Interface that implements a simple Dataset that can be used for very simple
 *   data handling. It can be used to represent key-value pairs or data values
 *   in a table for example.
 *
 * @author Carl Eric Codere
 */
public interface Dataset
{

 /** Returns the type class of the specified data item instance. */
 public Class getType(String dataItemName) throws DatasetException;

 /** Returns the value associated with the specified data item. The instance
  *  object returned is the actual Class instance as returned by getType for
  *  this data item.
 * @throws IOException 
 * @throws IllegalArgumentException 
  *  */
 public Object getValue(String dataItemName) throws DatasetException, IllegalArgumentException, IOException;

 /** Returns the value associated with the specified data item as a String representation. The
  *  format of the representation is the canonical string representation as defined as XMLSchema.
  *
  *
 * @throws IOException 
 * @throws IllegalArgumentException
 */


 /** Returns the value associated with the specified data item as a String representation. The
  *  format of the representation is the canonical string representation of the value
  *  as defined as XMLSchema. For tables, this acts on the current row.
  *
  * @param dataItemName The key name or column name in the case of tables.
  * @return The string representation of the value or null if there is not value.
  * @throws DatasetException
  * @throws IllegalArgumentException
  * @throws IOException
  */
 public String getAsString(String dataItemName) throws DatasetException, IllegalArgumentException, IOException;

 /** Used to get information on the data item to indicate if it can be modified
  *  or not.
  *
  * @param dataItemName The key name or column name in the case of tables.
  * @return true if the value cannot be modified
  * @throws DatasetException
  */
 public boolean isReadOnly(String dataItemName) throws DatasetException;
 

 /** Retrieves the  number, types and properties of this dataset as well
  *  as its keys.
  *
  * @return the description of this <code>Dataset</code> object's columns
  * @exception DatasetException if a database access error occurs
  */
 public DatasetMetaData getMetaData() throws DatasetException;


 /** Sets the specified data item value as a string value.
  *
  *  @throws DataSetException if the value does not exist or if it is read-only.
  */

 /** Sets the specified data item value as a string value. The string should
  *  be formatted as an XMLSchema canonical representation of the value. In
  *  the case of tables, the current row value is updated.
  *
  * @param dataItemName The key name or column name in the case of tables.
  * @param value The value to write
  * @throws DatasetException
  */
 public void setAsString(String dataItemName, String value) throws DatasetException;

 /** Sets the specified data item value as an Object value, the value set must be
  *  compatible with the Class specified by getType for this data item name.
  *
  *  @throws DataSetException if the value does not exist or if it is read-only or
  *    or if this Object cannot be written to this data item name.
  */
 public void setValue(String dataItemName, Object value) throws DatasetException;
}
