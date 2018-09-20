package com.gmail.zayarnyukpm.classes;

public class XeqYModel extends Model {

	public double f(double x){
		double result = x;
		return result;
	}

	public double f(){
		//System.out.println("Model var.length: "+var.length+"  varIndex: "+(var.length-1));
		double result = f(var[var.length-1]);
		return result;
	}
	
	public double d1f(double x){
		double result = 1;
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
	
	public double d4f(double x){
		double result = 0;
		return result;
	}
	public double d4f(){
		double result = d3f(var[var.length-1]);
		return result;
	}
	
	public double i1f(double x){
		double result = x*x/2;
		return result;
	}
	public double i1f(){
		double result = i1f(var[var.length-1]);
		return result;
	}
	
	public double i2f(double x){
		double result = x*x*x/6;
		return result;
	}
	public double i2f(){
		double result = i2f(var[var.length-1]);
		return result;
	}
	
	public double i3f(double x){
		double result = x*x*x*x/24;
		return result;
	}
	public double i3f(){
		double result = i3f(var[var.length-1]);
		return result;
	}
}
