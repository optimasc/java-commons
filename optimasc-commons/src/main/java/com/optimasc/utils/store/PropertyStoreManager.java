package com.optimasc.utils.store;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This storage manager handles storing and retrieving of data stored as 
 * key-value pairs in internal persistent storage.
 */
public interface PropertyStoreManager {

    /**
     * Stores a property using in the specified virtual file.
     * If the filename does not already exist it will be created. The value
     * cannot be null.
     * 
     * @param name
     *            the virtual file where the property will be stored
     * @param key
     *            the key used to identify the property to be stored
     * @param value
     *            the value of the property to be stored
     * @exception PropertyStoreManagerException
     *                if the value is null
     */
    public void storeProperty(String name, String key, String value) throws PropertyStoreManagerException;

    /**
     * Stores a property in the specified file. If
     * the filename does not already exist it will be created.The value cannot
     * be null.
     * 
     * @param name
     *            the virtual file where the property will be stored
     * @param key
     *            the key used to identify the property to be stored
     * @param value
     *            the value of the property to be stored
     * @param securityLevel
     *            security level used for storing the property
     * @exception PropertyStoreManagerException
     *                if the value is null
     */
//    public void storeProperty(String name, String key, String value, int securityLevel)
//            throws PropertyStoreManagerException;

    /**
     * Retrieves a property based on a name and key.
     * 
     * @param name
     *            the virtual file where the property is stored
     * @param key
     *            the key used to identify the property that needs to be
     *            retrieved
     * @return the value of the property
     */
    public String retrieveProperty(String name, String key) throws PropertyStoreManagerException;

    /**
     * Removes (and returns) the property associated with the key.
     * 
     * @param name
     *            The virtual file where the property is stored
     * @param key
     *            the key used to identify the property to remove
     * @return the value of the property that has been removed, or null if not
     *         property has been found with the provided key
     * @throws PropertyStoreManagerException
     */
    public String removeProperty(String name, String key) throws PropertyStoreManagerException;
    
    /**
     * Return an enumeration of the keys contained in the property store.
     * 
     * @param name 
     *              the virtual file where the properties are stored
     * @return an enumeration of the keys contained in the property storage file
     * @throws PropertyStoreManagerException
     */
    public Enumeration keys(String name) throws PropertyStoreManagerException;
    
    /**
     * Remove all properties associated with the file name.
     * 
     * @param name 
     *              the file where the properties are stored
     * @throws PropertyStoreManagerException
     */
    public void clear(String name) throws PropertyStoreManagerException;

}
