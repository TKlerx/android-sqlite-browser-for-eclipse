# android-sqlite-browser-for-eclipse
Eclipse Plugin to browse the sqlite database of an android app when debugging.

This code was hosted on google code before:

Automatically exported from code.google.com/p/android-sqlite-browser-for-eclipse

##Install

Download the latest jar file from [releases](https://github.com/TKlerx/android-sqlite-browser-for-eclipse/releases).
Place the jar in "[eclipse-folder]/dropins" and restart eclipse. 
Now go to "Window"->"Show View"->"Other" and search for SQlite Browser.
Do not choose an sqlite file from the file manager.

##Issues with Android 3.0+
It seems that this version is only compatible with SQLite v. 3.6.
Android 5 uses SQLite v. 3.8 and Android 3.0+ uses SQLite v. 3.7.x
This means this library will most probably not work for Android 3.0+

The sqljet library must be exchanged with an sqlite library that supports SQLite v. 3.8 (e.g. sqlite4java).
Feel free to do so and make a pull request


