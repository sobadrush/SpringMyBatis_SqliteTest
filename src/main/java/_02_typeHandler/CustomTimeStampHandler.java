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
 * 【教學】https://blog.csdn.net/u012702547/article/details/54572679
 */
public class CustomTimeStampHandler extends BaseTypeHandler<java.sql.Date> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, java.sql.Date parameter, JdbcType jdbcType) throws SQLException {
		System.err.println("===== TypeHandler setNonNullParameter() =====");
//		System.err.println("1 Date parameter >>> " + parameter);
//		System.err.println("2 Date parameter >>> " + parameter.getTime()); /*會變 milisecond*/
//		System.err.println("3 Date parameter >>> " + parameter.toString());
		ps.setString(i, String.valueOf(parameter));
	}

	@Override
	public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String sqlDateStr = rs.getString(columnName);
		System.err.println("TypeHandler sqlDateStr 111 >>> " + sqlDateStr);
		if (sqlDateStr != null) {
			return java.sql.Date.valueOf(sqlDateStr);
		}
		return null;
	}

	@Override
	public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String sqlDateStr = rs.getString(columnIndex);
		System.err.println("TypeHandler sqlDateStr 222 >>> " + sqlDateStr);
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


