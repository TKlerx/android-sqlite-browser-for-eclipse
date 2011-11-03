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
package com.questoid.sqlitebrowser;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.android.ddmlib.FileListingService.FileEntry;
import com.android.ddmuilib.explorer.DeviceExplorer;
import com.android.ide.eclipse.ddms.CommonAction;
import com.android.ide.eclipse.ddms.views.FileExplorerView;
import com.questelement.api.reflection.PrivateAccessor;
import com.questoid.sqlitebrowser.util.SqliteUtil;
import com.questoid.sqlitebrowser.view.SqliteBrowserView;

public class SqliteBrowserPluginStartup implements IStartup {
	private final String FILEEXPLORERVIEW_ID = FileExplorerView.ID;
	private CommonAction sqlitebrowserAction;
	private static boolean actionAdded = false;	
	
	@Override
	public void earlyStartup() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (workbenchWindow != null) {
					workbenchWindow.getActivePage().addPartListener(new IPartListener(){
						@Override
						public void partActivated(IWorkbenchPart part) {
							createsqlitebrowserAction(part);
						}

						@Override
						public void partBroughtToTop(IWorkbenchPart part) {
							// Pass
						}

						@Override
						public void partClosed(IWorkbenchPart part) {								
							final IViewPart view = (IViewPart) part.getAdapter(IViewPart.class);
							if(view != null && view.getViewSite().getId().indexOf(FILEEXPLORERVIEW_ID) > -1){	
								actionAdded = false;
							}							
						}

						@Override
						public void partDeactivated(IWorkbenchPart part) {
							// Pass							
						}

						@Override
						public void partOpened(IWorkbenchPart part) {
							// Pass							
						}
					});	
					
				}
			}
		});
	}	
	
	private void createsqlitebrowserAction(final IWorkbenchPart part) {
		final IViewPart view = (IViewPart) part.getAdapter(IViewPart.class);
		if(view != null && view.getViewSite().getId().indexOf(FILEEXPLORERVIEW_ID) > -1){										
			if(!actionAdded){
				actionAdded = true; //don't add action more than once									
				createsqlitebrowserActionGui(view);									
				// Listen to mTreeViewer SelectionChanged
				final DeviceExplorer mExplorer = (DeviceExplorer)com.questelement.api.reflection.PrivateAccessor.getPrivateField(view, "mExplorer");
				final TreeViewer mTreeViewer = (TreeViewer)com.questelement.api.reflection.PrivateAccessor.getPrivateField(mExplorer, "mTreeViewer");
				mTreeViewer.addSelectionChangedListener(new ISelectionChangedListener(){
					@Override
					public void selectionChanged(SelectionChangedEvent event) {	
			              ISelection selection = event.getSelection();
			                if (selection.isEmpty()) {								                	
			                	sqlitebrowserAction.setEnabled(false);
			                    return;
			                }
			                if (selection instanceof IStructuredSelection) {
			                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			                    Object element = structuredSelection.getFirstElement();
			                    if (element == null)
			                        return;
			                    if (element instanceof FileEntry) {								    
			                        if (structuredSelection.size() == 1 && SqliteUtil.isSqliteDbFile((FileEntry)element)) {								                        	
			                        	sqlitebrowserAction.setEnabled(true);
			                        } else {
			                        	sqlitebrowserAction.setEnabled(false);
			                        }
			                    }
			                }
					}});
				
			}	
		}
	}

	private void createsqlitebrowserActionGui(final IViewPart fileExplorerView){	
		final DeviceExplorer mExplorer = (DeviceExplorer)PrivateAccessor.getPrivateField(fileExplorerView, "mExplorer");
		final Tree mTree = (Tree)PrivateAccessor.getPrivateField(mExplorer, "mTree");		
		IActionBars actionBars = fileExplorerView.getViewSite().getActionBars();
		IMenuManager menuManager = actionBars.getMenuManager();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();		
		//Define action
		sqlitebrowserAction = new CommonAction("Open File in SQLite Browser...") {
            @Override
            public void run() {      
            	TreeItem[] items = mTree.getSelection();  
                if (items.length == 1) {
                	final FileEntry singleEntry = (FileEntry)items[0].getData();
                    if (SqliteUtil.isSqliteDbFile(singleEntry)) { 
                        //Open sqlitebrowserView 
                	      try {	    	 
                	    	 final IWorkbenchPage activePage = fileExplorerView.getViewSite().getPage();
                	    	 Display.getDefault().syncExec( new Runnable() {
                	    		    public void run(){
                	    		    	 try {                 	    		    		 
                	    		    		 SqliteBrowserView sqliteBrowserView = (SqliteBrowserView)activePage.findView(SqliteBrowserView.ID);
                	    		    		 if(sqliteBrowserView != null){
                	    		    			 activePage.hideView(sqliteBrowserView);
                	    		    		 }                	    		    		 
                	    		    		 SqliteBrowserView.mExplorer = mExplorer;
                	    		    		 SqliteBrowserView.fileEntry = singleEntry;
                	    		    		 activePage.showView(SqliteBrowserView.ID); // Open the view with new db file
                						} catch (Exception e) {							 
                							e.printStackTrace();
                						}
                	    		    }
                	    		  });
                	      }
                	      catch (Exception e) {	    	 
                	         e.printStackTrace();
                	      }
                    }
                }
            }
        };
        sqlitebrowserAction.setId("sqlitebrowserAction");	       
        sqlitebrowserAction.setImageDescriptor(SqliteBrowserPlugin.getImageDescriptor("icons/sqlitebrowser.gif"));
        sqlitebrowserAction.setToolTipText("Open File in SQLite Browser...");
        sqlitebrowserAction.setEnabled(false);				
		//Add action to menu bar
		menuManager.add(new Separator());
		menuManager.add(sqlitebrowserAction);	
		menuManager.update(true);
		//Add action to tool bar
		toolBarManager.add(new Separator());
		toolBarManager.add(sqlitebrowserAction);
		toolBarManager.update(true);
	}

}
