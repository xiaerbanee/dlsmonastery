package net.myspring.mybatis.dialect;


import org.springframework.data.domain.Pageable;

public class H2Dialect extends Dialect {
	
	final static String LIMIT_SQL_PATTERN = "%s limit %s offset %s";
	final static String LIMIT_SQL_PATTERN_FIRST = "%s limit %s";

	@Override
	public String getPageableSql(String sql, Pageable pageable) {
		if (pageable.getOffset() == 0) {
			return String.format(LIMIT_SQL_PATTERN_FIRST, sql, pageable.getPageSize());
		} else {
			return String.format(LIMIT_SQL_PATTERN, sql, pageable.getPageSize(), pageable.getOffset());
		}
	}

}
