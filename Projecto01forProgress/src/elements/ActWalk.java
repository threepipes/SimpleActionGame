package elements;

import main.KeyWords;

public class ActWalk  extends Action{
	public ActWalk(int priority, ActiveElement parent) {
		super(priority, parent);
		name = KeyWords.WALK;
	}
	
	@Override
	public void action() {
		// TODO 自動生成されたメソッド・スタブ

		if(parent.dx == -1){
			if(parent.vx > -parent.maxspeed) parent.vx -= parent.ax;
		}
		else if(parent.vx < parent.maxspeed) parent.vx += parent.ax;
	}

}
