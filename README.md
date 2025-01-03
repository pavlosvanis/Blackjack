# Blackjack

A Java-based implementation of the classic card game, Blackjack. This project allows players to compete against a computer dealer, adhering to standard Blackjack rules, with an intuitive graphical user interface designed for an engaging experience.

> **Note**: This project was created during my initial stages of learning Java and serves as an example of how I applied core concepts like OOP, GUI design, and game logic early in my development journey.

---

## Features

- **Gameplay**:
  - Supports 1–8 players in a single game.
  - Implements standard Blackjack rules:
    - Players can hit, stand, or quit.
    - Aces dynamically adjust between values of 1 and 11.
    - Dealer stands at 17 or higher.
  - Automatically reshuffles the deck when fewer than 52 cards remain.
  - Tracks player scores and displays a dynamic leaderboard.

- **User-Friendly Interface**:
  - Built with Java Swing for an intuitive GUI.
  - Displays player actions, dealer actions, and scores.
  - Provides a final leaderboard with rankings at the end of the game.

- **Error Handling**:
  - Prevents invalid inputs and provides error messages.

---

## Installation

1. **Clone the repository**:
    git clone https://github.com/pavlosvanis/Blackjack.git
    cd Blackjack

2. **Import into an IDE**:
- Open your Java IDE.
- Import the `Blackjack` project folder.

3. **Run the game**:
- Execute the `Game.java` file to start the game.

---

## Documentation

Detailed documentation is available in the **Documentation** folder:

- [Requirements Document](./Documentation/Requirements.pdf): Outlines the goals, functionality, and success criteria.
- [Design Document](./Documentation/Design.pdf): Details technical aspects, including UML diagrams, use cases, and key algorithms.

---

## Future Enhancements

1. **Display Dealer's Data**:
    - Display the dealer’s data (e.g., points, actions) during their turn, similar to the current player's data display in the bottom left of the GUI.

2. **Restart Game Option**:
   - Add functionality to allow players to restart the game after viewing the leaderboard instead of exiting completely.

3. **Implement a Timer for Player Decisions**:
   - Introduce a timer to ensure players make decisions (e.g., hit or stand) within a specified timeframe. If time expires, the player automatically stands.

4. **Display Probability of Busting**:
   - Calculate and display the probability of busting (going over 21) based on the current hand.

5. **Improved Game Termination**:
   - Allow the game to be exited at any point, not just after a round. Replace modal input dialogs to streamline this feature.

6. **Multiplayer Online Functionality**:
   - Add the ability for multiple players to connect and play online via a server.

7. **Mobile Adaptation**:
   - Port the game to mobile platforms using Android Studio, ensuring compatibility and an optimized user experience for smaller screens.

8. **Enhanced GUI**:
   - Improve the graphical user interface with animations, better card visuals, and a more modern design to increase engagement.


---

## Contribution

Contributions are welcome! If you’d like to suggest improvements or report issues, please create a pull request or open an issue in this repository.

---

## License

This project is licensed under the [MIT License](./LICENSE). See the LICENSE file for details.
