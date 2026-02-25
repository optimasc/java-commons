package com.optimasc.definitions;

import java.text.ParseException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.optimasc.text.Parsers;
import com.optimasc.util.DataUtilities;

public class SchemaUtilities
{
  
  /** Represents a default value of an attribute */
  public static class DefaultAttributeValue
  {
    public final String id;
    public final Object value;
    
    public DefaultAttributeValue(String id, Object value)
    {
      super();
      this.id = id;
      this.value = value;
    }
  }
  
  /**
   * Verifies the attribute key and value validity based on the schema. This
   * method verifies the following:
   * 
   * <p>This method performs the following checks:</p>
   * <ul>
   *   <li>The {@code key} must be an instance of {@link CharSequence}.</li>
   *   <li>If an {@link ItemDefinition} exists and allows a single value, the
   *       {@code value} must not be an array or an instance of <code>java.util.List</code>.</li>
   *   <li>If an {@link ItemDefinition} exists and allows multiple values, the
   *       {@code value} must be an array or a <code>java.util.List</code>.</li>
   *   <li>If a {@link TypeDefinition} exists, verifies that the value (or each
   *       element for multi-valued attributes) is assignable to the expected class.</li>
   *   <li>If a maximum value length
   *       ({@link ItemDefinition#KEY_MAX_VALUE_LENGTH}) is defined, verifies that
   *       the length of the value (<code>java.lang.CharSequence</code>), array, or <code>java.util.List</code>)
   *       does not exceed the allowed maximum.</li>
   *  </ul>
   *  
   * <p>If no schema definition exists for the attribute or its type, the corresponding
   * validation steps are skipped.</p> 
   * 
   * <p><strong>Note:</strong> This method does not throw exceptions directly.
   * It returns an <code>IllegalArgumentException</code> describing the validation
   * failure, or {@code null} if validation succeeds.</p>     
   * 
   * @param reg
   *          [in] The registry that contains the schema definitions.
   * @param key
   *          [in] The attribute name to verify. This value must be of type <code>java.lang.CharSequence</code>.
   * @param value
   *          [in] The attribute value to verify. This
   *          value must not be <code>null</code>.
   * @return <code>null</code> if validation succeeds an instance of
   *         <code>IllegalArgumentException</code>
   * 
   */
  public static IllegalArgumentException verifyAttribute(EntityRegistry reg, Object key,
      Object value)
  {
    String keyString = null;
    if (key instanceof CharSequence)
    {
      keyString = key.toString();
    }
    else
    {
      return new IllegalArgumentException("'key' must be of type '"
          + CharSequence.class.getName() + "'.");
    }
    ItemDefinition def = (ItemDefinition) reg.lookup(keyString, ItemDefinition.class);
    Class valueClass = value.getClass();
    /* The definition is not null */
    if (def != null)
    {
      String typeName = def.getTypeName();
      /* Verify if the maximum length defined for this attribute. */
      Number number = (Number) def.get(ItemDefinition.KEY_MAX_VALUE_LENGTH, Number.class);
      if (number != null)
      {
        int maxLength = number.intValue();
        if (maxLength >= 0)
        {
          if (DataUtilities.verifyLength(value, maxLength) == false)
          {
            return new IllegalArgumentException(
                "Expecting maximum length of value to be "
                    + Integer.toString(maxLength) + " for attribute '" + key + "'.");
          }
        }
      }
      /* Verify if the value is an array or list when we expect a single value */
      String valueType = def.getValueType();
      if (valueType.equals(ItemDefinition.VALUE_TYPE_SINGLE))
      {
        /* Verify if the value is of the correct object instance type */
        if (typeName != null)
        {
          /* Check if we have a type definition */
          TypeDefinition typeDef = (TypeDefinition) reg.lookup(typeName,
              TypeDefinition.class);
          if ((typeDef != null) && (typeDef.getClz() != null))
          {
            if (typeDef.getClz().isAssignableFrom(valueClass) == false)
            {
              return new IllegalArgumentException("Expecting a value of class '"
                  + typeDef.getClz().getName() + "' but got '" + valueClass.getName()
                  + "' for attribute '" + key + "'.");
            }
          }
        }
      }
      else
      /* Verify if the value is an array or list when we expect an alternative value, an array or list */
      {
        /* Check if we have a typeName */
        if (typeName != null)
        {
          /* Check if we have a type definition */
          TypeDefinition typeDef = (TypeDefinition) reg.lookup(typeName,
              TypeDefinition.class);
          if (typeDef != null)
          {
            if (valueClass.isArray() == true)
            {
              /* Verify if each element element of the array is valid. */
              if (typeDef.getClz().isAssignableFrom(valueClass.getComponentType()) == false)
              {
                return new IllegalArgumentException("Expecting array of class '"
                    + typeDef.getClz().getName() + "' but got '"
                    + valueClass.getComponentType().getName() + "' for attribute '" + key
                    + "'.");
              }
            }
            else if (List.class.isAssignableFrom(valueClass) == true)
            {
              /* Go through each element of the List individually to ensure it is valid */
              List list = (List) value;
              for (int i = 0; i < list.size(); i++)
              {
                Object o = list.get(i);
                if (typeDef.getClz().isAssignableFrom(o.getClass()) == false)
                {
                  return new IllegalArgumentException("Expecting list of class '"
                      + typeDef.getClz().getName() + "' but got '"
                      + o.getClass().getName() + "' for attribute '" + key + "'.");
                }
              }
            }
            else
            {
              return new IllegalArgumentException(
                  "Expecting an array or List value for attribute '" + key
                      + "' but found instead '" + value.getClass().getName() + "'.");
            }

          }
        }
      }
    }
    return null;
  }

  /**
   * Verifies that the specified object class can be instantiated and that all
   * mandatory attributes defined in the schema are present.
   * 
   * <p>
   * This method performs the following checks:
   * </p>
   * <ul>
   * <li>If {@code objectClass} is {@code null}, no validation is performed and
   * {@code null} is returned.</li>
   * <li>If no {@link ClassDefinition} exists in the registry for the specified
   * {@code objectClass}, no validation is performed and {@code null} is
   * returned.</li>
   * <li>If the class definition indicates an abstract class, an
   * {@link IllegalArgumentException} is returned.</li>
   * <li>If any mandatory attribute defined by the class schema is missing from
   * {@code attributeNames}, an {@link IllegalArgumentException} is returned.</li>
   * </ul>
   * 
   * <p>
   * <strong>Note:</strong> This method does not throw the exception directly.
   * Instead, it returns an {@code IllegalArgumentException} instance when
   * validation fails, or {@code null} when validation succeeds.
   * </p>
   * 
   * @param reg
   *          [in] The registry containing the schema class definitions.
   * @param objectClass
   *          [in] The expected object class identifier, or {@code null} to skip
   *          validation.
   * @param attributeNames
   *          [in] List of attribute names that will be assigned to the Entity.
   *          This value must not be <code>null</code>.
   * @return {@code null} if validation succeeds or is skipped; otherwise an
   *         {@link IllegalArgumentException} describing the validation failure.
   */
  public static IllegalArgumentException verifyClassDefinition(EntityRegistry reg,
      String objectClass, Enumeration/*<String>*/attributeNames)
  {
    if (objectClass == null)
      return null;
    ClassDefinition classDef = (ClassDefinition) reg.lookup(objectClass,
        ClassDefinition.class);
    if (classDef == null)
      return null;
    if (classDef.isAbstract())
    {
      return new IllegalArgumentException("Trying to create an object of '" + objectClass
          + "' which is abstract.");
    }
    List/*<String>*/list = Collections.list(attributeNames);
    String[] mandatoryAttributes = classDef.getMandatoryAttributes();
    /* Verify if all attributes are present in the mandatory list */
    for (int i = 0; i < mandatoryAttributes.length; i++)
    {
      if (list.contains(mandatoryAttributes[i]) == false)
      {
        return new IllegalArgumentException("Missing attribute '"
            + mandatoryAttributes[i] + "' for object '" + objectClass + "'.");
      }
    }
    return null;
  }
  
  
  /**
   * Verifies that the specified mandatory attributes are present in
   * the specified class.
   * 
   * <p>
   * This method performs the following checks:
   * </p>
   * <ul>
   * <li>If any mandatory attribute defined by the class schema is missing from
   * {@code attributeNames}, an {@link IllegalArgumentException} is returned.</li>
   * </ul>
   * 
   * <p>
   * <strong>Note:</strong> This method does not throw the exception directly.
   * Instead, it returns an {@code IllegalArgumentException} instance when
   * validation fails, or {@code null} when validation succeeds.
   * </p>
   * 
   * @param classDef
   *          [in] The schema ClassDefinition to verify against
   * @param attributeNames
   *          [in] List of attribute names that will be assigned to the Entity.
   *          This value must not be <code>null</code>.
   * @return {@code null} if validation succeeds or is skipped; otherwise an
   *         {@link IllegalArgumentException} describing the validation failure.
   */
  public static IllegalArgumentException verifyClassDefinition(ClassDefinition classDef, 
      Enumeration/*<String>*/attributeNames)
  {
    List/*<String>*/list = Collections.list(attributeNames);
    String[] mandatoryAttributes = classDef.getMandatoryAttributes();
    /* Verify if all attributes are present in the mandatory list */
    for (int i = 0; i < mandatoryAttributes.length; i++)
    {
      if (list.contains(mandatoryAttributes[i]) == false)
      {
        return new IllegalArgumentException("Missing attribute '"
            + mandatoryAttributes[i] + "' for object '" + classDef.getExpandedName() + "'.");
      }
    }
    return null;
  }
  

  /**
   * Converts a string representation of an attribute value to its native java
   * object representation. In case there is no schema definition associated
   * with that attribute (no {@link ItemDefinition} or {@link TypeDefinition}),
   * the original string value is returned unchanged.</p>
   * 
   * <p>
   * For single-valued attributes, a single object instance is returned. For
   * multi-valued attributes, the value is split and each element is converted,
   * resulting in an {@code Object[]}.
   * </p>
   * *
   * 
   * @param reg
   *          [in] The registry that contains the schema definitions.
   * @param key
   *          [in] The ID of the attribute to convert. This value must not be
   *          <code>null</code>.
   * @param value
   *          [in] The attribute value to convert.
   * @return The converted object, an {@code Object[]} for multi-valued
   *         attributes, or the original string if conversion is not possible.
   * @throws ParseException
   *           If the value cannot be parsed according to the attribute type
   */
  public static Object parseAttribute(EntityRegistry reg, Object key, String value)
      throws ParseException
  {
    ItemDefinition def = (ItemDefinition) reg
        .lookup(key.toString(), ItemDefinition.class);
    Object convertedValue = null;
    /* No definition, so we cannot do anything */
    if (def == null)
    {
      return value;
    }
    boolean isSingleValue = def.getValueType().equals(ItemDefinition.VALUE_TYPE_SINGLE);
    String typeName = def.getTypeName();
    /* No type name associated with it */
    if (typeName == null)
    {
      return value;
    }
    int maxLength = -1;
    Number number = (Number) def.get(ItemDefinition.KEY_MAX_VALUE_LENGTH, Number.class);
    if (number != null)
    {
      maxLength = number.intValue();
    }
    TypeDefinition typeDef = (TypeDefinition) reg.lookup(typeName, TypeDefinition.class);
    if (typeDef == null)
    {
      return value;
    }
    /* Only a single value */
    if (isSingleValue)
    {
      convertedValue = Parsers.parseSingleValue(value, typeDef.getClz(), typeDef.getFormatter(),
          maxLength);
    }
    else
    {
      /* Convert to an Object[] array */
      convertedValue = Parsers.parseSingleValue(value, String[].class, null, maxLength);
      if (convertedValue instanceof String[])
      {
        String[] strArray = (String[]) convertedValue;
        Object[] objArray = new Object[strArray.length];
        for (int i = 0; i < strArray.length; i++)
        {
          objArray[i] = Parsers.parseSingleValue(strArray[i], typeDef.getClz(),
              typeDef.getFormatter(), maxLength);
        }
        convertedValue = objArray;
      }
    }
    return convertedValue;
  }

}
