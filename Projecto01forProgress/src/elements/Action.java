package elements;

import java.awt.Point;

public abstract class Action {
	private static final int LOOP = -1;
	private static final int END = -2;
	protected final int priority;
	protected final int[][] mapR;
	protected final int[][] mapL;
	
	protected ActiveElement parent;
	protected String name = null;
	protected int icount = 0;
	
	public Action(int priority, ActiveElement parent, int[][] motionsR, int[][] motionsL) {
		this.priority = priority;
		this.parent =  parent;
		this.mapR = motionsR;
		this.mapL = motionsL;
		
		// put name by Action
	}
	
	public int getPriority(){
		return priority;
	}
	
	public Point getDrawPoint(){
		int[][] map = (parent.dx == 1) ? mapR : mapL;
		if(icount<map.length-1){
			icount++;
			if(map[icount][0] == LOOP)
				icount = map[icount][1];
			else if(map[icount][0] == END){
				//					iact = map[icount][1]; TODO
				icount = 0;
			}
			if(map[icount][2] > 0){
				// set stop
				//					stop = true;
				//					stopTime = System.currentTimeMillis() + map[iact][icount][2];
			}
		}else{
			icount = 0;
		}
		return new Point(map[icount][0],map[icount][1]);
	}
	
//	public int[][] getMap(){
//		if(parent.dx == 1) return mapR;
//		return mapL;
//	}
	
//	public int getIAct(){
//		return priority*2+(parent.dx == -1 ? 1 : 0);
//	}
	
	public String getName(){
		return name;
	}
	
	public boolean actionable(int priority){
		if(this.priority > priority){
			return true;
		}
		return false;
	}
	
	public abstract void action();

}
