/*
 * Blackjack
 */

import java.util.*;
import java.util.List;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {

	private Deck deck = new Deck();
	private Dealer dealer = new Dealer();		
	private List<Player> players = new LinkedList<Player>();	//linked list that holds players (since number of players differs each time)
	
	private JFrame mainFrm = new JFrame("blackjack");	
	private JPanel tablePnl = new JPanel();					//JPanel with dealerPnl, playerPnl, drawPnl
	private JPanel topPnl = new JPanel();					//JPanel with dataPnl and endBtn
	private JPanel botPnl = new JPanel();					//JPanel with currDataPnl
	private JPanel dealerPnl = new JPanel();				//JPanel with Dealer's cards images
	private JPanel playerPnl = new JPanel();				//JPanel with Player's cards images
	private JPanel drawPnl = new JPanel();					//JPanel with face-down deck image
	private JPanel dataPnl = new JPanel();					//JPanel with data of all players
	private JPanel currDataPnl = new JPanel();				//JPanel with data of current player
	private Color tableColor = new Color(0,81,44);

	private boolean stopGame = false;						//Boolean that ends game when it becomes true
	private JFrame frame = new JFrame();					// a general frame to be used by JOptionPanes

	public Game() throws IOException{
		
		mainFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				//exit window with close
		mainFrm.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrm.setLayout(new BorderLayout());
		
		mainFrm.add(topPnl,BorderLayout.NORTH);
		topPnl.setLayout(new BorderLayout());
		
		JButton endBtn = new JButton(new ImageIcon(ImageIO.read(Game.class.getResource("/endBtn.png"))));	//end button used to end the game and view leaderboard
		endBtn.setBorder(null);					
		topPnl.add(endBtn,BorderLayout.EAST);	
		
		topPnl.add(dataPnl,BorderLayout.WEST);		
		
		mainFrm.add(botPnl,BorderLayout.SOUTH);		//maybe change it somehow not sure yet
		botPnl.setLayout(new BorderLayout());			//name of player, total points, round points
		botPnl.setBackground(tableColor);										//2nd column the answers to these

		
		currDataPnl.setLayout(new GridLayout(5,2,10,10));	//rows, columns, hgap, vgap
		currDataPnl.setBackground(tableColor);
		
		botPnl.add(currDataPnl,BorderLayout.WEST);
		
		mainFrm.add(tablePnl,BorderLayout.CENTER);
		tablePnl.setLayout(new BorderLayout());
		
		
		tablePnl.add(dealerPnl,BorderLayout.NORTH);
		dealerPnl.setLayout(new FlowLayout());
		dealerPnl.setBackground(tableColor);
		
		tablePnl.add(playerPnl,BorderLayout.SOUTH);		
		playerPnl.setLayout(new FlowLayout());
		playerPnl.setBackground(tableColor);
		
		tablePnl.add(drawPnl,BorderLayout.CENTER);
		drawPnl.setLayout(new GridLayout());
		drawPnl.setBackground(tableColor);	
	
		drawPnl.add(new JLabel(new ImageIcon(ImageIO.read(Game.class.getResource("/back.gif")))), BorderLayout.CENTER);	//add JLabel of deck facing down		
		
		createPlayers();
		
		endBtn.addActionListener(new ActionListener(){		//if the end button is pressed 
			public void actionPerformed(ActionEvent e) {
						stopGame = true;					//boolean stopGame becomes true
						try {
							
							determineWinner();						//method to determineWinner and end game runs
							
						} catch (HeadlessException | IOException e1) {
							e1.printStackTrace();
						}									
			}
		} );
		
		while(stopGame==false) {	//while loop to run method while button has not been pressed	
			
			bettingPoints();
			
			dataPnl.removeAll();
			dataPnl.setLayout(new GridLayout(players.size()+1,3,10,0));	//rows, columns, hgap, vgap	
			dataPnl.add(new JLabel("Player's name"));
			dataPnl.add(new JLabel("Total points"));
			dataPnl.add(new JLabel("Round points"));			//points betted for this round	
			dataPnl.revalidate();
			dataPnl.repaint();
			
			for (Player plr: players) {
				dataPnl.add(new JLabel(plr.getName()));							
				dataPnl.add(new JLabel(Double.toString(plr.getTotalPoints())));		//the totalPoints of players change after each round
				dataPnl.add(new JLabel(Double.toString(plr.getRoundPoints())));
			}
			
			dataPnl.revalidate();
			mainFrm.setVisible(true);
			
			
			if (!dealer.getBlackJack()) {	//if the dealer did not get a blackjack the game will follow its order
				playerDecisions();
				dealersTurn();
				setPoints();
			}
			else {
				JOptionPane.showMessageDialog(frame,"The dealer has a blackjack and the round ends early." ,null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/blackjack.png"))));
				setPoints();	//if dealer got blackjack the program will go straight to setting points
			}
				
			
			
			}
	

				
	}
	
	
	public void createPlayers() throws HeadlessException, IOException {			//decide how many players will be playing, giving a name for each
		
		int playersNumber=0;		//number of players for the game
		boolean numGiven = false;	//valid number of players given
		
		String[] options = {"1","2","3","4","5","6","7","8"};	//array of Strings, to select 1-8 number of players
		
		while(!numGiven) {	//loop while user has not selected a valid number of players
			
			try {	//ask user to input playersNumber -> error-prone (user might press cancel instead of selecting number)
				
				playersNumber = Integer.parseInt((String)JOptionPane.showInputDialog(frame,"How many players will be playing?",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/cardPlayer.png"))),options,"1"));	//frame,String for message, String for title, how message is displayed, icon, array of Strings for options, first option shown
				
				numGiven = true;	//if user selects a valid number
				
			} catch (NumberFormatException e) {	//if user does not select a number -> NumberFormatException is thrown 
				
				JOptionPane.showMessageDialog(frame,"Please select a number of players.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/cardPlayer.png")))); 
			} 
		}

		for(int i=0;i<playersNumber;i++) {
			
			players.add(new Player());
		}
	
		
	}
	

	
	public void bettingPoints() throws HeadlessException, IOException {			//players bet roundPoints
		
		dealer.setHand(new Hand());			//set a new hand for dealer every round
		dealer.setBust(false);				//set that dealer has not busted for the beginning of each round
		dealer.setBlackJack(false);			//set that dealer does not have blackjack for the beginning of each round
		
		int numLost = 0; 					//number of players who have lost
		
		for (Player plr: players) {			//go through players list
			
			if(plr.getTotalPoints()>0) {	//player has enough points and can play
				
				plr.setBust(false);			//set for each player, in the beginning of each round, that they have not busted
				plr.setBlackJack(false);	//set for each player, in the beginning of each round, that they do not have a blackjack
				plr.setHand(new Hand());	//set a new hand for each player every round
				int points=0;
				boolean pointsGiven=false;
			
			
		//frame,String for message, String for title, how message is displayed, icon, array of Strings for options, first option shown
				while(!pointsGiven) {
					try {
						
						points = (Integer.parseInt((String)JOptionPane.showInputDialog(frame,"How many points does " + plr.getName() + " want to bet?",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png"))),null,null)));
						if (points<=plr.getTotalPoints()&&points>0)		//if player bets more than zero points and not more points than totalPoints
							pointsGiven = true;
					
						else if(points>plr.getTotalPoints())			//if player bets more points than totalPoints
							JOptionPane.showMessageDialog(frame,"You cannot bet more points than you have.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 
	
						else if(points<=0)							  //if player bets zero or less points
							JOptionPane.showMessageDialog(frame,"You need to bet more than 0 points.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 	
						}	
					catch (NumberFormatException e){				//if the player enters a String or even leaves input empty an exception is thrown
						JOptionPane.showMessageDialog(frame,"Please enter a valid number of points.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 
					}
			
				}
				plr.setRoundPoints(points);											//set the roundPoints that player betted
			
				plr.setTotalPoints(plr.getTotalPoints() - plr.getRoundPoints());	//remove the roundPoints each player bets from the totalPoints
			
				plr.getHand().drawCard(deck);										//give the first two cards to player who betted
				plr.getHand().drawCard(deck);		
			}
			
			
			else if (plr.getTotalPoints()==0) {	//player has zero points and cannot play
					JOptionPane.showMessageDialog(frame,plr.getName() + " does not have any points left and cannot play.", null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 	
					plr.setBust(false);			
					plr.setBlackJack(false);	
					plr.setRoundPoints(0);	//player does not bet any points	
					
					numLost++;				//player has lost
					
					if (numLost==players.size()){	//all players have lost
						JOptionPane.showMessageDialog(frame,"All players have lost. The game ends.", null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 	
						System.exit(0);	//terminate the program			
							
								
					}
			}	
		}
			
		
			dealer.getHand().drawCard(deck);		//give the dealer a card
			dealer.getHand().drawCard(deck);
		
		
			checkNaturalBlackjack();		//checks whether any of the players or the dealer initially has a blackJack
			
			}
	
	public void checkNaturalBlackjack() throws HeadlessException, IOException {	//checks whether any of the players or the dealer got blackJack from first two cards
		for (Player plr: players) {	
			
			if ((plr.hasBlackJack())&&(!dealer.hasBlackJack())) {		//if player has blackJack and dealer does not -> player wins
				
				double pointsWon = 2.5 * plr.getRoundPoints();	//player wins x1.5 of betted points (roundPoints x2.5 as betted roundPoints have bee subtracted from total points)
				plr.setTotalPoints(plr.getTotalPoints() + pointsWon);
				plr.setAlreadyBlackJack(true);
				
				JOptionPane.showMessageDialog(frame,plr.getName() + " has a blackjack. The player won: " + pointsWon,null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 

		
			}
			
			else if (dealer.hasBlackJack()&&(!plr.hasBlackJack())) {	//if dealer has blackjack and player does not -> Dealer wins
				
				//player loses roundPoints (no need to set totalPoints as roundPoints are subtracted from totalPoints when betted)
				
				dealer.setAlreadyBlackJack(true);
				
				JOptionPane.showMessageDialog(frame,"The dealer has a blackjack. " + plr.getName() + " lost: " + plr.getRoundPoints(),null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 

			}
			
			else if (plr.hasBlackJack()&& dealer.hasBlackJack()) {		//if both player and dealer have blackjack -> It is a tie
				
				plr.setTotalPoints(plr.getTotalPoints() + plr.getRoundPoints());	//player gets the roundPoints back
				plr.setAlreadyBlackJack(true);
				dealer.setAlreadyBlackJack(true);
				
				JOptionPane.showMessageDialog(frame,"Both " + plr.getName() + " and the dealer have a blackjack. The player gets his points back",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 
			
			}
			}
	}
	
	
	
	public void playerDecisions() throws IOException{		//players decide whether to hit or stand one by one and then decide whether to continue playing the game or not	
		
		playerPnl.removeAll();
		playerPnl.revalidate();
		playerPnl.repaint();
		
		dealerPnl.removeAll();
		dealerPnl.revalidate();
		dealerPnl.repaint();
		
		if(deck.reshuffle())
			deck = new Deck();
		
			
		dealerPnl.add(dealer.getHand().getInHand().get(0).getCardImage());	//add the first card dealer holds
		dealerPnl.add(new JLabel(new ImageIcon(ImageIO.read(Game.class.getResource("/back.gif")))));	//the second hard will be face down
					
			for (Player plr: players) {
				
				if(plr.getRoundPoints()>0 && !plr.getBlackJack()) {
					
					JOptionPane.showMessageDialog(frame,"Player " + plr.getName() + " will play now.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/cardPlayer.png")))); 
			
					currDataPnl.removeAll();
	
					currDataPnl.add(new JLabel("Current player:"));
					currDataPnl.add(new JLabel(plr.getName()));
				
					currDataPnl.add(new JLabel("Round points:"));
					currDataPnl.add(new JLabel(Double.toString(plr.getRoundPoints())));	//JLabel with RoundPoints as String
				
					currDataPnl.add(new JLabel("Value of hand:"));
					currDataPnl.add(new JLabel(Integer.toString(plr.getHand().valueOfHand()))); //JLabel with handValue as String
				
					currDataPnl.add(new JLabel("Bust:"));
				
					if(plr.getBust()==true) 
						currDataPnl.add(new JLabel("Yes"));
					
					else 
						currDataPnl.add(new JLabel("No"));					
				
					currDataPnl.add(new JLabel("Blackjack: "));
				
					if(plr.getBlackJack()==true)
						currDataPnl.add(new JLabel("Yes"));
				
					else
						currDataPnl.add(new JLabel("No"));
				
				
					playerPnl.removeAll();
				
					for(Card crd: plr.getHand().getInHand()) {	//for every card of the hand of the player
						playerPnl.add(crd.getCardImage());		//add the cardImage to the playerPnl
					}
				
					playerPnl.revalidate();
					playerPnl.repaint();			//needed as we used playerPnl.removeAll();
				
					currDataPnl.revalidate();
					currDataPnl.repaint();		   //needed as we used currDataPnl.removeAll();
				
				
					boolean stopD = false;		//boolean indicating whether player has decided to stop drawing or not
				
					
					do {
						
						try {
							Thread.sleep(1000);				//delay Thread for a second while the player sees his cards
						} catch (InterruptedException e) {
							e.printStackTrace();
						}		
												
						if (!plr.hasBlackJack()&&!plr.checkBust()) {		//player does not have blackjack and is not bust
						
							Object[] options = {"Hit","Stand"};				//array of Strings to choose whether to "Hit" or "Stand"
						
							int plrDecision = JOptionPane.showOptionDialog(frame, "Would " + plr.getName() 
							+ " like to Hit (draw) or Stand (stop drawing)?",null,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(ImageIO.read(Game.class.getResource("/cardPlayer.png"))),options,null);
						
							if (plrDecision == JOptionPane.YES_OPTION) {	//if "Hit" is pressed
								plr.setCrdDrawn(plr.getHand().drawCard(deck));	//draw a card and set it as the crdDrawn	
								stopD = false;								//do not stop drawing
							}
							
							else if (plrDecision == JOptionPane.NO_OPTION)	//if "Stand" is pressed
								stopD=true;									//stop drawing
							
						}
						
						else if(plr.hasBlackJack() && plr.getBlackJack()==false && dealer.getBlackJack()==false){	//if the player has a blackjack, but neither player nor dealer already had blackjack
							
							JOptionPane.showMessageDialog(frame,plr.getName() + " has a blackjack.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/blackjack.png")))); 
							stopD =true;				//stop drawing
							plr.setBlackJack(true);		//set blackjack from drawing boolean to true
						}
						
						else if (plr.checkBust()) {	//if the player went bust (over 21)
							
							JOptionPane.showMessageDialog(frame,plr.getName() + " went bust (over 21). The player lost: " + plr.getRoundPoints() 
							+ " points.", null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 
							
							stopD = true;
							plr.setBust(true);		//set bust to true
						}
						
						
						if (stopD==false && !(plr.getCrdDrawn()==null)) {
							playerPnl.add(plr.getCrdDrawn().getCardImage());		//add the card drawn 	
							playerPnl.revalidate();
							
							currDataPnl.removeAll();
							
							currDataPnl.add(new JLabel("Current player:"));
							currDataPnl.add(new JLabel(plr.getName()));
							
							currDataPnl.add(new JLabel("Round points:"));
							currDataPnl.add(new JLabel(Double.toString(plr.getRoundPoints())));	//JLabel with RoundPoints as String
							
							currDataPnl.add(new JLabel("Value of hand:"));
							currDataPnl.add(new JLabel(Integer.toString(plr.getHand().valueOfHand()))); //JLabel with handValue as String
							
							currDataPnl.add(new JLabel("Bust:"));
							
							if(plr.getBust()==true) 
								currDataPnl.add(new JLabel("Yes"));
							
							else
								currDataPnl.add(new JLabel("No"));
							
							currDataPnl.add(new JLabel("blackjack: "));
						
							if(plr.getBlackJack()==true)
								currDataPnl.add(new JLabel("Yes"));
						
							else
								currDataPnl.add(new JLabel("No"));
							
							
							currDataPnl.revalidate();	//because value of hand changed
							currDataPnl.repaint();
						}
						
						else if (plr.getBust() || plr.getBlackJack()) {
							currDataPnl.removeAll();
							
							currDataPnl.add(new JLabel("Current player:"));
							currDataPnl.add(new JLabel(plr.getName()));
							
							currDataPnl.add(new JLabel("Round points:"));
							currDataPnl.add(new JLabel(Double.toString(plr.getRoundPoints())));	//JLabel with RoundPoints as String
							
							currDataPnl.add(new JLabel("Value of hand:"));
							currDataPnl.add(new JLabel(Integer.toString(plr.getHand().valueOfHand()))); //JLabel with handValue as String
							
							currDataPnl.add(new JLabel("Bust:"));
							
							if(plr.getBust()==true) 
								currDataPnl.add(new JLabel("Yes"));
								
							else
								currDataPnl.add(new JLabel("No"));
							
							currDataPnl.add(new JLabel("blackjack: "));
						
							if(plr.getBlackJack()==true)
								currDataPnl.add(new JLabel("Yes"));
						
							else
								currDataPnl.add(new JLabel("No"));
							
							
							currDataPnl.revalidate();	//because value of hand changed
							currDataPnl.repaint();
							
							if (plr.getBust()==true)	//except from changing the currDataPnl show a messageDialog if player busts
								JOptionPane.showMessageDialog(frame,plr.getName() + " has gone bust and their turn ends.", null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 	

						}
						
				}while(stopD==false);
				
			}
				else if (plr.getRoundPoints()==0)	//player has  betted zero roundPoints
					JOptionPane.showMessageDialog(frame,plr.getName() + " has not betted any points and cannot play.", null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 	

				else if (plr.getBlackJack())		//player has already won blackjack
					JOptionPane.showMessageDialog(frame,plr.getName() + " has already won a blackjack.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/blackjack.png")))); 

					
			}
			

			Object[] options = {"No","Yes"};
			
			int plrDecision = JOptionPane.showOptionDialog(frame, "Would you like to continue playing? If not, press the End button on the top right of the screen.",null,JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(ImageIO.read(Game.class.getResource("/cardPlayer.png"))),options,null);
		
			 if (plrDecision == JOptionPane.YES_OPTION) //If "No" is selected
				try {
					Thread.currentThread().join();		//put thread to sleep until the program ends since we want JOptionPanes to stop appearing
				} catch (InterruptedException e) {
					e.printStackTrace();
				}		
			
		}
		

	
	public void dealersTurn() throws IOException{	//it is the dealer's turn
		
		dealerPnl.removeAll();
		
		JOptionPane.showMessageDialog(frame,"The dealer will play now.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/cardPlayer.png")))); 
				
		for(Card crd: dealer.getHand().getInHand()) {	//for every card of the hand of the dealer
			dealerPnl.add(crd.getCardImage());			//add the cardImage to the dealerPnl, no card is facing down now
			
			try {
				Thread.sleep(1000);		//delay the dealing of dealer's cards
				dealerPnl.revalidate();	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		if (deck.reshuffle())	//When returning true (deck needs reshuffling) -> deck is cleared and instantiated again
			deck = new Deck();
		
		while(dealer.getHand().valueOfHand()<17) {			//while the dealer's hand is valued under 17
			dealer.dealerDraws(deck);						//dealer draws if below 17	
			dealerPnl.add(dealer.getCrdDrawn().getCardImage());
			dealerPnl.revalidate();
		}
		
		
		if (dealer.checkBust())
			dealer.setBust(true);
			
		
		else if(dealer.hasBlackJack() && dealer.getBlackJack()==false) {
			dealer.setBlackJack(true);
		}
		
	}
	
	public void setPoints() throws HeadlessException, IOException {	//Correctly adjust points for players, at the end of the round
		for (Player plr: players) {	//for each Player in the players list
			
			if (!(dealer.getAlreadyBlackJack()||plr.getAlreadyBlackJack())) {	//if player or dealer already have blackjack -> points have been adjusted from checkNaturalBlackjack() method
			
				if ((dealer.hasBlackJack() && !plr.hasBlackJack())) {	//dealer got blackjack from drawing but player did not
					
					//Player's roundPoints have already been subtracted from totalPoints when betted so there is no need to subtract.
				
					JOptionPane.showMessageDialog(frame,"The dealer has a blackjack." + " " + plr.getName() + " lost: " + plr.getRoundPoints(),null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 
				
				}
				else if ((dealer.hasBlackJack() && plr.hasBlackJack())) {			//both dealer and player got blackjack from drawing
				
					plr.setTotalPoints(plr.getTotalPoints() + plr.getRoundPoints());	//player gets the roundPoints back
					JOptionPane.showMessageDialog(frame,"Both " + plr.getName() + " and the dealer have a blackjack. The player gets his points back.",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 
					
				}
			
				else if ((!dealer.hasBlackJack() && plr.hasBlackJack())) {	//player got a blackjack from drawing and dealer did not
				
					double pointsWon = 2.5 * plr.getRoundPoints();				//player wins 1.5 times the betted roundPoints (multiply by 2.5 because roundPoints were subtracted from totalPoints when betted)
					plr.setTotalPoints(plr.getTotalPoints() + pointsWon);
				
					JOptionPane.showMessageDialog(frame, plr.getName() + " has a blackjack. The player won: " + pointsWon,null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 
				}
			

			
				else if ((dealer.getHand().valueOfHand()>plr.getHand().valueOfHand())&&(!dealer.getBust())) {	//dealer's hand greater than player's and dealer not bust
					
					//Player's roundPoints have already been subtracted from totalPoints when betted so there is no need to subtract.
					JOptionPane.showMessageDialog(frame, plr.getName() + " has a lower hand than the dealer's " + dealer.getHand().valueOfHand() + ". The player lost: " + plr.getRoundPoints(),null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 
				}
			
				else if ((dealer.getHand().valueOfHand()<plr.getHand().valueOfHand())&&(!plr.getBust())) {	//player's hand greater than dealer's and player not bust
				
					double pointsWon = 2*plr.getRoundPoints();					//player wins the amount of roundPoints betted (multiply by 2 because roundPoints were subtracted from totalPoints when betted)
					plr.setTotalPoints(plr.getTotalPoints() + pointsWon);

					JOptionPane.showMessageDialog(frame, plr.getName() + " has a greater hand than the dealer's " + dealer.getHand().valueOfHand() + ".  The player won: " + pointsWon,null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 
				}
			
				else if (dealer.getHand().valueOfHand()==plr.getHand().valueOfHand()) {		//player's hand equal to dealer's
				
					plr.setTotalPoints(plr.getTotalPoints() + plr.getRoundPoints());	//player gets the roundPoints back
				
					JOptionPane.showMessageDialog(frame,plr.getName() + " has an equal hand to the dealer's. The player gets the betted round points back",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 

				}
			
			
				else if (plr.getBust()) {	//player busted 
				
					JOptionPane.showMessageDialog(frame,plr.getName() + " busted (went over 21). The player lost: " + plr.getRoundPoints() + " points",null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/bust.png")))); 
				}
			
				else if(dealer.getBust()&&!plr.getBust()) {		//dealer busted but player did not
				
				
					double pointsWon = 2*plr.getRoundPoints();				//player wins the amount of roundPoints betted (multiply by 2 because roundPoints were subtracted from totalPoints when betted)
					plr.setTotalPoints(plr.getTotalPoints() + pointsWon);	

					JOptionPane.showMessageDialog(frame,"The dealer busted. He gives points to all players who haven't gone over 21. " + plr.getName() + " won: " + pointsWon,null,JOptionPane.PLAIN_MESSAGE,new ImageIcon(ImageIO.read(Game.class.getResource("/points.png")))); 
				}		
			
			}	
		}  
	}
	
	public void determineWinner() throws HeadlessException, IOException {
		
		
		mainFrm.dispose();
	
		
		for (Player plr: players) 
			plr.setTotalPoints(plr.getTotalPoints()+plr.getRoundPoints()); 	//total points should include round points of round that has not finished
			
			
		
		Collections.sort(players, new Comparator<Player>() { //sort players according to totalPoints in ascending order
			public int compare(Player p1, Player p2) {
				return p1.getTotalPoints()<p2.getTotalPoints()?-1:p1.getTotalPoints()>p2.getTotalPoints()?1:0;
			}
		});
		
		Collections.reverse(players);	//change the sorted list to descending order
		
		
		
		JFrame endFrm = new JFrame("Leaderboard");
		endFrm.setLayout(new BorderLayout());
		endFrm.setBackground(tableColor);
		endFrm.setSize(626,417);				
		endFrm.setLocationRelativeTo(null);						//centered
		endFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//exit window with close
		endFrm.setResizable(false);	
		
		JPanel ldrPnl = new JPanel();
		endFrm.add(ldrPnl,BorderLayout.CENTER);
		
		ldrPnl.setLayout(new GridLayout(players.size()+1,3,8,8));	//rows equal to number of players + 1, 3 columns, horizotal gap, vertical gap
		ldrPnl.add(new JLabel("     "));
		ldrPnl.add(new JLabel("Player"));
		ldrPnl.add(new JLabel("Points"));
		ldrPnl.setBackground(tableColor);
		
		for (int i=1;i<=players.size();i++) {
			int k = i-1;
			ldrPnl.add(new JLabel(new StringBuilder().append(i).append(".").toString()));	
			ldrPnl.add(new JLabel(players.get(k).getName()));
			ldrPnl.add(new JLabel(Double.toString(players.get(k).getTotalPoints())));			
		}
		
		JPanel endSouthPnl = new JPanel();
		endSouthPnl.setLayout(new BorderLayout());
		endSouthPnl.setBackground(tableColor);
		
		endFrm.add(endSouthPnl,BorderLayout.SOUTH);
		
		int numWinners = 0; 								//number holding how many people have won
		double maxPoints = players.get(0).getTotalPoints();	//most points a player has (list is sorted in descending order)
		
		for (Player plr: players) {					//for-each loop going through every Player on the list
			if (plr.getTotalPoints()==maxPoints)	//if a Player has the same number of points as maxPoints
				numWinners++;			
		}
		
		
			if(numWinners ==1)	//if one player won
				endSouthPnl.add((new JLabel("The winner is " + players.get(0).getName() + " with a total of " + players.get(0).getTotalPoints() + " points.")),BorderLayout.WEST);
		
		
			else	//if a number of players won
				endSouthPnl.add((new JLabel("There are " + numWinners + " winners with a total of " + players.get(0).getTotalPoints() + " points.")),BorderLayout.WEST);
		
		JButton quitBtn = new JButton(new ImageIcon(ImageIO.read(Game.class.getResource("/quitBtn.png"))));
		quitBtn.setBorder(null);					
		endSouthPnl.add(quitBtn,BorderLayout.EAST);	
		
		endFrm.setVisible(true);
		
		
		quitBtn.addActionListener(new ActionListener(){		//if the quit button is pressed 
			public void actionPerformed(ActionEvent e) {
						System.exit(0);	//terminate the program								
			}
		} );
			

	}
	
	
	public static void main(String []args) throws IOException {	
		new Game();
	}
	
		
	}
	
	

