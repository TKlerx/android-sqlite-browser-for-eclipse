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

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SchemaContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {		
        if (parentElement instanceof SchemaTreeNode) {
        	SchemaTreeNode schemaTreeNode = (SchemaTreeNode)parentElement;
            return schemaTreeNode.getChildren().toArray();
        }
        return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return element;
	}

	@Override
	public boolean hasChildren(Object element) {
        if (element instanceof SchemaTreeNode) {
        	SchemaTreeNode schemaTreeNode = (SchemaTreeNode)element;
            return schemaTreeNode.getChildCount() > 0;
        }
        return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {			
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {		
	}
	
}
