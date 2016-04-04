package scene;

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

import main.MainPanel;
import elements.Enemy;
import elements.Event;
import elements.MapEvent;
import elements.Player;
import elements.TalkEvent;

public class Map {
	protected int[][] map;
	protected int[][] mapF;
    protected int mapSizeX;
    protected int mapSizeY;
    protected int offsetX;
    protected int offsetY;
    protected static Image mapImage;
    protected static Image backGroundImage;
    protected static int bgX;
    protected static int bgY;
    
    protected static HashMap<Integer, Integer> chipMap = new HashMap<Integer, Integer>();
    
    protected int enemyID = 0;
    
    protected Player player;
    protected HashMap<String, Enemy> enemyList = new HashMap<String, Enemy>();
    
    protected List<Event> eventList = new ArrayList<Event>();
    public static final int BLOCK_SIZE = 32;
	protected static final int SourceSizeX = 10;
	protected static final int SourceSizeY = 10;
    
    public Map(){
    }
    
    public void init(String mapName, String eventName, Player player, int toX, int toY){
    	int[][][] tmp = UseFile.readFile(mapName);
    	map = tmp[0];
    	mapF = tmp[1];
    	mapSizeX = map[0].length;
    	mapSizeY = map.length;
    	loadEvent(eventName);
    	this.player = player;
    	player.changeMap(this);
    	player.moveTo(toX, toY);
    	if(mapImage == null){
    		createCmap();
    		loadImage("MapChip.png", "background.png");
    		bgX = backGroundImage.getWidth(null);
    		bgY = backGroundImage.getHeight(null);
    	}
    }
	public void loadImage(String filename, String filenameBG){
		ImageIcon icon = new ImageIcon(getClass().getResource("/"+filename));
		mapImage = icon.getImage();

		icon = new ImageIcon(getClass().getResource("/"+filenameBG));
		backGroundImage = icon.getImage();
	}
	
	public Point getBGSize(){
		return new Point(bgX, bgY);
	}
	
	public void createCmap(){
		for(int i=0; i<SourceSizeX*SourceSizeY; i++) chipMap.put(i, -1);
		chipMap.put(-1,0);// 0:当たり判定なし
		chipMap.put(0, 0);
		chipMap.put(1, 0);
		chipMap.put(2, 0);
		chipMap.put(12, 0);
		chipMap.put(22, 0);
		chipMap.put(7, 1);// 1:右上がり坂小
		chipMap.put(8, 2);// 2:右上がり坂大
		chipMap.put(5, 3);// 3:坂下用台
		chipMap.put(27, 3);
		chipMap.put(28, 3);
		chipMap.put(17, 4);// 4:右下がり坂大
		chipMap.put(18, 5);// 5:右下がり坂小
	}
    
    public void setPlayer(Player player){
    	this.player = player;
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
    	if(chipMap.get(map[mappointY2][mappointX2]) == 3 || chipMap.get(map[mappointY2][mappointX1]) == 3)
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
    	for(int i=mappointY1; i<=mappointY2; i++)
    		for(int j=mappointX1; j<=mappointX2; j++){
    			if(chipMap.get(map[i][j]) == -1 || chipMap.get(map[i][j]) == 3) return new Point(j*BLOCK_SIZE,i*BLOCK_SIZE);
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
    	if((chipMap.get(map[mappointY2][mappointX2]) == 1 || chipMap.get(map[mappointY2][mappointX2]) == 2) 
    			/*&& ( onGround) /*&& (i==mappointX2 && j==mappointY2)*/){
    		b = chipMap.get(map[mappointY2][mappointX2]) == 1 ? (x+sizeX-1)%BLOCK_SIZE/2 : (x+sizeX-1)%BLOCK_SIZE/2+BLOCK_SIZE/2;
    		b -= BLOCK_SIZE - (y+sizeY-1)%BLOCK_SIZE;
    		//    				if(y+sizeY>(mappointY2+1)*BLOCK_SIZE) y = (mappointY2+1)*BLOCK_SIZE-sizeY;
    		if(!((y-b+sizeY-1)/BLOCK_SIZE == (y+sizeY-1)/BLOCK_SIZE && (b > 0 || onGround))){
    			return null;
    		}
    	}else if(((chipMap.get(map[mappointY2][mappointX1]) == 1 && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE-BLOCK_SIZE/2)>0)
    			|| (chipMap.get(map[mappointY2][mappointX1]) == 2 && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE)>=0) )
    			/*&& ( onGround)*/){
    		if(chipMap.get(map[mappointY2-1][mappointX2]) == 1)
    			b=(x+sizeX-1)%BLOCK_SIZE/2 + (y+sizeY-1)%BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	}else if(chipMap.get(map[(y+sizeY+4)/BLOCK_SIZE][mappointX2]) == 2){
    		b = (x+sizeX-1)%BLOCK_SIZE/2+BLOCK_SIZE/2 - (2*BLOCK_SIZE - (y+sizeY-1)%BLOCK_SIZE);
//    		return new Point(x, y-b-1);
    	}else if((chipMap.get(map[mappointY2][mappointX1]) == 5 || chipMap.get(map[mappointY2][mappointX1]) == 4) ){
    		b = chipMap.get(map[mappointY2][mappointX1]) == 4 ? (x)%BLOCK_SIZE/2 : (x)%BLOCK_SIZE/2+BLOCK_SIZE/2;
    		b = (y+sizeY-1)%BLOCK_SIZE - b;
    		if(!((y-b+sizeY-1)/BLOCK_SIZE == (y+sizeY-1)/BLOCK_SIZE && (b > 0 || onGround))){
    			return null;
    		}
    	}else if(((chipMap.get(map[mappointY2][mappointX2]) == 5 && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE-BLOCK_SIZE/2)>0)
    			|| (chipMap.get(map[mappointY2][mappointX2]) == 4 && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE)>=0))){
    		if(chipMap.get(map[mappointY2-1][mappointX1]) == 5)
    			b = (BLOCK_SIZE-(x)%BLOCK_SIZE)/2 + (y+sizeY-1)%BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	}else if(chipMap.get(map[(y+sizeY+4)/BLOCK_SIZE][mappointX1]) == 4){
    		b = - (x)%BLOCK_SIZE/2 + (y+sizeY-1)%BLOCK_SIZE - BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	}else{
    		return null;
    	}
    	
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
    	if((chipMap.get(map[mappointY2][mappointX2]) == 1 || chipMap.get(map[mappointY2][mappointX2]) == 2) 
    			/*&& ( onGround) /*&& (i==mappointX2 && j==mappointY2)*/){
    		b = chipMap.get(map[mappointY2][mappointX2]) == 1 ? (x+sizeX-1)%BLOCK_SIZE/2 : (x+sizeX-1)%BLOCK_SIZE/2+BLOCK_SIZE/2;
    		b -= BLOCK_SIZE - (y+sizeY-1)%BLOCK_SIZE;
    		//    				if(y+sizeY>(mappointY2+1)*BLOCK_SIZE) y = (mappointY2+1)*BLOCK_SIZE-sizeY;
    		if(!((y-b+sizeY-1)/BLOCK_SIZE == (y+sizeY-1)/BLOCK_SIZE && (b > 0 || onGround))){
    			return null;
    		}
    	}else if(((chipMap.get(map[mappointY2][mappointX1]) == 1 && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE-BLOCK_SIZE/2)>0)
    			|| (chipMap.get(map[mappointY2][mappointX1]) == 2 && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE)>=0) )
    			/*&& ( onGround)*/){
    		if(chipMap.get(map[mappointY2-1][mappointX2]) == 1)
    			b=(x+sizeX-1)%BLOCK_SIZE/2 + (y+sizeY-1)%BLOCK_SIZE;
//    		return new Point(x, y-b-1);
    	
    		
    	}else if((chipMap.get(map[mappointY2][mappointX1]) == 5 || chipMap.get(map[mappointY2][mappointX1]) == 4) ){
    		b = chipMap.get(map[mappointY2][mappointX1]) == 4 ? (x)%BLOCK_SIZE/2 : (x)%BLOCK_SIZE/2+BLOCK_SIZE/2;
    		b = (y+sizeY-1)%BLOCK_SIZE - b;
    		if(!((y-b+sizeY-1)/BLOCK_SIZE == (y+sizeY-1)/BLOCK_SIZE && (b > 0 || onGround))){
    			return null;
    		}
    	}else if(((chipMap.get(map[mappointY2][mappointX2]) == 5 && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE-BLOCK_SIZE/2)>0)
    			|| (chipMap.get(map[mappointY2][mappointX2]) == 4 && (b=(y+sizeY-1)-mappointY2*BLOCK_SIZE)>=0))){
    		if(chipMap.get(map[mappointY2-1][mappointX1]) == 5)
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
    
    public void draw(Graphics g, int offsetX, int offsetY, int offsetXbg, int offsetYbg){
    	int tileFirstX = offsetX/BLOCK_SIZE;
    	int tileFirstY = offsetY/BLOCK_SIZE;
    	int tileLastX = tileFirstX + MainPanel.Width/BLOCK_SIZE + 2;
    	int tileLastY = tileFirstY + MainPanel.Height/BLOCK_SIZE + 2;
    	if(tileLastX > mapSizeX) tileLastX = mapSizeX;
    	if(tileLastY > mapSizeY) tileLastY = mapSizeY;
    	
    	g.drawImage(backGroundImage, 0, 0, MainPanel.Width+12, MainPanel.Height+12,
    			-offsetXbg, -offsetYbg, MainPanel.Width-offsetXbg+12, MainPanel.Height-offsetYbg+12, null);
    	
		// draw map
		for(int i=0; i<mapSizeY; i++)
			for(int j=0; j<mapSizeX; j++){
				if(map[i][j] != -1){
					int cx = map[i][j]%SourceSizeX;
					int cy = map[i][j]/SourceSizeY;
					g.drawImage(mapImage, j*BLOCK_SIZE-offsetX, i*BLOCK_SIZE-offsetY, (j+1)*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetY,
						cx*BLOCK_SIZE, cy*BLOCK_SIZE, (cx+1)*BLOCK_SIZE, (cy+1)*BLOCK_SIZE, null);
				}
			}

    	// draw elements
		Iterator<Event> ite = eventList.iterator();
		while(ite.hasNext()){
			ite.next().draw(g, offsetX, offsetY);
		}
		Iterator<Enemy> it = enemyList.values().iterator();
		while(it.hasNext()){
			it.next().draw(g, offsetX, offsetY);
		}
		
		player.draw(g, offsetX, offsetY);

		for(int i=0; i<mapSizeY; i++)
			for(int j=0; j<mapSizeX; j++){
				if(mapF[i][j] != -1){
					int cx = mapF[i][j]%SourceSizeX;
					int cy = mapF[i][j]/SourceSizeY;
					g.drawImage(mapImage, j*BLOCK_SIZE-offsetX, i*BLOCK_SIZE-offsetY, (j+1)*BLOCK_SIZE-offsetX, (i+1)*BLOCK_SIZE-offsetY,
						cx*BLOCK_SIZE, cy*BLOCK_SIZE, (cx+1)*BLOCK_SIZE, (cy+1)*BLOCK_SIZE, null);
				}
			}
    }
}
