package de.timok.sqlitewrappers;

public class ColumnDef {
	String name;
	int type = -1;;

	public ColumnDef(final String name, final int type) {
		super();
		this.name = name;
		this.type = type;
	}

	public ColumnDef(final String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(final int type) {
		this.type = type;
	}

}
