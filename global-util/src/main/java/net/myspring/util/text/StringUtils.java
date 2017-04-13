package net.myspring.util.text;

import com.google.common.collect.Lists;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

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
}
