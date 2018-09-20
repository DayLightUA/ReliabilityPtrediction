package com.gmail.zayarnyukpm.classes;

public class SquareModel extends CubicModel {

	public SquareModel(){
		super();
		nParameters=10;
	}
	
	public SquareModel(double[] var){
		super(var);
	}
	
	public double f(double x){
		setT(x);
		double result = var[1]-var[0]*(mkt+mkt2)+var[5]*(var[2]+var[4]*x);
		return result;
	}
	
	public double d1f(double x){
		setT(x);
		double result =	-var[0]*(mk+mk*mkt)+var[5]*var[4];
		return result;
	}
	
	public double d2f(double x){
		setT(x);
		double result = -var[0]*mk*mk;
		return result;
	}
	
	public double d3f(double x){
		return 0;
	}
}
