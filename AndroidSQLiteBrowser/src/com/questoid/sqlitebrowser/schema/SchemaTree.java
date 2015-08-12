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

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import de.timok.sqlitewrappers.ColumnDef;
import de.timok.sqlitewrappers.JdbcHelper;
import de.timok.sqlitewrappers.TableDef;

public class SchemaTree{
    private final Object root;   
    
	public static SchemaTree createInstance(final File dbFile) throws SQLException {
        SchemaTreeNode root = new SchemaTreeNode();
		if (dbFile == null || !dbFile.exists()) {
            return new SchemaTree(root);
        }
        try {
			final List<String> tableNames = JdbcHelper.getTableNames(dbFile);
			for (final String tableName : tableNames) {
				final TableDef table = new TableDef(tableName);
				final SchemaTreeNode node = new SchemaTreeNode(table);

				final List<ColumnDef> columns = JdbcHelper.getColumnNames(dbFile, tableName);
				for (final ColumnDef columnDef : columns) {
					node.addChild(new SchemaTreeNode(table, columnDef));
                }
                root.addChild(node);
            }
			// final Set<String> indexNames = schema.getIndexNames();
			// for (final String indexName : indexNames) {
			// final ISqlJetIndexDef index = schema.getIndex(indexName);
			// root.addChild(new SchemaTreeNode(index));
			// }
		} catch (final SQLException e) {
            root = new SchemaTreeNode();
            throw e;
        }
        return new SchemaTree(root);
    }


	private SchemaTree(final SchemaTreeNode root) {
        this.root = root;
    }
    
    public int getChildCount(final Object parent) {
        return ((SchemaTreeNode) parent).getChildCount();
    }
    
    public int getIndexOfChild(final Object parent, final Object child) {
        return ((SchemaTreeNode) parent).getIndexOfChild((SchemaTreeNode) child);
    }

    public Object getChild(final Object parent, final int index) {
        return ((SchemaTreeNode) parent).getChildAt(index);
    }
    
    public Object getRoot() {
        return root;
    }
    
    public boolean isLeaf(final Object node) { 
        return ((SchemaTreeNode) node).isLeaf(); 
    } 
    
}
