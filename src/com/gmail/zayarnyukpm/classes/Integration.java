package com.gmail.zayarnyukpm.classes;

import java.math.BigDecimal;
import java.util.ArrayList;
/*Створює об'єкт для інтегрування заданої моделі за одним з праметрів для заданих рівнів.
 * 
 */
public class Integration extends Model{
	private ArrayList<double[]> discreteValues = new ArrayList<double[]>();
	private Model model;
	private int lowerLevelIndex = -1;
	private int uperLevelIndex = -1;
	private int parameterIndex = -1;
	private int sampling = 100;
	private double lowerLevelMultipler = 1;
	private double uperLevelMultipler = 1;
	private double lowerLevel;
	private double uperLevel;
	private double delta_t;
	private double integral;
	private boolean basicCalc = false;
	private boolean integrationIsOwn = true;
	private int partialDerevativeLevel = 0;
	private int descrimenantLevel=0;
	private int descrLevel=0;
	
	Integration (){
	}
	
	Integration (Model model, int lowerLevelIndex, int uperLevelIndex, int parameterIndex){
		this.model=model;
		setIndexes(lowerLevelIndex, uperLevelIndex, parameterIndex, -1);
		lowerLevel=model.getVar()[this.lowerLevelIndex];
		uperLevel=model.getVar()[this.uperLevelIndex];
	}
	
	Integration (Model model, int lowerLevelIndex, int uperLevelIndex, int parameterIndex, 
			double lowerLevelMultipler, double uperLevelMultipler){
		this (model, lowerLevelIndex, uperLevelIndex, parameterIndex);
		setMultiplers(lowerLevelMultipler, uperLevelMultipler);
		
	}
	
	Integration (Model model, double lowerLevel, double uperLevel, int parameterIndex, double delta){
		this.model=model;
		setLeveles(lowerLevel, uperLevel, parameterIndex, delta);
	}
	
	
	public double f(double t){
		if (basicCalc){
			int compare1;
			int index=0;
			int compare2;
			double[] v = model.getVar();
			double[] result;
			if (t<=lowerLevel) return integral;
			if(t<uperLevel){ for (int i=discreteValues.size()-1; i>=0; i--){
					if (t>discreteValues.get(i)[1]) index = i;
				}
				calcLast(t, v, index);
			} else if (t==uperLevel) {
				index = discreteValues.size()-1; 
			} else {
				index = discreteValues.size()-1;
				int k = new Double((t-uperLevel)/delta_t).intValue()+index+1;
				while(index<k){
					result = new double[3+descrimenantLevel];
					result[0] = discreteValues.get(index-1)[0]+delta_t;
					if (parameterIndex>=0) v[parameterIndex] = result[0];
					result[1] = model.f();
					result[2] = sq(discreteValues.get(index-1)[1], result[1], delta_t);
					discreteValues.add(result);
					index++;
				}
				calcLast(t, v, index); index++;
			}
			for (int i= 0; i<=index; i++) integral = integral+discreteValues.get(i)[2];
		} else f();
		return integral;
	}
	public double f(){
		System.out.println(name+" leveles: lowerLevel="+lowerLevel+"   uperLewel="+uperLevel+"   delta_t="+delta_t+"  sampling="+sampling);
		if(!basicCalc){
			System.out.println(name+" basicCalc");
			integral = 0;
			double[] v = model.getVar();
			if (parameterIndex>=0) v[parameterIndex] = lowerLevel;
			model.setVar(v);
			double[] result = new double[3+descrimenantLevel];
			double t = lowerLevel;
			result[0] = t;
			result[1] = model.f();
			result[2] = 0;
			discreteValues.add(result);
			integral=result[2];
			for (int j=1; j<sampling; j++){
				if (parameterIndex>=0) {
					v[parameterIndex] = v[parameterIndex]+delta_t;
					model.setVar(v);
					}
				result = new double[3+descrimenantLevel];
				result[0] = t+delta_t;
				result[1] = model.f();
				result[2] = sq(discreteValues.get(discreteValues.size()-1)[1], result[1], delta_t);
				System.out.println(name+" basicCalc: j="+j+"   result[0]="+result[0]+
						"   result[1]"+result[1]+
						"   result[2]"+result[2]);
				discreteValues.add(result);
				integral=integral+result[2];
			}
			basicCalc = true;
			System.out.println(name+"  Integral="+integral);
			return integral;
		}else{
			f(model.getVar()[parameterIndex]);
			System.out.println(name+"  Integral="+integral);
			return integral;
		}
	}
	
	
	private void calcLast(double x, double[] v, int index){
		double delta = x-discreteValues.get(index)[0];
		double[] result = new double[3];
		result[0] = x;
		if (parameterIndex>=0) v[parameterIndex] = result[0];
		result[1] = model.f();
		result[2] = sq(discreteValues.get(index)[1], result[1], delta);
		discreteValues.add(index+1, result);
	}
	
	private void calcLeveles(){
		lowerLevel = model.getVar()[lowerLevelIndex]*lowerLevelMultipler;
		uperLevel = model.getVar()[uperLevelIndex]*uperLevelMultipler;
		basicCalc = false;
	}
	
	private double sq(double y1, double y2, double delta_t){
		return y1*delta_t+(y2-y1)*delta_t/2;
	}
	
	public boolean setIndexes(int lowerLevelIndex,	int uperLevelIndex,	int parameterIndex, int sempling){
		if (sampling>0) this.sampling=sampling;
		int length = 21;
		if (model!=null) length = model.getVar().length;
		if (parameterIndex<length && parameterIndex>=0 && parameterIndex!=lowerLevelIndex){
			this.parameterIndex = parameterIndex;
			if ( lowerLevelIndex<length && uperLevelIndex<length && parameterIndex<length &&
					lowerLevelIndex>=0 && uperLevelIndex>=0 && parameterIndex>=0 &&
					lowerLevelIndex!=uperLevelIndex && lowerLevelIndex!=parameterIndex && parameterIndex!=lowerLevelIndex){
				this.lowerLevelIndex = lowerLevelIndex;
				this.uperLevelIndex = uperLevelIndex;
				calcLeveles();
				delta_t=(uperLevel-lowerLevel)/this.sampling;
				return true;
			} else return false;
		} else return false;
		
	}
	
	public void setLeveles(double lowerLevel,double uperLevel, int parameterIndex, double delta_t){
		int length = 21;
		if (model!=null) length = model.getVar().length;
		if (parameterIndex<length && parameterIndex>=0 && parameterIndex!=lowerLevelIndex)
			this.parameterIndex = parameterIndex;
		if (delta_t>0 && uperLevel>lowerLevel){
			this.lowerLevel=lowerLevel;
			this.uperLevel=uperLevel;
			this.delta_t=delta_t;
			lowerLevelIndex = -1;
			uperLevelIndex = -1;
			basicCalc = false;
			}
		sampling = new Double((uperLevel-lowerLevel)/delta_t).intValue();
	}
	
	public void setLeveles(double lowerLevel,double uperLevel, int parameterIndex, int sampling){
		if (sampling>0) this.sampling=sampling;
		setLeveles(lowerLevel, uperLevel, parameterIndex, 
				(uperLevel-lowerLevel)/this.sampling);
	}
	
	public void setMultiplers (double lowerLevelMultipler, double uperLevelMultipler){
		this.lowerLevelMultipler = lowerLevelMultipler;
		this.uperLevelMultipler =uperLevelMultipler;
		basicCalc = false;
	}
	
	public void setModel(Model model){
		this.model = model;
		var=model.getVar();
	}
	
}
