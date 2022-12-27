package com.optimasc.utils.store;


/**
 * Exception when something goes wrong in PropertyStore
 * @see PropertiesStore
 */
public class PropertyStoreManagerException extends Exception{

	
	/** Error, the property with the specified key was not found in the property store */
	public static final int ERROR_PROP_NOT_FOUND = (short)0x6F01;
	
	/** Error in argument call property store name is not valid. */
	public static final int ERROR_INVALID_ARG    = (short)0x6F02;
	
    /** Error, internal error  */
    public static final int INTERNAL_ERROR       =  (short)0x6FFF;
	
	private int reason;
	
    /**
     * Constructor that sets the exception message
     * @param message The message associated with the exception
     */
    public PropertyStoreManagerException(int reason, String message)
    {
        super(message);
        this.reason = reason;
    }
   
    public PropertyStoreManagerException(String message)
    {
        super(message);
        this.reason = INTERNAL_ERROR;
    }
    
   /** Generic routine to throw an exception when an error occurs
    *  in the property store manager.
    *  
    * @param error The error code 
    * @param name The name of the store that threw the error
    * @param param An additional parameter that can be null
 * @throws PropertyStoreManagerException 
    */
   public static final void throwIt(int error, String name, String param) throws PropertyStoreManagerException
   {
	   if (param == null)
		   param = "";
       throw new PropertyStoreManagerException(error, "ERROR: Property store "+name+Integer.toString(error));   
   }


  public int getReason()
  {
    return reason;
  }
   
   
}
