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

import java.util.List;
import java.util.Set;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.schema.ISqlJetColumnDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetIndexDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetSchema;
import org.tmatesoft.sqljet.core.schema.ISqlJetTableDef;

public class SchemaTree{
    private Object root;   
    
    public static SchemaTree createInstance(ISqlJetSchema schema) throws SqlJetException {
        SchemaTreeNode root = new SchemaTreeNode();
        if (schema == null) {
            return new SchemaTree(root);
        }
        try {
            Set<String> tableNames = schema.getTableNames();
            for (String tableName : tableNames) {
                ISqlJetTableDef table = schema.getTable(tableName);
                SchemaTreeNode node = new SchemaTreeNode(table);                
                List<ISqlJetColumnDef> columns = table.getColumns();
                for (ISqlJetColumnDef column : columns) {
                    node.addChild(new SchemaTreeNode(table, column));
                }
                root.addChild(node);
            }
            Set<String> indexNames = schema.getIndexNames();
            for (String indexName : indexNames) {
                ISqlJetIndexDef index = schema.getIndex(indexName);
                root.addChild(new SchemaTreeNode(index));
            }
        } catch (SqlJetException e) {
            root = new SchemaTreeNode();
            throw e;
        }
        return new SchemaTree(root);
    }

    private SchemaTree(SchemaTreeNode root) {
        this.root = root;
    }
    
    public int getChildCount(Object parent) {
        return ((SchemaTreeNode) parent).getChildCount();
    }
    
    public int getIndexOfChild(Object parent, Object child) {
        return ((SchemaTreeNode) parent).getIndexOfChild((SchemaTreeNode) child);
    }

    public Object getChild(Object parent, int index) {
        return ((SchemaTreeNode) parent).getChildAt(index);
    }
    
    public Object getRoot() {
        return root;
    }
    
    public boolean isLeaf(Object node) { 
        return ((SchemaTreeNode) node).isLeaf(); 
    } 
    
}
