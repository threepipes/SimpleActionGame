package elements;

import java.awt.Graphics;

import main.Map;

public class Bullet extends AttackCollision{
	protected static final double Speed = 16;
	
	
	public Bullet(double x, double y, int sx, int sy, int dx, int dy, Map stage) {
		super(x, y, sx, sy, stage);
		this.vx = dx*Speed;
		this.vy = dy*Speed;
	}
	

	public void draw(Graphics g, int offsetX, int offsetY){
		super.checkOnWindow(offsetX, offsetY);
		if(onWindow && !hit && (null == stage.checkHitBlock((int)x, (int)y, sizex, sizey)))
			g.fillOval((int)x-offsetX, (int)y-offsetY, sizex, sizey);
		else isAlive = false;
	}

}
