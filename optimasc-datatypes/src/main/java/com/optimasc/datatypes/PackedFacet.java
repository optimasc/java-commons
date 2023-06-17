package com.optimasc.datatypes;

/** Represents a facet/attribute for aggregates 
 *  types indicating if the memory must be
 *  packed as closely as possible in memory 
 *  for data fields, otherwise natural alignment
 *  of data is used between the fields.
 * 
 * @author Carl Eric Codere
 *
 */
public interface PackedFacet
{
    public boolean isPacked();
    public void setPacked(boolean packData);
}
