package main;

import java.awt.Graphics;

public class Bullet extends Element{
	protected static final double Speed = 16;
	
	
	public Bullet(double x, double y, int sx, int sy, int dx, int dy, Map stage) {
		super(x, y, sx, sy, stage);
		this.vx = dx*Speed;
		this.vy = dy*Speed;
	}
	
	public boolean getAlive(){
		return isAlive;
	}
	
	public void draw(Graphics g, int offsetX, int offsetY){
		super.draw(g, offsetX, offsetY);
		if(onWindow) g.fillOval((int)x-offsetX, (int)y-offsetY, sizex, sizey);
		else isAlive = false;
	}
	
	public void move(){
		if(stage.checkHitBlock((int)x, (int)y, sizex, sizey) != null) isAlive = false;
	}

	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void append(GameTask obj) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void done() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void update() {
		move();
	}

}
