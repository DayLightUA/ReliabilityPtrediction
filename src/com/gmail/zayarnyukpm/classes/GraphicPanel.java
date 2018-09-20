package com.gmail.zayarnyukpm.classes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphicPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	
	int gWidth = 800;
	int gHeight = 450;
	int xStart = 50;
	int yStart = 15;
	int xEnd = 30;
	int yEnd = 30;
	int xMarge=15;
	int yMarge=15;
	int tCenter=30;
	int tLength=60;
	int rowLength = 4;
	int xMin, xMax, yMin, yMax;
	
	double xMind, xMaxd, yMind, yMaxd;
	double ky;
	double kx;
	
	String xMins="0", xMaxs="0", yMins="0", yMaxs="0", xName="t";
	
	Color cordsColor = Color.BLACK;
	
	ArrayList<ModelContainer<PointInteger>> curvesList;
	ArrayList<PointDouble> crospoints;
	
	boolean xCros=false;
	boolean yCros=false;
	boolean margeXSeted = false;
	boolean margeYSeted = false;
	
	BufferedImage bImg;
	
	public GraphicPanel(){
		super();
	}
	public GraphicPanel(int width, int height){
		super();
		setSize(width, height);
	}
	
	public synchronized void paintComponent (Graphics g) {
		setSize(gWidth, gHeight);
		setBackground(Color.WHITE);
		bImg=paintBufImg();
		g.drawImage(bImg, 0, 0, this);
	}
	
	private synchronized BufferedImage paintBufImg(){
		bImg = new BufferedImage(gWidth, gHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g=bImg.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, gWidth, gHeight);
		paintModels(g);
		paintAxis(g);
		if (xCros || yCros)paintCrospoints(g);
		return bImg;
	}
	
	private void paintAxis(Graphics g){
		g.setColor(cordsColor);
		g.drawPolyline(xPointCords(),yPointCords(), 11);
		g.drawChars(DiferentMethods.chars(xMins, 7),0,7,xStart-tCenter,gHeight-yEnd+15);
		g.drawChars(DiferentMethods.chars(xMaxs, 7),0,7,gWidth-xEnd-tCenter-xMarge,gHeight-yEnd+15);
		g.drawChars(DiferentMethods.chars(yMins, 7),0,7,0,gHeight-yEnd);
		g.drawChars(DiferentMethods.chars(yMaxs, 7),0,7,0,yStart+yMarge);
		g.drawString(xName, gWidth-xEnd,gHeight-yEnd-rowLength/2);
		g.drawLine(xStart,gHeight-yEnd-rowLength/2, xStart, gHeight-yEnd+rowLength/2);
		g.drawLine(gWidth-xEnd-xMarge,gHeight-yEnd-rowLength/2, gWidth-xEnd-xMarge, gHeight-yEnd+rowLength/2);
		g.drawLine(xStart-rowLength/2,yStart+yMarge, xStart+rowLength/2, yStart+yMarge);
		g.drawLine(xStart-rowLength/2,gHeight-yEnd, xStart+rowLength/2, gHeight-yEnd);
	}
	
	private synchronized void paintModels(Graphics g){
		int textPositionX = xStart+tCenter;
		int textPositionY = tCenter;
		if (curvesList!=null){
			for (ModelContainer<PointInteger> curve: curvesList){
				System.out.println(curve.getName()+"   Index of model in list = "+curvesList.indexOf(curve));
				if (curve!=null){
					int nPoints = curve.size();
					String name = curve.getName();
					PointInteger p = (PointInteger) curve.get(0);
					g.setColor(p.color);
					if (textPositionX> gWidth-xEnd-tLength){textPositionX=xStart+tCenter; textPositionY+=20;}
					g.drawString(name, textPositionX, textPositionY);
					textPositionX+=tLength;
					int[] xCords = new int[nPoints];
					int[] yCords = new int[nPoints];
					for (int i=0; i<nPoints; i++){
						p =(PointInteger) curve.get(i);
						xCords[i] = p.x;
						yCords[i] = p.y;
					}
					g.drawPolyline(xCords, yCords, nPoints);
				}
			}
		}
		
	}
	
	private synchronized void paintCrospoints(Graphics g){
		if (crospoints!=null){
			for (PointDouble p: crospoints){
				PointInteger pi = recalcPoint(p);
				g.setColor(p.color);
				if (xCros){
					ArrayList<int[][]> textRecList = new ArrayList<int[][]>();
					int [] xTextRecX = {pi.x-tCenter, pi.x+tCenter};
					int [] xTextRecY = {gHeight-15, gHeight};
					int [][] xTextRec = {xTextRecX, xTextRecY};
					for (int[][] tr: textRecList){
						for (int i=0; i<textRecList.indexOf(tr); i++){
							if (xTextRec [0][0]<tr [0][1] && xTextRec [0][1]>tr [0][0])
								if(xTextRec [1][0]<tr [1][1] && xTextRec [1][1]>tr [1][0]){xTextRec [1][0]-=15; xTextRec [1][0]-=15;}
						}
						textRecList.add(xTextRec);
					}
					g.drawChars(chars(p.x), 0, 7, xTextRec [0][0], xTextRec [1][1]);
					g.drawLine(pi.x, pi.y, pi.x, xTextRec [1][0]);
				}
				if (yCros){
					ArrayList<int[][]> textRecList = new ArrayList<int[][]>();
					int [] yTextRecX = {0, 2*tCenter};
					int [] yTextRecY = {pi.y-15, pi.y};
					int [][] yTextRec = {yTextRecX, yTextRecY};
					for (int[][] tr: textRecList){
						for (int i=0; i<textRecList.indexOf(tr); i++){
							if (yTextRec [1][0]<tr [1][1] && yTextRec [1][1]>tr [1][0])
								if(yTextRec [0][0]<tr [0][1] && yTextRec [0][1]>tr [0][0]){yTextRec [0][0]+=2*tCenter; yTextRec [0][0]+=2*tCenter;}
						}
						textRecList.add(yTextRec);
					}
					g.drawChars(chars(p.y), 0, 7, yTextRec [0][0], yTextRec [1][1]);
					g.drawLine(pi.x, pi.y, yTextRec [0][1], pi.y);
				}
			}
		}
	}
	
	private int[] xPointCords(){
		int [] cords =new int[11];
		cords[0] = xStart;
		cords[1] = xStart-rowLength/2;
		cords[2] = xStart;
		cords[3] = xStart+rowLength/2;
		cords[4] = xStart;
		cords[5] = xStart;
		cords[6] = gWidth-xEnd-rowLength;
		cords[7] = gWidth-xEnd-rowLength;
		cords[8] = gWidth-xEnd;
		cords[9] = gWidth-xEnd-rowLength;
		cords[10] = gWidth-xEnd-rowLength;
		return cords;
	}
	
	private int[] yPointCords(){
		int [] cords =new int[11];
		cords[0] = yStart+rowLength;
		cords[1] = yStart+rowLength;
		cords[2] = yStart;
		cords[3] = yStart+rowLength;
		cords[4] = yStart+rowLength;
		cords[5] = gHeight-yEnd;
		cords[6] = gHeight-yEnd;
		cords[7] = gHeight-yEnd-rowLength/2;
		cords[8] = gHeight-yEnd;
		cords[9] = gHeight-yEnd+rowLength/2;
		cords[10] = gHeight-yEnd;
		return cords;
	}
	
	private char[] chars(double x){
		String s = x+"";
		int length = 7;
		return DiferentMethods.chars(s, length);
	}
	
	private PointInteger recalcPoint(PointDouble p){
		double x;
		double y;
		y=(p.y-yMind)*ky;
		int yi=(int) Math.ceil(y);
		if (yi-y>0.5)yi--;
		yi=gHeight-yEnd-yi;
		x=(p.x-xMind)*kx;
		int xi=(int) Math.ceil(x);
		if (xi-x>0.5)xi--;
		xi+=xStart;
		return new PointInteger(xi,yi, p.color);
	}

	public void addConvertPoints(ArrayList<ModelContainer<PointDouble>> curvesListD){
		yMaxd=-Double.MAX_VALUE;
		yMind=Double.MAX_VALUE;
		xMaxd=-Double.MAX_VALUE;
		xMind=Double.MAX_VALUE;
		for (ModelContainer<PointDouble> mc:curvesListD){
			for (PointDouble p:mc){
				if (p.y>yMaxd)yMaxd=p.y;
				if (p.y<yMind)yMind=p.y;
				if (p.x>xMaxd)xMaxd=p.x;
				if (p.x<xMind)xMind=p.x;
			}
		}
		margeXSeted = true;
		margeYSeted = true;
		addCurves(curvesListD);
	}
	
	public void addCurves(ArrayList<ModelContainer<PointDouble>> curvesListD){
		if (margeXSeted && margeYSeted){
			xMins=""+xMind;
			xMaxs=""+xMaxd;
			yMins=""+yMind;
			yMaxs=""+yMaxd;
			double dx = xMax-xMin;
			double dy = yMax-yMin;
			System.out.println("xMins="+xMins+" ||xMaxs="+xMaxs+" ||yMins="+yMins+" ||yMaxs="+yMaxs);
			ky=dy/(yMaxd-yMind);
			kx=dx/(xMaxd-xMind);	
			System.out.println("dx="+dx+" ||dy="+dy+" ||kx="+kx+" ||ky="+ky);
			curvesList=new ArrayList<ModelContainer<PointInteger>>();
			for (int i=0; i<curvesListD.size();i++){
				ModelContainer<PointInteger> mci = new ModelContainer<PointInteger>(curvesListD.get(i).getName());
				for (int j=0; j<curvesListD.get(i).size(); j++){
					PointDouble p=curvesListD.get(i).get(j);
					mci.add(recalcPoint(p));
				}curvesList.add(mci);
			}
		}
	}
	
	public void addCrospoints(ArrayList<PointDouble> crospoints, boolean xCros, boolean yCros){
		if (crospoints!=null){
			this.crospoints=crospoints;
			this.xCros=xCros;
			this.yCros=yCros;
		}
	}
	
	public BufferedImage getImg(){
		return bImg;
	}
	
	public int getMax(){
		return xMax-xMin;
	}
	
	public void setCrospointsOff(){
		xCros=false;
		yCros=false;
	}
	
	public void setMaxX(double xMin, double xMax){
		this.xMind=xMin;
		this.xMaxd=xMax;
		margeXSeted = true;
	}
	public void setMaxY(double yMin, double yMax){
		this.yMind=yMin;
		this.yMaxd=yMax;
		margeYSeted = true;
	}
	
	public void setSize(int width, int height){
		super.setSize(width, height);
		if (width>50 && width<2000 && height>50 && height<2000){
			gWidth=width; gHeight=height;
			xMin=xStart;
			xMax=gWidth-xEnd-xMarge;
			yMin=yStart+yMarge;
			yMax=gHeight-yEnd;
		}
	}
	
	public void setAxis(String xName){
		if (xName!=null)this.xName = xName;
	}
}
