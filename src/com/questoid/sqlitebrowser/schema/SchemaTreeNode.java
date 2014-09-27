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

import org.tmatesoft.sqljet.core.schema.ISqlJetColumnConstraint;
import org.tmatesoft.sqljet.core.schema.ISqlJetColumnDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetIndexDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetTableDef;

class SchemaTreeNode {
    
    private String name;
    private String objectType;
    private String type;
    private String schema;

    private List<SchemaTreeNode> children = new ArrayList<SchemaTreeNode>();

    SchemaTreeNode() {
        name = "<root>";
        objectType = "root";
        type = "";
        schema = "";
    }

    SchemaTreeNode(ISqlJetTableDef table) {
        name = table.getName();
        objectType = "table";
        type = "";
        schema = table.toSQL();
    }

    SchemaTreeNode(ISqlJetIndexDef index) {
        name = index.getName();
        objectType = "index";
        type = "";
        schema = index.toSQL();
    }

    SchemaTreeNode(ISqlJetTableDef table, ISqlJetColumnDef column) {
        name = column.getName();
        objectType = "field";
        List<String> names = column.getType() == null ? new ArrayList<String>() : column.getType().getNames();
        List<ISqlJetColumnConstraint> constraints = column.getConstraints();
        StringBuffer typeStringBuffer = new StringBuffer();
        for (String name : names) {
            if (typeStringBuffer.length() > 0) {
                typeStringBuffer.append(' ');
            }
            typeStringBuffer.append(name);
        }
        for (ISqlJetColumnConstraint constraint : constraints) {
            if (typeStringBuffer.length() > 0) {
                typeStringBuffer.append(' ');
            }
            typeStringBuffer.append(constraint.toString());
        }
        type = typeStringBuffer.toString();
        schema = "";
    }
    
    public void addChild(SchemaTreeNode child) {
        children.add(child);
    }
    
    public int getChildCount() {
        return children.size();
    }
    
    public boolean isLeaf() {
        return children.size() == 0;
    }
    
    public SchemaTreeNode getChildAt(int index) {
        return children.get(index);
    }
    
    public int getIndexOfChild(SchemaTreeNode child) {
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
    
    public String toString() {
        return getName();
    }

	public List<SchemaTreeNode> getChildren() {
		return children;
	}    
    
}
