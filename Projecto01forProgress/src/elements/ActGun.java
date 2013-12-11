package elements;

import java.util.ArrayList;

import main.KeyWords;
import main.Map;

public class ActGun extends Action{
	int maxnum;
	public ActGun(int priority, ActiveElement parent, int num) {
		super(priority, parent, null, null);
		name = KeyWords.GUN;
		this.maxnum = num;
	}
	
	@Override
	public void action() {
		if(parent.attackCols == null){
			parent.attackCols = new ArrayList<AttackCollision>();
		}
		if(parent.attackCols.size() < maxnum)
			parent.attackCols.add(new Bullet(parent.x+parent.sizex/2, parent.y+parent.sizey/2, 6, 3, parent.dx, 0, parent.stage));
		
	}
}
