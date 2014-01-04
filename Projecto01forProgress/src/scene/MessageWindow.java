package scene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MessageWindow {
	int wspaceW, wspaceH;
	int charNum, lineNum;
	int x, y, sizeX, sizeY;
	int line;
	boolean next = true;
	boolean fin = true;
	boolean isVisible = false;
	boolean cursorVisible = true;
	boolean nextList = false;
	StringBuilder messages = new StringBuilder();
	StringBuilder[] writeMes;
	MessagePlayer mplayer;
	
	public MessageWindow(int x, int y, int sizeX, int sizeY, String fontfile){
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		wspaceW = 50;
		lineNum = 4;
		int vspace = 10;
		int space = 5;
		wspaceH = (sizeY - lineNum*MessagePlayer.FontSizeH+(lineNum-1)*vspace)/2 - 22;
		mplayer = new MessagePlayer(vspace, space, fontfile);
		charNum = (sizeX - (wspaceW*2 - space)) / (MessagePlayer.FontSizeW + space);
		writeMes = new StringBuilder[lineNum];
		for(int i=0; i<lineNum; i++)
			writeMes[i] = new StringBuilder();
	}
	
	// ��b�⃁�b�Z�[�W���n�܂�O�i�C�x���g��������j�Ɉ�񂾂�(?)�Ă΂�C���b�Z�[�W���Z�b�g����
	public void setTalk(String message){
		messages.delete(0, messages.length());
		line = 0;
		for(int i=0; i<lineNum; i++) writeMes[i].delete(0, writeMes[i].length());
		messages.append(message);
		next = false;
		fin = false;
		if(!isVisible){
			TimerTask task = new MessageTask();
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(task, 0, 66);
		}
		isVisible = true;
	}
	
	// �^�C�}�[�^�X�N�ɂ�����I�ɌĂ΂�C�\�����镶������i�߂�
	public void setCursor(){
		if(messages.toString() == null || messages.length() == 0){
			fin = true;
		}else if(messages.charAt(0) == '\\'){
			messages.deleteCharAt(0);
			char c = messages.charAt(0);
			messages.deleteCharAt(0);
			switch(c){
			case 'n':
				line++;
				setCursor();
				break;
			case 'f':
				next = true;
				break;
			default:
				System.err.println("error code in message.");
				break;
			}
		}else if(writeMes[line].length() < charNum){
			writeMes[line].append(messages.charAt(0));
			messages.deleteCharAt(0);
		}else if(line < lineNum-1){
			line++;
			setCursor();
		}else next = true;
		
	}
	
	// �O���Ń{�^���������ꂽ�Ƃ��ɌĂ΂�C���̃y�[�W�Ɉڂ�
	// ��b�C�x���g���I�����Ă���΁C���b�Z�[�W�������ăE�B���h�E�����
	public void nextPage(){
		if(fin){
			for(int i=0; i<lineNum; i++){
				writeMes[i].delete(0, writeMes[i].length());
			}
			// ���̕ӂ�ŁC�E�B���h�E�����񂾂�����Ă����A�j���[�V�����������������i�^�C�}�[�^�X�N�j
		}else if(next){
			next = false;
			for(int i=0; i<lineNum; i++){
				writeMes[i].delete(0, writeMes[i].length());
			}
			line = 0;
			// �b���L�������ς��ꍇ�Ȃǂ́C�����ŏ������s���\��
		}else if(!next){
			while(!next && !fin){
				setCursor();
			}
		}
	}
	
	public void closeWindow(){
		isVisible = false;
	}
	
	public boolean isFinish(){
		return fin;
	}
	
	
	public boolean isVisible(){
		return isVisible;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setNextList(boolean next){
		nextList = next;
	}
	
	public void draw(Graphics g){
		if(isVisible){
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(10.0f));
			g2.draw(new Rectangle2D.Double(x, y, sizeX, sizeY));
			g2.fillRect(x+15, y+15, sizeX-30, sizeY-30);
			mplayer.drawString(g2, x+wspaceW, y+wspaceH, writeMes);
			
			if(next || (fin && nextList)){
				if(cursorVisible){
					g.setColor(Color.WHITE);
					g.drawRect(x+sizeX-30-20, y+sizeY-30-10, 10, 2);
				}
			}
		}
	}
	
	private class MessageTask extends TimerTask{
		int cursorCount = 0;
		@Override
		public void run() {
			if(!isVisible){
				cancel();
			}
			
			if(!next){
				setCursor();
			}
			if(next || nextList){
				if(cursorCount++ % 10 == 0) cursorVisible = !cursorVisible;
			}
			
		}
	}
	
}
