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
package com.questoid.sqlitebrowser.schema;

import java.util.ArrayList;
import java.util.List;

import de.timok.sqlitewrappers.ColumnDef;
import de.timok.sqlitewrappers.TableDef;

class SchemaTreeNode {
    
    private final String name;
    private final String objectType;
    private final String type;
    private final String schema;

    private final List<SchemaTreeNode> children = new ArrayList<SchemaTreeNode>();

    SchemaTreeNode() {
        name = "<root>";
        objectType = "root";
        type = "";
        schema = "";
    }

	SchemaTreeNode(final TableDef table) {
        name = table.getName();
        objectType = "table";
        type = "";
		// schema = table.toSQL();
		schema = "";
    }

	// SchemaTreeNode(final IndexDef index) {
	// name = index.getName();
	// objectType = "index";
	// type = "";
	// schema = index.toSQL();
	// }

	SchemaTreeNode(final TableDef table, final ColumnDef column) {
        name = column.getName();
        objectType = "field";
		// final List<String> names = column.getType() == -1 ? new
		// ArrayList<String>() : column.getType();
		// final List<ISqlJetColumnConstraint> constraints =
		// column.getConstraints();
		// final StringBuffer typeStringBuffer = new StringBuffer();
		// for (final String name : names) {
		// if (typeStringBuffer.length() > 0) {
		// typeStringBuffer.append(' ');
		// }
		// typeStringBuffer.append(name);
		// }
		type = Integer.toString(column.getType());
        schema = "";
    }
    
    public void addChild(final SchemaTreeNode child) {
        children.add(child);
    }
    
    public int getChildCount() {
        return children.size();
    }
    
    public boolean isLeaf() {
        return children.size() == 0;
    }
    
    public SchemaTreeNode getChildAt(final int index) {
        return children.get(index);
    }
    
    public int getIndexOfChild(final SchemaTreeNode child) {
        return children.indexOf(child);
    }
    
    public String getName() {
        return name;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getType() {
        return type;
    }

    public String getSchema() {
        return schema;
    }
    
    @Override
	public String toString() {
        return getName();
    }

	public List<SchemaTreeNode> getChildren() {
		return children;
	}    
    
}
