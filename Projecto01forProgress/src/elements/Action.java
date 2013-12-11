package elements;

import java.awt.Point;

public abstract class Action {
	private static final int LOOP = -1;
	protected final int priority;
	protected final int[][] mapR;
	protected final int[][] mapL;
	
	protected ActiveElement parent;
	protected String name = null;
	protected int icount = 0;
	protected int count = 0;
	protected boolean stop = false;
	
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
		if(map == null) return null;
		if(icount<map.length-1){
			if(!stop)icount++;
			if(map[icount][0] == LOOP)
				icount = map[icount][1];
			if(map[icount][2] > 0){
				if(count++ < map[icount][2]){
					stop = true;
				}else{
					stop = false;
					count = 0;
				}
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
