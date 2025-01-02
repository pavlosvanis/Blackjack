/*
 * Blackjack
 */

import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Deck {
	private ArrayList<Card> masterDeck; //the masterDeck is an ArrayList of six standard 52-card decks
	
	
	public Deck() throws IOException{
		String[] rank = {"2","3","4","5","6","7","8","9","10","Jack","Queen","King","Ace"};	//Array of Strings holding ranks of Cards
		String[] suit = {"Clubs","Diamonds","Hearts","Spades"};								//Array of Strings holding suits of Cards
		int[] value = {2,3,4,5,6,7,8,9,10,10,10,10,11};										//Array of integers holding values of Cards
																							//and each cell has the value of corresponding rank in the rank array
		
		JLabel cJLab;																		//JLabel used to hold the image of each Card
		
		masterDeck = new ArrayList<Card>();
		char suitChar;
		char rankChar;
		String path;
		
		
		for (int i=1;i<=6;i++) {	//loop that will run six times since masterDeck is 6 52 standard decks
			for(int s=0;s<4;s++) {	//go through all suits
			
				for(int r=0;r<13;r++) {	//go through all ranks
					
					//Image files are named "rankChar+suitChar.gif" -> for example a 2 of Clubs has an image "2C.gif"
					
					if(rank[r].equals("10"))			//only for cards with rank 10, 'T' was used as rankChar
						rankChar = 'T';
					else
						rankChar = rank[r].charAt(0);   //for every other card the first character of the rank is simply used for rankChar
					
					suitChar = suit[s].charAt(0);	    //for all cards the first character of the suit is used as suitChar
					
					
					
					path = new StringBuilder().append("/").append(rankChar).append(suitChar).append(".gif").toString();	 //path finding image of each card
					
					BufferedImage cImage = ImageIO.read(Deck.class.getResource(path));	//using libraries to input image file 
					cJLab = new JLabel(new ImageIcon(cImage));							//changing image into JLabel					
					
					masterDeck.add(new Card(rank[r],suit[s],value[r],i,cJLab));	//instantiate card objects to be inserted in the ArrayList deck
				}
			}
		}
				
			Collections.shuffle(masterDeck); //shuffle the masterDeck ArrayList using collections
					
		}
	
	
	
		

	public boolean reshuffle() { //method returning whether deck needs reshuffling
		
		if (masterDeck.size()>52)	//if the deck has more than 52 Cards
			return false;		
		
		else {					  //if the deck has 52 or less Cards
			
			masterDeck.clear();   	//Clear the deck
			return true;	
		}
	}
	
	
	
	
	
	
	//Getter(Accessor) and Setter(Mutator) methods for masterDeck
	
	public void setMasterDeck(ArrayList<Card> masterDeck) {this.masterDeck= masterDeck;}
	
	public ArrayList<Card> getMasterDeck() {return this.masterDeck;}
	
}
