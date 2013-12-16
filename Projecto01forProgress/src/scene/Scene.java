package scene;

import java.awt.Graphics;

public interface Scene {
	public void update();
	public void keyCheck();
	public void draw(Graphics g);
}
