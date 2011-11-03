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

import com.android.ddmlib.FileListingService;
import com.android.ddmlib.FileListingService.FileEntry;

public class SqliteUtil {
	
	/**
	 * fileEntry is a sqlite db file if
	 * 1. It has a .db file extension
	 * 2. It is a FileListingService.TYPE_FILE
	 * 3. Parent is a directory called "databases"
	 */
	public static boolean isSqliteDbFile(FileEntry fileEntry){
		if(fileEntry.getName().toLowerCase().endsWith(".db")){
			return true;
		}
		
		if(fileEntry.getType() != FileListingService.TYPE_FILE){
			return false;
		}
		
		if(fileEntry.getParent().getType() != FileListingService.TYPE_DIRECTORY || !fileEntry.getParent().getName().equalsIgnoreCase("databases")){
			return false;
		}	
		
		return true;	
	}
}
