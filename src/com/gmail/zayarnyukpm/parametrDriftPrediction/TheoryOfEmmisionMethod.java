package com.gmail.zayarnyukpm.parametrDriftPrediction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.gmail.zayarnyukpm.classes.CheckBox;
import com.gmail.zayarnyukpm.classes.Constants;
import com.gmail.zayarnyukpm.classes.DataPanel;
import com.gmail.zayarnyukpm.classes.DiferentMethods;
import com.gmail.zayarnyukpm.classes.GausianModel;
import com.gmail.zayarnyukpm.classes.GraphicPanel;
import com.gmail.zayarnyukpm.classes.Model;
import com.gmail.zayarnyukpm.classes.ModelContainer;
import com.gmail.zayarnyukpm.classes.MultipledModel;
import com.gmail.zayarnyukpm.classes.PointDouble;
import com.gmail.zayarnyukpm.classes.TemCalcThread;


public class TheoryOfEmmisionMethod {

	DriftPrediction startFrame;
	
	public JFrame temFrame;

	JButton backB;
	JButton saveDataB;
	JButton saveResultB;
	JButton saveGraphB;
	JButton startB;
	JButton modelsPreferencesB;
	
	DataPanel dataPanelProc;
	DataPanel dataPanelAL;
	DataPanel resultPanel;
	DataPanel specParamPanel;
	JPanel comboBoxPanel;
	
	GraphicPanel graph = new GraphicPanel(800, 600);
	
	JComboBox<String> driftModelJCB;
	JComboBox<String> distributionLowXJCB;
	JComboBox<String> distributionLowVJCB;
	//JCheckBox crospoints;
	CheckBox curveCheckBox;
	
	Box resultBox;
	Box uperBox;
	Box chuseModelsBox;
	Box leftBox;
	
	String[] parameterNames = {"m0", "m1", "msd", "k1", "k2", "u", "del1", "del2", "xi", "t"};
	String[] driftModelNames = {Constants.EXPONENTIAL_MODEL, Constants.LINEAR_MODEL, 
			Constants.SQUARE_MODEL, Constants.CUBIC_MODEL, Constants.SPECIAL_LOW_MODEL};
	String[] distributionModelNames = {Constants.GAUSIAN_MODEL, Constants.RECTANGULAR_MODEL,
			Constants.TRIANGULAR_MODEL, Constants.VEIBUL_MODEL, Constants.GRAM_SHARLE_MODEL};
	String[] curveNames = {"n(t) ", "ta(t)", "tvyk(t)"};
	String[] resultLabel = {"time"};
	String[] al = {"al_n", "al_ta", "al_tvyk"};
	String[] alSetup = {"Enter allow leveles"};
	String[] specParamColumnNames = {"1", "2"};
	String[] specParamNames = {"Asimetry", "Excesus", "alpha", "betta"};
	
	ArrayList<JCheckBox> modelCheckBoxList;
	ArrayList<JCheckBox> curveCheckBoxList;
	ArrayList<JTextField> parameterFieldsList;
	ArrayList<JTextField> specParamFieldList;
	ArrayList<ModelContainer<PointDouble>> curvesList;
	ArrayList<Model> driftModelsList = new ArrayList<Model>();
	ArrayList<Model> distributionModelList = new ArrayList<Model>();
	ArrayList<Color> colors = new ArrayList<Color>();

	boolean backIsEnable=false;
	int fWidth = 1200;
	int fHeight = 700;
	
	double[] var;
	
	int fulVarLength=21;
	
	int[] var1Indexes={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 0};
	int[] var2Indexes={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	int[] additVar1Indexes={0,1,2,3,4,5,6,7,8,9,10,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	int[] additVar2Indexes={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,10,9,8,7,6,5,4,3,2,1,0};
	
	int xIndex = 0;
	int vIndex = 20;
	int tIndex = 10;

	TheoryOfEmmisionMethod(ArrayList<JCheckBox> modelCheckBoxList, ArrayList<JTextField> parameterFieldsList, DriftPrediction startFrame){
		this.modelCheckBoxList=modelCheckBoxList;
		this.parameterFieldsList=parameterFieldsList;
		this.startFrame=startFrame;
		backIsEnable=true;
	}
	
	TheoryOfEmmisionMethod(DriftPrediction startFrame){
		this.startFrame=startFrame;
	}
	
	TheoryOfEmmisionMethod(){
		
	}
	
	public void go(){
		temFrame = new JFrame("Prediction with Theory of Emmision Method");
		
		if (parameterFieldsList!=null) dataPanelProc = new DataPanel(parameterNames, parameterFieldsList);
		else dataPanelProc = new DataPanel();		
		dataPanelProc.addElements();
		
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
		modelsPreferencesB = new JButton("M P");
		modelsPreferencesB.addActionListener(new ModelsPreferencesBListener());
		
		uperBox = new Box(BoxLayout.X_AXIS);
		uperBox.add(Box.createGlue()); uperBox.add(backB);
		uperBox.add(Box.createGlue()); uperBox.add(saveDataB);
		uperBox.add(Box.createGlue()); uperBox.add(saveResultB);
		uperBox.add(Box.createGlue()); uperBox.add(saveGraphB);
		uperBox.add(Box.createGlue()); uperBox.add(startB);
		uperBox.add(Box.createGlue()); 
		
		comboBoxPanel = new JPanel(new GridBagLayout());
		comboBoxPanel.setBorder(new LineBorder(Color.BLACK));
		driftModelJCB = new JComboBox<String>(driftModelNames);
		distributionLowXJCB = new JComboBox<String>(distributionModelNames);
		distributionLowVJCB = new JComboBox<String>(distributionModelNames);
		JTextField modelTF = new JTextField("Chuse drift model");
		JTextField distributionTF = new JTextField("Chuse x & v distributions");
		specParamFieldList = new ArrayList<JTextField>();
		for (int i=0; i<specParamNames.length; i++){
			JTextField tf = new JTextField(specParamNames[i]);
			specParamFieldList.add(tf);
		}
		if (specParamNames.length%specParamColumnNames.length>0){
			JTextField tf = new JTextField("0");
			tf.setEditable(false);
			specParamFieldList.add((tf));
		}
		specParamPanel = new DataPanel(specParamColumnNames, specParamColumnNames, specParamFieldList);
		specParamPanel.addElements();
		modelTF.setBorder(new LineBorder(Color.BLACK));
		distributionTF.setBorder(new LineBorder(Color.BLACK));
		modelTF.setEditable(false);
		distributionTF.setEditable(false);
		addElemToGridBagPanel(comboBoxPanel, modelTF, 0, 0, 2, 1);
		addElemToGridBagPanel(comboBoxPanel, distributionTF, 0, 1, 2, 2);
		addElemToGridBagPanel(comboBoxPanel, driftModelJCB, 2, 0, 5, 1);
		addElemToGridBagPanel(comboBoxPanel, distributionLowXJCB, 2, 1, 4, 1);
		addElemToGridBagPanel(comboBoxPanel, distributionLowVJCB, 2, 2, 4, 1);
		addElemToGridBagPanel(comboBoxPanel, modelsPreferencesB, 6, 1, 1, 2);
		addElemToGridBagPanel(comboBoxPanel, specParamPanel, 0, 3, specParamColumnNames.length+1, specParamColumnNames.length+1);
		
		resultPanel = new DataPanel(curveNames,resultLabel);
		resultPanel.setFieldLength(5, 8);
		resultPanel.setSize(200, 100);
		resultPanel.addElements();
		dataPanelAL = new DataPanel(al,alSetup);
		dataPanelAL.setFieldLength(5, 10);
		dataPanelAL.setSize(200, 100);
		dataPanelAL.addElements();
		
		resultBox = new Box(BoxLayout.X_AXIS);
		resultBox.add(dataPanelAL); 
		resultBox.add(resultPanel);
		
		leftBox = new Box(BoxLayout.Y_AXIS); 
		leftBox.add(dataPanelProc);
		leftBox.add(comboBoxPanel);
		leftBox.add(resultBox);
		leftBox.add(new JPanel());
		
		graph.setBorder(new LineBorder(Color.BLACK));
		
		temFrame = new JFrame("Parameters Drift Prediction");
		temFrame.getContentPane().add(BorderLayout.NORTH, uperBox);
		temFrame.getContentPane().add(BorderLayout.WEST, leftBox);
		temFrame.getContentPane().add(BorderLayout.CENTER, graph);
		temFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		temFrame.pack();
		temFrame.setLocationRelativeTo(null);
		temFrame.setVisible(true);
	}
	
	private synchronized void start() {
		String selectedDriftModel = (String) driftModelJCB.getSelectedItem();
		String selectedXDistribution = (String) distributionLowXJCB.getSelectedItem();
		String selectedVDistribution = (String) distributionLowVJCB.getSelectedItem();
		
		double[] driftProcVar = dataPanelProc.getData();
		double[] allowableLeveles = dataPanelAL.getData();
		double[] specialParam = specParamPanel.getData();
		
		double [] fulVar = new double[fulVarLength];
		for (int i=0; i<10; i++){
			fulVar[i+1]=driftProcVar[i];
			fulVar[fulVarLength-2-i]=driftProcVar[i];
		}
		fulVar[0]=0;
		fulVar[20]=0;
			
		MultipledModel forN_TModel = new MultipledModel(fulVar);
		forN_TModel.setName("forN_TModel = MultipledModel");
		MultipledModel forT_TModel = new MultipledModel(fulVar);
		forT_TModel.setName("forT_TModel = MultipledModel");
		GausianModel distribXModel = (GausianModel) DiferentMethods.newModel(selectedXDistribution, null, 0, this);
		distribXModel.setName("distribXModel = GausianModel");
		GausianModel distribVModel = (GausianModel) DiferentMethods.newModel(selectedVDistribution, null, 0, this);
		distribVModel.setName("distribVModel = GausianModel");
		Model driftXModel = DiferentMethods.newModel(selectedDriftModel, null, 0, this);
		driftXModel.setName("driftXModel = Model");
		Model driftVModel = DiferentMethods.newModel(selectedDriftModel, null, 0, this);
		driftVModel.setName("driftVModel = Model");
		Model vModel = DiferentMethods.newModel(Constants.XeqY_MODEL, null, 0, this);
		vModel.setName("vModel = Model");
		
		
		distribXModel.setModels(driftXModel);
		distribVModel.setModels(driftVModel);
		distribVModel.setAlphaAndBetta(specialParam[2], specialParam[3]);
		forT_TModel.setModels(distribXModel, additVar1Indexes, distribVModel, additVar2Indexes);
		forN_TModel.setModels(vModel, var1Indexes, forT_TModel, var2Indexes);
		
		forN_TModel.setVar(fulVar);
		
		
		TemCalcThread calcThread = new TemCalcThread(this);
		calcThread.setResultPanels(graph, resultPanel, dataPanelAL);
		calcThread.setModel(forN_TModel);
		calcThread.setVarIndexes(tIndex, xIndex, vIndex);
		calcThread.start();
		
		
	}
	
	private void addElemToGridBagPanel(JPanel panel, Component component, int x_pos, int y_pos, int gridwidth, int gridheight){
		GridBagLayout gbl;
		GridBagConstraints c = new GridBagConstraints();
		if (panel.getLayout() instanceof GridBagLayout){
			gbl = (GridBagLayout) panel.getLayout();
		} else panel.setLayout(gbl = new GridBagLayout());
		c.fill = GridBagConstraints.BOTH;
		c.gridx = x_pos; c.gridy = y_pos;
		c.gridwidth = gridwidth; c.gridheight = gridheight;
		gbl.setConstraints(component, c);
		panel.add(component);
	}
	
	private class BackBListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			temFrame.setVisible(false);
			startFrame.setData(dataPanelProc.getFieldsList(), null);
			startFrame.setVisible(true);
			temFrame.dispose();
		}
	}
	
	private class SaveDataBListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			var=dataPanelProc.getData();
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
			start();
		}
	}
	
	private class ModelsPreferencesBListener implements ActionListener{
		public void actionPerformed (ActionEvent e){
			
		}
	}
	
	
}
