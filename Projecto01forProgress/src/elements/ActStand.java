package elements;

import main.KeyWords;

public class ActStand extends Action{
	public ActStand(int priority, ActiveElement parent, int[][] mapr, int[][] mapl) {
		super(priority, parent, mapr,mapl);
		// TODO 自動生成されたコンストラクター・スタブ
		name = KeyWords.STAND;
	}
	
	@Override
	public void action() {

		if(parent.vx > 0){
			parent.vx -= parent.ax/2;
			if(parent.vx < 0) parent.vx = 0;
		}
		if(parent.vx < 0){
			parent.vx += parent.ax/2;
			if(parent.vx > 0) parent.vx = 0;
		}
		
	}
}
