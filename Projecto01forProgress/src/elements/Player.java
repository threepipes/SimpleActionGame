package elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import scene.Map;

import main.KeyWords;

public class Player extends ActiveElement{
//	protected Bullet blt = null;
	protected boolean land = false;
//	protected boolean sit = false;
	protected boolean damaged = false;
	protected boolean visible = true;
	protected boolean oldflag = false;

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
			{{0,0,0},{2,3,0},{3,3,0},{4,3,2},{3,3,0},{END,1,0}},		//8 land r
			{{0,0,0},{2,4,0},{3,4,0},{4,4,2},{3,4,0},{END,1,0}},		//9 land l
			{{0,0,0},{2,3,0},{3,3,0},{4,3,0},{LOOP,3,0}},		//10 sit r
			{{0,0,0},{2,4,0},{3,4,0},{4,4,0},{LOOP,3,0}},		//11 sit l
			{{0,0,0},{1,3,0},{2,3,0},{2,0,3},{END,1,0}},		//12 damaged r
			{{0,0,0},{1,4,0},{2,4,0},{3,0,3},{END,1,0}},		//13 damaged l
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
		loadAction(acts,new ActGun(4, this, 3));
		loadAction(acts,new ActSit(4, this, actmap[10], actmap[11], 12));
		loadAction(acts,new ActLand(5, this, actmap[8], actmap[9]));
		loadAction(acts,new ActDamage(6, this, actmap[12], actmap[13]));
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
	
	public void clearAttackCols(){
		if(attackCols != null)attackCols.clear();
	}
	
	public boolean landed(){
		if(!oldflag && onGround && oldVY >= 30) return true;
		oldflag = onGround;
		return false;
	}// 1ループで1回しか呼び出してはいけない
	
	
	public void move(){
		oldflag = onGround;
		super.move();
		if(!oldflag && onGround) land = true;

	}
	
	
	public void damage(int val, boolean isLeft){
		if(!nodamage){
			life -= val;
			nodamage = true;
			vy = -10;
			onGround = false;
			vx -= isLeft ? 10 : -10;
			TimerTask task = new FlashTask();
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, 0, 200);
			action(KeyWords.DAMAGE);
		}
	}
	
	public int getLife(){
		return life;
	}
	
	
	public void draw(Graphics g, int offsetX, int offsetY){

		// しゃがんで小さくなったサイズを戻す
		coly = 0;
		colys = 24;
		actions.doAction();
		
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
				g.drawString("onGround:"+onGround+"; iact:"+iact+"; icount:"+icount, 40, 80);
				g.drawString("vx:"+vx+"; vy:"+vy+"; ax:"+ax+"; ay:"+ay, 40, 100);
			}
			
		}
		g.setColor(Color.BLACK);
		super.draw(g, offsetX, offsetY);
		
		actions.update();
	}
	
	public class FlashTask extends TimerTask{
		private int count = 10;

		@Override
		public void run() {
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
