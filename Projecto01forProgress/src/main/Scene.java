package main;

import java.awt.Graphics;

public abstract class Scene {

	protected static final int KEY_RIGHT = 1;
	protected static final int KEY_LEFT = 2;
	protected static final int KEY_UP = 4;
	protected static final int KEY_DOWN = 8;
	protected static final int KEY_ATTACK = 16;
	
	protected int actmask = 0;
	
	public abstract void update();
	public abstract void keyCheck(int keymask);
	public abstract void draw(Graphics g);
}
