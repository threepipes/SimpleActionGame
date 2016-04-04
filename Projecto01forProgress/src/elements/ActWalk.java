package elements;

import main.KeyWords;

public class ActWalk  extends Action{
	public ActWalk(int priority, ActiveElement parent, int[][] mapr, int[][] mapl) {
		super(priority, parent, mapr,mapl);
		name = KeyWords.WALK;
	}
	
	@Override
	public void action() {
		// TODO 自動生成されたメソッド・スタブ

		if(parent.dx == -1){
			if(parent.vx > -parent.maxspeed) parent.vx -= parent.ax;
		}else if(parent.vx < parent.maxspeed) parent.vx += parent.ax;
	}

}
