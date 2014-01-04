package scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import main.KeyWords;
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
    protected static Image mapImage;
    protected static HashMap<Character, Point> chipMap = new HashMap<Character, Point>();
    
    protected int enemyID = 0;
    
    protected Player player;
    protected HashMap<String, Enemy> enemyList = new HashMap<String, Enemy>();
    
    protected List<Event> eventList = new ArrayList<Event>();
    public static final int BLOCK_SIZE = 32;
	private static final int SourceSizeX = 10;
	private static final int SourceSizeY = 10;
    
    public Map(){
    }
    
    public void init(String mapName, String eventName, Player player, int toX, int toY){
    	loadMap(mapName);
    	loadEvent(eventName);
    	this.player = player;
    	player.changeMap(this);
    	player.moveTo(toX, toY);
    	if(mapImage == null){
    		createCmap();
    		loadImage("MapChip.png");
    	}
    }

	public void loadImage(String filename){
		ImageIcon icon = new ImageIcon(getClass().getResource("/"+filename));
		mapImage = icon.getImage();
		
	}
	
	public void createCmap(){
		chipMap.put('1', new Point(0, 0));
		chipMap.put('2', new Point(32, 0));
		chipMap.put('3', new Point(64, 0));
		chipMap.put('4', new Point(0, 32));
		chipMap.put('5', new Point(32, 32));
		chipMap.put('6', new Point(64, 32));
		chipMap.put('7', new Point(0, 64));
		chipMap.put('8', new Point(32, 64));
		chipMap.put('9', new Point(64, 64));
		
		chipMap.put('a', new Point(128,0));
		chipMap.put('b', new Point(128+32,0));
		chipMap.put('c', new Point(128+64,0));
		chipMap.put('d', new Point(128,32));
		chipMap.put('e', new Point(128+32,32));
		chipMap.put('f', new Point(128+64,32));
		chipMap.put('g', new Point(128,64));
		chipMap.put('h', new Point(128+32,64));
		chipMap.put('i', new Point(128+64,64));
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
					boolean deleteOne = false;
					int x = Integer.parseInt(tokens.nextToken());
					int y = Integer.parseInt(tokens.nextToken());
					if(tokens.hasMoreTokens()){
						String tmp = tokens.nextToken();
						if(tmp.equals("d")) deleteOne = true;
					}
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
					TalkEvent te = new TalkEvent(x,y,1,1, list, this, deleteOne); // TODO
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
		Iterator<Enemy> it = enemyList.values().iterator();
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
    
    public boolean checkHitO(int x, int y, int sizeX, int sizeY){
    	int mappointX1 = (x)/BLOCK_SIZE;
    	int mappointX2 = (x+sizeX-1)/BLOCK_SIZE;
    	int mappointY1 = (y)/BLOCK_SIZE;
    	int mappointY2 = (y+sizeY-1)/BLOCK_SIZE;
    	if(mapSizeX <= (mappointX2)
    		|| mapSizeY <= (mappointY2)
    		|| 0 > (mappointX1)
    		|| 0 > (mappointY1)){
    		System.err.println("mapover:"+mappointX1+":"+mappointX2+":"+mappointY1+":"+mappointY2);
    		return false;
    	}
    	if(map[mappointY2][mappointX2] == 'O' || map[mappointY2][mappointX1] == 'O')
    		return true;
    	return false;
    }
    
    public Point checkHitBlock(int x, int y, int sizeX, int sizeY){
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
    			if("45B78Oabcdefghi".indexOf(map[j][i]) != -1) return new Point(i*BLOCK_SIZE,j*BLOCK_SIZE);
    		}
    	return null;
    }
    
    public Point checkHitSlope(int x, int y, int sizeX, int sizeY, int vx, boolean onGround){
    	int mappointX1 = x/BLOCK_SIZE;
    	int mappointY1 = y/BLOCK_SIZE;
    	int mappointX2 = (x+sizeX-1)/BLOCK_SIZE;
    	int mappointY2 = (y+sizeY-1)/BLOCK_SIZE;
    	int b = 0;
    	if(mapSizeX <= (mappointX2)
        		|| mapSizeY <= (mappointY2)
        		|| 0 > (mappointX1)
        		|| 0 > (mappointY1)){
        		System.err.println("mapover:"+mappointX1+":"+mappointX2+":"+mappointY1+":"+mappointY2);
        		return null;
    	}
    	if((map[mappointY2][mappointX2] == 'u' || map[mappointY2][mappointX2] == 'U') 
    			/*&& ( onGround) /*&& (i==mappointX2 && j==mappointY2)*/){
    		b = map[mappointY2][mappointX2] == 'u' ? (x+sizeX-1)%BLOCK_SIZE/2 : (x+sizeX-1)%BLOCK_SIZE/2+BLOCK_SIZE/2;
    		b -= BLOCK_SIZE - (y+sizeY-1)%BLOCK_SIZE;
    		//    				if(y+sizeY>(mappointY2+1)*BLOCK_SIZE) y = (mappointY2+1)*BLOCK_SIZE-sizeY;
    		if(!((y-b+sizeY-1)/BLOCK_SIZE == (y+sizeY-1)/BLOCK_SIZE && (b > 0 || onGround))){
    			return null;
    		}
    	}else if(((map[mappointY2][mappointX1] == 'u' && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE-BLOCK_SIZE/2)>0)
    			|| (map[mappointY2][mappointX1] == 'U' && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE)>=0) )
    			/*&& ( onGround)*/){
    		if(map[mappointY2-1][mappointX2] == 'u')
    			b=(x+sizeX-1)%BLOCK_SIZE/2 + (y+sizeY-1)%BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	}else if(map[(y+sizeY+4)/BLOCK_SIZE][mappointX2] == 'U'){
    		b = (x+sizeX-1)%BLOCK_SIZE/2+BLOCK_SIZE/2 - (2*BLOCK_SIZE - (y+sizeY-1)%BLOCK_SIZE);
//    		return new Point(x, y-b-1);
    		
    	}else if((map[mappointY2][mappointX1] == 's' || map[mappointY2][mappointX1] == 'S') ){
    		b = map[mappointY2][mappointX1] == 'S' ? (x)%BLOCK_SIZE/2 : (x)%BLOCK_SIZE/2+BLOCK_SIZE/2;
    		b = (y+sizeY-1)%BLOCK_SIZE - b;
    		if(!((y-b+sizeY-1)/BLOCK_SIZE == (y+sizeY-1)/BLOCK_SIZE && (b > 0 || onGround))){
    			return null;
    		}
    	}else if(((map[mappointY2][mappointX2] == 's' && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE-BLOCK_SIZE/2)>0)
    			|| (map[mappointY2][mappointX2] == 'S' && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE)>=0))){
    		if(map[mappointY2-1][mappointX1] == 's')
    			b = (BLOCK_SIZE-(x)%BLOCK_SIZE)/2 + (y+sizeY-1)%BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	}else if(map[(y+sizeY+4)/BLOCK_SIZE][mappointX1] == 'S'){
    		b = - (x)%BLOCK_SIZE/2 + (y+sizeY-1)%BLOCK_SIZE - BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	}else return null;
    	
		return new Point(x, y-b-1);
    }
    
    public Point checkHitSlopeForB(int x, int y, int sizeX, int sizeY, int vx, boolean onGround){
    	int mappointX1 = x/BLOCK_SIZE;
    	int mappointY1 = y/BLOCK_SIZE;
    	int mappointX2 = (x+sizeX-1)/BLOCK_SIZE;
    	int mappointY2 = (y+sizeY-1)/BLOCK_SIZE;
    	int b = 0;
    	if(mapSizeX <= (mappointX2)
        		|| mapSizeY <= (mappointY2)
        		|| 0 > (mappointX1)
        		|| 0 > (mappointY1)){
        		System.err.println("mapover:"+mappointX1+":"+mappointX2+":"+mappointY1+":"+mappointY2);
        		return null;
    	}
    	if((map[mappointY2][mappointX2] == 'u' || map[mappointY2][mappointX2] == 'U') 
    			/*&& ( onGround) /*&& (i==mappointX2 && j==mappointY2)*/){
    		b = map[mappointY2][mappointX2] == 'u' ? (x+sizeX-1)%BLOCK_SIZE/2 : (x+sizeX-1)%BLOCK_SIZE/2+BLOCK_SIZE/2;
    		b -= BLOCK_SIZE - (y+sizeY-1)%BLOCK_SIZE;
    		//    				if(y+sizeY>(mappointY2+1)*BLOCK_SIZE) y = (mappointY2+1)*BLOCK_SIZE-sizeY;
    		if(!((y-b+sizeY-1)/BLOCK_SIZE == (y+sizeY-1)/BLOCK_SIZE && (b > 0 || onGround))){
    			return null;
    		}
    	}else if(((map[mappointY2][mappointX1] == 'u' && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE-BLOCK_SIZE/2)>0)
    			|| (map[mappointY2][mappointX1] == 'U' && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE)>=0) )
    			/*&& ( onGround)*/){
    		if(map[mappointY2-1][mappointX2] == 'u')
    			b=(x+sizeX-1)%BLOCK_SIZE/2 + (y+sizeY-1)%BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	
    		
    	}else if((map[mappointY2][mappointX1] == 's' || map[mappointY2][mappointX1] == 'S') ){
    		b = map[mappointY2][mappointX1] == 'S' ? (x)%BLOCK_SIZE/2 : (x)%BLOCK_SIZE/2+BLOCK_SIZE/2;
    		b = (y+sizeY-1)%BLOCK_SIZE - b;
    		if(!((y-b+sizeY-1)/BLOCK_SIZE == (y+sizeY-1)/BLOCK_SIZE && (b > 0 || onGround))){
    			return null;
    		}
    	}else if(((map[mappointY2][mappointX2] == 's' && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE-BLOCK_SIZE/2)>0)
    			|| (map[mappointY2][mappointX2] == 'S' && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE)>=0))){
    		if(map[mappointY2-1][mappointX1] == 's')
    			b = (BLOCK_SIZE-(x)%BLOCK_SIZE)/2 + (y+sizeY-1)%BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	}else return null;
    	
		return new Point(x, y-b-1);
    }
    
    public Event checkEvent(){
    	Iterator<Event> it = eventList.iterator();
    	while(it.hasNext()){
    		Event e = it.next();
    		if(e.checkHit(player)){
    			if(e.getDelete()) it.remove();
    			return e;
    		}
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
		Iterator<Enemy> it = enemyList.values().iterator();
		while(it.hasNext()){
			it.next().draw(g, offsetX, offsetY);
		}
//		player.draw(g, offsetX, offsetY);
		
		// draw map
    	for(int i=tileFirstX; i<tileLastX; i++)
    		for(int j=tileFirstY; j<tileLastY; j++){
    			g.setColor(Color.ORANGE);
    			if(chipMap.containsKey(map[j][i])){
    				Point p = chipMap.get(map[j][i]);
    				g.drawImage(mapImage, i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, (i+1)*BLOCK_SIZE-offsetX, (j+1)*BLOCK_SIZE-offsetY,
    						p.x, p.y, p.x+BLOCK_SIZE, p.y+BLOCK_SIZE, null);
    			}else
    			switch(map[j][i]){
    			case 'E':
    				enemyList.put(KeyWords.ENEMY+enemyID++,new Enemy(i*BLOCK_SIZE, j*BLOCK_SIZE, this));
    				map[j][i] = 0;
    				break;
    			case 'R':
    				enemyList.put(KeyWords.ENEMY+enemyID++,new Enemy(i*BLOCK_SIZE, j*BLOCK_SIZE, this, 1));
    				map[j][i] = 0;
    				break;
    			case 'L':
    				enemyList.put(KeyWords.ENEMY+enemyID++,new Enemy(i*BLOCK_SIZE, j*BLOCK_SIZE, this, -1));
    				map[j][i] = 0;
    				break;
    			case 'B':
    				g.drawImage(mapImage, i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, (i+1)*BLOCK_SIZE-offsetX, (j+1)*BLOCK_SIZE-offsetY,
    						97, 0, 128, 32, null);
//    				g.fillRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, BLOCK_SIZE, BLOCK_SIZE);
    				break;
    			case 'u':
    				int[] x = {i*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX};
    				int[] y = {(j+1)*BLOCK_SIZE-offsetY, (j+1)*BLOCK_SIZE-offsetY, j*BLOCK_SIZE+BLOCK_SIZE/2-offsetY};
    				g.fillPolygon(x, y, 3);
    				break;
    			case 'U':
    				int[] x2 = {i*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX};
    				int[] y2 = {(j+1)*BLOCK_SIZE-offsetY-BLOCK_SIZE/2, (j+1)*BLOCK_SIZE-offsetY-BLOCK_SIZE/2, j*BLOCK_SIZE-offsetY};
    				g.fillPolygon(x2, y2, 3);
    				g.fillRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE+BLOCK_SIZE/2-offsetY, BLOCK_SIZE, BLOCK_SIZE/2);
    				break;
    			case 's':
    				int[] xs = {i*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX, (i)*BLOCK_SIZE-offsetX};
    				int[] ys = {(j+1)*BLOCK_SIZE-offsetY, (j+1)*BLOCK_SIZE-offsetY, j*BLOCK_SIZE+BLOCK_SIZE/2-offsetY};
    				g.fillPolygon(xs, ys, 3);
    				break;
    			case 'S':
    				int[] xs2 = {i*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetX, (i)*BLOCK_SIZE-offsetX};
    				int[] ys2 = {(j+1)*BLOCK_SIZE-offsetY-BLOCK_SIZE/2, (j+1)*BLOCK_SIZE-offsetY-BLOCK_SIZE/2, j*BLOCK_SIZE-offsetY};
    				g.fillPolygon(xs2, ys2, 3);
    				g.fillRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE+BLOCK_SIZE/2-offsetY, BLOCK_SIZE, BLOCK_SIZE/2);
    				break;
    			case 'O':
    				g.fillOval(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, BLOCK_SIZE, BLOCK_SIZE);
    				break;
    			case 'D':
    				g.setColor(Color.BLUE);
    				g.drawRect(i*BLOCK_SIZE-offsetX, j*BLOCK_SIZE-offsetY, BLOCK_SIZE-1, BLOCK_SIZE-1);
    				break;
    			case ' ':
    			}
    		}

		player.draw(g, offsetX, offsetY);
    }
}
