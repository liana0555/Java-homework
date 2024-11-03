package game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import cards.Card;

public class GameTest {

  @Test
  public void registerDefaultCardsTest() {
    var game = new Game();
    assertEquals(13, game.registeredCards.size());
  }

  @Test
  public void registerNoCardsTest() {
    var game = new Game(new Stack<Card>());
    assertEquals(0, game.registeredCards.size());
  }

  @Test
  public void registerCustomCardsTest() {
    class MyCustomCard extends Card {
    }
    var game = new Game(new Card[] { new MyCustomCard(), new MyCustomCard() });
    assertEquals(2, game.registeredCards.size());
  }

  @Test
  public void dispatchDecksLengthTest(){
    var game = new Game();

    game.dispatchDecks();

    assertEquals(game.table.deckWhite.size(), game.table.deckBlack.size());
    assertEquals(26, game.table.deckBlack.size());
    assertEquals(26, game.table.deckWhite.size());
  }
  @Test
  public void dispatchDecksNoShuffleFullControlTest(){
    var game = new Game();

    game.dispatchDecksNoShuffle();

    var deckBlack = new int[game.table.deckBlack.size()];
    var deckWhite = new int[game.table.deckWhite.size()];

    for (var i = 0; i < deckBlack.length; i++){
      deckBlack[i] = game.table.deckBlack.poll().cardIdx;
      deckWhite[i] = game.table.deckWhite.poll().cardIdx;
     }
    
    assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},  deckBlack);
    assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},  deckWhite);
    assertArrayEquals(deckBlack, deckWhite);
  }

  @Test
  public void dispatchDecksUniqueElementTest(){
    var game = new Game();

    game.dispatchDecks();

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
