/*
 * Blackjack
 */
 

public class Person {	//Parent Class of Player and Dealer
	
	private String name;				
	private Hand hand;				
	private boolean blackJack;			//boolean indicating if a person got blackjack from drawing
	private boolean alreadyBlackJack;	//boolean indicating if a person got blackjack from first two cards
	private boolean bust;				//boolean indicating if a person went bust (over 21) or not
	private Card crdDrawn;				//Last card a person has drawn

	public Person() {				
		this.hand = new Hand();
		this.name = "";
		blackJack = false;			
		alreadyBlackJack = false;	
		bust = false;
		crdDrawn = null;
	}
	
	public boolean hasBlackJack() {			//returns whether a person has blackjack
		if (this.hand.valueOfHand()==21)	//if value of hand is 21
			return true;
		else
			return false;
	}
	
	public boolean checkBust() {		  //returns whether a person is bust (over 21)
		if (this.hand.valueOfHand()>21) {	//if value of hand is over 21
			return true;
		}
		else
			return false;
	}
	
	
	//Getter (accessor) methods and Setter (mutator) methods
	public Hand getHand() {
		return this.hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean getBlackJack()	{
		return this.blackJack;
	}
	
	public void setBlackJack(boolean blackJack) {
		this.blackJack = blackJack;
	}
	
	public boolean getAlreadyBlackJack() {
		return this.alreadyBlackJack;
	}

	public void setAlreadyBlackJack(boolean alreadyBlackJack) {
		this.alreadyBlackJack = alreadyBlackJack;
	}
	
	public boolean getBust() {
		return bust;
	}
	
	public void setBust(boolean bust) {
		this.bust = bust;
	}
	
	public Card getCrdDrawn() {
		return crdDrawn;
	}
	
	public void setCrdDrawn(Card crdDrawn) {
		this.crdDrawn=crdDrawn;
		
	}
	
	
}
           