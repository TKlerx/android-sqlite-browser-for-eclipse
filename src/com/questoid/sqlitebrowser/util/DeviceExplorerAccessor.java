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
package com.questoid.sqlitebrowser.util;

import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.FileListingService.FileEntry;
import com.android.ddmlib.SyncService.ISyncProgressMonitor;
import com.android.ddmlib.TimeoutException;
import com.android.ddmuilib.console.DdmConsole;
import com.android.ddmuilib.explorer.DeviceExplorer;
import com.questelement.api.reflection.PrivateAccessor;

public class DeviceExplorerAccessor {
	
	public static void pullFile(DeviceExplorer mExplorer, FileEntry fileEntry, String tempDbFilePath) {
		IDevice mCurrentDevice = (IDevice)PrivateAccessor.getPrivateField(mExplorer, "mCurrentDevice");			
		try {
			final SyncService sync = mCurrentDevice.getSyncService();				
		    ISyncProgressMonitor monitor = SyncService.getNullProgressMonitor();
		    sync.pullFile(fileEntry, tempDbFilePath, monitor);
		    sync.close();				
		} catch (IOException e) {				
		    DdmConsole.printErrorToConsole("Failed to open database file " + fileEntry.getName());
		    DdmConsole.printErrorToConsole(e.getMessage());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdbCommandRejectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SyncException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
