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
		return count + "d" + size;
	}

	public double roll() {
		if(count == 0) return size;
		double result = 0;
		Random rand = new Random();
		boolean isInt = MathUtil.isInt(size);
		int bound = (int) FastMath.round(size);
		for(int i = 0; i < count; ++i) {
			result += (isInt == true ? rand.nextInt(bound) + 1 : rand.nextDouble() * size);
		}
		return result;
	}

}
