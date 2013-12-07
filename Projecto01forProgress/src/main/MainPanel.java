package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements KeyListener, Runnable{
	private static final boolean DEBUG = true;
	
	public static final int Width = 800;
	public static final int Height = 600;
	
	private static final int KEY_RIGHT = 1;
	private static final int KEY_LEFT = 2;
	private static final int KEY_UP = 4;
	private static final int KEY_DOWN = 8;
	private static final int KEY_ATTACK = 16;


	
	private int keymask = 0;
	private int actmask = 0;
	
	private int offsetX = 0;
	private int offsetY = 0;	

	private Player player;
	private List<Enemy> enemyList = new ArrayList<Enemy>();
	

	public static final int MAP_NUM = 3;
	private int mapNo = 0;
	private Map[] stage = new Map[MAP_NUM];	
	
	public MainPanel() {
		setPreferredSize(new Dimension(Width, Height));
		setFocusable(true);
		addKeyListener(this);

		for(int i=0; i<MAP_NUM; i++)stage[i] = new Map(this);
		stage[mapNo].loadMap("map0"+mapNo+".dat");
		stage[mapNo].loadEvent("map_event0"+mapNo+".evt");
		
		
		player = new Player(40, 1700, stage[mapNo]);
		
		Thread anime = new Thread(this);
		anime.start();
		player.loadImage("hito.png");
	}
	
	public void addEnemy(Enemy enemy){
		enemyList.add(enemy);
	}

	private void setOffset(){
		offsetX = (int) (player.getX() - Width/2);
		if(offsetX < 0) offsetX = 0;
		else if(offsetX > stage[mapNo].getSizeTile().x*stage[mapNo].BLOCK_SIZE - Width)
			offsetX = stage[mapNo].getSizeTile().x*stage[mapNo].BLOCK_SIZE - Width;
		if(stage[mapNo].getSizeTile().x*stage[mapNo].BLOCK_SIZE < Width) offsetX = 0; 
		offsetY = (int) (player.getY() - Height/2);
		if(offsetY < 0) offsetY = 0;
		else if(offsetY > stage[mapNo].getSizeTile().y*stage[mapNo].BLOCK_SIZE - Height)
			offsetY = stage[mapNo].getSizeTile().y*stage[mapNo].BLOCK_SIZE - Height;
		if(stage[mapNo].getSizeTile().x*stage[mapNo].BLOCK_SIZE < Width) offsetX = 0; 
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		player.draw(g, offsetX, offsetY);
		Iterator<Enemy> it = enemyList.iterator();
		while(it.hasNext()){
			it.next().draw(g, offsetX, offsetY);
		}
		stage[mapNo].draw(g, offsetX, offsetY);
		
		if(DEBUG){
			g.setColor(Color.BLACK);
			g.drawString("px:"+(int)player.getX()+"; py:"+(int)player.getY()
					+"; (xtile):"+(int)player.getX()/stage[mapNo].BLOCK_SIZE+"; (ytile):"+(int)player.getY()/stage[mapNo].BLOCK_SIZE, 40, 20);
			g.drawString("ox:"+offsetX+"; oy:"+offsetY+"; Life:"+player.getLife()+"; mapNo:"+mapNo, 40, 40);
			g.drawString("keymask:"+Integer.toBinaryString(keymask)+"; actmask:"+Integer.toBinaryString(actmask), 40, 60);
		}
	}
	
	public void update(){
		setOffset();
		
		doKeyEvent();
		player.move(offsetX, offsetY);
		Iterator<Enemy> it = enemyList.iterator();
		while(it.hasNext()){
			Enemy temp = it.next();
			temp.walk();
			temp.move();
			if(player.checkHit(temp)) player.damage(1);
			if(player.checkFired(temp)){
				temp.death();
				it.remove();
			}
		}
		
	}
	
	private void checkEvent(){
		//TODO
		Event e = stage[mapNo].checkEvent(player.getX(), player.getY());
		if(e != null){
			if(e instanceof MapEvent){
				MapEvent me = (MapEvent)e;
				
				if(mapNo != me.toMap){
					enemyList.clear();
					stage[mapNo].destMap();
					mapNo = me.toMap;
					stage[mapNo].loadMap("map0"+mapNo+".dat");
					stage[mapNo].loadEvent("map_event0"+mapNo+".evt");
				}
				player = new Player(me.toX*stage[mapNo].BLOCK_SIZE, me.toY*stage[mapNo].BLOCK_SIZE, stage[mapNo]);
				player.loadImage("hito.png");
			}
		}
	}
	
	private void doKeyEvent(){
		if((keymask & KEY_LEFT) > 0) player.walkLeft();
		if((keymask & KEY_RIGHT) > 0) player.walkRight();
		if((keymask & KEY_UP) > 0 && (~actmask & KEY_UP) > 0){
			player.jump();
			actmask |= KEY_UP;
		}
		if((keymask & KEY_DOWN) > 0 && (~actmask & KEY_DOWN) > 0){
			checkEvent();
			actmask |= KEY_DOWN;
		}
		if((keymask & KEY_ATTACK) > 0 && (~actmask & KEY_ATTACK) > 0){
			player.attack();
			actmask |= KEY_ATTACK;
		}
		if((keymask & ~KEY_ATTACK & ~KEY_DOWN) == 0)player.stand();
		// player Ç™attack downÇÃÇ›Ç»ÇÁÇŒÅCplayer ÇÕ stand
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
			actmask &= ~KEY_UP;
			break;
		case KeyEvent.VK_DOWN:
			keymask &= ~KEY_DOWN;
			actmask &= ~KEY_DOWN;
			break;
		case KeyEvent.VK_V:
			keymask &= ~KEY_ATTACK;
			actmask &= ~KEY_ATTACK;
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
