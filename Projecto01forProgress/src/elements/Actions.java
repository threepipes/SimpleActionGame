package elements;

import java.awt.Point;
import java.util.HashMap;

public class Actions {
	protected class ActMap{
		public int[][][] map;
		public HashMap<String, Action> actions;
		public ActMap(int[][][] map, HashMap<String, Action> actions){
			this.map = map;
			this.actions = actions;
		}
	}
	private static final int LOOP = -1;
	private static final int END = -2;
	protected static HashMap<String, ActMap> actmaps = new HashMap<String,ActMap>();
	protected int nowPriority;
	protected String parentName;
	protected int iact = 0;
	protected int icount = 0;
	
	public Actions(ActiveElement parent, int[][][] map, HashMap<String, Action> actionList){
		parentName = parent.getName();
		if(actmaps.get(parentName) == null){
			actmaps.put(parentName, new ActMap(map, actionList));
		}
	}
	
	public Point getDrawPoint(){
		return new Point(iact,icount);
	}
	
	public void changeCount(){
		int[][][] map = actmaps.get(parentName).map;
		if(iact<map.length){
			if(icount<map[iact].length-1){
				icount++;
				if(map[iact][icount][0] == LOOP)
					icount = map[iact][icount][1];
				else if(map[iact][icount][0] == END){
					iact = map[iact][icount][1];
					icount = 0;
				}
				if(map[iact][icount][2] > 0){
					// set stop
//					stop = true;
//					stopTime = System.currentTimeMillis() + map[iact][icount][2];
				}
			}else{
				icount = 0;
			}
		}else{
			iact = 0;
			icount = 0;
		}
	}
	
	public void doAction(String actname){
		ActMap actmap = actmaps.get(parentName);
		if(actmap != null){
			Action act = actmap.actions.get(actname);
			if(act.actionable(nowPriority)) act.action();
			nowPriority = act.getPriority();
			iact = act.getIAct();
			changeCount();
		}
	}
}
