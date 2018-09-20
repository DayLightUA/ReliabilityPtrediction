package com.gmail.zayarnyukpm.parametrDriftPrediction;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.gmail.zayarnyukpm.Prediction;
import com.gmail.zayarnyukpm.classes.CheckBox;
import com.gmail.zayarnyukpm.classes.DataPanel;
import com.gmail.zayarnyukpm.classes.DiferentMethods;
import com.gmail.zayarnyukpm.classes.GraphicCalcThread;
import com.gmail.zayarnyukpm.classes.GraphicPanel;
import com.gmail.zayarnyukpm.classes.ModelContainer;
import com.gmail.zayarnyukpm.classes.PointDouble;

public class DriftPrediction {
	
	public static DriftPrediction program;
	private Prediction whoCall;
	JFrame driftFrame;
	JFrame dialogFrame;
	JPanel mainPanel;
	Box uperBPanel;
	Box lowerBPanel;
	CheckBox checkBox;
	Box dialogBPanel;
	
	JButton loadData;
	JButton enterData;
	JButton saveData;
	JButton qzm;
	JButton tem;
	JButton ok;
	JButton cancel;
	
	GraphicPanel graph;
	DataPanel dataPanel;
	GraphicCalcThread t;
	
	ArrayList<JCheckBox> checkBoxList;
	ArrayList<ModelContainer<PointDouble>> curvesList;
	ArrayList<JTextField> parameterFieldsList;
	String[] modelNames = {"Exponential model", "Linear model", "Square model", "Cubic model"};
	String[] curveNames = {"m(t) ", "a1(t)", "a2(t)"};
	int fWidth = 1000;
	int fHeight = 600;
	
	double[] var = new double[10];
	
	boolean isEnterData = false;
	boolean isSaveAsked = false;
	boolean okEvent = false;
	boolean methodIsRuned=false;
	
	public DriftPrediction(Prediction whoCall) {
		this.whoCall=whoCall;
	}
	public DriftPrediction(){
		
	}

	public void go(){
		loadData = new JButton("Load Drift Parameters");
		enterData = new JButton("Enter Drift Parameters");
		saveData = new JButton("Save Drift Parameters");
		qzm = new JButton("Use Quantile Zone Method");
		tem = new JButton("Use Theory of Emmisions");
		
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		ok.addActionListener(new OkBListener());
		cancel.addActionListener(new CancelBListener());
		dialogBPanel = new Box(BoxLayout.X_AXIS);
		dialogBPanel.add(Box.createGlue());
		dialogBPanel.add(cancel);
		dialogBPanel.add(Box.createGlue());
		dialogBPanel.add(ok);
		dialogBPanel.add(Box.createGlue());
		
		graph = new GraphicPanel();
		
		loadData.addActionListener(new LoadDataBListener());
		enterData.addActionListener(new EnterDataBListener());
		saveData.addActionListener(new SaveDataBListener());
		qzm.addActionListener(new QzmBListener());
		tem.addActionListener(new TemBListener());
		
		uperBPanel = new Box(BoxLayout.X_AXIS);
		lowerBPanel = new Box(BoxLayout.X_AXIS);
		checkBox = new CheckBox(BoxLayout.Y_AXIS, modelNames, new CheckBListener());
		checkBoxList = checkBox.getCheckBoxList();
		
		uperBPanel.add(Box.createGlue());
		uperBPanel.add(enterData);
		uperBPanel.add(Box.createGlue());
		uperBPanel.add(loadData);
		uperBPanel.add(Box.createGlue());
		uperBPanel.add(saveData);
		uperBPanel.add(Box.createGlue());
		
		lowerBPanel.add(Box.createGlue());
		lowerBPanel.add(qzm);
		lowerBPanel.add(Box.createGlue());
		lowerBPanel.add(tem);
		lowerBPanel.add(Box.createGlue());
		
		driftFrame = new JFrame("Parameters Drift Prediction");
		driftFrame.getContentPane().add(BorderLayout.NORTH, uperBPanel);
		driftFrame.getContentPane().add(BorderLayout.WEST, checkBox);
		driftFrame.getContentPane().add(BorderLayout.CENTER, graph);
		driftFrame.getContentPane().add(BorderLayout.SOUTH, lowerBPanel);
		driftFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		driftFrame.setSize(fWidth, fHeight);
		driftFrame.setLocationRelativeTo(null);
		driftFrame.setVisible(true);
		
		dataPanel = new DataPanel(); 
		dataPanel.addElements();
		parameterFieldsList = dataPanel.getFieldsList();
		start();
	}
	
	public void enterData(){
		dialogFrame = new JFrame("Please Enter Proces Parameters");
		dialogFrame.getContentPane().add(BorderLayout.CENTER, dataPanel);
		dialogFrame.getContentPane().add(BorderLayout.SOUTH, dialogBPanel);
		dialogFrame.pack();
		dialogFrame.setLocationRelativeTo(null);
		dialogFrame.setVisible(true);
		isEnterData = true;
	}
	
	private void saveAsk(){
		dialogFrame = new JFrame("To Save Current Data");
		dialogFrame.getContentPane().add(BorderLayout.CENTER, dialogBPanel);
		dialogFrame.setSize(400, 150);
		dialogFrame.setLocationRelativeTo(null);
		dialogFrame.setVisible(true);
		isSaveAsked = true;
	}

	public void start(){
		t=new GraphicCalcThread();
		t.setParameters(curvesList, graph, dataPanel, checkBoxList, modelNames, curveNames);
		t.start();
	}
	
	public void setData(ArrayList<JTextField> data, ArrayList<JCheckBox> checkBoxes){
		if (checkBoxes==null) checkBoxes=checkBoxList;
		if (parameterFieldsList.size()==data.size() && checkBoxList.size()==checkBoxes.size()){
			parameterFieldsList=data;
			checkBoxList=checkBoxes;
		}
	}
	
	public void setVisible(boolean b){
		driftFrame.setVisible(b);
	}
	
	private void notifyThread(){
		synchronized (t) {
		    t.notify();
		}
	}
	
	public static void main(){
		if (program==null){
			program = new DriftPrediction();
			program.go();
		} else {
			DriftPrediction p = new DriftPrediction();
			useData(p);
			
		}
	}
	
	private static void useData(DriftPrediction p){
		p.checkBoxList=program.checkBoxList;
		p.parameterFieldsList=program.parameterFieldsList;
		p.var=program.var;
		try {program.finalize();} catch (Throwable e) {e.printStackTrace();}
		program=p;
	}
	
	
	
	private class LoadDataBListener implements ActionListener {
		public synchronized void actionPerformed(ActionEvent e){
			var = DiferentMethods.loadData(var);
			for (int i=0; i<var.length; i++){
				parameterFieldsList.get(i).setText(""+var[i]);
			}
			notifyThread();
		}
	}
	
	private class EnterDataBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if (var[0]!=0 || var[2]!=0 || var[3]!=0 || var[4]!=0 || var[5]!=0) 
			saveAsk(); else enterData();
		}
	}
	
	private class SaveDataBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			var = dataPanel.getData();
			DiferentMethods.saveData(var);
		}
	}
	
	private class QzmBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			checkBoxList = checkBox.getCheckBoxList();
			QuantileZoneMethod qzm;
			if (dataPanel!=null){
				parameterFieldsList = dataPanel.getFieldsList();
				qzm = new QuantileZoneMethod(checkBoxList, parameterFieldsList, program);
			} else {
				qzm = new QuantileZoneMethod(program);
				System.out.println("new QZM");
			}
			driftFrame.setVisible(false);
			qzm.go();
			methodIsRuned=true;
		}
	}
	
	private class TemBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			checkBoxList = checkBox.getCheckBoxList();
			TheoryOfEmmisionMethod tem;
			if (dataPanel!=null){
				parameterFieldsList = dataPanel.getFieldsList();
				tem = new TheoryOfEmmisionMethod(checkBoxList, parameterFieldsList, program);
			} else {
				tem = new TheoryOfEmmisionMethod(program);
				System.out.println("new TEM");
			}
			driftFrame.setVisible(false);
			tem.go();
			methodIsRuned=true;
		}
	}
	
	private class OkBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			dialogFrame.setVisible(false);
			dialogFrame = null;
			if (isSaveAsked){DiferentMethods.saveData(var); isSaveAsked=false; enterData();}
			if (isEnterData){
				var = dataPanel.getData();
				checkBoxList=checkBox.getCheckBoxList();
				parameterFieldsList=dataPanel.getFieldsList(); 
				notifyThread();
				isEnterData=false;
			}
		}
	}
	
	
	private class CancelBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if (isEnterData){isEnterData=false; dialogFrame.setVisible(false);
			dialogFrame = null;}
			if (isSaveAsked){isSaveAsked=false; dialogFrame.setVisible(false);
			dialogFrame = null; enterData();}
		}
	}
	
	public class CheckBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if (!methodIsRuned)notifyThread();
		}
	}
}
