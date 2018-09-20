package com.gmail.zayarnyukpm.classes;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class DataPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static int version = 1;
	private Box parameterNamesBox;
	private String [] parameterNames = {"m0", "m1", "msd", "k1", "k2", "u", "del1", "del2", "xi", "t"};
	private ArrayList<JTextField> parameterFieldsList = new ArrayList<JTextField>();
	private double [] parameterVars;
	private int f1Length = 10;
	private int f2Length = 12;
	private int nDataColumns = 1;
	private String [] columnNames;
	
	public DataPanel(){
		super();
		parameterVars = new double [nDataColumns*parameterNames.length] ;
	}
	
	public DataPanel(double[] var){
		super();
		if (var.length == parameterNames.length) parameterVars = var;
		else System.out.println("Diferent sizes, create standart panel. varLength: "+var.length+" namesLangth: "+parameterNames.length);
	}
	
	public DataPanel(String[] pNames, String[] columnNames, ArrayList<JTextField> pFieldsList){
		super();
		if ((columnNames!=null&&pNames.length == pFieldsList.size()/columnNames.length)||(columnNames==null&&pNames.length == pFieldsList.size())){
			this.columnNames=columnNames;
			if (columnNames!=null) {nDataColumns=columnNames.length; System.out.println("columnNames.length:"+columnNames.length);}
			parameterNames = pNames;
			parameterFieldsList = pFieldsList;
		} else System.out.println("Diferent sizes, create standart panel");
	}
	
	public DataPanel(String[] pNames, String[] columnNames){
		super();
		ArrayList<JTextField> pFieldsList=new ArrayList<JTextField>();
		for (int i=0; i<pNames.length*columnNames.length ;i++){
			pFieldsList.add(new JTextField(""));
		}
		this.columnNames=columnNames;
		nDataColumns=columnNames.length;
		parameterNames = pNames;
		parameterFieldsList = pFieldsList;
	}
	
	public DataPanel(String[] pNames, ArrayList<JTextField> pFieldsList){
		this(pNames, null, pFieldsList);
	}
	
	public void addElements(){
		setBorder(new LineBorder(Color.BLACK));
		((FlowLayout) getLayout()).setAlignment(FlowLayout.LEFT);
		addNames();
		addFields();
		repaint();
	}
	
	private void addNames(){
		parameterNamesBox = new Box(BoxLayout.Y_AXIS);
		JTextField tf1;
		if (columnNames!=null){
			tf1 = new JTextField("");
			tf1.setColumns(f1Length);
			tf1.setEditable(false);
			parameterNamesBox.add(tf1);
		}
		parameterNamesBox.add(Box.createGlue());
		for (int i = 0; i<parameterNames.length; i++){
			tf1 = new JTextField(parameterNames[i]);
			tf1.setColumns(f1Length);
			tf1.setEditable(false);
			parameterNamesBox.add(Box.createGlue());
			parameterNamesBox.add(tf1);
			
		}
		add(parameterNamesBox);
	}
	
	private void addFields(){
		boolean listIsFree = true;
		if (!parameterFieldsList.isEmpty()) listIsFree = false;
		for (int i=0; i<nDataColumns; i++){
			Box columnBox = new Box(BoxLayout.Y_AXIS);
			JTextField tf;
			if (columnNames!=null){
				tf = new JTextField(columnNames[i]);
				tf.setColumns(f2Length);
				tf.setEditable(false);
				columnBox.add(tf);
			}
			for (int j = 0; j<parameterNames.length; j++){
				if (listIsFree)	{
					if (parameterVars==null) parameterVars = new double[parameterNames.length*nDataColumns];
					tf = new JTextField(""+parameterVars[j+i*parameterNames.length]); 
					parameterFieldsList.add(tf);
				} else tf = parameterFieldsList.get(j+parameterNames.length*i);
				tf.setColumns(f2Length);
				columnBox.add(Box.createGlue());
				columnBox.add(tf);
				
			}
			add(columnBox);
		}
	}
	
	public double[] getData(){
		parameterVars = new double[parameterFieldsList.size()];
		for (int i=0; i<parameterFieldsList.size(); i++){
			try{
				parameterVars[i] = Double.parseDouble(parameterFieldsList.get(i).getText());
			} catch (NumberFormatException NumEx){
				parameterVars[i] = 0; 
			}
		}
		return parameterVars;
	}
	
	public ArrayList<JTextField> getFieldsList(){
		return parameterFieldsList;
	}
	
	public void setData(double[] data){
		if (data.length == parameterFieldsList.size()){
			parameterVars=data;
			for (int i=0; i<parameterVars.length; i++){
				parameterFieldsList.get(i).setText(""+parameterVars[i]);
			}
		}
	}
	
	public void setFieldLength(int nameFieldsLength, int dataFieldsLength){
		if (nameFieldsLength>4 && nameFieldsLength<100){
			f1Length=nameFieldsLength;
		} else System.out.println("nameFieldsLength: size out of range!");
		if (dataFieldsLength>4 && dataFieldsLength<100){
			f2Length=dataFieldsLength;
		} else System.out.println("dataFieldsLength: size out of range!");
	}
}
