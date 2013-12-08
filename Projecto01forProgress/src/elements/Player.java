package elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import main.KeyWords;
import main.Map;

public class Player extends ActiveElement{
	protected List<Bullet> bltList = new ArrayList<Bullet>();
	protected Bullet blt = null;
	protected boolean land = false;
	protected boolean sit = false;
	protected boolean damaged = false;
	protected boolean visible = true;
	protected boolean oldflag;
	protected int life;

	private static final int BUL_NUM = 4;
	protected static final int Size = 24;
	private static final boolean DEBUG = true;
	
	
	protected int[][][] actmap = {
			{{0,0,0},{0,0,0},{LOOP,1,0}},								//0 stand r
			{{0,0,0},{1,0,0},{LOOP,1,0}},								//1 stand l
			{{0,0,0},{1,1,0},{2,1,0},{3,1,0},{4,1,0},{LOOP,1,0}},				//2 accel r
			{{0,0,0},{1,2,0},{2,2,0},{3,2,0},{4,2,0},{LOOP,1,0}},				//3 accel l
			{{0,0,0},{4,1,0},{0,1,0},{1,1,0},{1,1,0},{2,1,0},{3,1,0},{LOOP,1,0}},	//4 dash r
			{{0,0,0},{4,2,0},{0,2,0},{1,2,0},{1,2,0},{2,2,0},{3,2,0},{LOOP,1,0}},	//5 dash l
			{{0,0,0},{0,3,0},{1,3,0},{LOOP,1,0}},							//6 Jump r
			{{0,0,0},{0,4,0},{1,4,0},{LOOP,1,0}},							//7 Jump l
			{{0,0,0},{2,3,0},{3,3,0},{4,3,0},{4,3,0},{4,3,0},{3,3,0},{END,0,0}},		//8 land r
			{{0,0,0},{2,4,0},{3,4,0},{4,4,0},{4,4,0},{4,4,0},{3,4,0},{END,1,0}},		//9 land l
			{{0,0,0},{2,3,0},{3,3,0},{4,3,0},{LOOP,3,0}},		//10 sit r
			{{0,0,0},{2,4,0},{3,4,0},{4,4,0},{LOOP,3,0}},		//11 sit l
			{{0,0,0},{1,4,0},{2,4,0},{3,0,0},{LOOP,3,0}},		//13 damaged l
			{{0,0,0},{1,3,0},{2,3,0},{2,0,0},{LOOP,3,0}},		//12 damaged r
			};
	
	public Player(double x, double y, Map stage) {
		super(x, y, Size/2, Size, stage);
		name = KeyWords.PLAYER;
		sizex = Size/2;
		sizey = Size;
		this.maxspeed = 8;
		life = 10;
		loadActions(/*filename*/);
	}
	
	public void loadActions(/*filename*/){
		HashMap<String, Action> acts = new HashMap<String, Action>();
		loadAction(acts,new ActStand(0, this, actmap[0], actmap[1]));
		loadAction(acts,new ActWalk(1, this, actmap[2], actmap[3]));
		loadAction(acts,new ActDash(2, this, actmap[4], actmap[5]));
		loadAction(acts,new ActJump(3, this, actmap[6], actmap[7]));
		loadAction(acts,new ActSit(4, this, actmap[10], actmap[11], 12));
		actions = new Actions(this, acts);
	}
	
	public void loadAction(HashMap<String, Action> acts, Action act){
		acts.put(act.getName(), act);
	}
	
	public void changeMap(Map stage){
		this.stage = stage;
	}
	
	
	public void action(String action){
		actions.reserveAction(action);
	}
	
	public void motionRequest(String act){
		actions.motionRequest(act);
	}
	
	public void attack(){
		if(!sit && !damaged && bltList.size() < BUL_NUM) bltList.add(new Bullet(x+Size/2/2-8/2, y+Size/2-4/2, 8, 4, dx, dy, stage));
	}
	
	
	public void walkLeft(){
		if(!sit && !damaged) super.walkLeft();
	}
	
	public void walkRight(){
		if(!sit && !damaged) super.walkRight();
	}
	
	public void sit(){
		if(!damaged && onGround){
			sit = true;
			if(vx != 0){
				vx -= vx*0.1;
				vx = (int)vx;
				coly = 12;
				colys = 12;
			}
		}
	}
	
	public void move(){
		if(!stop){
			if(damaged){
				vx = vx*0.98;
			}
			oldflag = onGround;
			super.move();
			if(!oldflag && onGround) land = true;

			// ‚¢‚¸‚ê‚ÍActiveElement‚ÉˆÚ‚·—\’è
//			chengeAct();
//			changeCount();
//			Point tmp = actions.getDrawPoint();
//			iact = tmp.x;
//			icount = tmp.y;
		}else{
			// doTimer
			if(System.currentTimeMillis() > stopTime)
				stop = false;
			
		}
		if(bltList.size() > 0){
			Iterator<Bullet> it = bltList.iterator();
			while(it.hasNext()){
				it.next().move();
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
	
	public void chengeAct(){
		if(damaged){
			if(dx>0)iact = 12;
			else iact = 13;
		}else if(sit){
			if(dx>0)iact = 10;
			else iact = 11;
			sit = false;
		}else if(vx==0 && land){
			if(dx>0)iact = 8;
			else iact = 9;
		}else if(!onGround){
			if(dx>0)iact = 6;
			else iact = 7;
			if(vy<0) iact-=6;
		}else{
			land = false;
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
	
	
	public void changeCount(){
		if(iact<actmap.length){
			if(icount<actmap[iact].length-1){
				icount++;
				if(actmap[iact][icount][0] == LOOP)
					icount = actmap[iact][icount][1];
				else if(actmap[iact][icount][0] == END){
					land = false;
					iact = actmap[iact][icount][1];
					icount = 0;
				}
				if(actmap[iact][icount][2] > 0){
					// set stop
//					stop = true;
//					stopTime = System.currentTimeMillis() + actmap[iact][icount][2];
				}
			}else{
				icount = 0;
			}
		}else{
			iact = 0;
			icount = 0;
		}
		
	}
	
	public void damage(int val, boolean isLeft){
		if(!nodamage){
			life -= val;
			damaged = true;
			nodamage = true;
			vy = -10;
			onGround = false;
			vx -= isLeft ? 10 : -10;
			TimerTask task = new FlashTask();
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, 0, 200);
		}
	}
	
	public int getLife(){
		return life;
	}
	
	
	public void draw(Graphics g, int offsetX, int offsetY){

		actions.doAction();
		// ‚µ‚á‚ª‚ñ‚Å¬‚³‚­‚È‚Á‚½ƒTƒCƒY‚ð–ß‚·
		coly = 0;
		colys = 24;
		
		Point point = actions.getDrawPoint();
		g.setColor(Color.BLACK);
		if(image == null)g.drawRect((int)x-offsetX, (int)y-offsetY, Size/2-1, Size-1);
		else{
			try{
				if(visible)g.drawImage(image, (int)x-offsetX, (int)y-offsetY,
						(int)x-offsetX+sizex, (int)y-offsetY+sizey,
						point.x*sizex, point.y*sizey,
						point.x*sizex+sizex-1, point.y*sizey+sizey-1
						,null);
			}catch(ArrayIndexOutOfBoundsException e){
				System.err.println("iact:"+iact+"; icount:"+icount);
			}
			if(DEBUG){
				g.drawString("land:"+land+"; iact:"+iact+"; icount:"+icount, 40, 80);
				g.drawString("vx:"+vx+"; vy:"+vy+"; ax:"+ax+"; ay:"+ay, 40, 100);
			}
			
		}
		if(bltList.size() > 0){
			Iterator<Bullet> it = bltList.iterator();
			while(it.hasNext()){
				Bullet tmp = it.next();
				if(!tmp.getAlive()) it.remove();
				tmp.draw(g, offsetX, offsetY);
			}
		}
		
		actions.update();
	}
	
	public class FlashTask extends TimerTask{
		private int count = 10;

		@Override
		public void run() {
//			if(count == 9) dx = -dx;
			if(count < 10){
				damaged = false;
				visible = !visible;
			}
			
			if(count <= 0){
				nodamage = false;
				visible = true;
				cancel();
			}
			count--;
		}

	}

}
