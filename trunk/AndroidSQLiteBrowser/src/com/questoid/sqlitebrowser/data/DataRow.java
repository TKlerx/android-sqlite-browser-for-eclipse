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

import org.tmatesoft.sqljet.core.SqlJetException;

import org.tmatesoft.sqljet.core.table.ISqlJetCursor;

public class DataRow {
    private Object[] data;
    private long rowId;
    
    public static DataRow read(ISqlJetCursor cursor, long index, String[] names) throws SqlJetException {
        Object[] data = new Object[names.length];
        for(int i = 0; i < names.length; i++) {
            data[i] = cursor.getValue(names[i]);
        }
        return new DataRow(data, index);
    }
    
    private DataRow(Object[] data, long rowId) {
        this.data = data;
        this.rowId = rowId;
    }
    
    public int getFieldsCount() {
        return data.length;
    }
    
    public Object getValueAt(int field) {
        return data[field];
    }
    
    public long getID() {
        return rowId;
    }
}
