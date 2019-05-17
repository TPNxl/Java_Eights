package Offline;

import java.util.ArrayList;
import java.util.Random;

import static Offline.MainCode.*;

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
        String returnStatement = "";
        switch(color) {
            case "red":
                returnStatement += "\u001b[31m";
                break;
            case "blue":
                returnStatement += "\u001b[34m";
                break;
            case "yellow":
                returnStatement += "\u001b[33m";
                break;
            case "green":
                returnStatement += "\u001b[32m";
                break;
            default:
                break;
        }
        returnStatement += color;
        returnStatement += "\u001b[0m";
        return returnStatement;
    }
    public String toString() {
        String returnStatement = "";
        if(isSpecial()) {
            returnStatement += "\u001b[35m";
        }
        else {
            switch(color) {
                case "red":
                    returnStatement += "\u001b[31m";
                    break;
                case "blue":
                    returnStatement += "\u001b[34m";
                    break;
                case "yellow":
                    returnStatement += "\u001b[33m";
                    break;
                case "green":
                    returnStatement += "\u001b[32m";
                    break;
                default:
                    break;
            }
        }
        if(getType().equals("plusFour")) {
            returnStatement += ("+4 card");
        } else if (getType().equals("color")) {
            returnStatement += ("Change color card");
        } else {
            if(getType().equals("plusTwo")) {
                returnStatement += (getColor()+" "+"+2 card");
            } else {
                returnStatement += (getColor()+" "+getType());
            }
        }
        returnStatement += "\u001b[0m";
        return returnStatement;
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
