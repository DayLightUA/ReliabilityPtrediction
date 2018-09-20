package com.gmail.zayarnyukpm.defectiveness;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.gmail.zayarnyukpm.Prediction;
import com.gmail.zayarnyukpm.classes.DefCalcThread;
import com.gmail.zayarnyukpm.classes.GraphicPanel;


public class Defectiveness {
	
	private Prediction whoCall;
	public static Defectiveness program;
	JFrame defectFrame;
	JPanel mainPanel;
	Box graphicBox, buttonBox, tFieldBox;
	GraphicPanel graph1, graph2;
	
	Button backB, saveB, startB;
	
	JTextField timeField, pDefField, pFField, fRaiteField;
	
		
	public Defectiveness(){
	}
		
	public Defectiveness(Prediction whoCall){
		this();
		this.whoCall=whoCall;
	}
	
	public void go(){
		defectFrame = new JFrame("Defectiveness Prediction");
		mainPanel = new JPanel();
		init(mainPanel);
		
		defectFrame.add(mainPanel);
		defectFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		defectFrame.setSize(800, 600);
		defectFrame.setLocationRelativeTo(null);
		defectFrame.setVisible(true);
		
		
                

	}
	
	private void init(JPanel panel){
		graphicBox = Box.createHorizontalBox();
		tFieldBox = Box.createHorizontalBox();
		buttonBox = Box.createHorizontalBox();
		
		graph1 = new GraphicPanel(400, 400);
		graph2 = new GraphicPanel(400, 400);
		graphicBox.add(graph1);
		graphicBox.add(graph2);
		
		backB = new Button("<-Back");
		backB.addActionListener(new BListener());
		saveB = new Button("Save Graphics");
		saveB.addActionListener(new BListener());
		startB = new Button("Start");
		startB.addActionListener(new BListener());
		buttonBox.add(Box.createGlue()); buttonBox.add(backB);
		buttonBox.add(Box.createGlue()); buttonBox.add(saveB);
		buttonBox.add(Box.createGlue()); buttonBox.add(startB);
		buttonBox.add(Box.createGlue());
		
		timeField = new JTextField("enter time");
		JLabel timeL = new JLabel("enter time");
		Box tBox = Box.createVerticalBox();
		tBox.add(timeL);
		tBox.add(timeField);
		pDefField = new JTextField("enter Pdef");
		JLabel pDefL = new JLabel("enter Pdef");
		Box pDBox = Box.createVerticalBox();
		pDBox.add(pDefL);
		pDBox.add(pDefField);
		pFField = new JTextField("enter Pf");
		JLabel pFL = new JLabel("enter Pf");
		Box pFBox = Box.createVerticalBox();
		pFBox.add(pFL);
		pFBox.add(pFField);
		fRaiteField = new JTextField("enter \u03BB");
		JLabel fRaiteL = new JLabel("enter \u03BB");
		Box fRBox = Box.createVerticalBox();
		fRBox.add(fRaiteL);
		fRBox.add(fRaiteField);
		tFieldBox.add(Box.createGlue()); tFieldBox.add(pDBox);
		tFieldBox.add(Box.createGlue()); tFieldBox.add(pFBox);
		tFieldBox.add(Box.createGlue()); tFieldBox.add(tBox);
		tFieldBox.add(Box.createGlue()); tFieldBox.add(fRBox);
		tFieldBox.add(Box.createGlue());
		
		panel.setLayout(new BorderLayout());
		panel.add(buttonBox, BorderLayout.NORTH);
		panel.add(graphicBox, BorderLayout.CENTER);
		panel.add(tFieldBox, BorderLayout.SOUTH);
	}
	
	
	
	private class BListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			switch (((Button)evt.getSource()).getLabel()){
			case "<-Back": returnAndClose(); break;
			case "Save Graph": saveGraphics(); break;
			case "Start": start(); break;
			}
		}
			
			public void returnAndClose(){
				whoCall.runFrame.setVisible(true);
				defectFrame.setVisible(false);
				defectFrame.dispose();
				whoCall.returnBack();
			}
			
			public void saveGraphics(){
				
			}
			
			public void start(){
				DefCalcThread tr = new DefCalcThread(graphicBox, timeField, pDefField, pFField, fRaiteField, graph1, graph2);
				tr.start();
			}
	}
	
		public static void main(String[] args){
		Defectiveness d = new Defectiveness();
		d.go();
	}
	
}

