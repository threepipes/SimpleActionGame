package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements KeyListener, Runnable{
//	private static final boolean DEBUG = true;
	
	public static final int Width = 800;
	public static final int Height = 600;
	
	private static final int KEY_RIGHT = 1;
	private static final int KEY_LEFT = 2;
	private static final int KEY_UP = 4;
	private static final int KEY_DOWN = 8;
	private static final int KEY_ATTACK = 16;

	private int keymask = 0;
	
	private Scene nowScene;

	public MainPanel() {
		setPreferredSize(new Dimension(Width, Height));
		setFocusable(true);
		addKeyListener(this);
		nowScene = new SMainGame();
		
		// Threadの開始は一番最後
		Thread anime = new Thread(this);
		anime.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		nowScene.draw(g);
		
//		if(DEBUG){
//			g.setColor(Color.BLACK);
//			g.drawString("px:"+(int)player.getX()+"; py:"+(int)player.getY()
//					+"; (xtile):"+(int)player.getX()/Map.BLOCK_SIZE+"; (ytile):"+(int)player.getY()/Map.BLOCK_SIZE, 40, 20);
//			g.drawString("ox:"+offsetX+"; oy:"+offsetY+"; Life:"+player.getLife()+"; mapNo:"+mapNo, 40, 40);
//			g.drawString("keymask:"+Integer.toBinaryString(keymask)+"; actmask:"+Integer.toBinaryString(actmask), 40, 60);
//		}
	}
	
	public void update(){
		nowScene.keyCheck(keymask);
		nowScene.update();
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_LEFT:
			keymask |= KEY_LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			keymask |= KEY_RIGHT;
			break;
		case KeyEvent.VK_UP:
			keymask |= KEY_UP;
			break;
		case KeyEvent.VK_DOWN:
			keymask |= KEY_DOWN;
			break;
		case KeyEvent.VK_V:
			keymask |= KEY_ATTACK;
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_LEFT:
			keymask &= ~KEY_LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			keymask &= ~KEY_RIGHT;
			break;
		case KeyEvent.VK_UP:
			keymask &= ~KEY_UP;
			break;
		case KeyEvent.VK_DOWN:
			keymask &= ~KEY_DOWN;
			break;
		case KeyEvent.VK_V:
			keymask &= ~KEY_ATTACK;
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	@Override
	public void run() {
		while(true){
			update();
			repaint();
			
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
