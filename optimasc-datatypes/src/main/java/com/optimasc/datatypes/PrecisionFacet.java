package com.optimasc.datatypes;

public interface PrecisionFacet
{
  public int getTotalDigits();
  public int getFractionDigits();
  public void setTotalDigits(int value);
  public void setFractionDigits(int value);
}
