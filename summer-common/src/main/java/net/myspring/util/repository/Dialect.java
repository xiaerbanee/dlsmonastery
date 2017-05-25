package net.myspring.util.repository;


import org.springframework.data.domain.Pageable;

public abstract class Dialect {

	/**
	 * 返回分页sql，无占位符，limit和 offset 直接写死在sql中
	 */
	public  abstract  String getPageableSql(String sql, Pageable pageable);

	/**
	 * 将sql转换为总记录数SQL
	 * @param sql  sql语句
	 * @return 总记录数的sql
	 */
	public String getCountSql(String sql) {
		return "select count(*) from (" + sql + ") temp_sql";
	}
}
