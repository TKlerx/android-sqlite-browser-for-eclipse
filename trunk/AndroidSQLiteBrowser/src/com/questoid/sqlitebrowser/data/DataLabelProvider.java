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

import java.io.ByteArrayInputStream;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.tmatesoft.sqljet.core.internal.memory.SqlJetMemoryPointer;

public class DataLabelProvider extends LabelProvider implements ITableLabelProvider {

	private Display display = Display.getDefault();

	private Image resize(Image image, int width, int height) {
		Image scaled = new Image(display, width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		gc.dispose();
		image.dispose(); // don't forget about me!
		return scaled;
	}

	private Image makeScaledImage(byte[] bytes) {
		ImageData data = new ImageData(new ByteArrayInputStream(bytes));
		Image image = new Image(display, data);
		int height = data.height;
		int width = data.width;

		int length = height > width ? height : width;
		float percent = 100;
		if (length > 64) {
			percent = 64f / length;

			height *= percent;
			width *= percent;
		}

		Image scaled = resize(image, width, height);

		return scaled;
	}

	@Override
	public Image getColumnImage(Object element, int index) {

		DataRow dataRow = (DataRow) element;
		if (dataRow != null && dataRow.getValueAt(index) != null) {
			Object val = dataRow.getValueAt(index);
			if (val instanceof SqlJetMemoryPointer) {

				SqlJetMemoryPointer p = (SqlJetMemoryPointer) val;

				int size = p.remaining();
				byte[] bytes = new byte[size];
				p.getBytes(bytes);

				try {
					return makeScaledImage(bytes);
				} catch (SWTException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	@Override
	public String getColumnText(Object element, int index) {
		DataRow dataRow = (DataRow) element;
		if (dataRow != null && dataRow.getValueAt(index) != null) {

			Object val = dataRow.getValueAt(index);
			if (val instanceof SqlJetMemoryPointer) {
				return "";
			}

			return dataRow.getValueAt(index).toString();
		} else {
			return "";
		}

	}
}
