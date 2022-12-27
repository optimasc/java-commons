package com.optimasc.utils;

public abstract class PropertyAbstractBase
{
  abstract  void clear();
  abstract  void remove(String key);
  abstract  String getString(String key, String def); 
//  abstract  boolean getBoolean(String key, boolean def); 
//  abstract  float getFloat(String key, float def); 
//  abstract  int getInt(String key, int def); 
  abstract  void putString(String key, String value); 
//  abstract  void putInt(String key, int value); 
//  abstract  void putFloat(String key, float value); 
//  abstract  void putBoolean(String key, boolean value); 
}
