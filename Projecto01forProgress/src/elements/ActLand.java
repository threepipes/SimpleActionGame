package elements;

import main.KeyWords;

public class ActLand extends ActionContinue{
	public ActLand(int priority, ActiveElement parent, int[][] mapr, int[][] mapl) {
		super(priority, parent, mapr, mapl);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		name = KeyWords.LAND;
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}
	
	@Override
	public void action() {
		if(parent.vx != 0){
			parent.vx -= parent.vx*0.1;
			parent.vx = (int)parent.vx;
		}
	}

}
