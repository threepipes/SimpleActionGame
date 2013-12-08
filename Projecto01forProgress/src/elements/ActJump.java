package elements;

import main.KeyWords;

public class ActJump extends Action{
	public ActJump(int priority, ActiveElement parent) {
		super(priority, parent);
		// TODO 自動生成されたコンストラクター・スタブ
		name = KeyWords.JUMP;
	}
	
	@Override
	public void action() {

		if(parent.onGround){
			parent.vy = -24;
		}
		
	}
}
