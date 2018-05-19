package _02_typeHandler;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 【教學】https://my.oschina.net/amoshuang/blog/134199
 */
public class CustomTimeStampHandler extends BaseTypeHandler<java.sql.Date> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String sqlDateStr = rs.getString(columnName);
		System.err.println("@@ sqlDateStr 111 >>> " + sqlDateStr);
		if (sqlDateStr != null) {
			return java.sql.Date.valueOf(sqlDateStr);
		}
		return null;
	}

	@Override
	public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String sqlDateStr = rs.getString(columnIndex);
		System.err.println(" @@ sqlDateStr 222 >>> " + sqlDateStr);
		if (sqlDateStr != null) {
			return java.sql.Date.valueOf(sqlDateStr);
		}
		return null;
	}

	@Override
	public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
