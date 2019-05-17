package Online;

import java.util.ArrayList;
import java.util.Random;

import static Online.Server.*;

public class Card {
    // Fields
    private String type;
    private String color;
    // Constructors
    Card(String typeX, String colorX) {
        type = typeX;
        color = colorX;
    }
    public Card(int typeInt, int colorInt) {
        type = intToCardType(typeInt);
        color = intToColor(colorInt);
    }
    // Accessors
    String getType() {
        return type;
    }
    String getColor() {
        return color;
    }
    public String toString() {
        if(getType().equals("plusFour")) {
            return ("+4 card");
        } else if (getType().equals("color")) {
            return ("Change color card");
        } else {
            if(getType().equals("plusTwo")) {
                return (getColor()+" "+"+2 card");
            } else {
                return(getColor()+" "+getType());
            }
        }
    }
    boolean isSpecial() {
        String type = getType();
        return type.equals("plusFour") || type.equals("color");
    }
    boolean isNumber() {
        String type = getType();
        try {
            Integer.valueOf(type);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    // No mutators because once the value of a card is set it should never be able to be changed
    
    // Static methods
    static String intToColor(int cardColor) {
        switch(cardColor) {
            case 0:
                return "red";
            case 1:
                return "blue";
            case 2:
                return "green";
            case 3:
                return "yellow";
        }
        return null;
    }
    static String intToCardType(int cardType) {
        if(cardType<10) {
            return Integer.toString(cardType);
        } else {
            switch(cardType) {
                case 10:
                    return "plusTwo";
                case 11:
                    return "plusFour";
                case 12:
                    return "color";
                case 13:
                    return "reverse";
            }
        }
        return null;
    }
    static Card generateCard() {
        Random rand = new Random();
        int cardType = rand.nextInt(14);
        int cardColor = rand.nextInt(4);
        String type = intToCardType(cardType);
        String color = intToColor(cardColor);
        return new Card(type, color);
    }
    static Card generateCardNoSpecial() {
        Random rand = new Random();
        int cardType = rand.nextInt(9);
        int cardColor = rand.nextInt(4);
        String type = intToCardType(cardType);
        String color = intToColor(cardColor);
        return new Card(type, color);
    }
    static boolean validCard(Card currentCard, Card cardToCheck) {
        return cardToCheck.getType().equals(currentCard.getType()) || cardToCheck.getColor().equals(currentCard.getColor()) || cardToCheck.isSpecial();
    }
    static ArrayList<Card> validCard(ArrayList<Card> cards) {
        ArrayList<Card> validcards = new ArrayList<>();
        for (Card card : cards) {
            if (Card.validCard(CURRENT_CARD, card)) {
                validcards.add(card);
            }
        }
        return validcards;
    }
}
