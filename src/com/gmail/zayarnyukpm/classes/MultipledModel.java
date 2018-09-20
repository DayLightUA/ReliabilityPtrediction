package com.gmail.zayarnyukpm.classes;

import java.util.ArrayList;

public class MultipledModel extends Model{
	
	int[] var1Index;
	int[] var2Index;
	boolean varIndexesSeted = false;
	boolean modelsIsSeted=false;
	Model model1;
	Model model2;
	double var1[];
	double var2[];
	Integration integral;
	ArrayList additionalParamList;
	int startIndex=0;
	int tIndex;
	
	public MultipledModel(){
		var=new double[nParameters];
		for (int i =0; i<var.length; i++){
			var[i]=0;
		}
	}
	public MultipledModel(double[] var){
		super(var);
	}
	
	
	public double f(double t){
		if (var1Index[tIndex]>=0) var1[var1Index[tIndex]] = t;
		if (var2Index[tIndex]>=0) var2[var2Index[tIndex]] = t;
		setVars();
		double result = model1.f()*model2.f();
		return result;
	}
	public double f(){
		double result = f(var[tIndex]);
		return result;
	}
	
	public double d1f(double t){
		int i=0;
		double result = 0;
		if ( var1Index[tIndex]>=0 ) i=i++;
		if ( var2Index[tIndex]>=0 ) i=i+2;
		switch (i){
		case 0: return result;
		case 1: var1[var1Index[tIndex]] = t; 
			setVars();
			result = model1.d1f()*model2.f();
			return result;
		case 2: var2[var2Index[tIndex]] = t;
			setVars();
			result = model1.f()*model2.d1f();
			return result;
		case 3: var1[var1Index[tIndex]] = t; var2[var2Index[tIndex]] = t;
			setVars();
			result = model1.d1f()*model2.f()+model1.f()*model2.d1f();
			return result;
		}
		return result;
	}
	public double d1f(){
		return d1f(var[tIndex]);
	}
	
	public double d2f(double t){
		int i=0;
		double result = 0;
		if ( var1Index[tIndex]>=0 ) i=i++;
		if ( var2Index[tIndex]>=0 ) i=i+2;
		switch (i){
		case 0: return result;
		case 1: var1[var1Index[tIndex]] = t; 
			setVars();
			result = model1.d2f()*(model2.f());
			return result;
		case 2: var2[var2Index[tIndex]] = t;
			setVars();
			result = model1.f()*model2.d2f();
			return result;
		case 3: var1[var1Index[tIndex]] = t; var2[var2Index[tIndex]] = t;
			setVars();
			result = (model1.d2f()*model2.f())
					+(model1.f()*model2.d2f())
					+(model1.d1f()*model2.d1f())
					+(model1.d1f()*model2.d1f());
			return result;
		}
		return result;
	}
	public double d2f(){
		return d2f(var[tIndex]);
	}
	
	public double d3f(double t){
		int i=0;
		double result = 0;
		if ( var1Index[tIndex]>=0 ) i=i++;
		if ( var2Index[tIndex]>=0 ) i=i+2;
		switch (i){
		case 0: return result;
		case 1: var1[var1Index[tIndex]] = t; 
			setVars();
			result = model1.d3f()*model2.f();
			return result;
		case 2: var2[var2Index[tIndex]] = t;
			setVars();
			result = model1.f()*model2.d3f();
			return result;
		case 3: var1[var1Index[tIndex]] = t; var2[var2Index[tIndex]] = t;
			setVars();
			result = model1.d3f()*model2.f()
					+model1.f()*model2.d3f()
					+model1.d2f()*model2.d1f()
					+model1.d2f()*model2.d1f()
					+model1.d2f()*model2.d1f()
					+model1.d1f()*model2.d2f()
					+model1.d1f()*model2.d2f()
					+model1.d1f()*model2.d2f();
			return result;
		}
		return result;
	}
	
	private Model newModel(String modelName){
		return DiferentMethods.newModel(modelName, additionalParamList, startIndex, this);
	}
	
	public boolean setVar(double[] v){
		boolean result = false;
		if (v!=null){
			var=v;
			varIsSeted=true;
			result = true;
		} else result = false;
		if (modelsIsSeted&&varIndexesSeted) result=result&&setModelsVar();
		varIsSeted=result;
		return result;
	}
	
	private void setVars(){
		model1.setVar(var1);
		model2.setVar(var2);
	}
	
	private boolean setModelsVar() {
		var1 = new double[model1.getVar().length];
		var2 = new double[model2.getVar().length];

		if (varIndexesSeted){
			for (int i=0; i<var.length; i++){
				if (var1Index[i]>=0)var1[var1Index[i]]=var[i];
				if (var2Index[i]>=0)var2[var2Index[i]]=var[i];
			}	
			model1.setVar(var1);
			model2.setVar(var2);
			return true;
		} else if (var1.length+var2.length==var.length){
				for (int i=0; i<var1.length; i++){
					var1[i]=var[i];
					var2[i]=var[i+var1.length];
				}
				model1.setVar(var1);
				model2.setVar(var2);
				return true;
		} else return false;
	}
	
	public void setModels(Model m1, int[] varIndex1, Model m2, int[] varIndex2){
		model1=m1;
		model2=m2;
		var1Index=varIndex1;
		var2Index=varIndex2;
		modelsIsSeted = true;
		varIndexesSeted = true;
	}
	
	public Model getModel1(){
		return model1;
	}
	public Model getModel2(){
		return model2;
	}
}
