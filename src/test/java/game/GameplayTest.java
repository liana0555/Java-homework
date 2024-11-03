package game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import cards.Card;
import game.EventQueue.Event;

public class GameplayTest {

  @Test
  public void runGameNoShuffleTest() {
    var game = new Game();
    game.dispatchDecksNoShuffle();

    while (!game.table.isFinished())
      game.playRound();
  }

  @Test
  public void runGameTest() {
    var game = new Game();
    game.dispatchDecks();

    // while (!game.table.isFinished())
    // game.playRound();
  }

  @Test
  public void countTurnsNoShuffleTest() {
    var game = new Game();
    game.dispatchDecksNoShuffle();

    var turn = 0;
    while (!game.table.isFinished()) {
      game.playRound();
      turn += 1;
    }

    /*
      Since we perform no shuffle,
      Generated deck are equal
      And it will result a draw.
      The whole game will be a war until players run out of cards

      26 starting cards
      1. 26 - 1
      2. 25 - 3
      3. 22 - 3
      4. 19 - 3
      5. 16 - 3
      6. 13 - 3
      7. 10 - 3
      8. 7 - 3
      9. 4 - 3
      10. 1 - 3
    */
    assertEquals(10, turn);
  }

  @Test
  public void countTurnsTest() {
    var game = new Game();
    game.dispatchDecks(1);

    var turn = 0;
    while (!game.table.isFinished()) {

      game.playRound();
      // System.out.println();
      game.events.forEach(e -> {

        // System.out.println("" + e );
        if (e == Event.COMPARE_CARDS)
          // System.out.println("Winner: " + e.winner);
        if (e == Event.COLLECT_CARDS){
          
          // System.out.println("Black: " + e.blackCard.cardIdx);
          // System.out.println("White: " + e.whiteCard.cardIdx);
        }
                  
      });
        // if (game.table.isWar())
          // System.out.println("War is ongoing");
      turn += 1;
    }

    // assertEquals(471, turn);
  }
  // Make sure we dont lose cards during the game
  @Test
  public void countCardsTest() {
    var game = new Game();
    game.dispatchDecks(0);

    while (!game.table.isFinished()) 
      game.playRound();

    assertEquals(0, game.table.invisible.size());
    assertEquals(0, Integer.min(game.table.deckBlack.size(), game.table.deckWhite.size()));
    assertEquals(52, Integer.max(game.table.deckBlack.size(), game.table.deckWhite.size()));
  }
  @Test
  public void finalDeckNullCheckTest() {
    var game = new Game();
    game.dispatchDecks(0);

    while (!game.table.isFinished()) 
      game.playRound();

    game.table.deckBlack.forEach(e -> {
      if (e == null)
        assertTrue(false, "One element in black deck is null");
    });
    game.table.deckWhite.forEach(e -> {
      if (e == null)
        assertTrue(false, "One element in white deck is null");
    });
  }
  @Test
  public void finalDeckUniqueTest() {
    var game = new Game();
    game.dispatchDecks(0);

    while (!game.table.isFinished()) 
      game.playRound();

    var set = new HashSet<String>();

    for ( var vCard : game.table.deckBlack){
      var card = game.registeredCards.get(vCard.cardIdx);
      var path = card.getAssetPath(vCard.suit);
      if (set.contains(path ))
        assertTrue(false, "Black deck contains non unique elements " + path );
      set.add(path);
    }
    for ( var vCard : game.table.deckWhite){
      var card = game.registeredCards.get(vCard.cardIdx);
      var path = card.getAssetPath(vCard.suit);
      if (set.contains(path))
        assertTrue(false, "White deck contains non unique elements " + path );
      set.add(path);
    }
  }
}
