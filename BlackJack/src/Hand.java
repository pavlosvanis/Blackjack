/*
 * Blackjack
 */

import java.util.ArrayList;

public class Hand {
	private ArrayList<Card> inHand;	//ArrayList holding the cards of the hand

	
	
	public Hand() {
		inHand = new ArrayList<Card>();		
	
	}


	public Card drawCard(Deck deck) {
		
		Card crdTemp = deck.getMasterDeck().get(deck.getMasterDeck().size()-1);	//the top card from masterDeck 

		inHand.add(crdTemp); 													//add the top card from masterDeck to inHand 
		
		deck.getMasterDeck().remove(deck.getMasterDeck().size()-1);				//remove the top card from masterDeck
		
		return crdTemp;															//Returns the drawn card from the deck		
		
	}
		
	public int valueOfHand(){		//returns the total value of a hand (adjustments for ace's values are made)
				
		int total = calculateValue();
		int aceNum = searchAces(); 		
		
		while (( total>21) && (aceNum>=1)) { //if hand's value is over 21 and contains at least an Ace (whose value has not been changed)	
			for (Card crd: inHand) {	
				if (crd.getRank().equals("Ace")) { 	
					total = total - 10;				//Change Ace's value from 11 to 1 (by reducing total by 10)
					aceNum --;					    //Since Ace's value has been changed -> reduce aceNum by 1
					break;						   //Ensure only the value of the first ace found changes each time
				}											
			}
		}
		
		return total;
		
	}
		
	
	public int calculateValue() {		//returns the sum of the cards' values in a hand (no adjustments for Aces)
		int totalVal = 0;				
					
		for (Card crd: inHand) {		//for each card in the ArrayList inHand that contains all of hand's cards
			
			totalVal = totalVal + crd.getValue(); //total value is the sum of all cards' values
		}	
		
		return totalVal;
	}
	
	public int searchAces() {			//returns the number of Aces in a hand
		
		int aceNum = 0;					//number of Aces in a hand
		for (Card crd: inHand) {		//for each card in the ArrayList inHand
			
			if (crd.getRank().equals("Ace"))	//if a card's rank is "Ace"
				aceNum++;									
		}
		
		return aceNum;
	}
	
	public ArrayList<Card> getInHand() {		//access inHand ArrayList from other classes
		return inHand;
	}
	
}
