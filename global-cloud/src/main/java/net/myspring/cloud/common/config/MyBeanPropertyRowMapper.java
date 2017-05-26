package net.myspring.cloud.common.config;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MyBeanPropertyRowMapper<T>  extends BeanPropertyRowMapper<T> {

    public MyBeanPropertyRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        Class<?> requiredType = pd.getPropertyType();
        Object value = null;
        if (LocalDateTime.class.equals(requiredType) || LocalDate.class.equals(requiredType) || LocalTime.class.equals(requiredType)) {
            if(rs.getObject(index) != null) {
                if (LocalDateTime.class.equals(requiredType)) {
                    value = rs.getTimestamp(index).toLocalDateTime();;
                } else if (LocalDate.class.equals(requiredType)) {
                    value = rs.getDate(index).toLocalDate();
                } else if (LocalTime.class.equals(requiredType)) {
                    value = rs.getTime(index).toLocalTime();
                }
            }
            return value;
        } else {
            return super.getColumnValue(rs, index, pd);
        }
    }
}
