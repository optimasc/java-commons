package com.optimasc.datatypes;

public class EnumerationHelper implements EnumerationFacet
{
  protected Datatype datatype;
  protected Object[] enumeration;
  
  public EnumerationHelper(Datatype parent)
  {
    super();
    datatype = parent;
  }
  
  public Object[] getChoices()
  {
    return enumeration;
  }

  public void setChoices(Object[] choices)
  {
    Class clz = datatype.getClassType();
    for (int i = 0; i < choices.length; i++)
    {
      if (clz.isInstance(choices[i]) == false)
      {
        throw new IllegalArgumentException("Enumeration elements should be of type '"+ clz.getName()+"'");
      }
    }
    enumeration = choices;
  }

  public boolean validateChoice(Object value)
  {
    if (enumeration != null)
    {
      if (enumeration.length > 0)
      {
        int i;
        for (i = 0; i < enumeration.length; i++)
        {
          Object s1 = enumeration[i];
          if (s1 == null)
            throw new IllegalArgumentException(
                "Invalid object type - enumeration should contain non null objects");
          if (s1.equals(value))
          {
            return true;
          }
        }
      }
    }
    return true;
  }

}
