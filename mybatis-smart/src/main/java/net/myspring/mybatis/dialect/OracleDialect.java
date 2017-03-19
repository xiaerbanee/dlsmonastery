package net.myspring.mybatis.dialect;


import org.springframework.data.domain.Pageable;

public class OracleDialect extends Dialect{

	/**
	 * 分页sql
	 */
	final static String LIMIT_SQL_PATTERN = "select * from ( select row__.*, rownum rownum__ from ( %s ) row__ where rownum <=  %s ) where rownum__ > %s ";
	/**
	 * 分页sql首页
	 */
	final static String LIMIT_SQL_PATTERN_FIRST = "select * from ( %s ) where rownum <= %s";
	
	@Override
	public String getPageableSql(String sql, Pageable pageable) {
		sql = sql.trim();
		// no supports "for update", kill it
		sql.replaceAll("for\\s+update", "");
		if (pageable.getOffset() == 0) {
			return String.format(LIMIT_SQL_PATTERN_FIRST, sql, pageable.getPageSize());
		} else {
			return String.format(LIMIT_SQL_PATTERN, sql, pageable.getOffset() + pageable.getPageSize(), pageable.getOffset());
		}
	}
}

