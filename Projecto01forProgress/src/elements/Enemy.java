package elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import main.KeyWords;
import main.Map;

public class Enemy extends ActiveElement{
	protected static final int Size = 24;
	protected static Random rand = new Random();
	public Enemy(double x, double y, Map stage) {
		super(x, y, Size, Size, stage);
		name = KeyWords.ENEMY;
		if(rand.nextInt(2) == 0) dx = -1;
		maxspeed = 3;
		ay = 1;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public Enemy(double x, double y, Map stage, int dx) {
		super(x, y, Size, Size, stage);
		this.dx = dx;
		maxspeed = 3;
		ay = 1;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	public void draw(Graphics g, int offsetX, int offsetY) {
		// TODO 自動生成されたメソッド・スタブ
		g.setColor(Color.RED);
		if(isAlive)g.drawRect((int)x-offsetX, (int)y-offsetY, Size-1, Size-1);
		
	}
	
	public void walk(){
		if(dx>0) walkRight();
		else walkLeft();
	}
	
	public void move(){

		if(vy < 20) vy += ay;

		
		double newX = x + vx;
		double newY = y + vy;
		
		// check x only
		Point p = stage.checkHitBlock((int)newX, (int)y, sizex, sizey);
		if(p == null){
			x = newX;
			if(vx > 0) dx = 1;
			else if(vx < 0) dx = -1;
		}
		else{
			if(vx >= 0){
				x = p.x*Map.BLOCK_SIZE - sizex;
				if(vx > 5) vx = -2;
				else vx = 0;
			}else{
				x = (p.x+1)*Map.BLOCK_SIZE;
				if(vx < -5) vx = 2;
				else vx = 0;
			}
			dx = -dx;
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

}
