package elements;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

import main.Map;

public abstract class ActiveElement extends Element{
	protected String name;
	
	protected boolean onGround = false;
	protected double maxspeed = 3;
	protected int dx = 1;
	protected int dy = 0;

	protected boolean nodamage = false;
	protected boolean stop = false;
	protected long stopTime;
	
	protected Image image = null;
	protected int icount = 0;
	protected int iact = 0;
	
	protected Actions actions;
	

	protected static final int LOOP = -1;
	protected static final int END = -2;
	
	public ActiveElement(double x, double y, int sizex, int sizey, Map stage) {
		super(x, y, sizex, sizey, stage);
		ax = 0.5;
		ay = 3;
	}
	
	public void loadImage(String filename){
		ImageIcon icon = new ImageIcon(getClass().getResource("/"+filename));
		image = icon.getImage();
		
	}
	
	public String getName(){
		return name;
	}
	
	public double getVX(){
		return vx;
	}
	
	public double getVY(){
		return vy;
	}
	
	public boolean isGround(){
		return onGround;
	}
	
	public void changeDir(int dx){
		this.dx = dx;
	}
	
	public void walkRight(){
		if(vx < maxspeed) vx += ax;
		dx = 1;
		dy = 0;
	}
	
	public void walkLeft(){
		if(vx > -maxspeed) vx -= ax;
		dx = -1;
		dy = 0;
	}
	
	public void stand(){
		if(vx > 0){
			vx -= ax/2;
			if(vx < 0) vx = 0;
		}
		if(vx < 0){
			vx += ax/2;
			if(vx > 0) vx = 0;
		}
	}
	
	public void death(){
		isAlive = false;
	}
	
	
	public void jump(){
		if(onGround){
			vy = -24;
//			ay = 3;
		}
	}
	
	
	
	public void move(){

		if(vy < 20) vy += ay;

		
		double newX = x + vx;
		double newY = y + vy;
		
		// check x only
		Point p = stage.checkHitBlock((int)newX, (int)y, sizex, sizey);
		if(p == null){
			x = newX;
//			if(vx > 0) dx = 1;
//			else if(vx < 0) dx = -1;
		}
		else{
			if(vx >= 0){
				x = p.x*Map.BLOCK_SIZE - sizex;
				vx = 0;
			}else{
				x = (p.x+1)*Map.BLOCK_SIZE;
				vx = 0;
			}
		}
		// check y after x
		p = stage.checkHitBlock((int)x, (int)newY, sizex, sizey);
		if(p == null){
			y = newY;
			onGround = false;
		}
		else{
			if(vy >= 0){
				y = p.y*Map.BLOCK_SIZE - sizey;
				onGround = true;
				vy = 0;
			}else if(vy < 0){
				y = (p.y+1)*Map.BLOCK_SIZE;
				vy = 0;
			}
		}
	}
	
	
	public abstract void draw(Graphics g, int offsetX, int offsetY);


}
