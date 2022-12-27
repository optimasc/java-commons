/*
 * @(#)ResultSetMetaData.java	1.26 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.optimasc.dataset;

import com.optimasc.datatypes.Datatype;

/**
 * An object that can be used to get information about the types 
 * and properties of the keys or columns of a dataset.
 */
public interface DatasetMetaData {

    /**
     * Returns the number of columns or keys in this dataset object.
     *
     * @return the number of columns or keys
     * @exception DatasetException if a database access error occurs
     */
    int getCount() throws DatasetException;

    /**
     * Indicates whether the designated key/column is automatically numbered, thus read-only.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     */
    boolean isAutoIncrement(int index) throws DatasetException, IllegalArgumentException;

    /**
     * Indicates the nullability of values in the designated column.		
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the nullability status of the given column; one of <code>columnNoNulls</code>,
     *          <code>columnNullable</code> or <code>columnNullableUnknown</code>
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     */
    boolean isNullable(int index) throws DatasetException, IllegalArgumentException;

    /**
     * Gets the designated column's suggested title for use in printouts and
     * displays.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the suggested column title
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     */
    String getLabel(int index) throws DatasetException, IllegalArgumentException;	

    /**
     * Get the designated column's name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return column name
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     */
    String getName(int index) throws DatasetException, IllegalArgumentException;

    /**
     * Gets the designated column's table name. 
     *
     * @param column the first column is 1, the second is 2, ...
     * @return table name or "" if not applicable
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     */
    String getTableName(int index) throws DatasetException, IllegalArgumentException;

    /**
     * Retrieves the designated column's SQL type.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return SQL type from Datatype
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     * @see Types
     */
    int getType(int index) throws DatasetException, IllegalArgumentException;

    /**
     * Retrieves the designated column's database-specific type name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return type name used by the database. If the column type is
     * a user-defined type, then a fully-qualified type name is returned.
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     */
    String getTypeName(int index) throws DatasetException, IllegalArgumentException;

    /**
     * Indicates whether the designated column is definitely not writable.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     */
    boolean isReadOnly(int index) throws DatasetException, IllegalArgumentException;

    //--------------------------JDBC 2.0-----------------------------------

    /**
     * <p>Returns the fully-qualified name of the Java class whose instances 
     * are manufactured if the method <code>ResultSet.getObject</code>
     * is called to retrieve a value 
     * from the column.  <code>ResultSet.getObject</code> may return a subclass of the
     * class returned by this method.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the fully-qualified name of the class in the Java programming
     *         language that would be used by the method 
     * <code>ResultSet.getObject</code> to retrieve the value in the specified
     * column. This is the class name used for custom mapping.
     * @exception DatasetException if a database access error occurs
     * @exception IllegalArgumentException if the index parameter is not pointing to a valid column.
     * @since 1.2
     */
    String getClassName(int index) throws DatasetException, IllegalArgumentException;
    
    //-------------------- Extensions --------------------//
    
    String getComment(int index) throws DatasetException, IllegalArgumentException;
    
    Datatype getDatatype(int index)  throws DatasetException, IllegalArgumentException;
}
