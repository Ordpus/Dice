package com.ordpus.util.helper;

import org.apache.commons.math3.util.FastMath;

public class MathUtil {

	public static boolean isInt(double d) {
		return FastMath.abs(d - FastMath.round(d)) < 0.00001;
	}

}
