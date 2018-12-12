package com.ordpus.util;

import java.security.NoSuchAlgorithmException;

import com.ordpus.util.helper.ArraysUtil;

public class StdOut {

	public static void println(Object... o) {
		print(o);
		System.out.println();
	}

	public static void print(Object... o) {
		if(o != null && o.length > 0) for(Object obj : o)
			System.out.print(obj + " ");
	}

	public static void printf(String format, Object[] args) {
		System.out.printf(format, args);
	}

	public static void printArray(Object o) throws NoSuchAlgorithmException {
		System.out.println(ArraysUtil.arrayToStr(o));
	}

	public static String genSpace(int i) {
		String result = "";
		for(int j = 0; j < i; ++j)
			result += " ";
		return result;
	}

}
