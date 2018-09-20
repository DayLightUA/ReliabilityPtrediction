package com.gmail.zayarnyukpm.classes;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JTextField;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class DefCalcThread extends Thread{
	Box graphicBox;
	JTextField timeField;
	JTextField pDefField;
	JTextField pFField;
	JTextField fRaiteField;
	JTextField resultField;
	double time, pDef, pF, fRaite, delta;
	double precision = 0.0000001;
	double xMin, xMax;
	double yMin, yMax;
	double marge11, marge12, marge21, marge22;
	Model calcM;
	Model nCalcM;
	GraphicPanel graph1;
	GraphicPanel graph2;
	double[] var1 = {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
	double[] var2 = {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0};
	double[] values = new double[4];
	String[] valNames = new String[4];
	int nPoints = 20;
	Color[] colors = { new Color(255, 0, 0), new Color(255, 166, 0), new Color(255, 235, 41),
			new Color(0, 222, 0), new Color(0, 173, 0), new Color(0, 199, 255),
			new Color(0, 64, 194), new Color(176, 0, 105), new Color(170, 95, 0),
			new Color(0, 0, 0)};
	String[] pName = {"Pdef", "Pf" , "t", "\u03BB"};
	
	
	public DefCalcThread(Box graphicBox, JTextField timeField, JTextField pDefField,
			JTextField pFField, JTextField fRaiteField, GraphicPanel graph1, GraphicPanel graph2){
		this.timeField=timeField;
		this.pDefField=pDefField;
		this.pFField=pFField;
		this.fRaiteField=fRaiteField;
		
		boolean haveUnknownParameter=false;
		boolean i =true;
		while (i){
			try {
				time = Double.parseDouble(timeField.getText());
			} catch (NumberFormatException ae){
				time = -1;
				haveUnknownParameter=true;
			}
			try {
				pDef = Double.parseDouble(pDefField.getText());
			} catch (NumberFormatException ae){
				pDef = -1;
				if (haveUnknownParameter) break;
				else haveUnknownParameter=true;
			}
			try {
				pF = Double.parseDouble(pFField.getText());
			} catch (NumberFormatException ae){
				pF = -1;
				if (haveUnknownParameter) break;
				else haveUnknownParameter=true;
			}
			try {
				fRaite = Double.parseDouble(fRaiteField.getText());
			} catch (NumberFormatException ae){
				fRaite = -1;
				if (haveUnknownParameter){
					break;
				}	else haveUnknownParameter=true;
			}
			i=false;
		}
		
		this.graphicBox=graphicBox;
		this.graph1=graph1;
		this.graph2=graph2;
	}
	
	public void start(){
		if (pF>=0 && pDef>=0){
			calcM = new LinearModel();
			nCalcM = new ExponentialModel();
			//var2[0]=-1;
			//var2[1]=1;
			values[0] = pF;
			values[1] = pDef;
			valNames[0] = "Pf";
			valNames[1] = "Pdef";
			if (pDef>(1-pDef)) marge12 = (1-pDef)/5;
				else marge12 = pDef/5;
			if (pF>(1-pF)) marge11 = (1-pF)/5;
				else marge11 = pF/5;
			if (time>=0){
				values[2] = time;
				values[3] = fRaite = 1;
				marge21 = time*0.1;
				resultField=fRaiteField;
				valNames[2] = "t";
				valNames[3] = "\u03BB";
			} else{
				values[3] = time = 1;
				precision = 0.1;
				marge21 = fRaite*0.1;
				values[2] = fRaite;
				resultField=timeField;
				valNames[3] = "t";
				valNames[2] = "\u03BB";
			}
			calculate();
		} else if (time>=0 && fRaite>=0){
			calcM = new LinearModel();
			nCalcM = new ExponentialModel();
			var1[0]=-1;
			var1[1]=1;
			values[0] = fRaite;
			values[1] = time;
			valNames[1] = "t";
			valNames[0] = "\u03BB";
			marge11 = fRaite*0.1;
			marge12 = time*0.1;
			if (pF>=0){
				values[2] = pF;
				values[3] = pDef = 0.5;
				valNames[2] = "Pf";
				valNames[3] = "Pdef";
				if (pF>(1-pF)) marge21 = (1-pF)/5;
					else marge21 = pF/5;
				resultField=pDefField;
			} else{
				values[3] = pF = 0.5;
				values[2] = pDef;
				if (pDef>(1-pDef)) marge21 = (1-pDef)/5;
					else marge21 = pDef/5;
				resultField=pFField;
				valNames[3] = "Pf";
				valNames[2] = "Pdef";
			}
			calculate();
		}
	}
	
	private void calculate(){
		var1[3] = values[0];
		var1[9] = values[1];
		var2[3] = values[2];
		calcM.setVar(var1);
		delta = calcM.f();
		var2[6] = delta;
		nCalcM.setVar(var2);
		var2[9] =var2[9]+DiferentMethods.iterateCalculation(var2[6], nCalcM.f(), 
				nCalcM.d1f(), nCalcM.d2f(), nCalcM.d3f(), precision);
		System.out.println("var29="+var2[9]);
		
		
		
		values[3] = var2[9];
		System.out.println("val0: "+values[0]+"|| val1: "+values[1]+"|| val2: "+values[2]+"|| val3: "+values[3]);
		resultField.setText(""+values[3]);
		calcMarges();
		graph1.addConvertPoints(calcCurves(true));
		graph1.setAxis(valNames[1]);
		graph2.addConvertPoints(calcCurves(false));
		graph2.setAxis(valNames[3]);
		graph1.repaint();
		graph2.repaint();
	}
	
	private void calcMarges(){
		yMin = Double.MAX_VALUE;
		yMax = -Double.MAX_VALUE;
		calcMinMax();
		calcX();		
	}
	
	private void calcMinMax(){
		double[] val1 = new double[4];
		val1[0] = values[0]-marge11;
		val1[1] = values[0]-marge11;
		val1[2] = values[0]+marge11;
		val1[3] = values[0]+marge11;
		double[] val2 = new double[4];
		val2[0] = values[1]-marge12;
		val2[1] = values[1]+marge12;
		val2[2] = values[1]-marge12;
		val2[3] = values[1]+marge12;
		for (int i=0; i<4; i++){
			var1[3]=val1[i];
			var1[9]=val2[i];
			calcM.setVar(var1);
			double result = calcM.f();
			if (yMin > result) yMin = result;
			if (yMax < result) yMax = result;
		}
		System.out.println("yMin: "+yMin+"|| yMax: "+yMax);
	}
	
	private void calcX(){
		double[] m = new double[4];
		xMin = -Double.MAX_VALUE;
		xMax = Double.MAX_VALUE;
		var2[3]=values[2]+marge21;
		nCalcM.setVar(var2);
		m[0] = DiferentMethods.iterateCalculation(yMin, nCalcM.f(), 
				nCalcM.d1f(), nCalcM.d2f(), nCalcM.d3f(), precision);
		m[1] = DiferentMethods.iterateCalculation(yMax, nCalcM.f(), 
				nCalcM.d1f(), nCalcM.d2f(), nCalcM.d3f(), precision);
		m[0]=m[0]+var2[9];
		m[1]=m[1]+var2[9];
		var2[3]= values[2]-marge21;
		nCalcM.setVar(var2);
		m[2] = DiferentMethods.iterateCalculation(yMin, nCalcM.f(), 
				nCalcM.d1f(), nCalcM.d2f(), nCalcM.d3f(), precision);
		m[3] = DiferentMethods.iterateCalculation(yMax, nCalcM.f(), 
				nCalcM.d1f(), nCalcM.d2f(), nCalcM.d3f(), precision);
		m[2]=m[2]+var2[9];
		m[3]=m[3]+var2[9];
		for (int i=0; i<4; i++){
			System.out.println("m["+i+"]: "+m[i]);
			if (m[i]>var2[9] && m[i]<xMax) xMax = m[i];
			if (m[i]<var2[9] && m[i]>xMin) xMin = m[i];
		}
		
		System.out.println("xMin: "+xMin+"|| xMax: "+xMax);
		
	}
	
	private ArrayList<ModelContainer<PointDouble>> calcCurves(boolean isFirstModel){
		ArrayList<ModelContainer<PointDouble>> curvesList = new ArrayList<ModelContainer<PointDouble>>();
		double[] var;
		String val0Name, val1Name;
		Model model;
		double xMin, xMax, lineEnd, yMarge;
		if (isFirstModel) {
			var=var1;
			var[3]=values[0];
			model = calcM;
			xMin = values[1]-marge12;
			xMax = values[1]+marge12;
			yMarge = marge11;
			lineEnd = xMax;
			val0Name = valNames[0];
			val1Name = valNames[1];
		}else{
			var=var2;
			var[3]=values[2];
			model = nCalcM;
			xMin = this.xMin;
			xMax = this.xMax;
			yMarge = marge21;
			lineEnd = xMin;
			val0Name = valNames[2];
			val1Name = valNames[3];
		}
		double yStep = yMarge/4;
		var[3]= var[3]-yMarge;
		for (int i=0; i<9; i++){
			ModelContainer<PointDouble> mc = new ModelContainer<PointDouble>(
					val0Name+"_"+new String(DiferentMethods.chars((var[3]+""), 5)));
			double xStep = (xMax-xMin)/(nPoints-1);
			var[9]=xMin;
			PointDouble p;
			for (int j=0; j<nPoints; j++){
				model.setVar(var);
				p = new PointDouble (var[9], model.f(), colors[i]);
				System.out.println("x="+p.x+" ||y="+p.y+" ||col"+i);
				mc.add(p);
				var[9]= var[9]+xStep;
			}
			curvesList.add(mc);
			var[3]+=yStep;
		}
		double val0, val1;
		if (isFirstModel){
			var[3] = values[0];
			var[9] = values[1];
			val0 = values[0];
			val1 = values[1];
		}else{
			var[3]=values[2];
			var[9] = values[3];
			val0 = values[2];
			val1 = values[3];
		}
		model.setVar(var);
		double y = model.f();
		System.out.println("y="+y);
		ModelContainer<PointDouble> r1c = new ModelContainer<PointDouble>(
				val1Name+"="+new String(DiferentMethods.chars((val1+""), 5)));
		r1c.add(new PointDouble(val1, yMin, colors[9]));
		r1c.add(new PointDouble(val1, y, colors[9]));
		curvesList.add(r1c);
		ModelContainer<PointDouble> r2c = new ModelContainer<PointDouble>(
				val0Name+"="+new String(DiferentMethods.chars((val0+""), 5)));
		r2c.add(new PointDouble(val1, y, colors[9]));
		r2c.add(new PointDouble(lineEnd, y, colors[9]));
		curvesList.add(r2c);
		
		return curvesList;
	}
	
}
