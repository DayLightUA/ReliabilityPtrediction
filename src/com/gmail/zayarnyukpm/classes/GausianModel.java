package com.gmail.zayarnyukpm.classes;

import java.math.BigDecimal;
import java.math.MathContext;

import org.nevec.rjm.BigDecimalMath;

public class GausianModel extends Model {
	Model model1;
	Model model2;
	double[] var1 = new double[10];
	double[] var2 = new double[10];
	double alpha;
	boolean isSpeedDistribution = false;
	int nParameters=11;
	
	
	boolean withModels = false;
	
	public GausianModel(){
		var=new double[nParameters];
		for (int i =0; i<var.length; i++){
			var[i]=0;
		}
	}
	
	public GausianModel(double[] var, Model model){
		super(var);
		setModels(model);
	}
	
	public GausianModel(double[] var){
		super(var);
	}
	
	public double f(double x, double t){
		double result = 1/(model2.f(t)*Constants.PI2SQRT)
				*(Math.pow(Math.E, -(x-model1f(t))*(x-model1f(t)))
						/(2*model2.f(t)*model2.f(t)));
		
		return result;
	}
	public double f(){
		double result = f(var[0], var[var.length-1]);
		return result;
	}
	
	public double d1f(double x, double t){
		double result = f(x,t)*xMinM(x,t);
		return result;
	}
	public double d1f(){
		double result = d1f(var[0], var[var.length-1]);
		return result;
	}

	
	public double d2f(double x, double t){
		double result = d1f(x,t)*xMinM(x,t)
			+f(x,t)*mOneDivSigma(t);
		return result;
	}
	public double d2f(){
		double result = d2f(var[0], var[var.length-1]);
		return result;
	}
	
	public double d3f(double x, double t){
		double result = d2f(x,t)*xMinM(x,t)
				+2*d1f(x,t)*mOneDivSigma(t);
		return result;
	}
	public double d3f(){
		double result = d3f(var[0], var[var.length-1]);
		return result;
	}
	
	public double d4f(double x, double t){
		double result = -f(x,t)*6*mOneDivSigma(t)
				+xMinM(x,t)*xMinM(x,t)+3*mOneDivSigma(t)*mOneDivSigma(t);
		return result;
	}
	public double d4f(){
		double result = d4f(var[0], var[var.length-1]);
		return result;
	}
	
	private double xMinM(double x, double t){
		return -(2*x*(-model1.f(t))/(2*model2.f(t)*model2.f(t)));
	}
	private double mOneDivSigma(double t){
		return -1/(model2.f(t)*model2.f(t));
	}
	private double model1f(double t){
		if (isSpeedDistribution) return model1.d1f(t);
		else return model1.f(t);
	}
	
	public void setAlphaAndBetta (double alpha, double speedDistribution){
		if (alpha==0) alpha=1;
		this.alpha=alpha;
		if (speedDistribution==0) isSpeedDistribution = true;
	}
	
	public void setModels(Model model){
		if (model!=null){
			model1=model; model2=model.cloneModel();
			withModels=true;
			setVar(var);
		}
	}
	
	public boolean setVar(double[] var){
		boolean result=super.setVar(var);
		for (int i=0; i<10; i++){
			var1[i]=var[i+1]; 
			var2[i]=var[i+1];
		}
		var1[5]=0;
		var2[0]=0;
		var2[1]=0;
		var2[5]=1;
		if (isSpeedDistribution) var2[5] = var2[5]*alpha*alpha;
		if (withModels){
			result=result&&model1.setVar(var1);
			result=result&&model2.setVar(var2);
		}
		return result&&withModels;
	}
}
