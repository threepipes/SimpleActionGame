package elements;

import java.awt.Graphics;

import main.Map;

public class AttackCollision extends Element{
	protected boolean hit = false;
	
	public AttackCollision(double x, double y, int sizex, int sizey, Map stage) {
		super(x, y, sizex, sizey, stage);
	}
	
	public boolean checkHitBlock(){
		if(stage.checkHitBlock((int)x+colx, (int)y+coly, colxs, colys) == null) return false;
		return true;
	}
	
	public boolean getAlive(){
		return isAlive;
	}
	
	public boolean checkHit(Element ele){
		boolean tmp = super.checkHit(ele);
		if(tmp) hit = true;
		return tmp;
	}
	
	public void changeCollision(int colx, int coly, int colsizex, int colsizey){
		this.colx = colx;
		this.coly = coly;
		this.colxs= colsizex;
		this.colys= colsizey;
	}
	
	public void move(){
		vx+= ax;
		vy+= ay;
		x += vx;
		y += vy;
	}
	
	public void draw(Graphics g, int offsetX, int offsetY){
		
	}

}
