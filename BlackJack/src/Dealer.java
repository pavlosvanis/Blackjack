/*
 * Blackjack
 */

public class Dealer extends Person {	//Child Class of Person, inheriting global variables and actions
	
	public Dealer() {
		super.setName("Dealer");	//Constructor chaining (calling the constructor of the Person Class)
	}
	
	public void dealerDraws(Deck deck) {		//choosing whether dealer draws a card or not
		
		Card temp=null;
		if (this.getHand().valueOfHand()<17) {		//if hand is valued under 17 
			temp = this.getHand().drawCard(deck);	//dealer draws a card
			this.setCrdDrawn(temp);
	}
	}

}
