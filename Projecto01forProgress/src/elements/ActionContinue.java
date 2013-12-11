package elements;

import java.awt.Point;


public abstract class ActionContinue extends Action{
	private static final int END = -1;
	protected boolean nextAct = false;
	public ActionContinue(int priority, ActiveElement parent, int[][] mapr, int[][] mapl) {
		super(priority, parent, mapr, mapl);
	}
	
	public boolean isFin(){
		return nextAct;
	}
	
	public void start(){
	}
	
	public Point getDrawPoint(){
		int[][] map = (parent.dx == 1) ? mapR : mapL;
		nextAct = false;
		if(icount<map.length-1){
			if(!stop)icount++;
			if(map[icount][0] == END){
				nextAct = true;
				icount = 0;
			}
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
	
}
