package de.timok.sqlitewrappers;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcHelper {
	public static List<String> getTableNames(final File dbFile) throws SQLException {
		final List<String> result = new ArrayList<String>();
		final Connection conn = getConnection(dbFile);
		final DatabaseMetaData schema = conn.getMetaData();
		final ResultSet rs = schema.getTables(null, null, "%", null);

		while (rs.next()) {
			final String tableName = rs.getString(3);
			result.add(tableName);
		}
		conn.close();
		return result;
	}

	public static List<ColumnDef> getColumnNames(final File dbFile, final String tableName) throws SQLException {
		final List<ColumnDef> result = new ArrayList<ColumnDef>();
		final Connection conn = getConnection(dbFile);
		final DatabaseMetaData schema = conn.getMetaData();
		final ResultSet rs = schema.getColumns(null, null, tableName, null);

		while (rs.next()) {
			final String columnName = rs.getString(4);
			final int columnType = rs.getInt(5);
			result.add(new ColumnDef(columnName, columnType));
		}
		conn.close();
		return result;
	}

	public static Connection getConnection(final File dbFile) throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + dbFile);
	}

}
