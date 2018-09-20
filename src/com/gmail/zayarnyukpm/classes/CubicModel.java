package com.gmail.zayarnyukpm.classes;

public class CubicModel extends Model {
	
	double mk, mkt, mkt2, mkt3;
	
	public CubicModel(){
		nParameters=10;
		var=new double[nParameters];
		for (int i =0; i<var.length; i++){
			var[i]=0;
		}
	}
	
	public CubicModel(double[] var){
		super(var);
	}
	
	public double f(double x){
		setT(x);
		double result = var[1]+(-var[0]*(mkt+mkt2+mkt3)+(var[5]*(var[2]+(var[4]*x))));
		return result;
	}
	
	public double d1f(double x){
		setT(x);
		double result =
				-var[0]*(mk+mk*mkt+mk*mkt2)+var[5]*var[4];
		return result;
	}
	
	public double d2f(double x){
		setT(x);
		double result =
				-var[0]*mk*mk*(1+mkt);
		return result;
	}
	
	public double d3f(double x){
		setT(x);
		double result = -var[0]*mk*mk*mk;
		return result;
	}
	
	void setT(double x){
		mk = -var[3];
		mkt = mk*x;
		mkt2 = mkt*mkt/2;
		mkt3 = mkt*mkt2/3;
	}
}
