package elements;

import java.awt.Color;
import java.awt.Graphics;

import scene.Map;



public class MapEvent extends Event{
	public final int toX;
	public final int toY;
	public final int toMap;

	public MapEvent(int x,int y, int sizeX, int sizeY, Map stage, int toMap,int toX, int toY) {
		super(x,y,sizeX, sizeY, stage);
		this.toX = toX;
		this.toY = toY;
		this.toMap = toMap;
	}
	
	public void draw(Graphics g, int offsetX, int offsetY){
		g.setColor(Color.BLUE);
		g.drawRect((int)x-offsetX, (int)y-offsetY, sizex-1, sizey-1);
	}

}
