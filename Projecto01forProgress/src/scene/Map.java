package scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import main.MainPanel;

import elements.Enemy;
import elements.Event;
import elements.MapEvent;
import elements.Player;
import elements.TalkEvent;

public class Map {
	protected char[][] map;
    protected int mapSizeX;
    protected int mapSizeY;
    protected int offsetX;
    protected int offsetY;
    
    protected Player player;
    protected List<Enemy> enemyList = new ArrayList<Enemy>();
    
    protected List<Event> eventList = new ArrayList<Event>();
    protected MainPanel mainPanel;
    public static final int BLOCK_SIZE = 32;
    
    public Map(MainPanel main){
    	mainPanel = main;
    }
    
    public void init(String mapName, String eventName, Player player, int toX, int toY){
    	loadMap(mapName);
    	loadEvent(eventName);
    	this.player = player;
    	player.changeMap(this);
    	player.moveTo(toX, toY);
    }
    
    public void setPlayer(Player player){
    	this.player = player;
    }
    
    public void loadMap(String filename){
    	BufferedReader reader = new BufferedReader(new InputStreamReader(
    			getClass().getResourceAsStream("/"+filename)));
    	
    	try {
			mapSizeX = Integer.parseInt(reader.readLine());
			mapSizeY = Integer.parseInt(reader.readLine());
			map = new char[mapSizeY][mapSizeX];
			
			for(int i=0; i<mapSizeY; i++){
				StringBuilder line = new StringBuilder(reader.readLine());
				for(int j=0; j<mapSizeX; j++){
					map[i][j] = line.charAt(j);
				}
			}
			
		} catch (NumberFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
    
    public void loadEvent(String filename){
    	BufferedReader reader = new BufferedReader(new InputStreamReader(
    			getClass().getResourceAsStream("/"+filename)));
    	try {		
    		String line = reader.readLine();
			while(line != null){
				StringTokenizer tokens = new StringTokenizer(line, ",");
				String token = tokens.nextToken();
				if(token.equals("MOVE")){
					int x = Integer.parseInt(tokens.nextToken());
					int y = Integer.parseInt(tokens.nextToken());
					int toMap = Integer.parseInt(tokens.nextToken());
					int toX = Integer.parseInt(tokens.nextToken());
					int toY = Integer.parseInt(tokens.nextToken());
					MapEvent me = new MapEvent(x,y,1,1,this,toMap,toX,toY);
					if(x > 0 && x < mapSizeX
			    			&& y > 0 && y < mapSizeY)eventList.add(me);
					else System.err.println("event x or y over");
					
//					map[y][x] = 'D';//TODO
				}else if(token.equals("TALK")){
					int x = Integer.parseInt(tokens.nextToken());
					int y = Integer.parseInt(tokens.nextToken());
					List<String[]> list = new ArrayList<String[]>();
					line = reader.readLine();
					while(!line.equals("END")){
						String[] buf = new String[2];
						tokens = new StringTokenizer(line, "@");
						buf[0] = tokens.nextToken().trim();
						buf[1] = tokens.nextToken().trim();
						list.add(buf);
						line = reader.readLine();
					}
					TalkEvent te = new TalkEvent(x,y,1,1, list, this); // TODO
					if(x > 0 && x < mapSizeX
			    			&& y > 0 && y < mapSizeY)eventList.add(te);
					else System.err.println("event x or y over");
				}else{
					System.err.println("event name error");
				}
				
				
				line = reader.readLine();
			}
			
		} catch (NumberFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
    
    public void destMap(){
    	map = null;
    	player = null;
    	enemyList.clear();
    	eventList.clear();
    }
    
    public Point getSizeTile(){
    	return new Point(mapSizeX, mapSizeY);
    }
    
    public void update(){

		player.move();
		Iterator<Enemy> it = enemyList.iterator();
		while(it.hasNext()){
			Enemy temp = it.next();
			temp.walk();
			temp.move();
			if(player.checkHit(temp)) player.damage(1,player.getX()<=temp.getX());
			if(player.checkFired(temp)){
				temp.death();
				it.remove();
			}
		}
    }
    
    public Point checkHitBlock(int x, int y, int sizeX, int sizeY, double vy){
    	int mappointX1 = (x)/BLOCK_SIZE;
    	int mappointX2 = (x+sizeX-1)/BLOCK_SIZE;
    	int mappointY1 = (y)/BLOCK_SIZE;
    	int mappointY2 = (y+sizeY-1)/BLOCK_SIZE;
    	if(mapSizeX <= (mappointX2)
    		|| mapSizeY <= (mappointY2)
    		|| 0 > (mappointX1)
    		|| 0 > (mappointY1)){
    		System.err.println("mapover:"+mappointX1+":"+mappointX2+":"+mappointY1+":"+mappointY2);
    		return null;
    	}
    	for(int i=mappointX1; i<=mappointX2; i++)
    		for(int j=mappointY1; j<=mappointY2; j++){
//    			if((map[j][i] == 'u' || map[j][i] == 'U') && (vy>=0 || j==mappointY2) /*&& (i==mappointX2 && j==mappointY2)*/){
//    				int b = map[j][i] == 'u' ? BLOCK_SIZE : BLOCK_SIZE/2;
//    				if(y+sizeY>(j+1)*BLOCK_SIZE) y = (j+1)*BLOCK_SIZE-sizeY;
//    				if(b - (y+sizeY-1)%BLOCK_SIZE < ((x+sizeX-1)%BLOCK_SIZE)/2+1)
//    					return new Point(x+sizeX-1, y+sizeY-1-((y+sizeY-1)%BLOCK_SIZE+((x+sizeX-1)%BLOCK_SIZE)/2+1-b));
//    			}else if((map[j][i] == 's' || map[j][i] == 'S') && (vy>=0 || j==mappointY2)){
//    				int b = map[j][i] == 's' ? BLOCK_SIZE : BLOCK_SIZE/2;
//    				int newx = BLOCK_SIZE - x%BLOCK_SIZE;
//    				if(y+sizeY>(j+1)*BLOCK_SIZE) y = (j+1)*BLOCK_SIZE-sizeY;
//    				if(b - (y+sizeY-1)%BLOCK_SIZE < (newx/2+1))
//    					return new Point(x-BLOCK_SIZE, y+sizeY-1-((y+sizeY-1)%BLOCK_SIZE+(newx)/2+1-b));
//    			}else if("usUSB".indexOf(map[j][i]) != -1)
    			if(map[j][i] == 'B') return new Point(i*BLOCK_SIZE,j*BLOCK_SIZE);
    		}
    	return null;
    }
    
    public Event checkEvent(){
    	Iterator<Event> it = eventList.iterator();
    	while(it.hasNext()){
    		Event e = it.next();
    		if(e.checkHit(player))
    			return e;
    	}
    	return null;
    }
    
    public void draw(Graphics g, int offsetX, int offsetY){
    	int tileFirstX = offsetX/BLOCK_SIZE;
    	int tileFirstY = offsetY/BLOCK_SIZE;
    	int tileLastX = tileFirstX + MainPanel.Width/BLOCK_SIZE + 2;
    	int tileLastY = tileFirstY + MainPanel.Height/BLOCK_SIZE + 2;
    	if(tileLastX > mapSizeX) tileLastX = mapSizeX;
    	if(tileLastY > mapSizeY) tileLastY = mapSizeY;
    	
    	// draw elements
		Iterator<Event> ite = eventList.iterator();
		while(ite.hasNext()){
			ite.next().draw(g, offsetX, offsetY);
		}
		Iterator<Enemy> it = enemyList.iterator();
		while(it.hasNext()){
			it.next().draw(g, offsetX, offsetY);
		}
		player.draw(g, offsetX, offsetY);
		
    	
		// draw map
    	for(int i=tileFirstX; i<tileLastX; i++)
    		for(int j=tileFirstY; j<tileLastY; j++){
    			g.setColor(Color.ORANGE);
    			switch(map[j][i]){
    			case 'E':
    				enemyList.add(new Enemy(i*BLOCK_SIZE, j*BLOCK_SIZE, this));
    				map[j][i] = 0;
    				break;
    			case 'R':
    				enemyList.add(new Enemy(i*BLOCK_SIZE, j*BLOCK_SIZE, this, 1));
    				map[j][i] = 0;
    				break;
    			case 'L':
    				enemyList.add(new Enemy(i*BLOCK_SIZE, j*BLOCK_SIZE, this, -1));
    				map[j][i] = 0;
    				break;
    			case 'B':
    				g.fillRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, BLOCK_SIZE, BLOCK_SIZE);
    				break;
//    			case 'u':
//    				int[] x = {i*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX};
//    				int[] y = {(j+1)*BLOCK_SIZE-offsetY, (j+1)*BLOCK_SIZE-offsetY, j*BLOCK_SIZE+BLOCK_SIZE/2-offsetY};
//    				g.fillPolygon(x, y, 3);
//    				break;
//    			case 'U':
//    				int[] x2 = {i*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX};
//    				int[] y2 = {(j+1)*BLOCK_SIZE-offsetY-BLOCK_SIZE/2, (j+1)*BLOCK_SIZE-offsetY-BLOCK_SIZE/2, j*BLOCK_SIZE-offsetY};
//    				g.fillPolygon(x2, y2, 3);
//    				g.fillRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE+BLOCK_SIZE/2-offsetY, BLOCK_SIZE, BLOCK_SIZE/2);
//    				break;
//    			case 's':
//    				int[] xs = {i*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX, (i)*BLOCK_SIZE-offsetX};
//    				int[] ys = {(j+1)*BLOCK_SIZE-offsetY, (j+1)*BLOCK_SIZE-offsetY, j*BLOCK_SIZE+BLOCK_SIZE/2-offsetY};
//    				g.fillPolygon(xs, ys, 3);
//    				break;
//    			case 'S':
//    				int[] xs2 = {i*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX, (i)*BLOCK_SIZE-offsetX};
//    				int[] ys2 = {(j+1)*BLOCK_SIZE-offsetY-BLOCK_SIZE/2, (j+1)*BLOCK_SIZE-offsetY-BLOCK_SIZE/2, j*BLOCK_SIZE-offsetY};
//    				g.fillPolygon(xs2, ys2, 3);
//    				g.fillRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE+BLOCK_SIZE/2-offsetY, BLOCK_SIZE, BLOCK_SIZE/2);
//    				break;
    			case 'D':
    				g.setColor(Color.BLUE);
    				g.drawRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, BLOCK_SIZE-1, BLOCK_SIZE-1);
    				break;
    			case ' ':
    			}
    		}
    }
}
