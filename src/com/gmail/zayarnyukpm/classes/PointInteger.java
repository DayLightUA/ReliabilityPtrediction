package com.gmail.zayarnyukpm.classes;

import java.awt.Color;

public class PointInteger extends PointDouble{
	int x;
	int y;
	Color color;
	
	PointInteger(){
		x=0; y=0; color=Color.BLACK;
	}
	
	PointInteger(int x, int y){
		this.x=x; this.y=y; color=Color.BLACK;
	}
	PointInteger(int x, int y, Color color){
		this.x=x; this.y=y; this.color=color;
	}

}
