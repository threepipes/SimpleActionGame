package elements;

import main.KeyWords;

public class ActLand extends ActionContinue{
	public ActLand(int priority, ActiveElement parent, int[][] mapr, int[][] mapl) {
		super(priority, parent, mapr, mapl);
		// TODO 自動生成されたコンストラクター・スタブ
		name = KeyWords.LAND;
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	public void action() {
		if(parent.vx != 0){
			parent.vx -= parent.vx*0.1;
			parent.vx = (int)parent.vx;
		}
	}

}
