package com.gmail.zayarnyukpm.classes;

import java.awt.Color;

public class PointDouble {
	double x;
	double y;
	Color color;
	
	public PointDouble(){
		x=0; y=0; color=Color.BLACK;
	}
	
	public PointDouble(double x, double y){
		this.x=x; this.y=y; color=Color.BLACK;
	}
	public PointDouble(double x, double y, Color color){
		this.x=x; this.y=y; this.color=color;
	}
}
