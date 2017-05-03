package net.myspring.future.common.config;

import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.time.LocalTimeUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Created by liuj on 2016-08-19.
 */
@ControllerAdvice
public class MyWebBindingInitializer {

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        binder.setAutoGrowCollectionLimit(10000);
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(StringUtils.isBlank(text) ? null : HtmlUtils.htmlEscape(text));
            }
            @Override
            public String getAsText() {
                Object value = getValue();
                return value == null ? null: Objects.toString(value);
            }
        });

        // LocalDateTime 类型转换
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                LocalDateTime localDateTime = LocalDateTimeUtils.parse(text);
                setValue(localDateTime);
            }
        });


        // LocalDate 类型转换
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalDateUtils.parse(text));
            }
        });

        // LocalTime 类型转换
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(LocalTimeUtils.parse(text));
            }
        });
    }
}
