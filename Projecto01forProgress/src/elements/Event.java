package elements;

import java.awt.Graphics;

import scene.Map;


public abstract class Event extends ActiveElement{
	
	// É^ÉCÉãç¿ïW
	protected boolean deleteOne;
	public Event(int eventX, int eventY, int sizeX, int sizeY, Map stage, boolean deleteOne){
		super(eventX*Map.BLOCK_SIZE, eventY*Map.BLOCK_SIZE, sizeX*Map.BLOCK_SIZE, sizeY*Map.BLOCK_SIZE, stage);
		this.deleteOne = deleteOne;
	}
	
	public abstract void draw(Graphics g, int offsetX, int offsetY);
	public boolean getDelete(){
		return deleteOne;
	}
}
