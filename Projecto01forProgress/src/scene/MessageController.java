package scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

public class MessageController {
	private static final int IMAGE_SIZE = 128;
	private MessageWindow window;
	private Image charaImage;
	private List<String[]> talkList;
	private Iterator<String[]> talkIt;
	private String[] talk = new String[2];
	private HashMap<String, Point> imageMap = new HashMap<String, Point>();
	private boolean finTalk = true;
	
	public MessageController(MessageWindow window, String imageName){
		this.window = window;
		ImageIcon icon = new ImageIcon(getClass().getResource("/"+imageName));
		charaImage = icon.getImage();
		setImage();
	}
	
	public void setImage(){
		imageMap.put("chara01", new Point(0,0));
		imageMap.put("chara02", new Point(128,0));
		imageMap.put("chara03", new Point(256,0));
		imageMap.put("chara04", new Point(384,0));
	}
	
	public void setTalkList(List<String[]> talkList){
		this.talkList = talkList;
		talkIt = this.talkList.iterator();
		if(talkIt.hasNext()){
			talk = talkIt.next();
			window.setTalk(talk[1]);
			if(talkIt.hasNext())window.setNextList(true);
			else window.setNextList(false);
			finTalk = false;
		}else finTalk = true;
	}
	
	public void nextTalk(){
		if(!window.isFinish()) window.nextPage();
		else if(talkIt.hasNext()){
			talk = talkIt.next();
			window.setTalk(talk[1]);
			if(talkIt.hasNext())window.setNextList(true);
			else window.setNextList(false);
		}else{
			window.closeWindow();
			finTalk = true;
		}
	}
	
	public boolean isFinish(){
		return finTalk;
	}
	
	public void draw(Graphics g, boolean bottom){
		if(window.isVisible()){
			Graphics2D g2 = (Graphics2D)g;
			if(!bottom) window.setY(20);
			else window.setY(600 - 200);
			window.draw(g2);
			Point p = imageMap.get(talk[0]);
			if(p != null){
				int x = 580, y = bottom ? 300 : 170;
				int y2 = bottom ? y-20 : y-64;
				g2.setColor(Color.BLACK);
				g2.drawImage(charaImage, x, y, x+IMAGE_SIZE, y+IMAGE_SIZE, p.x, p.y, p.x+IMAGE_SIZE, p.y+IMAGE_SIZE, null);
				g2.drawOval(x, y, IMAGE_SIZE, IMAGE_SIZE);
				g2.fillRoundRect(x-145, y2+90, 150, 30, 10, 10);
				window.mplayer.drawString(g, x-140, y2+85, talk[0]);
			}

		}

	}

}
