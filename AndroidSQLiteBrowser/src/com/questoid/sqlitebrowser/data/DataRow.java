/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY. 
 * See the GNU General Public License for more details.
 * 
 * Copyright (C) 2010 Questoid.com
 */
package com.questoid.sqlitebrowser.data;

public class DataRow {
    private final Object[] data;
    private final long rowId;
    
	// public static DataRow read(final ISqlJetCursor cursor, final long index,
	// final String[] names) throws SqlJetException {
	// final Object[] data = new Object[names.length];
	// for(int i = 0; i < names.length; i++) {
	// data[i] = cursor.getValue(names[i]);
	// }
	// return new DataRow(data, index);
	// }
    
	public DataRow(final Object[] data, final long rowId) {
        this.data = data;
        this.rowId = rowId;
    }
    
    public int getFieldsCount() {
        return data.length;
    }
    
    public Object getValueAt(final int field) {
        return data[field];
    }
    
    public long getID() {
        return rowId;
    }
}
