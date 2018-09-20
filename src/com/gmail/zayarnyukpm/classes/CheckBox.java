package com.gmail.zayarnyukpm.classes;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JCheckBox;

public class CheckBox extends Box{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
	
	public CheckBox (int axis){
		super(axis);
	}
	
	public CheckBox (int axis, String[] checkBoxNames){
		super(axis);
		this.add(Box.createGlue());
		for (String s:checkBoxNames){
			JCheckBox c = new JCheckBox(s);
			checkBoxList.add(c);
			this.add(c);
		}
		this.add(Box.createGlue());
	}
	public CheckBox (int axis, ArrayList<JCheckBox> cBoxLis){
		super(axis);
		this.add(Box.createGlue());
		for (JCheckBox c:cBoxLis){
			this.add(c);
		}
		this.add(Box.createGlue());
		checkBoxList=cBoxLis;
	}
	public CheckBox (int axis, String[] checkBoxNames, ActionListener listener){
		super(axis);
		this.add(Box.createGlue());
		for (String s:checkBoxNames){
			JCheckBox c = new JCheckBox(s);
			c.addActionListener(listener);
			checkBoxList.add(c);
			this.add(c);
		}
		this.add(Box.createGlue());
	}
	
	public ArrayList<JCheckBox> getCheckBoxList(){
		return checkBoxList;
	}
	
	public JCheckBox getCheckBox(int i){
		return checkBoxList.get(i);
	}
	
	public boolean[] getState(){
		int capacity = checkBoxList.size();
		boolean[] state = new boolean[capacity];
		for (int i=0; i<capacity; i++){
			state[i]= checkBoxList.get(i).isSelected();
		}
		return state;
	}
}
