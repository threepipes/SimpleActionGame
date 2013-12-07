package main;

import java.awt.Graphics;

public interface GameTask {
	
	public void append(GameTask obj);
	
	public void init(String filename);
	public void draw(Graphics g,int offsetX, int offsetY);
	public void update();
	public void done();
}
