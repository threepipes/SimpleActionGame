package elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import scene.Map;

import main.KeyWords;

public class Enemy extends ActiveElement{
	protected static final int Size = 24;
	protected static Random rand = new Random();
	protected AttackCollision defaultAC;
	protected final double ay = 1;
	public Enemy(double x, double y, Map stage) {
		super(x, y, Size, Size, stage);
		name = KeyWords.ENEMY;
		if(rand.nextInt(2) == 0) dx = -1;
		maxspeed = 3;
		attackCols = new ArrayList<AttackCollision>();
		defaultAC = new AttackCollision(x,y,sizex,sizey, stage);
		attackCols.add(defaultAC);
		loadAction();
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public Enemy(double x, double y, Map stage, int dx) {
		super(x, y, Size, Size, stage);
		this.dx = dx;
		maxspeed = 3;
		attackCols = new ArrayList<AttackCollision>();
		defaultAC = new AttackCollision(x,y,sizex,sizey, stage);
		attackCols.add(defaultAC);
		loadAction();
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public void loadAction(){
		HashMap<String, Action> acts = new HashMap<String, Action>();
//		loadAction(acts,new ActStand(0, this, null,null));
		loadAction(acts,new ActWalk(1, this, null, null));
		actions = new Actions(this, acts);
	}
	
	public void loadAction(HashMap<String, Action> acts, Action act){
		acts.put(act.getName(), act);
	}
	
	@Override
	public void draw(Graphics g, int offsetX, int offsetY) {
		// TODO 自動生成されたメソッド・スタブ
		actions.doAction();
		g.setColor(Color.RED);
		if(isAlive)g.drawRect((int)x-offsetX, (int)y-offsetY, Size-1, Size-1);
		actions.update();
	}
	
	public void walk(){
		actions.reserveAction(KeyWords.WALK);
	}
	
	public void move(){

		if(vy < 20) vy += ay;
		oldVY = vy;
		
		double newX = x + vx;
		double newY = y + vy;

		boolean oldGround = onGround;
		// check x only
		Point p = stage.checkHitBlock((int)newX, (int)y, sizex, sizey);
		if(p == null || stage.checkHitO((int)newX, (int)y, sizex, sizey)){
			x = newX;
			if(vx > 0) dx = 1;
			else if(vx < 0) dx = -1;
		}
		else{
			if(vx >= 0){
				x = p.x - sizex;
				if(vx > 5) vx = -2;
				else vx = 0;
			}else{
				x = p.x+Map.BLOCK_SIZE;
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
				y = p.y - sizey;
				onGround = true;
				vy = 0;
			}else if(vy < 0){
				y = p.y+Map.BLOCK_SIZE;
				vy = 0;
			}
		}
		if((p=stage.checkHitSlope((int)newX, (int)y, sizex, sizey, (int)vx, oldGround)) != null && oldVY > 0){
			if(vx!=0)x = p.x;
			y = p.y;
			vy = 0;
			onGround = true;
		}
		defaultAC.moveTo(x, y);
	}

}
