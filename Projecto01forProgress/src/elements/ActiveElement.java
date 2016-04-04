package elements;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import scene.Map;


public abstract class ActiveElement extends Element{
	protected String name;
	
	protected boolean onGround = false;
	protected double maxspeed = 3;
	protected int dx = 1;
	protected int dy = 0;
	protected final double ax = 0.5;
	protected final double ay = 3;
	protected int life;
	
	protected double oldVY;

	protected boolean nodamage = false;
	protected long stopTime;
	
	protected Image image = null;
	protected int icount = 0;
	protected int iact = 0;
	
	protected Actions actions;
	protected List<AttackCollision> attackCols;

	protected static final int LOOP = -1;
	protected static final int END = -1;
	
	public ActiveElement(double x, double y, int sizex, int sizey, Map stage) {
		super(x, y, sizex, sizey, stage);
	}
	
	public void loadImage(String filename){
		ImageIcon icon = new ImageIcon(getClass().getResource("/"+filename));
		image = icon.getImage();
		
	}
	
	public String getName(){
		return name;
	}
	
	public void setLife(int life){
		this.life = life;
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
	
	public void death(){
		isAlive = false;
	}
	

	public boolean checkFired(Element element){
		if(attackCols != null){
			Iterator<AttackCollision> it = attackCols.iterator();

			while(it.hasNext()){
				if(it.next().checkHit(element)){
					return true;
				}
			}
		}
		return false;
	}
	
	public void move(){

		if(vy < 30) vy += ay;
		oldVY = vy;
		
		double newX = x + vx;
		double newY = y + vy;

		boolean oldGround = onGround;
		// check y after x
		Point p = stage.checkHitBlock((int)newX, (int)y, sizex, sizey);
		if(p == null || stage.checkHitO((int)newX, (int)y, sizex, sizey)){
			x = newX;
//			if(vx > 0) dx = 1;
//			else if(vx < 0) dx = -1;
		}
		else{
			if(vx >= 0){
				x = p.x - sizex;
			}else{
				x = p.x+Map.BLOCK_SIZE;
			}
			vx = 0;
		}
		p = stage.checkHitBlock((int)x, (int)newY, sizex, sizey);
		if(p == null){
			y = newY;
			onGround = false;
		}
		else{
			if(vy >= 0){
				y = p.y - sizey;
				onGround = true;
				vy = 0;
			}else if(vy < 0){
				y = p.y+Map.BLOCK_SIZE;
				vy = 0;
			}
		}
		// check x only
		
		if(attackCols != null){
			Iterator<AttackCollision> it = attackCols.iterator();
			while(it.hasNext()){
				it.next().move();
			}
		}
		if((p=stage.checkHitSlope((int)newX, (int)y, sizex, sizey, (int)vx, oldGround)) != null && oldVY > 0){
//			if(p.x > 0)
			if(vx != 0)x = p.x;
			y = p.y;
			vy = 0;
			onGround = true;
		}
		
	}
	
	
	public void draw(Graphics g, int offsetX, int offsetY){
		if(attackCols != null){
			Iterator<AttackCollision> it = attackCols.iterator();
			while(it.hasNext()){
				AttackCollision tmp = it.next();
				if(tmp.getAlive())tmp.draw(g, offsetX, offsetY);
				else it.remove();
			}
		}
	}


}
