/*
 * Blackjack
 */

import java.awt.HeadlessException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Player extends Person{ //Child Class of Person, inheriting global variables and actions
	
	private double totalPoints;		//total points a player holds
	private double roundPoints;		//points bet in this round

	public Player() throws HeadlessException, IOException {
		
		String name="";
		boolean nameGiven=false;
		
		while(!nameGiven) {	//while loop to ensure that name given is neither empty nor null
			name = (String)JOptionPane.showInputDialog(new JFrame(),"What is the player's name?",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Player.class.getResource("/cardPlayer.png"))),null,null);
	
			if (name==null||name.isEmpty()||String.valueOf(name.charAt(0)).equals(" ")) {	//if the name is null, empty or a space is entered
				JOptionPane.showMessageDialog(new JFrame(),"Please enter a name for the player.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Player.class.getResource("/cardPlayer.png")))); 
			}
		
			else
				nameGiven = true;
		}
	

		super.setName(name);	//Constructor chaining (calling the constructor of the Person Class)
		
		totalPoints = 100; 	//player begins with 100 totalPoints
	}
	
	
	public double getTotalPoints() {	//returns how many total points a player has
		return this.totalPoints;
	}
	
	
	public double getRoundPoints() {	//returns how many points a player bets in a round
		return this.roundPoints;
	}
	
	public void setRoundPoints(double roundPoints) {	//sets how many points a player set in a round
		this.roundPoints = roundPoints;
	}
	
	public void setTotalPoints(double totalPoints) {	//sets how many totalPoints a player has
		this.totalPoints = totalPoints;
	}
	
}
