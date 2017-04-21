package net.myspring.util.text;

import com.google.common.collect.Lists;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class StringUtils extends org.apache.commons.lang3.StringUtils{

	public static <E> List<E> getSplitList(String str, String splitter) {
		List<E> list = Lists.newArrayList();
		if (isNotBlank(str)) {
			String[] arr = str.split(splitter);
			for (String item : arr) {
				if (isNotBlank(item) && !list.contains(item.trim())) {
					E value = (E) item.trim();
					list.add(value);
				}
			}
		}
		return list;
	}

	public static String getReplaced(String str) {
		List<String> beforeList = Lists.newArrayList();
		beforeList.add("，");
		beforeList.add(" ");
		beforeList.add("　");
		beforeList.add("\n");
		beforeList.add("	");
		return getReplaced(str, beforeList, ",");
	}

	public static String getReplaced(String str, List<String> beforeList, String after) {
		String result = trim(str);
		if (StringUtils.isNotBlank(result)) {
			for (String before : beforeList) {
				result = StringUtils.replace(result, before, after);
			}
		}
		return result;
	}

	public static String getFormatId(String id, String prefix) {
		return getFormatId(id, prefix, "000000000000");
	}

	public static String getFormatId(String id, String prefix, String format) {
		if (id == null) {
			return "";
		} else {
			java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat(format);
			return prefix + decimalFormat.format(id);
		}
	}

	public static String getEncryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(8);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, 1024);
		return EncodeUtil.encodeHex(salt) + EncodeUtil.encodeHex(hashPassword);
	}

	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = EncodeUtil.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, 1024);
        String pwd = EncodeUtil.encodeHex(salt) + EncodeUtil.encodeHex(hashPassword);
        return password.equalsIgnoreCase(pwd);
	}

	public static  String toString(Object object) {
		if(object == null) {
			return "";
		} else {
			return String.valueOf(object);
		}
	}
}
