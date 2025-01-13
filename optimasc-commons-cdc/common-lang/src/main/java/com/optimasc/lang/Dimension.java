package com.optimasc.lang;

/** Structure contains the dimensions of a drawn object. This is
 *  the portable version of java.awt.geom.Dimension2D */
public class Dimension
{
  protected double width;
  protected double height;
  
  /** Creates an instance of Dimension with a width of zero and a height of zero.  */
  public Dimension()
  {
    width = 0;
    height = 0;
  }
  
  
  /** Returns the width of this dimension. */
  public double getWidth()
  {
    return width;
  }


  /** Returns the height of this dimension. */
  public double getHeight()
  {
    return height;
  }



  public void setSize(int width, int height)
  {
    this.width = width;
    this.height = height;
  }


}
