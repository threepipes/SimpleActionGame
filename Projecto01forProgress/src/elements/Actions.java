package elements;

import java.awt.Point;
import java.util.HashMap;

public class Actions {
	protected static HashMap<String, HashMap<String, Action>> actmaps = new HashMap<String,HashMap<String, Action>>();
	protected int nowPriority;
	protected String nowMotionName;
	protected String parentName;
	protected boolean req = false;
	protected int iact = 0;
	protected int icount = 0;
	protected Action reserve;
	protected Action defaultAction;
	
	public Actions(ActiveElement parent, HashMap<String, Action> actionList/*, Action def*/){
		parentName = parent.getName();
		if(!actmaps.containsKey(parentName)){
			actmaps.put(parentName, actionList);
		}
//		defaultAction = def;
	}
	
	public void update(){
		nowPriority = -1;
//		reserve = null;
//		reserve = defaultAction;
	}
	
	public Point getDrawPoint(){
		return actmaps.get(parentName).get(nowMotionName).getDrawPoint();
	}
	
	public void motionRequest(String act){
		Action tmp = actmaps.get(parentName).get(act);
		if(tmp != null){
			nowMotionName = act;
			req = true;
		}
	}
	
	public void reserveAction(String actname){
		Action act = actmaps.get(parentName).get(actname);
		if(act != null){
			if(act.actionable(nowPriority)){
				reserve = act;
				nowPriority = act.getPriority();
				if(!req){
					nowMotionName = actname;
				}else req = false;
				
			}
		}
	}
	
	public void doAction(){
		if(reserve != null){
			reserve.action();
		}// 何もアクションがない場合はどうする？
	}
}
