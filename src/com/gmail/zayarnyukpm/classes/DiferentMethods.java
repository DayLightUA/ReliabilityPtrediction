package com.gmail.zayarnyukpm.classes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public abstract class DiferentMethods {
	
	static JFrame dialogFrame;
	
	
	
	public static double[] iterateCalculations (double[] var){
		double[] result = new double[12];
		double[] v = new double[var.length];
		for (int i=0; i<var.length; i++) v[i]=var[i];
		ArrayList<Model> models = new ArrayList<Model>();
		models.add(new ExponentialModel());
		models.add(new LinearModel());
		models.add(new SquareModel());
		models.add(new CubicModel());
		v[5]=0;
		for (int i=0; i<4; i++){
			result[i]=calculate(v, models.get(i));
		}
		v[5]=-var[5];
		for (int i=0; i<4; i++){
			result[i+4]=calculate(v, models.get(i));
		}
		v[5]=var[5];
		for (int i=0; i<4; i++){
			result[i+8]=calculate(v, models.get(i));
		}
		return result;
	}
	
	private static double calculate(double[] v, Model m){
		double result;
		System.out.println("IC model is "+m.toString());
		m.setVar(v);
		double d1 = -v[6];
		
		double d2 = -v[7];
		double del1= iterateCalculation(d1, m.f(), m.d1f(), m.d2f(), m.d3f(), v[8]);
		System.out.println("IC del1="+del1);
		double del2= iterateCalculation(d2, m.f(), m.d1f(), m.d2f(), m.d3f(), v[8]);
		System.out.println("IC del2="+del2);
		if (del1>del2) result=v[9]+del1; else result=v[9]+del2;
		if (result<0) result=-1;
		return result;
	}
		
	public static double iterateCalculation(double allowLevel, double function, double d1func, double d2func, double d3func, double precision){
		double delta=0, 
				al, func, d1, d2, d3, p;
		al = allowLevel;
		func = function;
		d1 = d1func;
		p = precision;
		d2 = d2func;
		d3 = d3func;
				
		double del = 0;
		double u, k, l, r;
		for (int i=0; i<10000; i++){
			u = -(func-al);
			k = d1+del*d2/2;
			l = del*del*d3/6;	
			if (k!=0)	r = u/(k+l);
			else r=1000000;
			if ((r-del)<=precision || (del-r)<=precision ){delta=r; break;} 
			else {del=r; if (i==9999) delta = -Double.MAX_VALUE;}
		}
		return delta;
		
		/*BigDecimal del = BigDecimal.ZERO;
		BigDecimal u;
		BigDecimal k;
		BigDecimal l;
		BigDecimal r;
		for (int i=0; i<10000; i++){
			u = (function.add(allowLevel)).negate();
			k = del.multiply(d2func).divide(new BigDecimal("2"), 128, RoundingMode.HALF_EVEN);
			l = del.pow(2).multiply(d3func).divide(new BigDecimal("6"), 128, RoundingMode.HALF_EVEN);	
			k=d1func.add(k).add(l);
			if (k.doubleValue()!=0)	r = u.divide(k, 128, RoundingMode.HALF_EVEN);
			else r=new BigDecimal("1000000");
			if ((((r.subtract(del)).abs()).doubleValue())<=precision){delta=r.doubleValue(); break;} 
			else {del=r; if (i==9999) delta = -99999999;}
		}
		return delta; */

	}
		
	public static char[] chars(String s, int length){
		int l = s.length();
		int EIndex=0;
		int symbolLength=length;
		char[] chars =new char [symbolLength];
		char[] allChars1 = s.toCharArray();
		char[] allChars2;
		if (allChars1.length>2 && allChars1[l-1]=='0' && allChars1[l-2]=='.') {
			l=l-2;
			allChars2=new char[l];
			for (int i=0; i<(l);i++) allChars2[i]=allChars1[i];
		} else { allChars2=allChars1;}
		for(int i=0; i<symbolLength; i++)chars[i]=' ';
		if (l>length){
			if (s.contains("E")){
				EIndex = s.indexOf("E");
				for (int i=0; i<(l-EIndex);i++){
					chars[(length-1-i)]=allChars2[(l-1-i)];
					symbolLength--;
				}
			}
		} else {symbolLength=l;}
		for(int i=0; i<symbolLength; i++)chars[i]=allChars2[i];
		int counter=1;
		for (int i=length-1; i>=0; i--) if (chars[i]==' '){counter++;} else {break;}
		counter=counter/2;
		for (int i=0; i<counter; i++){
			for (int j=length-1; j>0; j--) chars[j]=chars[j-1];
			chars[0]=' ';
		}
		return chars;
	}
	
	public static ArrayList<ModelContainer<PointDouble>> calcModels(double stap, int nPoints, double[] var, 
			ArrayList<JCheckBox> modelCheckBoxList, String[] modelNames, ArrayList<JCheckBox> curveCheckBoxList,
			String[] curveNames, double[] iterateResults , ArrayList<PointDouble> crospointList){
		
		ArrayList<Model> modelsList = new ArrayList<Model>();
		ArrayList<Color> colors = new ArrayList<Color>();
		modelsList.add(new ExponentialModel());
		modelsList.add(new LinearModel());
		modelsList.add(new SquareModel());
		modelsList.add(new CubicModel());
		ArrayList<ModelContainer<PointDouble>> models = new ArrayList<ModelContainer<PointDouble>>();
		colors.add(Color.RED);
		colors.add(Color.CYAN);
		colors.add(Color.GREEN);
		colors.add(Color.YELLOW);
		colors.add(Color.BLACK);
		
		ModelContainer<PointDouble> model = new ModelContainer<PointDouble>("      del1");
		model.add(new PointDouble(0, var[6], colors.get(4)));
		model.add(new PointDouble(stap*(nPoints+10), var[6], colors.get(4)));
		models.add(model);
		model = new ModelContainer<PointDouble>("      del2");
		model.add(new PointDouble(0, var[7], colors.get(4)));
		model.add(new PointDouble(stap*(nPoints+10), var[7], colors.get(4)));
		models.add(model);
		
		double[] v1 = new double[var.length];
		double[] v2 = new double[var.length];
		double[] v3 = new double[var.length];
		
		ArrayList<double[]> vars = new ArrayList<double[]>();
		vars.add(v1);
		vars.add(v2);
		vars.add(v3);
		for (int i=0; i<var.length; i++) {v1[i]=var[i]; v2[i]=var[i]; v3[i]=var[i];}
		v1[5]=0; v2[5]=-var[5];
		if (curveCheckBoxList==null) {
			curveCheckBoxList=new ArrayList<JCheckBox>(); 
			for(int i=0; i<3; i++) curveCheckBoxList.add(new JCheckBox(" ", true));
			}
		if (crospointList!=null) crospointList.clear(); else crospointList=new ArrayList<PointDouble>();
		
		for (int i=0; i<modelNames.length; i++){
			if (modelCheckBoxList.get(i).isSelected()){
				String name=modelNames[i].substring(0,2)+"_";
				for (int j=0; j<curveNames.length; j++){
					if (curveCheckBoxList.get(j).isSelected()){
						String n=name+curveNames[j]+";";
						model = new ModelContainer<PointDouble>(n);
						modelsList.get(i).setVar(vars.get(j));
						if (iterateResults!=null && iterateResults[i+j*4]>0){
							crospointList.add(new PointDouble(iterateResults[i+j*4], modelsList.get(i).f(iterateResults[i+j*4]) , colors.get(i)));
						}
						for (double k=0; k<stap*(nPoints+10); k+=stap){
							model.add(new PointDouble(k, modelsList.get(i).f(k), colors.get(i)));
						} models.add(model);
					}
				}
			}
		}
		return models;
	}
	
	public static void saveData(double [] var){
		try{
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.showSaveDialog(dialogFrame);
			FileOutputStream fileStream = new FileOutputStream(fileChoose.getSelectedFile());
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(var);
			os.close();
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public static synchronized double[] loadData(double[] var) {
		double[] data =new double [0];
		try{
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.showOpenDialog(dialogFrame);
			FileInputStream fileStream = new FileInputStream(fileChoose.getSelectedFile());
			ObjectInputStream os = new ObjectInputStream(fileStream);
			data=(double[]) os.readObject();
			os.close();
		} catch(IOException ex){
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (data.length==var.length)var=data; else System.out.println("mising data");
		return var;
	}
	
	public static void saveResult(ArrayList<JTextField> result, String[] columnNames){
		String textResult = "";
		int length = 9;
		String sResult;
		for (int i=0; i<columnNames.length; i++){
			sResult = String.copyValueOf(chars(columnNames[i], length));
			textResult+="|"+sResult;
		}
		textResult+="|";
		for (int j=0; j< result.size()/columnNames.length; j++){
			textResult += "/n";
			for (int i=0; i<columnNames.length; i++){
				sResult = String.copyValueOf(chars(result.get(i+j*columnNames.length).getText(), length));
				textResult+="|"+sResult;
			} 
			textResult+="|";
		}
		
		try{
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.showSaveDialog(dialogFrame);
			FileWriter writer = new FileWriter(fileChoose.getSelectedFile(), false);
			writer.write(textResult);
	        writer.close();
		} catch(IOException ex){ex.printStackTrace();}
	}
	
	public static void saveGraphic(BufferedImage bImg, String addName){
		JFileChooser fileChoose = new JFileChooser();
		fileChoose.showSaveDialog(dialogFrame);
		String way = fileChoose.getSelectedFile().getPath();
		fileChoose.getSelectedFile().delete();
		way = way +addName+ ".jpg";
		File file = new File(way);
		try{
			ImageIO.write(bImg, "jpeg", file);
		} catch(IOException ex){ex.printStackTrace();}
	}
	public static void saveGraphic(BufferedImage bImg){
		saveGraphic(bImg, "");
	}

	public static void saveGraphic(GraphicPanel graph){
		String fileName = "C:/img";
		try
        {
            JFileChooser fileChooser= new  JFileChooser();
            TextFileFilter filter = new TextFileFilter(".jpg");
            fileChooser.addChoosableFileFilter(filter);
            int  result = fileChooser.showSaveDialog(null);
            if(result==JFileChooser.APPROVE_OPTION)
            	fileName = fileChooser.getSelectedFile().getAbsolutePath();
            ImageIO.write(graph.getImg(), "jpeg", new  File(fileName+".jpg"));
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
	
	public static Model newModel(String modelName, ArrayList additionalParamList, int startIndex, Object who_ask){
		switch (modelName){
		case Constants.EXPONENTIAL_MODEL: return new ExponentialModel();
		case Constants.LINEAR_MODEL: return new LinearModel(); 
		case Constants.SQUARE_MODEL: return new SquareModel(); 
		case Constants.CUBIC_MODEL: return new CubicModel();
		case Constants.GAUSIAN_MODEL: return new GausianModel();
		case Constants.RECTANGULAR_MODEL: return new RectangularModel();
		case Constants.TRIANGULAR_MODEL: return new TriangularModel();
		case Constants.VEIBUL_MODEL: return new VeibulModel();
		case Constants.GRAM_SHARLE_MODEL: return new GramSharleModel();
		case Constants.XeqY_MODEL: return new XeqYModel();
		case Constants.MULTIPLED_MODEL: new MultipledModel();
			break;
		}
		System.out.println(who_ask.toString()+": Generate Zero Model, must be: "+modelName);
		return new Model();
	}
}
