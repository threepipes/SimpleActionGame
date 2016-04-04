package elements;

import java.awt.Point;
import java.util.HashMap;

public class Actions {
	protected HashMap<String, Action> actmaps;
	protected int nowPriority = -1;
	protected String nowMotionName;
	protected String oldMotionName;
	protected String nowActionName;
	protected String parentName;
	protected boolean req = false;
	protected boolean conFin = true; // isContinueActionFinished
	protected int iact = 0;
	protected int icount = 0;
	protected Action reserve;
	
	public Actions(ActiveElement parent, HashMap<String, Action> actionList){
		parentName = parent.getName();
		actmaps = actionList;
	}
	
	public void update(){
		nowPriority = -1;
//		reserve = null;
//		reserve = defaultAction;
	}
	
	public Point getDrawPoint(){
		Action act = actmaps.get(conFin ? nowMotionName : nowActionName);
		if(!conFin)
			if(act instanceof ActionContinue)
				conFin = ((ActionContinue)act).isFin();
		Point p = act.getDrawPoint();
		if(p == null){
			p = actmaps.get(oldMotionName).getDrawPoint();
			nowMotionName = oldMotionName;
		}
		return p;
	}
	
	public void motionRequest(String act){
		if(conFin){
			Action tmp = actmaps.get(act);

			if(tmp != null){
				oldMotionName = nowMotionName;
				nowMotionName = act;
				req = true;
			}
		}
	}
	
	public void reserveAction(String actname){
		if(conFin){
			Action act = actmaps.get(actname);

			if(act != null){
				if(act.actionable(nowPriority)){
					reserve = act;
					nowPriority = act.getPriority();
					if(!req){
						oldMotionName = nowMotionName;
						nowMotionName = actname;
					}else req = false;
					nowActionName = actname;
				}
			}
		}
	}
	
	public void doAction(){
		if(reserve != null){
			reserve.action();
		}// 何もアクションがない場合はどうする？
		if(reserve instanceof ActionContinue){
			conFin = false;
		}
	}
}
