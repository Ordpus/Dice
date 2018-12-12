package com.ordpus.util.ast.nodes;

import com.ordpus.util.helper.ArraysUtil;

import lombok.Getter;

public class DAstContentWrapper {
	public double[] result;
	public double res;
	public StringBuilder builder;
	@Getter
	boolean overSized = false, overCount = false, overLength = false, hasProblem = false;

	public DAstContentWrapper() {}

	public DAstContentWrapper(double res, StringBuilder builder) {
		this.res = res;
		this.builder = builder;
	}

	public DAstContentWrapper(double[] result, double res, StringBuilder builder) {
		this.res = res;
		this.result = result;
		this.builder = builder;
	}

	@Override
	public String toString() {
		return "\n  result: {" + (result != null ? ArraysUtil.oneDArrayToStrHor(result) : "null") + "}\n  res:  " + ArraysUtil.sum(result);
	}

	public static class DAstContentWrapperBuilder {

		private boolean overSized = false, overCount = false, overLength = false, hasProblem = false;

		public DAstContentWrapperBuilder overSized() {
			overSized = true;
			hasProblem = true;
			return this;
		}

		public DAstContentWrapperBuilder overCount() {
			overCount = true;
			hasProblem = true;
			return this;
		}

		public DAstContentWrapperBuilder overLength() {
			overLength = true;
			hasProblem = true;
			return this;
		}

		public DAstContentWrapper build() {
			DAstContentWrapper result = new DAstContentWrapper();
			result.overCount = overCount;
			result.overSized = overSized;
			result.overLength = overLength;
			result.hasProblem = hasProblem;
			return result;
		}

	}
}
