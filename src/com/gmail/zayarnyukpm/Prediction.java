package com.gmail.zayarnyukpm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.gmail.zayarnyukpm.defectiveness.Defectiveness;
import com.gmail.zayarnyukpm.parametrDriftPrediction.DriftPrediction;

public class Prediction {
	public JFrame runFrame;
	JButton drift;
	JButton defects;
	Box buttonBox;
	private DriftPrediction prediction;
	private Defectiveness defectiveness;
	
	private void go(){
		drift=new JButton("Drift Prediction");
		defects=new JButton("Defectiveness");
		
		buttonBox = new Box(BoxLayout.X_AXIS);
		buttonBox.add(Box.createGlue());
		buttonBox.add(drift);
		buttonBox.add(Box.createGlue());
		buttonBox.add(defects);
		buttonBox.add(Box.createGlue());
		
		drift.addActionListener(new DriftBListener());
		defects.addActionListener(new DefectsBListener());
		
		runFrame = new JFrame("Chuse Tipe of Prediction");
		runFrame.getContentPane().add(BorderLayout.CENTER, buttonBox);
		runFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		runFrame.setSize(350, 100);
		runFrame.setLocationRelativeTo(null);
		runFrame.setVisible(true);
	}
	private void startDriftPrediction(){
		runFrame.setVisible(false);
		prediction = new DriftPrediction(this);
		prediction.go();
	}
	private void startDefictiveness(){
		runFrame.setVisible(false);
		defectiveness = new Defectiveness(this);
		defectiveness.go();
	}
	public static void main(String[] args) {
		Prediction p = new Prediction();
		p.go();
	}
	private class DriftBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			startDriftPrediction();
		}
	}
	private class DefectsBListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			startDefictiveness();
		}
	}
	
	public void returnBack(){
		prediction = null;
		defectiveness = null;
	}
}
