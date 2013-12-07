package main;

import javax.swing.JFrame;

public class Gymnast extends JFrame{
	public Gymnast() {
		this.setTitle("Gymnasts Fantasy");
		
		MainPanel panel = new MainPanel();
		this.add(panel);
		
		pack();
	}
	
	
	public static void main(String[] args){
		Gymnast mainFrame = new Gymnast();
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		
	}

}
