package net.myspring.util.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;

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

	protected String getOrder(Sort sort)  {
		StringBuilder sb = new StringBuilder();
		if(sort!=null) {
			sb.append(" ORDER BY ");
			Iterator<Sort.Order> iterator = sort.iterator();
			while (iterator.hasNext()) {
				Sort.Order order = iterator.next();
				sb.append(order.getProperty()).append(" ").append(order.getDirection());
				if(iterator.hasNext()) {
					sb.append(", ");
				}
			}
		}
		return sb.toString();
	}
}
