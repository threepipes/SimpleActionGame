package elements;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import scene.Map;


public class TalkEvent extends Event{
	private List<String[]> talkList;
	public TalkEvent(int x, int y, int sx, int sy, List<String[]> talkList, Map stage, boolean deleteOne) {
		super(x, y, sx, sy, stage, deleteOne);
		this.talkList = talkList;
	}
	
	public List<String[]> getTalk(){
		return talkList;
	}
	
	public boolean getDelete(){
		return deleteOne;
	}
	
	public void draw(Graphics g, int offsetX, int offsetY){
		g.setColor(Color.CYAN);
		g.fillRect((int)x-offsetX, (int)y-offsetY, sizex, sizey);
	}
}
