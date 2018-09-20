package com.gmail.zayarnyukpm.classes;

import java.math.BigDecimal;
import java.math.MathContext;

public class Model implements Cloneable {
	
	protected double[] var;
	protected boolean varIsSeted=false;
	protected int nParameters=1;
	protected String name="null";
	
	
	public Model(){
		var=new double[nParameters];
		for (int i =0; i<var.length; i++){
			var[i]=0;
		}
	}	
	
	public Model(double[] var){
		if (var.length!=0){
			this.var=var;
			nParameters=var.length;
			varIsSeted=true;
		} else System.out.println("Created zero model");
	}
	
	public double f(double x){
		double result = x;
		return result;
	}
	public double f(){
		double result = f(var[var.length-1]);
		return result;
	}
	
	public double d1f(double x){
		double result = 0;
		return result;
	}
	public double d1f(){
		double result = d1f(var[var.length-1]);
		return result;
	}
	
	public double d2f(double x){
		double result = 0;
		return result;
	}
	public double d2f(){
		double result = d2f(var[var.length-1]);
		return result;
	}
	
	public double d3f(double x){
		double result = 0;
		return result;
	}
	public double d3f(){
		double result = d3f(var[var.length-1]);
		return result;
	}
	
	
	
	
	public double i1f(double x){
		double result = x;
		return result;
	}
	public double i1f(){
		double result = i1f(var[var.length-1]);
		return result;
	}
	
	
	public double i2f(double x){
		double result = x;
		return result;
	}
	public double i2f(){
		double result = i2f(var[var.length-1]);
		return result;
	}
	
	
	public double i3f(double x){
		double result = x;
		return result;
	}
	public double i3f(){
		double result = i3f(var[var.length-1]);
		return result;
	}
	
	public double[] getVar(){
		return var;
	}
	
	public boolean setVar(double[] var){
		if (var.length==nParameters){
			this.var=var; 
			varIsSeted=true;
			return true;
		}else return true;
		
	}
	
	public Model cloneModel(){
		try {
			return (Model) clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void setName(String name){
		if (name!=null) this.name=name;
		else this.name="null";
	}
	
	public String getName(){
		return name;
	}
}
