package elements;

import java.awt.Rectangle;

import scene.Map;



public class Element {
	protected double x;
	protected double y;
	protected double vx = 0;
	protected double vy = 0;
	protected double ax = 0;
	protected double ay = 0;
	protected int sizex;
	protected int sizey;
	protected int colx;
	protected int coly;
	protected int colxs;
	protected int colys;
	
	
	protected boolean isAlive;
	protected boolean onWindow;
	protected Map stage;
	
	public Element(double x, double y, int sizex, int sizey, Map stage) {
		this.x = x;
		this.y = y;
		this.sizex = sizex;
		this.sizey = sizey;
		this.colx = 0;
		this.coly = 0;
		this.colxs = sizex;
		this.colys = sizey;
		isAlive = true;
		onWindow = true;
		this.stage = stage;
	}
	
	public void moveTo(double x, double y){
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
	}
	
	public void checkOnWindow(int offsetX, int offsetY){
		// 200‚Ì‰æ–ÊŠO—P—\
		if((y+vy <= -200-sizey+offsetY || y+vy >= 800+sizey+offsetY)
				|| (x+vx <= -200-sizex+offsetX || x+vx >= 1000+sizex+offsetX))
			onWindow = false;
	}
	
	public Rectangle getRect(){
		return new Rectangle((int)x+colx,(int)y+coly,colxs,colys);
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
