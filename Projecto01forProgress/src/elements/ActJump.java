package elements;

import main.KeyWords;

public class ActJump extends Action{
	public ActJump(int priority, ActiveElement parent) {
		super(priority, parent);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		name = KeyWords.JUMP;
	}
	
	@Override
	public void action() {

		if(parent.onGround){
			parent.vy = -24;
		}
		
	}
}
