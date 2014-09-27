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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class SchemaLabelProvider implements ILabelProvider, ITableLabelProvider{

	@Override
	public void addListener(ILabelProviderListener listener) {				
	}

	@Override
	public void dispose() {		
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {	
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {		
		
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {		
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
        if (element instanceof SchemaTreeNode) {
        	SchemaTreeNode schemaTreeNode = (SchemaTreeNode)element;
            switch (columnIndex) {
                case 0:
                    return schemaTreeNode.getName();
                case 1:
                    return schemaTreeNode.getObjectType();
                case 2:
                    return schemaTreeNode.getType();
                case 3:
                    return schemaTreeNode.getSchema();
                default:
                	return "";
            }
        }
        return "";
	}

	@Override
	public Image getImage(Object element) {		
		return null;
	}

	@Override
	public String getText(Object element) {	
		return null;
	}
	
}