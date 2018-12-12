package com.ordpus.util.helper;

import java.text.DecimalFormat;

import org.apache.commons.math3.util.FastMath;

public class MathUtil {

	public static boolean isInt(double d) {
		return FastMath.abs(d - FastMath.round(d)) < 0.00001;
	}

	public static String num2Str(double d) {
		if(isInt(d)) return Long.toString(FastMath.round(d));
		else return new DecimalFormat("#.###").format(d);
	}

	public static double operator(char op, double a, double b) {
		switch(op) {
			case '+':
				return a + b;
			case '-':
				return a - b;
			case '*':
				return a * b;
			case '/':
				return a / b;
			default:
				return 0;
		}
	}

}
