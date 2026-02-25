package com.optimasc.definitions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.optimasc.io.CSVReader;
import com.optimasc.io.LineReader;
import com.optimasc.text.StringUtilities;
import com.optimasc.utils.BaseTypeUtilities;

/** Reads and writes named entities using a specific CSV format.
 *  Simple reader and writer for Entities.
 * 
 * The CSV format assumes the following:
 * <ul>
 *  <li>The stream is encoded in UTF-8 with no BOM.</li>  
 *  <li>The 1st row of the CSV should contain the attribute name (which
 *   will be considered not case-sensitive).</li>  
 * </ul>
 * 
 * @author Carl Eric Codere
 *
 */
public class CSVEntitySerializer implements EntitySerializer
{
  public static final String SEPARATOR_CHAR = ";";
  
  /** Used to instantiate the object instances */
  protected EntityFactory factory;

  /** Constructs a CSV Serializer with the 
   *  specified factory used to instantiate
   *  objects when loading.
   * 
   * @param factory [in] The factory that will
   *   be used to instantiate objects when
   *   reading from a stream.
   */
  public CSVEntitySerializer(EntityFactory factory)
  {
    super();
    this.factory = factory;
  }
  
  
  public void write(EntityRegistry registry, OutputStream out) throws IOException
  {
  }
  
  /**
   * Loads definitions from a stream. 
   * 
   * The item definitions are assumed to be in CSV format encoded
   * in UTF-8 format with no BOM.  
   * 
   * <p>The CSV should contain a heading row, and 
   * each heading row is loaded and converted to uppercase
   * for any comparison. It is then compared with definitions
   * passed keys and if these are present, they are converted to their correct
   * types, all other values in the CSV are not loaded. Furthermore, the following 
   * values entries are required in the dataset: </p>
   * 
   * <dl>
   *  <dt><code>NAME</code></dt>
   *  <dd>The qualified name of the item</dd>
   * </dl>
   * 
   * <p>By default, each entry that contains {@link #SEPARATOR_CHAR} in its entry
   * is considered an array of strings that will be split by that separator and 
   * added as a value as a <code>String[]</code>.</p>
   * 
   * @param is
   *          InputStream for the Item definition registry
   * @param columnDefinitions
   *          Definitions of columns that will be read from the inputstream, the
   *          names of the column header should be the OID or actualy name of 
   *          the item, with comparison done without case-sensitivity.          
   * @throws IOException
   */
  public EntityRegistry load(InputStream is) throws IOException
  {
    String line = "";
    String csvDelimiter = ","; // CSV files typically use comma as delimiter
    
    EntityRegistry registry = new EntityRegistry();

    /* Header to column index mapping */
    Hashtable/*<String, Integer>*/ mapping = new Hashtable/*<String, Integer>*/();

    // Read the header line (first line)
    String headers[]= null;
    String headerLine = LineReader.readUTF8Line(is);
    if (headerLine != null)
    {
      headers = StringUtilities.split(headerLine,csvDelimiter);
      for (int i = 0; i < headers.length; i++)
      {
        mapping.put(headers[i].toUpperCase(), new Integer(i));
      }
    }
    

    /* Verify minimum mandatory definition keys */
    if (mapping.get(ItemDefinition.KEY_NAME) == null)
    {
      throw new IllegalArgumentException("'" + ItemDefinition.KEY_NAME
          + "' is not present but is required.");
    }

    // Read data lines
    while ((line = LineReader.readUTF8Line(is)) != null)
    {
      String namespaceURI = null;
      String qualifiedName= null;

      // Split the line by comma, handling quoted fields
      String[] row = CSVReader.parseCSVLine(line, csvDelimiter);
      Hashtable/*<String,String>*/ rowMap = new Hashtable/*<String,String>*/();
      for (int i=0; i < row.length; i++)
      {
        rowMap.put(headers[i].toUpperCase(), row[i]);
      }
      
      qualifiedName = (String) rowMap.get(ItemDefinition.KEY_NAME);
      rowMap.remove(ItemDefinition.KEY_NAME);
      namespaceURI = (String) rowMap.get(ItemDefinition.KEY_NAME_NS_URI);
      rowMap.remove(ItemDefinition.KEY_NAME_NS_URI);
      
      // Parse the values into objects.
      Map/*<String,Object>*/ attributes = new HashMap/*<String,Object>*/();
      Iterator/*<String>*/ it = rowMap.keySet().iterator();
      while (it.hasNext())
      {
        String key = (String) it.next();
        String value = (String) rowMap.get(key);
        Object outObject;
        try
        {
          outObject = SchemaUtilities.parseAttribute(factory.getSchemaRegistry(), key, value);
        } catch (ParseException e)
        {
          IllegalArgumentException exc = new IllegalArgumentException("Conversion of objects failed.");
          exc.initCause(e);
          throw exc;
        }
        attributes.put(key,outObject);
      }
      
      
      Entity object = factory.getObjectInstance(null, namespaceURI, qualifiedName, new Hashtable(), attributes);
      registry.add(object);
      
    } // end while
    return registry;
  }

}
