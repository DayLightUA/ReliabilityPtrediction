package com.gmail.zayarnyukpm.classes;

import java.util.ArrayList;

import javax.swing.JCheckBox;

public class GraphicCalcThread extends Thread{
	
	ArrayList<ModelContainer<PointDouble>> curvesList;
	GraphicPanel graph;
	double[] var, iterateResults;
	ArrayList<JCheckBox> modelCheckBoxList,curveCheckBoxList;
	String[] modelNames, curveNames; 
	ArrayList<PointDouble> crospointList;
	DataPanel dataPanel;
	public volatile boolean isRuning;
	

	
	public void run(){
		isRuning=true;
		while (isRuning){
			System.out.println("Tread2 is wait");
			thisWait();
			System.out.println("Tread2 stop wait");
			calc();
		}
	}
	
	private synchronized void thisWait(){
		try {
			wait();
		} catch (InterruptedException e) {
			return;
		}
	}
	
	public synchronized void calc(){
		double xMind=0;
		double xMaxd=1;
		double maxTime=Math.pow(10, 12);
		double maxMarge=0; 
		double minMarge=0;
		int i = 0;
		var = dataPanel.getData();
		if(var[6]>var[7]){maxMarge=var[6];minMarge=var[7];} else {maxMarge=var[7];minMarge=var[6];}
		boolean needMore = true;
		int nPoints=graph.getMax();
		while (needMore){
			var = dataPanel.getData();
			graph.setMaxX(xMind, xMaxd);
			double stap=xMaxd/nPoints;
			curvesList=DiferentMethods.calcModels(stap, nPoints, var, 
					modelCheckBoxList, modelNames, curveCheckBoxList, 
					curveNames, iterateResults , crospointList);
			needMore=needMore(curvesList, minMarge, maxMarge);
			graph.addConvertPoints(curvesList);
			graph.repaint();
			xMaxd=2*xMaxd;
			if(xMaxd>=maxTime)break;
			i++;
			System.out.println("i="+i+"  xMaxd="+xMaxd);
		}
	}
	
	private boolean needMore(ArrayList<ModelContainer<PointDouble>> curveList, double minMarge, double maxMarge){
		boolean result = true;
		int counter=0;
		for (ModelContainer<PointDouble> curve:curveList){
			if (hadCrosed(curve, minMarge, maxMarge)) counter++;
		}
		if (counter>=(curveList.size()-2)*2/3) result=false; else result=true;
		return result;
	}
	
	public static boolean hadCrosed(ModelContainer<PointDouble> curve, double minMarge, double maxMarge){
		boolean result = false;
		if (curve!=null) for (int i=(curve.size()-1); i>=0; i--){
			double y = curve.get(i).y;
			if (y>maxMarge || y<minMarge) { result=true; System.out.println("Have a result: p.y="+y+" minMarge="+minMarge+"  maxMarge="+maxMarge); return result;}
			else {result=false; System.out.println("noCrosed, i="+i);}
		} else {result=false; System.out.println("curve is null");}
		return result;
	}
	
	public void setParameters(ArrayList<ModelContainer<PointDouble>> curvesList, GraphicPanel graph,
			DataPanel dataPanel, ArrayList<JCheckBox> modelCheckBoxList, String[] modelNames, 
			String[] curveNames){
		this.curvesList=curvesList;
		this.graph=graph;
		this.dataPanel=dataPanel;
		this.modelCheckBoxList=modelCheckBoxList;
		this.modelNames=modelNames;
		this.curveNames=curveNames;
	}
	
	public void setParameters(ArrayList<ModelContainer<PointDouble>> curvesList, GraphicPanel graph, DataPanel dataPanel, double[] iterateResults, 
			ArrayList<JCheckBox> modelCheckBoxList,ArrayList<JCheckBox> curveCheckBoxList, String[] modelNames, String[] curveNames, ArrayList<PointDouble> crospointList ){
		this.curvesList=curvesList;
		this.graph=graph;
		this.dataPanel=dataPanel;
		this.iterateResults=iterateResults;
		this.modelCheckBoxList=modelCheckBoxList;
		this.curveCheckBoxList=curveCheckBoxList;
		this.modelNames=modelNames;
		this.curveNames=curveNames; 
		this.crospointList=crospointList;
	}
	

	
}
