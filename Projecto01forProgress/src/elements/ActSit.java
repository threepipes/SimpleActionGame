package elements;

import main.KeyWords;

public class ActSit extends Action{
	protected int size;
	public ActSit(int priority, ActiveElement parent, int[][] mapr, int[][] mapl, int size) {
		super(priority, parent, mapr, mapl);
		name = KeyWords.SIT;
		this.size = size;
	}
	
	@Override
	public void action() {
		if(parent.onGround){
			if(parent.vx != 0){
				parent.vx -= parent.vx*0.1;
				parent.vx = (int)parent.vx;
				parent.colys = size;
				parent.coly = parent.sizey - size;
			}
		}
		
	}
}
