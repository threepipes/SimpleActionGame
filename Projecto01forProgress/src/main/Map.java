package main;

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

public class Map implements GameTask{
	protected char[][] map;
    protected int mapSizeX;
    protected int mapSizeY;
    protected int offsetX;
    protected int offsetY;
    protected List<Event> eventList = new ArrayList<Event>();
    protected MainPanel mainPanel;
    public static final int BLOCK_SIZE = 32;
    
    public Map(MainPanel main){
    	mainPanel = main;
    }
    

    @Override
    public void init(String filename) {
    	
    	
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
					MapEvent me = new MapEvent(x,y,toMap,toX,toY);
					if(x > 0 && x < mapSizeX
			    			&& y > 0 && y < mapSizeY)eventList.add(me);
					else System.err.println("event x or y over");
					
					map[y][x] = 'D';//TODO
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
    	eventList.clear();
    }
    
    @Override
    public void append(GameTask obj) {
    	// TODO 自動生成されたメソッド・スタブ
    	
    }
    
    @Override
    public void done() {
    	// TODO 自動生成されたメソッド・スタブ
    	
    }
    
    
    @Override
    public void update() {
    	// TODO 自動生成されたメソッド・スタブ
    	
    }
    
    public Point getSizeTile(){
    	return new Point(mapSizeX, mapSizeY);
    }
    
    public Point checkHitBlock(int x, int y, int sizeX, int sizeY){
    	int mappointX1 = (x)/BLOCK_SIZE;
    	int mappointX2 = (x+sizeX-1)/BLOCK_SIZE;
    	int mappointY1 = (y)/BLOCK_SIZE;
    	int mappointY2 = (y+sizeY-1)/BLOCK_SIZE;
    	if(mapSizeX < (mappointX1 > mappointX2 ? mappointX1 : mappointX2)
    		|| mapSizeY < (mappointY1 > mappointY2 ? mappointY1 : mappointY2)
    		|| 0 > (mappointX1 < mappointX2 ? mappointX1 : mappointX2)
    		|| 0 > (mappointY1 < mappointY2 ? mappointY1 : mappointY2)){
    		System.err.println("mapover:"+mappointX1+":"+mappointX2+":"+mappointY1+":"+mappointY2);
    		return null;
    	}
    	for(int i=mappointX1; i<=mappointX2; i++)
    		for(int j=mappointY1; j<=mappointY2; j++){
    			if(map[j][i] == 'B') return new Point(i,j);
    		}
    	return null;
    }
    
    public Event checkEvent(double x, double y){
    	int mappointX = (int)x/BLOCK_SIZE;
    	int mappointY = (int)y/BLOCK_SIZE;
    	Iterator<Event> it = eventList.iterator();
    	while(it.hasNext()){
    		Event e = it.next();
    		if(e.eventX == mappointX && e.eventY == mappointY)
    			return e;
    	}
    	
    	return null;
    }
    
    public void draw(Graphics g, int offsetX, int offsetY){
    	int tileFirstX = offsetX/BLOCK_SIZE;
    	int tileFirstY = offsetY/BLOCK_SIZE;
    	int tileLastX = tileFirstX + mainPanel.Width/BLOCK_SIZE + 2;
    	int tileLastY = tileFirstY + mainPanel.Height/BLOCK_SIZE + 2;
    	if(tileLastX > mapSizeX) tileLastX = mapSizeX;
    	if(tileLastY > mapSizeY) tileLastY = mapSizeY;
    	int difX = offsetX%BLOCK_SIZE;
    	int difY = offsetY%BLOCK_SIZE;
    	
    	for(int i=tileFirstX; i<tileLastX; i++)
    		for(int j=tileFirstY; j<tileLastY; j++){
    			g.setColor(Color.ORANGE);
    			switch(map[j][i]){
    			case 'E':
    				mainPanel.addEnemy(new Enemy(i*BLOCK_SIZE, j*BLOCK_SIZE, this));
    				map[j][i] = 0;
    				break;
    			case 'B':
    				g.fillRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, BLOCK_SIZE, BLOCK_SIZE);
    				break;
    			case 'D':
    				g.setColor(Color.BLUE);
    				g.drawRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, BLOCK_SIZE-1, BLOCK_SIZE-1);
    				break;
    			case ' ':
    			}
    		}
    }
}
