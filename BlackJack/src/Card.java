/*
 * Blackjack
 */


import javax.swing.JLabel;

public class Card { //Immutable Class (once a Card Object is created, data fields never change)
	
	//data fields are encapsulated

	private String rank;		//The rank of the Card e.g. Ace,2,3...Jack,Queen,King
	private String suit;		//The suit of the Card e.g. spades,clubs,hearts,diamonds
	private int value; 			//The value of the Card e.g. 2 is 2, 10 and face cards are 10, Ace is either 1 or 11
	private JLabel cardImage; 	//Graphical image of a card
	private int numDeck;		//The deck to which Card belongs (to distinguish cards with same rank, suit and value)

	
	public Card(String rank,String suit, int value, int numDeck, JLabel cardImage) {	//constructor to instantiate Cards with rank,suit,value, numDeck, cardImage
		this.rank=rank;
		this.suit=suit;
		this.value=value;
		this.numDeck=numDeck;
		this.cardImage=cardImage;
	}
	
	//Getter(Accessor) methods for the private instance variables never referring to mutable data fields
	
	public String getRank() {return this.rank;}
	
	public String getSuit() {return this.suit;}
	
	public int getValue() {return this.value;}
	
	public int getNumDeck() {return this.numDeck;}
	
	public JLabel getCardImage() {return this.cardImage;}
	
	//Setter(Mutator) methods omitted (no changes to data fields)
	
	//toString method
	public String toString() {
		return rank + " of " + suit;
	}
	
}





