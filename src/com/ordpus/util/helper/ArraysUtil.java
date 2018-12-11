package com.ordpus.util.helper;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.FastMath;

import com.ordpus.util.StdOut;

public class ArraysUtil {

	public static Map<Integer, Method> funcMap;

	static {
		funcMap = new HashMap<Integer, Method>();
		try {
			StdOut.printArray(ArraysUtil.class.getDeclaredMethods());
			funcMap.put(0, ArraysUtil.class.getDeclaredMethod("sum2", Object.class, Object.class));

			funcMap.put(1, ArraysUtil.class.getDeclaredMethod("mean2", Object.class, Object[].class));
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
		} catch(SecurityException e) {
			e.printStackTrace();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String arrayToStr(Object o) throws NoSuchAlgorithmException {
		int dim = dim(o.getClass().getTypeName());
		try {
			if(dim < 0) throw new NoSuchAlgorithmException("This is not an array");
			else if(dim == 1) return oneDArrayToStr(o);
			else if(dim == 2) return matrixToStr(o);
			else return arrayToStr(o, 0, dim, 0, Array.getLength(o), 0, new StringBuilder());
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String oneDArrayToStr(Object o) throws NoSuchAlgorithmException {
		if(dim(o.getClass().getTypeName()) != 1) throw new NoSuchAlgorithmException("The dimension of the arr should be 1");
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < Array.getLength(o); ++i)
			result.append(Array.get(o, i)).append('\n');
		return result;
	}

	public static String matrixToStr(Object o) throws NoSuchAlgorithmException {
		if(dim(o.getClass().getTypeName()) != 2) throw new NoSuchAlgorithmException("The dimension of the arr should be 2");
		StringBuilder result = new StringBuilder();
		for(int y = 0; y < Array.getLength(o); ++y)
			for(int x = 0; x < Array.getLength(Array.get(o, y)); ++x) {
				result.append(Array.get(Array.get(o, y), x));
				if(x == Array.getLength(Array.get(o, y)) - 1) {
					if(y < Array.getLength(o) - 1) result.append('\n');
				} else result.append(" ");
			}
		return result;
	}

	public static Object sum(Object o) throws NoSuchAlgorithmException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Counter a = new Counter();
		check(o, 0, a);
		long round = FastMath.round(a.d);
		if(FastMath.abs(a.d - round) < 0.0001) return round;
		return a.d;
	}

	public static Object mean(Object o) throws NoSuchAlgorithmException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Counter a = new Counter(), b = new Counter();
		check(o, 1, a, b);
		double result = a.d / b.d;
		long round = FastMath.round(result);
		if(FastMath.abs(result - round) < 0.0001) return round;
		return result;
	}

	private static void check(Object o, int arg, Object... args) throws NoSuchAlgorithmException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int dim = dim(o.getClass().getTypeName());
		if(dim < 0) throw new NoSuchAlgorithmException("This is not an array");
		arrIteration(o, 0, dim, 0, Array.getLength(o), 0, funcMap.get(arg), args);
	}

	private static String arrayToStr(Object o, int dim, int max, int index, int leng, int tab, StringBuilder builder) {
		if(dim != 0) builder.append('\n');
		builder.append(StdOut.genSpace(tab));
		if(dim < max) {
			int length = Array.getLength(o);
			builder.append('[');
			for(int i = 0; i < length; ++i) {
				Object obji = Array.get(o, i);
				if(dim < max - 1) arrayToStr(obji, dim + 1, max, i, Array.getLength(obji), tab + 1, builder);
				else builder.append(obji);
				if(i != length - 1) builder.append(", ");
				else if(dim < max - 1) builder.append('\n').append(StdOut.genSpace(tab));
			}
		}
		builder.append("]");
		return builder;
	}

	private static void arrIteration(Object o, int dim, int max, int index, int leng, int tab, Method method, Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchAlgorithmException {
		if(dim < max) {
			int length = Array.getLength(o);
			for(int i = 0; i < length; ++i) {
				Object obji = Array.get(o, i);
				if(dim < max - 1) arrIteration(obji, dim + 1, max, i, Array.getLength(obji), tab + 1, method, args);
				else method.invoke(null, obji, args);
			}
		}
	}

	@SuppressWarnings("unused")
	private static void sum2(Object o, Object count) {
		Counter c = (Counter) count;
		c.d += ((Number) o).doubleValue();
	}

	@SuppressWarnings("unused")
	private static void mean2(Object o, Object[] count) {
		Counter c = (Counter) count[0];
		Counter c2 = (Counter) count[1];
		c.d += ((Number) o).doubleValue();
		c2.d += 1;
	}

	private static int dim(String str) {
		int dim = 0;
		for(int i = str.length() - 1; i >= 0; --i) {
			if(str.charAt(i) == ']') dim++;
			if(str.charAt(i) != ']' && str.charAt(i) != '[') break;
		}
		return dim;
	}

}