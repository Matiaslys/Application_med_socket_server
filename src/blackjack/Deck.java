package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    ArrayList<Card> cards = new ArrayList<>();
    public Deck() {
        cards.add(new Card("King",10, "Hearts", 0));
        cards.add(new Card("King",10, "Diamonds", 0));
        cards.add(new Card("King",10, "Spades", 0));
        cards.add(new Card("King",10, "Clubs", 0));
        cards.add(new Card("Queen",10, "Hearts", 0));
        cards.add(new Card("Queen",10, "Diamonds", 0));
        cards.add(new Card("Queen",10, "Spades", 0));
        cards.add(new Card("Queen",10, "Clubs", 0));
        cards.add(new Card("Jack",10, "Hearts", 0));
        cards.add(new Card("Jack",10, "Diamonds", 0));
        cards.add(new Card("Jack",10, "Spades", 0));
        cards.add(new Card("Jack",10, "Clubs", 0));
        cards.add(new Card("10",10, "Hearts", 0));
        cards.add(new Card("10",10, "Diamonds", 0));
        cards.add(new Card("10",10, "Spades", 0));
        cards.add(new Card("10",10, "Clubs", 0));
        cards.add(new Card("9",9, "Hearts", 0));
        cards.add(new Card("9",9, "Diamonds", 0));
        cards.add(new Card("9",9, "Spades", 0));
        cards.add(new Card("9",9, "Clubs", 0));
        cards.add(new Card("8",8, "Hearts", 0));
        cards.add(new Card("8",8, "Diamonds", 0));
        cards.add(new Card("8",8, "Spades", 0));
        cards.add(new Card("8",8, "Clubs", 0));
        cards.add(new Card("7",7, "Hearts", 0));
        cards.add(new Card("7",7, "Diamonds", 0));
        cards.add(new Card("7",7, "Spades", 0));
        cards.add(new Card("7",7, "Clubs", 0));
        cards.add(new Card("6",6, "Hearts", 0));
        cards.add(new Card("6",6, "Diamonds", 0));
        cards.add(new Card("6",6, "Spades", 0));
        cards.add(new Card("6",6, "Clubs", 0));
        cards.add(new Card("5",5, "Hearts", 0));
        cards.add(new Card("5",5, "Diamonds", 0));
        cards.add(new Card("5",5, "Spades", 0));
        cards.add(new Card("5",5, "Clubs", 0));
        cards.add(new Card("4",4, "Hearts", 0));
        cards.add(new Card("4",4, "Diamonds", 0));
        cards.add(new Card("4",4, "Spades", 0));
        cards.add(new Card("4",4, "Clubs", 0));
        cards.add(new Card("3",3, "Hearts", 0));
        cards.add(new Card("3",3, "Diamonds", 0));
        cards.add(new Card("3",3, "Spades", 0));
        cards.add(new Card("3",3, "Clubs", 0));
        cards.add(new Card("2",2, "Hearts", 0));
        cards.add(new Card("2",2, "Diamonds", 0));
        cards.add(new Card("2",2, "Spades", 0));
        cards.add(new Card("2",2, "Clubs", 0));
        cards.add(new Card("ES",1, "Hearts", 0));
        cards.add(new Card("ES",1, "Diamonds", 0));
        cards.add(new Card("ES",1, "Spades", 0));
        cards.add(new Card("ES",1, "Clubs", 0));
        Shuffle();
    }

    public Card Draw() {
    int cardNum;
    Random random = new Random();
    cardNum = random.ints(0,(cards.size())).findFirst().getAsInt();
    Card card = cards.get(cardNum);
    cards.remove(cardNum);
    return card;
    }

    public void Shuffle() {
        Collections.shuffle(cards);
    }
}
