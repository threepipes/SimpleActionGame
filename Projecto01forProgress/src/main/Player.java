package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player extends ActiveElement{
	private static final int BUL_NUM = 4;
	protected List<Bullet> bltList = new ArrayList<Bullet>();
	protected Bullet blt = null;
	protected static final int Size = 24;
	protected static final int LOOP = -1;
	protected int life;
	
	protected int[][][] actmap = {
			{{0,0},{LOOP,LOOP}},
			{{1,0},{LOOP,LOOP}},
			{{4,1},{0,1},{1,1},{1,1},{2,1},{3,1},{LOOP,LOOP}},
			{{4,2},{0,2},{1,2},{1,2},{2,2},{3,2},{LOOP,LOOP}},
			{{1,1},{2,1},{3,1},{4,1},{LOOP,LOOP}},
			{{1,2},{2,2},{3,2},{4,2},{LOOP,LOOP}},
			{{0,3},{1,3},{LOOP,LOOP}},
			{{2,3},{3,3},{LOOP,LOOP}},
			};
	
	public Player(double x, double y, Map stage) {
		super(x, y, Size/2, Size, stage);
		sizex = Size/2;
		sizey = Size;
		this.maxspeed = 8;
		life = 10;
	}
	
	public void attack(){
		if(bltList.size() < BUL_NUM) bltList.add(new Bullet(x+Size/2/2-8/2, y+Size/2-4/2, 8, 4, dx, dy, stage));
	}
	
	public void move(int offsetX, int offsetY){
		super.move();
		if(bltList.size() > 0){
			Iterator<Bullet> it = bltList.iterator();
			while(it.hasNext()){
				it.next().move(offsetX, offsetY);
			}
		}
	}
	
	public void chengeAct(){
		if(!onGround){
			if(dx>0)iact = 6;
			else iact = 7;
			if(vy<0) iact-=6;
		}
		else{
			if(vx>0){
				if(vx>5) iact = 2;
				else iact = 4;
			}
			else if(vx<0){
				if(vx<-5) iact = 3;
				else iact = 5;
			}
			else {
				if(dx>0)iact = 0;
				else iact = 1;
			}
		}
	}
	
	public boolean checkFired(Element enemy){
		Iterator<Bullet> it = bltList.iterator();

		while(it.hasNext()){
			if(enemy.checkHit(it.next())){
				it.remove();
				return true;
			}
		}

		return false;
	}
	
	public void changeCount(){
		if(iact<actmap.length){
			if(icount<actmap[iact].length-1){
				icount++;
				if(actmap[iact][icount][0] == LOOP)
					icount = 0;
			}else{
				icount = 0;
			}
		}else{
			iact = 0;
			icount = 0;
		}
		
	}
	
	public void damage(int val){
		life -= val;
	}
	
	public int getLife(){
		return life;
	}
	
	
	public void draw(Graphics g, int offsetX, int offsetY){
		g.setColor(Color.BLACK);
		if(image == null)g.drawRect((int)x-offsetX, (int)y-offsetY, Size/2-1, Size-1);
		else{
			chengeAct();
			changeCount();
			g.drawImage(image, (int)x-offsetX, (int)y-offsetY,
					(int)x-offsetX+sizex, (int)y-offsetY+sizey,
					actmap[iact][icount][0]*sizex, actmap[iact][icount][1]*sizey,
					actmap[iact][icount][0]*sizex+sizex-1, actmap[iact][icount][1]*sizey+sizey-1
					,null);
			
		}
		if(bltList.size() > 0){
			Iterator<Bullet> it = bltList.iterator();
			while(it.hasNext()){
				Bullet tmp = it.next();
				if(!tmp.getAlive()) it.remove();
				tmp.draw(g, offsetX, offsetY);
			}
		}
	}
	

}
