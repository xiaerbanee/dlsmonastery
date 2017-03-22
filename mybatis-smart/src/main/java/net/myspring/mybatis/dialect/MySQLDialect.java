package net.myspring.mybatis.dialect;

import net.myspring.mybatis.context.ProviderContextUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;

public class MySQLDialect extends Dialect {
	@Override
	public String getPageableSql(String sql, Pageable pageable) {
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
			sb.append(" LIMIT ").append(pageable.getPageSize());
		} else {
			sb.append(" LIMIT ").append(" ").append(pageable.getOffset()).append(",").append(pageable.getPageSize());
		}
		return sb.toString();
	}

}
