package com.gmail.zayarnyukpm.classes;

import java.math.BigDecimal;

public class GramSharleModel extends GausianModel {
	BigDecimal a = new BigDecimal("0");
	BigDecimal e = new BigDecimal("0");
	
	public GramSharleModel(double[] var) {
		super (var);
	}
	public GramSharleModel() {
		super ();
	}
	public GramSharleModel(double[] var, Model model) {
		super (var, model);
	}

	public BigDecimal f(BigDecimal x, BigDecimal t){
		return super.f(x, t)
				.add((a.negate().divide(new BigDecimal("6"))) .multiply(super.d3f(x,t)))
				.add((e.divide(new BigDecimal("24"))) .multiply(super.d4f(x,t)));
	}
	
	public void setAsimetryExcessus(BigDecimal a, BigDecimal e){
		this.a=a;
		this.e=e;
	}
}
