package scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class MessagePlayer {
	public final static int FontSizeH = 24;
	public final static int FontSizeW = 16;
	static Image fontImage;
	int vspace;
	int space;
	HashMap<Character, Point> charMap = new HashMap<Character, Point>();
	
	public MessagePlayer(int vspace, int space, String fontfile){
		this.vspace = vspace;
		this.space = space;
		
		if(fontImage == null /*&& fontfile != null*/){
			ImageIcon icon = new ImageIcon(getClass().getResource("/"+fontfile));
			fontImage = icon.getImage();
		}
		createMap();
	}
	
	public void drawChar(Graphics g, int x, int y, char c){
		Point pos = charMap.get(c);
		if(pos == null) g.drawString(""+c, x, y+22);
		else{
			g.drawImage(fontImage, x, y, x+FontSizeW, y+FontSizeH, pos.x, pos.y, pos.x+FontSizeW, pos.y+FontSizeH, null);
		}
	}
	
	public void drawString(Graphics g, int x, int y, StringBuilder[] str){
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, FontSizeW));
		for(int j=0; j<str.length; j++)
			for(int i=0; i<str[j].length(); i++){
				drawChar(g,x+i*(FontSizeW+space),y+j*(FontSizeH+vspace),str[j].charAt(i));
			}
	}
	
	public void drawString(Graphics g, int x, int y, String str){
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, FontSizeW));
		for(int i=0; i<str.length(); i++){
			drawChar(g,x+i*(FontSizeW+space),y,str.charAt(i));
		}
	}
	
	private void createMap(){
		charMap.put('‚ ', new Point(0,0));
		charMap.put('‚¢', new Point(16,0));
		charMap.put('‚¤', new Point(32,0));
		charMap.put('‚¦', new Point(48,0));
		charMap.put('‚¨', new Point(64,0));
		charMap.put('‚©', new Point(80,0));
		charMap.put('‚«', new Point(96,0));
		charMap.put('‚­', new Point(112,0));
		charMap.put('‚¯', new Point(128,0));
		charMap.put('‚±', new Point(144,0));
		charMap.put('‚³', new Point(0,24));
		charMap.put('‚µ', new Point(16,24));
		charMap.put('‚·', new Point(32,24));
		charMap.put('‚¹', new Point(48,24));
		charMap.put('‚»', new Point(64,24));
		charMap.put('‚½', new Point(80,24));
		charMap.put('‚¿', new Point(96,24));
		charMap.put('‚Â', new Point(112,24));
		charMap.put('‚Ä', new Point(128,24));
		charMap.put('‚Æ', new Point(144,24));
		charMap.put('‚È', new Point(0,48));
		charMap.put('‚É', new Point(16,48));
		charMap.put('‚Ê', new Point(32,48));
		charMap.put('‚Ë', new Point(48,48));
		charMap.put('‚Ì', new Point(64,48));
		charMap.put('‚Í', new Point(80,48));
		charMap.put('‚Ð', new Point(96,48));
		charMap.put('‚Ó', new Point(112,48));
		charMap.put('‚Ö', new Point(128,48));
		charMap.put('‚Ù', new Point(144,48));
		charMap.put('‚Ü', new Point(0,72));
		charMap.put('‚Ý', new Point(16,72));
		charMap.put('‚Þ', new Point(32,72));
		charMap.put('‚ß', new Point(48,72));
		charMap.put('‚à', new Point(64,72));
		charMap.put('‚â', new Point(80,72));
		charMap.put('‚ä', new Point(96,72));
		charMap.put('‚æ', new Point(112,72));
		charMap.put('‚í', new Point(128,72));
		charMap.put('‚ð', new Point(144,72));
		charMap.put('‚ç', new Point(0,96));
		charMap.put('‚è', new Point(16,96));
		charMap.put('‚é', new Point(32,96));
		charMap.put('‚ê', new Point(48,96));
		charMap.put('‚ë', new Point(64,96));
		charMap.put('‚ñ', new Point(80,96));
		charMap.put('‚¡', new Point(96,96));
		charMap.put('‚Á', new Point(112,96));
		charMap.put('‚¥', new Point(128,96));
		charMap.put('[', new Point(144,96));
		charMap.put('‚á', new Point(0,120));
		charMap.put('‚ã', new Point(16,120));
		charMap.put('‚å', new Point(32,120));
		charMap.put('A', new Point(48,120));
		charMap.put('B', new Point(64,120));
	}
}
