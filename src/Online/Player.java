package Online;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static Online.Server.*;

public class Player {
    private Random rand = new Random();
    
    // Fields
    private String type;
    private ArrayList<Card> cards = new ArrayList<>();
    private int ID;
    // Constructors
    public Player(int idX, String typeX) {
        ID = idX;
        deal(CARDS_PER_PERSON);
        type = typeX;
    }
    public Player(int idX, int cardsNumber, String typeX) {
        ID = idX;
        deal(cardsNumber);
        type = typeX;
    }
    public Player(int idX, ArrayList<Card> cardsInput, String typeX) {
        ID = idX;
        cards = cardsInput;
        type = typeX;
    }
    // Accessors
    public ArrayList<Card> getCards() {
        return cards;
    }
    public int cards() {
        return cards.size();
    }
    private int getID() { return ID; }
    boolean isHuman() {
        if(type.equals("human")) {
            return true;
        } else {
            return false;
        }
    }
    boolean isComputer() {
        if(!(type.equals("human"))) {
            return true;
        } else {
            return false;
        }
    }
    String identifier() {
        if(isHuman() && CURRENT_PLAYER==ID) {
            return "You";
        } else {
            return "Player "+ID;
        }
    }
    private String identifier(Player p) {
        if(p.isHuman() && CURRENT_PLAYER==p.ID) {
            return "You";
        } else {
            return "Player "+p.getID();
        }
    }
    // Mutators
    public void addCard(Card card) {
        cards.add(card);
    }
    public void removeCard(Card card) {
        for(int i = 0; i < cards.size(); i++) {
            if(cards.get(i).equals(card)) {
                cards.remove(i);
                break;
            }
        }
    }
    public void removeCard(int index) {
        cards.remove(index);
    }
    // Class-specific methods
    String deal(int number) {
        for(int i = 0; i<number; i++) {
            Card dealtCard = Card.generateCard();
            cards.add(dealtCard);
            if(number == 1) {
                return dealtCard.toString();
            }
        }
        return "";
    }
    void play(Card card) throws IOException {
        switch(card.getType()) {
            case "plusTwo":
                CURRENT_CARD = new Card("plusTwo",CURRENT_CARD.getColor());
                nextPlayer().deal(2);
                break;
            case "plusFour":
                CURRENT_CARD = new Card("plusFour",CURRENT_CARD.getColor());
                nextPlayer().deal(4);
                chooseColor("plusFour");
                break;
            case "color":
                CURRENT_CARD = new Card("color",CURRENT_CARD.getColor());
                chooseColor("color");
                break;
            case "reverse":
                CURRENT_CARD = new Card("reverse",CURRENT_CARD.getColor());
                if(CURRENT_DIRECTION.equals("forward")) {
                    CURRENT_DIRECTION = "reverse";
                } else {
                    CURRENT_DIRECTION = "forward";
                }
                break;
            default:
                CURRENT_CARD = card;
                break;
        }
    }
    private String favorableColor() {
        int[] numColors = {0,0,0,0,0};
        String[] colors = {"special","red","blue","green","yellow"};
        for(int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if(card.isSpecial()) {
                numColors[0]++;
            } else if(card.getColor().equals("red")) {
                numColors[1]++;
            } else if (card.getColor().equals("blue")) {
                numColors[2]++;
            } else if (card.getColor().equals("green")) {
                numColors[3]++;
            } else if (card.getColor().equals("yellow")) {
                numColors[4]++;
            }
        }
        String maxColor = "red";
        int max = 0;
        for(int i = 1; i < numColors.length-1; i++) {
            if(numColors[i]>max) {
                max = numColors[i];
                maxColor = colors[i];
            }
        }
        return maxColor;
    }
    private void chooseColor(String type) throws IOException {
        if(isHuman()) {
            CURRENT_CARD = new Card(type, askForColor());
        } else {
            CURRENT_CARD = new Card(type, favorableColor());
        }
    }

    public String turn() throws IOException {
        ArrayList<Card> validcards = Card.validCard(cards);
        String returnStatement = "";
        Card cardPlayed;
        if(isHuman()) {
            playerTurnMessage(ID);
        }
        if(validcards.size() != 0) {
            // Play the card
            if(isComputer()) {
                cardPlayed = validcards.get(rand.nextInt(validcards.size()));
                play(cardPlayed);
            } else {
                cardPlayed = cards.get(askForValidCard(cards));
                play(cardPlayed);
            }
            // Return statements
            returnStatement += identifier()+" played a "+CURRENT_CARD.toString()+". ";
            if(cardPlayed.getType().equals("plusFour")||cardPlayed.getType().equals("plusTwo")) {
                returnStatement += nextPlayer().identifier()+" now has " +nextPlayer().cards()+" cards. " ;
            }
            if(cardPlayed.getType().equals("plusFour")||cardPlayed.getType().equals("color")) {
                returnStatement += identifier()+" changed the color to "+CURRENT_CARD.getColor()+". ";
            }
            if(cardPlayed.getType().equals("reverse")) {
                returnStatement += identifier()+" changed the direction to "+CURRENT_DIRECTION+". ";
            }
            // Remove the card
            cards.remove(cardPlayed);
            // More return statements
            if(ID == 1) {
                returnStatement += "You have "+cards()+" cards left";
            } else {
                returnStatement += "Player "+ID+" has "+cards()+" cards left";
            }
        }
        else {
            if(isHuman()) {
                returnStatement += "You don't have any valid cards. You drew a "+deal(1)+". You now have "+cards()+" cards";
            } else {
                deal(1);
                returnStatement += "Player "+ID+" didn't have any valid cards. Player "+ID+" drew a card. Player "+ID+" now has "+cards()+" cards";
            }
        }
        return returnStatement;
    }

    // Static methods
    static Player nextPlayer() {
        if(CURRENT_DIRECTION.equals("forward")) {
            switch(CURRENT_PLAYER) {
                case 1:
                    return p2;
                case 2:
                    return p3;
                case 3:
                    return p4;
                case 4:
                    return p1;
                default:
                    return null;
            }
        }
        else if(CURRENT_DIRECTION.equals("reverse")) {
            switch(CURRENT_PLAYER) {
                case 1:
                    return p4;
                case 2:
                    return p1;
                case 3:
                    return p2;
                case 4:
                    return p3;
                default:
                    return null;
            }
        } else {
            CURRENT_DIRECTION = "forward";
            return null;
        }
    }
}
