package elements;

import main.KeyWords;

public class ActDash   extends Action{
	public ActDash(int priority, ActiveElement parent) {
		super(priority, parent);
		name = KeyWords.DASH;
	}
	
	@Override
	public void action() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		if(parent.dx == -1){
			if(parent.vx > -parent.maxspeed) parent.vx -= parent.ax;
		}
		else if(parent.vx < parent.maxspeed) parent.vx += parent.ax;
	}
}
