package com.optimasc.datatypes.io;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.NumberEnumerationFacet;
import com.optimasc.datatypes.NumberRangeFacet;
import com.optimasc.datatypes.NumberRangeSetterFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.aggregate.BagType;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.defined.BinaryType;
import com.optimasc.datatypes.defined.ByteType;
import com.optimasc.datatypes.defined.DateType;
import com.optimasc.datatypes.defined.DoubleType;
import com.optimasc.datatypes.defined.IntType;
import com.optimasc.datatypes.defined.LongType;
import com.optimasc.datatypes.defined.NonNegativeIntegerType;
import com.optimasc.datatypes.defined.ShortType;
import com.optimasc.datatypes.defined.SingleType;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.defined.TimestampType;
import com.optimasc.datatypes.defined.UnsignedByteType;
import com.optimasc.datatypes.defined.UnsignedIntType;
import com.optimasc.datatypes.defined.UnsignedShortType;
import com.optimasc.datatypes.defined.YearType;
import com.optimasc.datatypes.derived.NameType;
import com.optimasc.datatypes.derived.NormalizedStringType;
import com.optimasc.datatypes.derived.TokenType;
import com.optimasc.datatypes.generated.AltLangMapType;
import com.optimasc.datatypes.generated.LanguageType;
import com.optimasc.datatypes.generated.MapType;
import com.optimasc.datatypes.generated.UnionType;
import com.optimasc.datatypes.manager.TypeSymbolTable;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.datatypes.primitives.DecimalType;
import com.optimasc.datatypes.primitives.DurationType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.primitives.URIType;
import com.optimasc.datatypes.utils.DataValidator;
import com.optimasc.text.NumericFormatters;
import com.optimasc.text.NumericFormatters.IntegerConverter;
import com.optimasc.text.StandardDateFormats;
import com.optimasc.text.StandardFormatters;
import com.sapient.BeanUtil;

/** Converts XMLSchema specifications to created datatypes.
 * 
 *  It currently supports a subset of the full XMLSchema specification:
 *  
 *  <ul>
 *   <li>unions are supported as anonymous types in a simpletype only and only 1 level deep.</li>
 *   <li>lists are supported only with the listType attribute and only 1 level deep</li>
 *   <li>restrictions on simple types are generally supported as defined in the specification.</li>
 *  </ul>
 *  
 *  It is not permitted to mix lists and unions in this implementation.
 *  
 * */
public class XMLSchemaDeserializer implements Deserializer 
{
  public static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
  public static final String RDF_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  /** Used for RDF definition of lists. */
  public static final String DEX_RDF_NAMESPACE = "http://www.optimasc.com/rdf/";
  
  /** list type qualifier */
  public static final String RDF_ARRAY_ORDERED = "Seq";   
  public static final String RDF_ARRAY = "Bag";  
  public static final String RDF_ALT_ARRAY = "Alt";  
  public static final String RDF_ALT_LANG_ARRAY = "AltLang";  
  
  protected static final String ANNOTATION_DOCUMENTATION = "documentation";
  
  public static class FacetData
  {
    String baseType;
    String[] allowedFacets;
    Class classType;

    public FacetData(String baseType, String[] allowedFacets, Class classType)
    {
      this.baseType = baseType;
      this.allowedFacets = allowedFacets;
      this.classType = classType;
    }
  }

  protected static final String[] FACET_STRINGS =
  {
      "length",
      "minLength",
      "maxLength",
      "pattern",
      "enumeration",
      "whiteSpace"
  };

  protected static final String[] FACET_BOOLEAN =
  {
      "pattern",
      "whitespace",
  };

  protected static final String[] FACET_DECIMAL =
  {
      "totalDigits",
      "fractionDigits",
      "pattern",
      "whiteSpace",
      "enumeration",
      "maxInclusive",
      "maxExclusive",
      "minInclusive",
      "minExclusive"
  };

  protected static final String[] FACET_FLOAT =
  {
      "pattern",
      "enumeration",
      "whiteSpace",
      "maxInclusive",
      "maxExclusive",
      "minInclusive",
      "minExclusive"
  };

  public static final String[] FACET_DOUBLE = FACET_FLOAT;
  public static final String[] FACET_DURATION = FACET_FLOAT;
  public static final String[] FACET_DATETIME = FACET_FLOAT;
  public static final String[] FACET_TIME = FACET_FLOAT;
  public static final String[] FACET_GYEARMONTH = FACET_FLOAT;
  public static final String[] FACET_GYEAR = FACET_FLOAT;
  public static final String[] FACET_GMONTHDAY = FACET_FLOAT;
  public static final String[] FACET_GDAY = FACET_FLOAT;
  public static final String[] FACET_GMONTH = FACET_FLOAT;
  public static final String[] FACET_DATE = FACET_FLOAT;

  /* This is already sorted according to String.compareTo
     so as to do a binary search as required. */
  public static FacetData[] facetsInformation =
  {
/*      new FacetData("ENTITIES", FACET_STRINGS, StringType.class),
      new FacetData("ENTITY", FACET_STRINGS, StringType.class),
      new FacetData("ID", FACET_STRINGS, StringType.class),
      new FacetData("IDREF", FACET_STRINGS, StringType.class),
      new FacetData("IDREFS", FACET_STRINGS, StringType.class),
      new FacetData("NCName", FACET_STRINGS, NameTypeType.class),
      new FacetData("NMTOKEN", FACET_STRINGS, StringType.class),
      new FacetData("NMTOKENS", FACET_STRINGS, StringType.class),
      new FacetData("NOTATION", FACET_STRINGS, StringType.class),*/
      new FacetData("Name", FACET_STRINGS, NameType.class),
/*      new FacetData("QName", FACET_STRINGS, StringType.class),*/

      new FacetData("anyURI", FACET_STRINGS, URIType.class),
      new FacetData("base64Binary", FACET_STRINGS, BinaryType.class),
      new FacetData("boolean", FACET_BOOLEAN, BooleanType.class),
      new FacetData("byte", FACET_DECIMAL, ByteType.class),
      new FacetData("date", FACET_DATE, DateType.class),
      new FacetData("dateTime", FACET_DATETIME, TimestampType.class),
      new FacetData("decimal", FACET_DECIMAL, DecimalType.class),
      new FacetData("double", FACET_DOUBLE, DoubleType.class),
      new FacetData("duration", FACET_DURATION, DurationType.class),
      new FacetData("float", FACET_FLOAT, SingleType.class),
      new FacetData("gDay", FACET_GDAY, null),
      new FacetData("gMonth", FACET_GMONTH, null),
      new FacetData("gMonthDay", FACET_GMONTHDAY, null),
      new FacetData("gYear", FACET_GYEAR, YearType.class),
      new FacetData("hexBinary", FACET_STRINGS, BinaryType.class),
      new FacetData("int", FACET_DECIMAL, IntType.class),
      new FacetData("integer", FACET_DECIMAL, IntegralType.class),
      new FacetData("language", FACET_STRINGS, LanguageType.class),
      new FacetData("long", FACET_DECIMAL, LongType.class),
//      new FacetData("nonNegativeInteger", FACET_DECIMAL),
      new FacetData("normalizedString", FACET_STRINGS, NormalizedStringType.class),
      new FacetData("short", FACET_DECIMAL, ShortType.class),
      new FacetData("string", FACET_STRINGS, StringType.class),
      new FacetData("time", FACET_TIME, null),
      new FacetData("token", FACET_STRINGS, TokenType.class),
      new FacetData("unsignedByte", FACET_DECIMAL, UnsignedByteType.class),
      new FacetData("unsignedInt", FACET_DECIMAL, UnsignedIntType.class),
//      new FacetData("unsignedLong", FACET_DECIMAL, UnsignedLongType.class),
      new FacetData("unsignedShort", FACET_DECIMAL, UnsignedShortType.class),
  };

  public static class FacetComparator implements Comparator<Object>
  {

    public int compare(Object arg0,Object arg1)
    {
      FacetData obj1 = (FacetData) arg0;
      String obj2 = (String) arg1;
      return obj1.baseType.compareTo(obj2);
    }

  }

  public static final FacetComparator baseTypeComparator = new FacetComparator();

  public XMLSchemaDeserializer()
  {
    super();
  }
  
  /** Parses an annotation and returns all the annotation elements. If
   *  there is no annotations, then it returns null.
   *  
   * @param root Root Element that might contain annotations
   * @return Annotation information, with documentation, appinfo 
   *  keys and values.
   */
  protected static Hashtable<String,String>  parseAnnotation(Element root)
  {
    Hashtable<String,String> hashtable = new Hashtable<String,String>(); 
    /*** Find the annotation element ***/
    NodeList annotationList = root.getElementsByTagNameNS(XSD_NAMESPACE, "annotation");
    if (annotationList.getLength() > 1)
    {
      throw new IllegalArgumentException("Not allowed to have more than 1 annotation in '"
          + root.getNodeName() + "'.");
    } else if (annotationList.getLength()==1)
    {
      Element annotationElement = (Element) annotationList.item(0);
      NodeList documentationList = annotationElement.getElementsByTagNameNS(XSD_NAMESPACE, ANNOTATION_DOCUMENTATION);
      if (documentationList.getLength() > 1)
      {
        throw new IllegalArgumentException("Not allowed to have more than 1 documentation in '"
            + root.getNodeName() + "'.");
      } else if (documentationList.getLength()==1)
      {
        Element documentationElement = (Element) documentationList.item(0);
        hashtable.put(ANNOTATION_DOCUMENTATION,documentationElement.getTextContent());
      }
    }
    return hashtable;
  }
  
  /** Returns a list of nodes with the specified tag name
   *  as immediate children of this element.
   * 
   * @return
   */
  protected static Vector<Element> getChildren(Node node, String namespaceURI, String name)
  {
    int length;
    Vector<Element> elements = new Vector<Element>();
    NodeList list = node.getChildNodes();
    length = list.getLength();
    for (int i = 0; i < length; i++)
    {
      Node childNode = list.item(i);
      if (childNode.getNodeType() == Node.ELEMENT_NODE)
      {
        Element elementNode = (Element)childNode;
        if ((elementNode.getNamespaceURI().equals(namespaceURI)) && (elementNode.getLocalName().equals(name)))
        {
          elements.add(elementNode);
        }
      }
    }
    return elements;
  }
  
  
  /** Parses an XMLSchema simpleType structure and returns its 
   *  datatype representation.
   *  
   * @param element The root element of the simple type.
   * @param nameRequired true if the simpleType name must be
   *   specified, otherwise, false.
   * @return Type Reference information
   */
  protected TypeReference parseSimpleType(TypeSymbolTable symbolTable, Element element, String xsdPrefix, boolean nameRequired)
  {
    String typeDocumentation;
    Datatype listType;
    Element elem = element;
    typeDocumentation = null;
    Hashtable<String, String> annotationElements = null;
    /* Get Type name */
    String dataTypeName = elem.getAttribute("name");
    if ((dataTypeName == null) && (nameRequired == true))
    {
      throw new IllegalArgumentException("'name' attribute is required for simpleType element.");
    }

    /** Parse annotations and documentation. */
    annotationElements = parseAnnotation(elem);
    if (annotationElements != null)
    {
      typeDocumentation = annotationElements.get(ANNOTATION_DOCUMENTATION);
    }
    /*** Is this a union ***/
    Vector<Element> unionList = getChildren(elem,XSD_NAMESPACE, "union");
    if (unionList.size() > 1)
    {
      throw new IllegalArgumentException("Not allowed to have more than 1 union in '"
          + dataTypeName + "' simpleType.");
    }
    if (unionList.size() == 1)
    {
      Element unionElement = (Element)unionList.get(0);
      Vector<Element> simpleTypeList =  getChildren(unionElement, XSD_NAMESPACE, "simpleType");

      UnionType unionType = new UnionType();
      for (int i = 0; i < simpleTypeList.size(); i++)
      {
//        Datatype datatype = parseSimpleType(symbolTable,(Element) simpleTypeList.get(i),xsdPrefix,false);
      // TODO: To complete here
      //  unionType.addVariantType(datatype);
      }
      unionType.setComment(typeDocumentation);
      return new NamedTypeReference(dataTypeName,unionType);
    }
    
    
    /*** Is this a list ***/
    Vector<Element> listList = getChildren(elem,XSD_NAMESPACE, "list");
    if (listList.size() > 1)
    {
      throw new IllegalArgumentException("Not allowed to have more than 1 list in '"
          + dataTypeName + "' simpleType.");
    }
    if (listList.size() == 1)
    {
      Element listElement = (Element)listList.get(0);
      String listItemType = listElement.getAttribute("itemType");
      if (listItemType == null)
      {
        throw new IllegalArgumentException("'itemType' must not be null");
      }
      
      TypeReference listElementDatatype = symbolTable.get(listItemType);
      if (listElementDatatype == null)
      {
        throw new IllegalArgumentException("Datatype '"+listItemType+"' is not defined.");
      }
      
      /* Check if we have the type of list definition -- DEX extension */
      String listOrdering = listElement.getAttributeNS(DEX_RDF_NAMESPACE,"listType");
      if ((listOrdering==null) || (listOrdering.equals(RDF_ARRAY)))
      {
        listType = new BagType(listElementDatatype,listElementDatatype.getType().getClassType());
      } else
      if ((listOrdering.equals(RDF_ARRAY_ORDERED)))
      {
        listType = new SequenceType(listElementDatatype,listElementDatatype.getType().getClassType());
      }/* else
      if ((listOrdering.equals(RDF_ALT_ARRAY)))
      {
        listType = new MapType(listElementDatatype); 
      } else
      if ((listOrdering.equals(RDF_ALT_LANG_ARRAY)))
      {
          listType = new AltLangMapType(listElementDatatype); 
      }*/ else
      {
        throw new IllegalArgumentException("'listType' value is invalid");
      }
      listType.setComment(typeDocumentation);
      return new NamedTypeReference(dataTypeName,listType);
    }
    
    
    /*** Find the restriction element ***/
    Vector<Element> restrictionList = getChildren(elem,XSD_NAMESPACE, "restriction");
    if (restrictionList.size() > 1)
    {
      throw new IllegalArgumentException("Not allowed to have more than 1 restriction in '"
          + dataTypeName + "' simpleType.");
    } else if (restrictionList.size() == 0)
    {
      return null;
    }

    Element restrictionElement = (Element) restrictionList.get(0);
    /* Base type. */
    String baseType = restrictionElement.getAttribute("base");
    if (baseType == null)
    {
      throw new IllegalArgumentException("'base' attribute is required in '" + dataTypeName
          + "'");
    }
    if (baseType.startsWith(xsdPrefix) == false)
    {
      throw new IllegalArgumentException(
          "'base' attribute is not a XMLSChema built-in datatype in '" + dataTypeName + "'");
    }
    baseType = getLocalName(baseType);

    /* Search the baseType */
    int foundIndex = Arrays.binarySearch(facetsInformation, baseType, baseTypeComparator);
    /* Check if this is an official XMLSchema basetype, if not look it up, in the
     * local definitions, and if not found, then give an error.
     */
    if (foundIndex < 0)
    {
      throw new IllegalArgumentException("'base' is of type '" + baseType + "' '"
          + dataTypeName + "' which is unknown.");
    }

    if (foundIndex >= 0)
    {
      FacetData facetInfo = facetsInformation[foundIndex];
      Map<String, Object> restrictions = null;
      try
      {
        restrictions = getRestrictions(facetInfo, restrictionElement);
      } catch (ParseException e)
      {
        return null;
      }
      /** Some restrictions are currently not supported, indicate it. */
      if (restrictions.get("length") != null)
      {
        throw new UnsupportedOperationException(
            "facet of type 'length' is not supported, replace it with 'minLength' and 'maxLength' facets.");
      }
      if (restrictions.get("minExclusive") != null)
      {
        throw new UnsupportedOperationException(
            "facet of type 'minExclusive' is not supported, adapt to use 'minInclusive' instead.");
      }
      if (restrictions.get("maxExclusive") != null)
      {
        throw new UnsupportedOperationException(
            "facet of type 'maxExclusive' is not supported, adapt to use 'maxInclusive' instead.");
      }
      NamedTypeReference o = createDataType(baseType, dataTypeName, restrictions, null);
      if (typeDocumentation != null)
      {
        o.setComment(typeDocumentation);
      }
      return o;
    }
    return null;
  }
  
  
  protected void loadStandardDatatypes(TypeSymbolTable symbolTable, String xsdPrefix)
  {
    for (int i = 0; i < facetsInformation.length; i++)
    {
      FacetData facetInfo = facetsInformation[i];
      if (facetInfo.classType != null)
      {
        Datatype datatype = null;
        try
        {
          datatype = (Datatype) facetInfo.classType.newInstance();
        } catch (InstantiationException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IllegalAccessException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        if (datatype != null)
        {
          symbolTable.put(new QName(XSD_NAMESPACE,facetInfo.baseType,xsdPrefix),new NamedTypeReference(facetInfo.baseType, datatype));
        }
      }
    }
  }

  
  
  public TypeSymbolTable load(InputStream stream)
  {
    String xsdPrefix;
    TypeSymbolTable symbolTable = new TypeSymbolTable();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db;
    try
    {
      db = dbf.newDocumentBuilder();
      Document doc = db.parse(stream);
      // Now for each simpleType decode the information.
      //NodeList simpleTypeList = doc.getChildNodes();
      Element rootElement = doc.getDocumentElement();

      if (rootElement.getNamespaceURI().equals(XSD_NAMESPACE) == false)
      {
        throw new IllegalArgumentException("This is not a XMLSchema stream.");
      }

      if (rootElement.getLocalName().equals("schema") == false)
      {
        throw new IllegalArgumentException("This is not a XMLSchema stream.");
      }
      xsdPrefix = rootElement.getPrefix();
      loadStandardDatatypes(symbolTable,xsdPrefix);

      /*** For each simpleType ***/
      Vector<Element> simpleTypeList = getChildren(rootElement, XSD_NAMESPACE, "simpleType");

      for (int i = 0; i < simpleTypeList.size(); i++)
      {
        TypeReference typeRef = parseSimpleType(symbolTable,(Element) simpleTypeList.get(i),xsdPrefix,true);
        if (typeRef instanceof NamedTypeReference)
        {
          NamedTypeReference namedType = (NamedTypeReference) typeRef;
          if (namedType.getTypeName()!=null)
          {
            symbolTable.put(namedType);
          }
        }
      }
    } catch (ParserConfigurationException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (FileNotFoundException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return symbolTable;
  }

  protected static final boolean isValidFacet(String baseType, String facet)
  {
    return true;
  }

  /** Converts a facet value to an Object representation. 
   * @throws ParseException */
  protected static Object facetToObject(FacetData facetData, String name, String value) throws ParseException
  {
    /* nonNegativeInteger */
    if ((name.equals("length")) || (name.equals("minLength")) || (name.equals("maxLength"))
        || (name.equals("fractionDigits")))
    {
      long longValue = Long.parseLong(value);
      if (longValue < 0)
      {
        throw new IllegalArgumentException("facet '" + name + "' must be non-negative");
      }
      if (longValue > Integer.MAX_VALUE)
      {
        throw new IllegalArgumentException("facet '" + name
            + "' is beyond Integer.MAX_VALUE supported");
      }
      return new Integer((int) longValue);
    }
    if ((name.equals("maxInclusive")) || (name.equals("maxExclusive"))
        || (name.equals("minInclusive")) || (name.equals("minExclusive")))
    {
      /** Check if facetData has IntegerType parent. */
      if (IntegralType.class.isAssignableFrom(facetData.classType))
      {
        long longValue = Long.parseLong(value);
        return new Long(longValue);
      }
      /** Check if facetData has RealType parent. */
      if (RealType.class.isAssignableFrom(facetData.classType))
      {
        double doubleValue = Double.parseDouble(value);
        return new Double(doubleValue);
      }
    }
    if (name.equals("totalDigits"))
    {
      long longValue = Long.parseLong(value);
      if (longValue < 0)
      {
        throw new IllegalArgumentException("facet '" + name + "' must be positive");
      }
      if (longValue > Integer.MAX_VALUE)
      {
        throw new IllegalArgumentException("facet '" + name
            + "' is beyond Integer.MAX_VALUE supported");
      }
      return new Integer((int) longValue);
    }
    if (name.equals("enumeration"))
    {
      if (IntegralType.class.isAssignableFrom(facetData.classType))
      {
        Number v = new BigInteger(value); 
        return v;
      }
      if (DecimalType.class.isAssignableFrom(facetData.classType))
      {
        Number v = new BigDecimal(value); 
        return v;
      }
      if (StringType.class.isAssignableFrom(facetData.classType))
      {
        return value;
      }
      if (DateTimeType.class.isAssignableFrom(facetData.classType))
      {
        return DataValidator.validate((Datatype) DateTimeType.getInstance().getType(), value);
      }
    }
    /* pattern, enumeration are kept as strings. */
    return value;
  }

  /**
   * Parses all restrictions allowed for a simpleType of the specified type, and
   * creates a hashtable containing the following information: Key<String>
   * representing the facet name. Object, or List<Object>> in the case
   * of enumeration
   * 
   * @param facetData [in] The facet data.
   * @param restrictionElement
   * @return
   */
  protected static Map<String,Object> getRestrictions(FacetData facetData, Element restrictionElement) throws ParseException
  {
    Map<String,Object> restrictionsData = new Hashtable<String,Object>();
    String value;
    NodeList facetElements = restrictionElement.getChildNodes();
    List<Object> enumAttribute = new ArrayList<Object>();
    for (int j = 0; j < facetElements.getLength(); j++)
    {
      Node node = (Node) facetElements.item(j);
      if (node instanceof Element)
      {
        Element element = (Element) node;
        for (int k = 0; k < facetData.allowedFacets.length; k++)
        {
          /* This is a known facet, others are ignored. */
          if (facetData.allowedFacets[k].equals(element.getLocalName()))
          {
            value = element.getAttribute("value");
            /**
             * Enumeration is composed of a vector, otherwise its a simple
             * string.
             */
            if (element.getLocalName().equals("enumeration"))
            {
              Object valueObject = facetToObject(facetData, element.getLocalName(), value);
              enumAttribute.add(valueObject);
            } else
            /* Replace old value */
            {
              Object valueObject = facetToObject(facetData, element.getLocalName(), value);
              restrictionsData.put(element.getLocalName(), valueObject);
            }
          }
        }
      }
    }
    /* Add all the enumeration values, if present. */
    if (enumAttribute.size() > 0)
    {
      /* Internally the format is choices property */
      restrictionsData.put("choices",enumAttribute);
    }
    return restrictionsData;
  }

  /** Extracts the local name out of a prefix:name */
  protected static final String getLocalName(String qualifiedName)
  {
    int index = qualifiedName.indexOf(':');
    if (index == 0)
    {
      return qualifiedName;
    }
    return qualifiedName.substring(index + 1);
  }

  protected static NamedTypeReference createDataType(String datatype, String name, Map<String,Object> restrictions,
      String comment)
  {
    Datatype datatypeInstance = null;
    /* Search the baseType */
    int foundIndex = Arrays.binarySearch(facetsInformation, datatype, baseTypeComparator);
    /* Check if this is an official XMLSchema basetype, if not look it up, in the
     * local definitions, and if not found, then give an error.
     */
    if (foundIndex < 0)
    {
      throw new IllegalArgumentException("'base' is of type '" + datatype + "' '" + datatype
          + "' which is unknown.");
    }

    if (foundIndex >= 0)
    {
      FacetData facetInfo = facetsInformation[foundIndex];
      if (facetInfo.classType != null)
      {
        try
        {
          /* Create the correct object type. */
          datatypeInstance = (Datatype) facetInfo.classType.newInstance();
//          datatypeInstance.setName(name);
          datatypeInstance.setComment(comment);
          /* Set Length */
          Number minLengthAttr = (Number) restrictions.get("minLength"); 
          Number maxLengthAttr = (Number) restrictions.get("maxLength");
          Number minInclusiveAttr = (Number) restrictions.get("minInclusive");
          Number maxInclusiveAttr = (Number) restrictions.get("maxInclusive");
          List<Object> choicesAttr = (List<Object>) restrictions.get("choices");
          
          int minLength = 0;
          int maxLength = Integer.MIN_VALUE;
          if (minLengthAttr!=null)
          {
            minLength = minLengthAttr.intValue();
          }
          if (maxLengthAttr!=null)
          {
            maxLength = maxLengthAttr.intValue();
          }
          if (datatypeInstance instanceof LengthFacet)
          {
            ((LengthFacet)datatypeInstance).setLength(minLength, maxLength);
          }
          
          if ((minInclusiveAttr!=null) || (maxInclusiveAttr!=null))
          {
            if (datatypeInstance instanceof NumberRangeSetterFacet)
            {
              ((NumberRangeSetterFacet)datatypeInstance).setRange(minInclusiveAttr, maxInclusiveAttr);
            }
          }
          
          if (choicesAttr!=null)
          {
            if (datatypeInstance instanceof NumberEnumerationFacet)
            {
              ((NumberEnumerationFacet)datatypeInstance).setChoices(choicesAttr.toArray(new Number[0]));
            }
            if (datatypeInstance instanceof EnumerationFacet)
            {
              ((EnumerationFacet)datatypeInstance).setChoices(choicesAttr.toArray(new Object[0]));
            }
          }
        } catch (InstantiationException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IllegalAccessException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    return new NamedTypeReference(name,datatypeInstance);
  }

}
