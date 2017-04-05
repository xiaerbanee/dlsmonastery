/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package net.myspring.util.text;

import com.google.common.io.BaseEncoding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * hex/base64 编解码工具集，依赖Guava, 取消了对Commmon Codec的依赖
 */
public class EncodeUtil {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	/**
	 * Hex编码, 将byte[]编码为String，默认为ABCDEF为大写字母.
	 */
	public static String encodeHex(byte[] input) {
		return BaseEncoding.base16().encode(input);
	}

	/**
	 * Hex解码, 将String解码为byte[].
	 * 
	 * 字符串有异常时抛出IllegalArgumentException.
	 */
	public static byte[] decodeHex(CharSequence input) {
		return BaseEncoding.base16().decode(input);
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return BaseEncoding.base64().encode(input);
	}

	/**
	 * Base64解码.
	 * 
	 * 如果字符不合法，抛出IllegalArgumentException
	 */
	public static byte[] decodeBase64(CharSequence input) {
		return BaseEncoding.base64().decode(input);
	}

	/**
	 * Base64编码, URL安全.(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 */
	public static String encodeBase64UrlSafe(byte[] input) {
		return BaseEncoding.base64Url().encode(input);
	}

	/**
	 * Base64解码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 * 
	 * 如果字符不合法，抛出IllegalArgumentException
	 */
	public static byte[] decodeBase64UrlSafe(CharSequence input) {
		return BaseEncoding.base64Url().decode(input);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String part) {
		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}
}
