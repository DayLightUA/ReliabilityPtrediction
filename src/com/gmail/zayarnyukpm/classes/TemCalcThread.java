package com.gmail.zayarnyukpm.classes;

import java.math.BigDecimal;

import com.gmail.zayarnyukpm.parametrDriftPrediction.TheoryOfEmmisionMethod;

public class TemCalcThread extends Thread {

	GraphicPanel graph;
	DataPanel resultPanel;
	DataPanel allowLeveles;
	MultipledModel forN_TModel;
	MultipledModel forT_TModel;
	int tIndex;
	int xIndex;
	int vIndex;
	Integration intN_T;
	Integration intN_V;
	Integration intT;
	TheoryOfEmmisionMethod startProces;
	double NTResult;
	double TTResult;
	
	public TemCalcThread(TheoryOfEmmisionMethod startProces){
		super();
		this.startProces=startProces;
	}
	
	public void start(){
		BigDecimal[] tempVar;
		
		intN_V = new Integration();
		intN_V.setName("intN_V = Integration");
		intN_V.setModel(forN_TModel);
		intN_V.setLeveles(0, ((MultipledModel)forN_TModel.getModel2()).getModel2().f(), vIndex, -1);
		
		intN_T = new Integration();
		intN_T.setName("intN_T = Integration");
		intN_T.setModel(intN_V);
		intN_T.setLeveles(0, forN_TModel.getVar()[tIndex], tIndex, -1);
		
		tempVar=forN_TModel.getVar();
		tempVar[xIndex] = allowLeveles.getData()[0];
		
		NTResult = DiferentMethods.iterateCalculation(new BigDecimal(allowLeveles.getData()[0]), intN_T.f(), intN_V.f(), null, null, 1);
		System.out.println("Result = "+NTResult);
		
		
	}
	
	public void setResultPanels(GraphicPanel graph, DataPanel resultPanel, DataPanel allowLeveles){
		if (graph!=null) this.graph=graph; else graph=new GraphicPanel();
		this.resultPanel=resultPanel; 
		this.allowLeveles=allowLeveles;
	}
	
	public void setVarIndexes(int tIndex, int xIndex, int vIndex){
		this.tIndex=tIndex;
		this.xIndex=xIndex;
		this.vIndex=vIndex;
	}
	
	public void setModel(MultipledModel forN_TModel){
		this.forN_TModel = forN_TModel;
		forT_TModel=(MultipledModel) forN_TModel.getModel2();
	}
}
