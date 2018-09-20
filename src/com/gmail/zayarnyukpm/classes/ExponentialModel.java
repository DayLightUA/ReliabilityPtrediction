package com.gmail.zayarnyukpm.classes;

public class ExponentialModel extends Model {
	
	double mk, mkt, me_mkt;
	int nParameters=10;
	
	public ExponentialModel(){
		super();
	}
	
	public ExponentialModel(double[] var){
		super(var);
	}
	
	public double f(double x){
		setT(x);
		double result = var[1]+var[0]*(1+me_mkt)+var[5]*(var[2]+var[4]*x);
		return result;
	}
	
	public double d1f(double x){
		setT(x);
		double result = var[0]*me_mkt*mk+var[5]*var[4];
		return result;
	}
	
	public double d2f(double x){
		setT(x);
		double result= var[0]*me_mkt*mk*mk;
		return result;
	}
	
	public double d3f(double x){
		setT(x);
		double result= var[0]*me_mkt*mk*mk*mk;
		return result;
	}
	
	void setT(double x){
		mk = -var[3];
		mkt = mk*x;
		me_mkt = -Math.pow(Math.E, mkt);
	}
	
	public boolean setVar(double[] var){
		if (var.length==nParameters){
			this.var=var;
			varIsSeted=true;
			return true;
		} else return false;
	}
}
