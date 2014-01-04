package main;

import java.awt.Graphics;
import java.util.List;

import scene.Map;
import scene.MessageController;
import scene.MessageWindow;
import elements.Event;
import elements.MapEvent;
import elements.Player;
import elements.TalkEvent;

public class SMainGame extends Scene{
	
	private int offsetX = 0;
	private int offsetY = 0;	
	private boolean talking;

	private Player player;
	
	public static final int MAP_NUM = 3;
	private int mapNo = 0;
	private Map[] stage = new Map[MAP_NUM];	
	private MessageWindow messageW;
	private MessageController messageC;
	private List<String[]> talkList;
	
	public SMainGame() {
		for(int i=0; i<MAP_NUM; i++)stage[i] = new Map();
		player = new Player(40, 1700, stage[mapNo]);
		stage[mapNo].init("map0"+mapNo+".dat","map_event0"+mapNo+".evt", player, 40, 1700);
		
		player.loadImage("hito.png");
		
		messageW = new MessageWindow(20, MainPanel.Height-200, MainPanel.Width-40, 180, "gamefont.png");
		messageC = new MessageController(messageW, "face.png");
	}
	

	private void setOffset(){
		offsetX = (int) (player.getX() - MainPanel.Width/2);
		if(offsetX < 0) offsetX = 0;
		else if(offsetX > stage[mapNo].getSizeTile().x*Map.BLOCK_SIZE - MainPanel.Width)
			offsetX = stage[mapNo].getSizeTile().x*Map.BLOCK_SIZE - MainPanel.Width;
		if(stage[mapNo].getSizeTile().x*Map.BLOCK_SIZE < MainPanel.Width) offsetX = 0; 
		offsetY = (int) (player.getY() - MainPanel.Height/2);
		if(offsetY < 0) offsetY = 0;
		else if(offsetY > stage[mapNo].getSizeTile().y*Map.BLOCK_SIZE - MainPanel.Height)
			offsetY = stage[mapNo].getSizeTile().y*Map.BLOCK_SIZE - MainPanel.Height;
		if(stage[mapNo].getSizeTile().x*Map.BLOCK_SIZE < MainPanel.Width) offsetX = 0; 
	}
	
	private void checkEvent(){
		//TODO
		Event e = stage[mapNo].checkEvent();
		if(e != null){
			if(e instanceof MapEvent){
				MapEvent me = (MapEvent)e;
				
				if(mapNo != me.toMap){
					// playerが壁の中に行く危険がある(要：無敵処理)
					player.clearAttackCols();
					stage[mapNo].destMap();
					mapNo = me.toMap;
					stage[mapNo].init("map0"+mapNo+".dat","map_event0"+mapNo+".evt", player
							, me.toX*Map.BLOCK_SIZE, me.toY*Map.BLOCK_SIZE);
				}else{
					player.moveTo(me.toX*Map.BLOCK_SIZE, me.toY*Map.BLOCK_SIZE);
				}
			}else if(e instanceof TalkEvent){
				TalkEvent te = (TalkEvent) e;
				
				player.action(KeyWords.STAND);
				talkList = te.getTalk();
				
				messageC.setTalkList(talkList);
				talking = true;
			}
		}
	}
	
	@Override
	public void update() {

		if(!talking){
			setOffset();
			stage[mapNo].update();
		}
		
	}

	
	@Override
	public void keyCheck(int keymask) {
		actmask &= keymask;
		
		if(talking){
		if((keymask & KEY_ATTACK) > 0 && (~actmask & KEY_ATTACK) > 0){
			messageC.nextTalk();
			actmask |= KEY_ATTACK;
			talking = messageW.isVisible();
		}
		return;
		}

		if((keymask & KEY_LEFT) > 0 && (keymask & KEY_RIGHT) == 0){
			player.changeDir(-1);
			if(player.getVX() < -5)player.action(KeyWords.DASH);
			else player.action(KeyWords.WALK);
		}
		if((keymask & KEY_RIGHT) > 0){
			player.changeDir(1);
			if(player.getVX() > 5)player.action(KeyWords.DASH);
			else player.action(KeyWords.WALK);
		}
		if((keymask & KEY_UP) > 0 && (~actmask & KEY_UP) > 0){
			player.action(KeyWords.JUMP);
			actmask |= KEY_UP;
		}
		if((keymask & KEY_DOWN) > 0 && (~actmask & KEY_DOWN) > 0){
			checkEvent();
			actmask |= KEY_DOWN;
		}
		if((keymask & KEY_ATTACK) > 0 && (~actmask & KEY_ATTACK) > 0){
			player.action(KeyWords.GUN);
			actmask |= KEY_ATTACK;
		}
		if((keymask & ~KEY_ATTACK & ~KEY_UP) == 0){
			player.action(KeyWords.STAND);
			if(player.getVX() != 0) player.motionRequest(KeyWords.WALK);
		}
		// player がattack のみならば，player は stand

		if(player.landed()){
			player.action(KeyWords.LAND);
		}else if(!player.isGround() && player.getVY() >= 0) 
			player.motionRequest(KeyWords.JUMP);
		if((keymask & KEY_DOWN) > 0){
			player.action(KeyWords.SIT);
		}
		
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO 自動生成されたメソッド・スタブ
		stage[mapNo].draw(g, offsetX, offsetY);
		
		messageC.draw(g, player.getY() < 600);
		
	}

}
