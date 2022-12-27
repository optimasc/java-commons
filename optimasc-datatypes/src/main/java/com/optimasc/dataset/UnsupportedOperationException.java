package com.optimasc.dataset;

/** This exception is thrown when the operation is currently not
 *  supported by the dataset provider or data conversion system.
 *  
 * @author Carl Eric Codère
 *
 */
public class UnsupportedOperationException extends DatasetException
{

  public UnsupportedOperationException(String s)
  {
    super(s);
  }

}
