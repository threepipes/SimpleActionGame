package main;

import java.awt.Rectangle;


public class Element {
	protected double x;
	protected double y;
	protected double vx = 0;
	protected double vy = 0;
	protected double ax = 0;
	protected double ay = 0;
	protected int sizex;
	protected int sizey;
	protected boolean isAlive;
	protected boolean onWindow;
	protected Map stage;
	
	public Element(double x, double y, int sizex, int sizey, Map stage) {
		this.x = x;
		this.y = y;
		this.sizex = sizex;
		this.sizey = sizey;
		isAlive = true;
		onWindow = true;
		this.stage = stage;
	}
	
	
	public void move(int offsetX, int offsetY){
		if(y+vy <= 0-sizey+offsetY || y+vy >= 600+sizey+offsetY) onWindow = false;
		else{
			y+= vy;
		}
		if(x+vx <= 0-sizex+offsetX || x+vx >= 800+sizex+offsetX) onWindow = false;
		else{
			x+= vx;
		}	
	}
	
	public Rectangle getRect(){
		return new Rectangle((int)x,(int)y,sizex,sizey);
	}
	
	public boolean checkHit(Element ele){
		return this.getRect().intersects(ele.getRect());
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
}
