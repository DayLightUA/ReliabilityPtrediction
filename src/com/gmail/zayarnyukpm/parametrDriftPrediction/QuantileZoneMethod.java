package com.gmail.zayarnyukpm.parametrDriftPrediction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.gmail.zayarnyukpm.classes.CheckBox;
import com.gmail.zayarnyukpm.classes.CubicModel;
import com.gmail.zayarnyukpm.classes.DataPanel;
import com.gmail.zayarnyukpm.classes.DiferentMethods;
import com.gmail.zayarnyukpm.classes.ExponentialModel;
import com.gmail.zayarnyukpm.classes.GraphicPanel;
import com.gmail.zayarnyukpm.classes.LinearModel;
import com.gmail.zayarnyukpm.classes.Model;
import com.gmail.zayarnyukpm.classes.ModelContainer;
import com.gmail.zayarnyukpm.classes.PointDouble;
import com.gmail.zayarnyukpm.classes.SquareModel;

public class QuantileZoneMethod {
	
	DriftPrediction startFrame;
	public JFrame qzmFrame;
	JCheckBox crospoints;
	JButton backB;
	JButton saveDataB;
	JButton saveResultB;
	JButton saveGraphB;
	JButton startB;
	DataPanel dataPanel;
	DataPanel resultPanel;
	GraphicPanel graph = new GraphicPanel();
	CheckBox modelCheckBox;
	CheckBox curveCheckBox;
	Box checkBox;
	Box uperPanel;
	Box lowerPanel;
	String [] parameterNames = {"m0", "m1", "msd", "k1", "k2", "u", "del1", "del2", "xi", "t"};
	String[] modelNames = {"Exponential model", "Linear model", "Square model", "Cubic model"};
	String[] curveNames = {"m(t) ", "a1(t)", "a2(t)"};
	ArrayList<JCheckBox> modelCheckBoxList;
	ArrayList<JCheckBox> curveCheckBoxList;
	ArrayList<JTextField> parameterFieldsList;
	ArrayList<PointDouble> crospointList;
	ArrayList<ModelContainer<PointDouble>> curvesList;
	double[] var = new double[10];
	double[] iterateResults;
	boolean backIsEnable=false;
	int fWidth = 1200;
	int fHeight = 700;
	
	ArrayList<Model> modelsList = new ArrayList<Model>();
	ArrayList<Color> colors = new ArrayList<Color>();
	
	QuantileZoneMethod(ArrayList<JCheckBox> modelCheckBoxList, ArrayList<JTextField> parameterFieldsList, DriftPrediction startFrame){
		this.modelCheckBoxList=modelCheckBoxList;
		this.parameterFieldsList=parameterFieldsList;
		this.startFrame=startFrame;
		backIsEnable=true;
	}
	
	QuantileZoneMethod(DriftPrediction startFrame){
		this.startFrame=startFrame;
	}
	
	QuantileZoneMethod(){
		
	}
	
	public void go(){
		
		qzmFrame = new JFrame("Prediction with Quantile Zone Method");
		
		if (modelCheckBoxList!=null) modelCheckBox = new CheckBox(BoxLayout.Y_AXIS, modelCheckBoxList);
		else {
			modelCheckBox = new CheckBox(BoxLayout.Y_AXIS, modelNames); 
			modelCheckBoxList = modelCheckBox.getCheckBoxList();
		}
		if (parameterFieldsList!=null) dataPanel = new DataPanel(parameterNames, parameterFieldsList);
		else dataPanel = new DataPanel(var);
		dataPanel.addElements();
		curveCheckBox = new CheckBox(BoxLayout.Y_AXIS, curveNames);
		curveCheckBoxList = curveCheckBox.getCheckBoxList();
		crospoints = new JCheckBox("Crospoints");
		checkBox = new Box(BoxLayout.Y_AXIS);
		checkBox.add(Box.createGlue());
		checkBox.add(crospoints);
		checkBox.add(Box.createGlue());
		checkBox.add(curveCheckBox);
		checkBox.add(Box.createGlue());
		
		backB = new JButton("<-Back");
		backB.setEnabled(backIsEnable);
		backB.addActionListener(new BackBListener());
		saveDataB = new JButton("Save Data");
		saveDataB.addActionListener(new SaveDataBListener());
		saveResultB = new JButton("Save Result");
		saveResultB.addActionListener(new SaveResultBListener());
		saveGraphB = new JButton("Save Graphics");
		saveGraphB.addActionListener(new SaveGraphBListener());
		startB = new JButton("Start");
		startB.addActionListener(new StartBListener());
		resultPanel = new DataPanel(modelNames, curveNames);
		
		uperPanel = new Box(BoxLayout.X_AXIS);
		uperPanel.add(Box.createGlue()); uperPanel.add(backB);
		uperPanel.add(Box.createGlue()); uperPanel.add(saveDataB);
		uperPanel.add(Box.createGlue()); uperPanel.add(saveResultB);
		uperPanel.add(Box.createGlue()); uperPanel.add(saveGraphB);
		uperPanel.add(Box.createGlue()); uperPanel.add(startB);
		uperPanel.add(Box.createGlue()); 
		
		lowerPanel = new Box(BoxLayout.X_AXIS);
		lowerPanel.add(Box.createGlue()); lowerPanel.add(modelCheckBox);
		lowerPanel.add(Box.createGlue()); lowerPanel.add(checkBox);
		lowerPanel.add(Box.createGlue()); lowerPanel.add(resultPanel);
		lowerPanel.add(Box.createGlue()); 
		
		qzmFrame = new JFrame("Parameters Drift Prediction");
		qzmFrame.getContentPane().add(BorderLayout.NORTH, uperPanel);
		qzmFrame.getContentPane().add(BorderLayout.WEST, dataPanel);
		qzmFrame.getContentPane().add(BorderLayout.CENTER, graph);
		qzmFrame.getContentPane().add(BorderLayout.SOUTH, lowerPanel);
		qzmFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		qzmFrame.setSize(fWidth, fHeight);
		qzmFrame.setLocationRelativeTo(null);
		qzmFrame.setVisible(true);
	}
	
	private void start(){
		double xMind=0;
		double xMaxd=calcMarges();
		int nPoints=graph.getMax();
		graph.setMaxX(xMind, xMaxd);
		double stap=xMaxd/nPoints;
		crospointList = new ArrayList<PointDouble>();
		curvesList=DiferentMethods.calcModels(stap, nPoints, var, modelCheckBoxList, modelNames,
				curveCheckBoxList, curveNames, iterateResults, crospointList);
		System.out.println("curvesListSize = "+curvesList.size());
		graph.addConvertPoints(curvesList);
		if (crospoints.isSelected()){
			graph.addCrospoints(crospointList, crospoints.isSelected(), false);
		} else graph.setCrospointsOff();
		graph.repaint();
	}
	
	private double calcMarges(){
		double mt=0;
		double counter=0;
		double Dt=0;
		for (int i=0; i<iterateResults.length; i++){
			if (iterateResults[i]>0){
				mt = mt+ iterateResults[i];
				counter = counter+1;
			}
		}
		if (counter!=0) mt = mt/counter; else mt=0;
		counter=0;
		for (int i=0; i<iterateResults.length; i++){
			if (iterateResults[i]>0){
				Dt = Dt+(iterateResults[i]-mt)*(iterateResults[i]-mt);
				counter = counter+1;
			}
		} 
		if (counter!=0) Dt = Math.sqrt(Dt)/counter; else Dt=0;;
		return mt+3*Dt;
	}
	
	private class BackBListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			qzmFrame.setVisible(false);
			startFrame.setData(dataPanel.getFieldsList(), modelCheckBox.getCheckBoxList());
			startFrame.setVisible(true);
			qzmFrame.dispose();
		}
	}
	
	private class SaveDataBListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			var=dataPanel.getData();
			DiferentMethods.saveData(var);
		}
	}
	
	private class SaveResultBListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			ArrayList<JTextField> results;
			results = resultPanel.getFieldsList();
			DiferentMethods.saveResult(results, curveNames);
		}
	}
	
	private class SaveGraphBListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			DiferentMethods.saveGraphic(graph);
		}
	}
	
	private class StartBListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			var = dataPanel.getData();
			iterateResults=DiferentMethods.iterateCalculations(var);
			resultPanel.setData(iterateResults);
			resultPanel.addElements();
			start();
		}
	}
}
