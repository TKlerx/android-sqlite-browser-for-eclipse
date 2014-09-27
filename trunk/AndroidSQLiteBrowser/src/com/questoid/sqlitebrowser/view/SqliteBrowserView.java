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

package com.questoid.sqlitebrowser.view;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.internal.memory.SqlJetMemoryPointer;
import org.tmatesoft.sqljet.core.schema.ISqlJetColumnDef;
import org.tmatesoft.sqljet.core.schema.ISqlJetSchema;
import org.tmatesoft.sqljet.core.schema.ISqlJetTableDef;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import com.android.ddmlib.FileListingService.FileEntry;
import com.android.ddmuilib.explorer.DeviceExplorer;
import com.questoid.sqlitebrowser.data.DataContentProvider;
import com.questoid.sqlitebrowser.data.DataLabelProvider;
import com.questoid.sqlitebrowser.data.DataRow;
import com.questoid.sqlitebrowser.schema.SchemaContentProvider;
import com.questoid.sqlitebrowser.schema.SchemaLabelProvider;
import com.questoid.sqlitebrowser.schema.SchemaTree;
import com.questoid.sqlitebrowser.util.DeviceExplorerAccessor;

public class SqliteBrowserView extends ViewPart {
	public static final String ID = "com.questoid.sqlitebrowser.view.sqliteBrowserView";
	private static final String TEMP_DB_FILE_NAME = "sqlitedbfile.db";
	public static DeviceExplorer mExplorer;
	public static FileEntry fileEntry;

	private File dbFile;

	public SqliteBrowserView() {
		super();
	}

	@Override
	public void createPartControl(final Composite parent) {
		if (mExplorer != null) {

			pullFile(); // pull file from device.

			final ISqlJetSchema sqlJetSchema = getSqlJetSchema(dbFile);
			SchemaTree schemaTreeModel = null;
			String[] tableNames = null;
			try {

				schemaTreeModel = SchemaTree.createInstance(sqlJetSchema);
				tableNames = sqlJetSchema.getTableNames().toArray(new String[0]);
			} catch (final SqlJetException e) {
				e.printStackTrace();
			}
			//
			final TabFolder sqlitebrowserTabFolder = new TabFolder(parent, SWT.BORDER);
			// Schema structure gui
			createSchemaGui(sqlitebrowserTabFolder, schemaTreeModel);
			// Browse data gui
			createDataGui(sqlitebrowserTabFolder, tableNames);
			// create SQLQuery Gui
			// RefactorClass.createSQLQueryGui(sqlitebrowserTabFolder, dbFile);
		} else {
			Label label = null;
			label = new Label(parent, SWT.LEFT);
			label.setText("Select db file in File Explorer, and open it in SQLite Browser...");
			label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		}
	}

	private void createDataGui(final TabFolder tabFolder, final String[] tableNames) {
		final TabItem browseDataTabItem = new TabItem(tabFolder, SWT.NULL);
		browseDataTabItem.setText("Browse Data");
		final Composite dataComposite = new Composite(tabFolder, SWT.NONE);
		dataComposite.setLayout(new GridLayout(4, false));

		//
		Label label = null;
		label = new Label(dataComposite, SWT.LEFT);
		label.setText("Table: ");
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));

		final Combo guiTableCombo = new Combo(dataComposite, SWT.READ_ONLY);
		guiTableCombo.setItems(tableNames);
		guiTableCombo.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));

		final Button refreshBtn = new Button(dataComposite, SWT.PUSH);
		refreshBtn.setText("refresh");
		refreshBtn.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));

		label = new Label(dataComposite, SWT.LEFT);
		label.setText("  "); // Blank label to grab horizontal space
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		//
		final Table guiTable = new Table(dataComposite, SWT.SINGLE | SWT.FULL_SELECTION);
		final TableViewer guiTableViewer = new TableViewer(guiTable);
		guiTableViewer.setLabelProvider(new DataLabelProvider());
		guiTableViewer.setContentProvider(new DataContentProvider());
		//

		guiTableCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				loadTableData(dbFile, guiTableViewer, guiTableCombo.getText());
			}
		});

		// refresh button
		refreshBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				pullFile(); // pull file from device.
				loadTableData(dbFile, guiTableViewer, guiTableCombo.getText());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				// System.out.println("SqliteBrowserView.createDataGui(...).new SelectionListener() {...}.widgetDefaultSelected()");
			}
		});

		// cell double click listener.
		guiTable.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				final Point pt = new Point(event.x, event.y);

				final ViewerCell cell = guiTableViewer.getCell(pt);
				final int column = cell.getColumnIndex();

				final DataRow row = (DataRow) cell.getElement();
				final Object cellData = row.getValueAt(column);

				handleCellData(cellData);
			}
		});

		guiTableCombo.select(0);
		browseDataTabItem.setControl(dataComposite);
		loadTableData(dbFile, guiTableViewer, guiTableCombo.getText()); // Load first selected item on start
	}

	private void createSchemaGui(final TabFolder tabFolder, final SchemaTree schemaTreeModel) {
		final TabItem dbStructureTabItem = new TabItem(tabFolder, SWT.NULL);
		dbStructureTabItem.setText("Database Structure");
		final Tree tree = new Tree(tabFolder, SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// create the jface wrapper
		final TreeViewer treeViewer = new TreeViewer(tree);
		final String[] schemaColumnNames = new String[] { "Name", "Object", "Type", "Schema" };
		final int[] schemaColumnWidths = new int[] { 200, 100, 150, 800 };
		final int[] schemaColumnAlignments = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
		for (int i = 0; i < schemaColumnNames.length; i++) {
			final TreeColumn treeColumn = new TreeColumn(tree, schemaColumnAlignments[i]);
			treeColumn.setText(schemaColumnNames[i]);
			treeColumn.pack();
			treeColumn.setWidth(schemaColumnWidths[i]);
		}
		final SchemaContentProvider schemaContentProvider = new SchemaContentProvider();
		treeViewer.setContentProvider(schemaContentProvider);
		treeViewer.setLabelProvider(new SchemaLabelProvider());
		treeViewer.setInput(schemaTreeModel.getRoot());
		// Add table viewer to database structure tab item
		dbStructureTabItem.setControl(tree);
	}

	private ISqlJetSchema getSqlJetSchema(final File dbFile) {
		SqlJetDb db = null;
		try {
			db = SqlJetDb.open(dbFile, true);
			return db.getSchema();
		} catch (final SqlJetException e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (final SqlJetException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private void loadTableData(final File dbFile, final TableViewer guiTableViewer, final String dbTableName) {
		SqlJetDb db = null;
		try {
			db = SqlJetDb.open(dbFile, true);
			final ISqlJetTable dbTable = db.getTable(dbTableName);
			final ArrayList<DataRow> data = new ArrayList<DataRow>();
			final ISqlJetTableDef tableDef = dbTable.getDefinition();
			final List<String> names = new ArrayList<String>();
			for (final ISqlJetColumnDef column : tableDef.getColumns()) {
				names.add(column.getName());
			}
			final String[] namesArray = names.toArray(new String[names.size()]);

			final Table guiTable = guiTableViewer.getTable();
			createGuiTableColumns(guiTable, namesArray);

			dbTable.getDataBase().runReadTransaction(new ISqlJetTransaction() {
				@Override
				public Object run(final SqlJetDb db) throws SqlJetException {
					final ISqlJetCursor cursor = dbTable.open();
					try {
						int count = 0;
						while (!cursor.eof()) {
							data.add(DataRow.read(cursor, count, namesArray));
							cursor.next();
							count++;
						}
					} finally {
						cursor.close();
					}
					return null;
				}
			});
			// Populate data and refresh table viewer
			guiTableViewer.setInput(data);
			guiTableViewer.refresh();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (final SqlJetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void setFocus() {

	}

	private void createGuiTableColumns(final Table guiTable, final String[] columnNames) {
		// Remove all old columns first
		final TableColumn[] columns = guiTable.getColumns();
		for (final TableColumn column : columns) {
			column.dispose();
		}
		// Add new columns
		for (int i = 0; i < columnNames.length; i++) {
			final TableColumn tableColumn = new TableColumn(guiTable, SWT.LEFT);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(100);
		}
		// Set table fill and scroll
		guiTable.setHeaderVisible(true);
		guiTable.setLinesVisible(true);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 4;
		guiTable.setLayoutData(gridData);
	}

	private void pullFile() {
		final String defaultPath = System.getProperty("user.home");
		final String tempDbFilePath = defaultPath + File.separator + TEMP_DB_FILE_NAME;

		// Write .db file to a temp file
		DeviceExplorerAccessor.pullFile(mExplorer, fileEntry, tempDbFilePath);
		//
		dbFile = new File(tempDbFilePath);
	}

	private void handleCellData(final Object cellData) {

		if (cellData instanceof SqlJetMemoryPointer) {
			// save as file
			final Shell shell = new Shell(Display.getDefault());

			final FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFilterNames(new String[] { "All Files (*.*)" });
			dialog.setFilterExtensions(new String[] { "*.*" }); // Windows
			dialog.setFilterPath(System.getProperty("user.home"));

			final String fileName = dialog.open();
			if (fileName != null) {

				final SqlJetMemoryPointer p = (SqlJetMemoryPointer) cellData;
				final int count = p.remaining();
				RandomAccessFile file = null;
				try {
					file = new RandomAccessFile(fileName, "rw");
					p.writeToFile(file, 0, count);
					file.close();
				} catch (final IOException e) {
					e.printStackTrace();
				} finally {
					if (file != null) {
						try {
							file.close();
						} catch (final IOException e) {
						}
					}
				}
			}
		} else if (cellData != null) {
			// copy clipboard
			final Clipboard cb = new Clipboard(Display.getDefault());
			final TextTransfer textTransfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { cellData.toString() }, new Transfer[] { textTransfer });
		}
	}
}
