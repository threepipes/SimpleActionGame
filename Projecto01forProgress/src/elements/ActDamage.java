package elements;

import main.KeyWords;

public class ActDamage extends ActionContinue{
	public ActDamage(int priority, ActiveElement parent, int[][] mapr, int[][] mapl) {
		super(priority, parent, mapr, mapl);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		name = KeyWords.DAMAGE;
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
