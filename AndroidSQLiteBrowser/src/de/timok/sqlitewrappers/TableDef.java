package de.timok.sqlitewrappers;

public class TableDef {
	String name;
	String createStatement;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public TableDef(final String name) {
		super();
		this.name = name;
	}

}
