package com.gmail.zayarnyukpm.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

public class ModelContainer<E> extends ArrayList<E> implements List<E>, RandomAccess, Cloneable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String modelName;
	
	public ModelContainer(){
		super();
	}
	
	public ModelContainer(Collection<? extends E> c){
		super(c);
	}
	
	public ModelContainer(int initialCapacity){
		super(initialCapacity);
	}
	
	public ModelContainer(String name){
		super();
		setName(name);
	}
	
	public void setName(String name){
		modelName = name;
	}
	
	public String getName(){
		return modelName;
	}
}
