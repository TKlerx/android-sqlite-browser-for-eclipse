/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY. 
 * See the GNU General Public License for more details.
 * 
 * Copyright (C) 2010 QuestElement.com
 */
package com.questelement.api.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PrivateAccessor {
	
  public static Object getPrivateField (Object o, String fieldName) {  
    // Go and find the private field... 
    final Field fields[] = o.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; ++i) {
      if (fieldName.equals(fields[i].getName())) {
        try {
          fields[i].setAccessible(true);
          return fields[i].get(o);
        } 
        catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    } 
    return null;
  }
  
  public static Object invokePrivateMethod (Object o, String methodName, Object[] params) {  	    
	    // Go and find the private method... 
	    final Method methods[] = o.getClass().getDeclaredMethods();
	    for (int i = 0; i < methods.length; ++i) {
	      if (methodName.equals(methods[i].getName())) {
	        try {
	          methods[i].setAccessible(true);
	          return methods[i].invoke(o, params);
	        } 
	        catch (IllegalAccessException e) {
	        	e.printStackTrace();
	        }
	        catch (InvocationTargetException e) {
	        	e.printStackTrace();        	
	        }
	      }
	    }	   
	    return null;
	  }  
}

