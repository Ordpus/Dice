package com.ordpus.util.info;

import java.util.Random;

import org.apache.commons.math3.util.FastMath;

import com.ordpus.util.helper.MathUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DiceWrapper {

	public int count;
	public double size;

	public String getSize() {
		if((int) size == size) return Integer.toString((int) size);
		return Double.toString(size);
	}

	@Override
	public String toString() {
		return count + "d" + MathUtil.num2Str(size);
	}

	/**
	 *
	 * @return {rollStr, resultStr, result}
	 */
	public Object[] roll() {
		StringBuilder result = new StringBuilder();
		double c = 0;
		boolean isInt = MathUtil.isInt(size);
		int bound = (int) FastMath.round(size);
		if(count == 0) {
			String str = MathUtil.num2Str(size);
			return new Object[] { str, str, size };
		}
		Random rand = new Random();
		for(int i = 0; i < count; ++i) {
			double r = (isInt ? rand.nextInt(bound) + 1 : rand.nextDouble() * size);
			result.append(MathUtil.num2Str(r));
			if(i != count - 1) result.append(" + ");
			c += r;
		}
		return new Object[] { result.toString(), isInt ? Double.toString(c) : Long.toString(FastMath.round(c)), c };
	}

}
