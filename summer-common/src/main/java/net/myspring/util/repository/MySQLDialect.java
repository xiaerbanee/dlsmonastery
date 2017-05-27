package net.myspring.util.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;

public class MySQLDialect extends Dialect {
	private static MySQLDialect mySQLDialect = new MySQLDialect();
	public MySQLDialect() {}

	public static MySQLDialect getInstance() {
		return mySQLDialect;
	}

	@Override
	public  String getPageableSql(String sql, Pageable pageable) {
		StringBuilder sb = new StringBuilder(sql);
		if(pageable.getSort() != null) {
			sb.append(" ORDER BY ");
			Iterator<Sort.Order> iterator = pageable.getSort().iterator();
			while (iterator.hasNext()) {
				Sort.Order order = iterator.next();
				sb.append(order.getProperty()).append(" ").append(order.getDirection());
				if(iterator.hasNext()) {
					sb.append(", ");
				}
			}
		}
		if (pageable.getOffset() == 0) {
			sb.append(" LIMIT  "+pageable.getPageSize());
		} else {
			sb.append(" LIMIT  "+ pageable.getOffset()+",  "+pageable.getPageSize());
		}
		return sb.toString();
	}

}
