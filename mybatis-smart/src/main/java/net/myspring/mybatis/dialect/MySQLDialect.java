package net.myspring.mybatis.dialect;

import net.myspring.mybatis.context.ProviderContextUtils;
import org.springframework.data.domain.Pageable;

public class MySQLDialect extends Dialect {
	@Override
	public String getPageableSql(String sql, Pageable pageable) {
		StringBuilder sb = new StringBuilder(sql);
		if(pageable.getSort() != null) {
			sb.append(ProviderContextUtils.getSql(pageable.getSort()));
		}
		if (pageable.getOffset() == 0) {
			sb.append(" LIMIT ").append(pageable.getPageSize());
		} else {
			sb.append(" LIMIT ").append(" ").append(pageable.getOffset()).append(",").append(pageable.getPageSize());
		}
		return sb.toString();
	}

}
