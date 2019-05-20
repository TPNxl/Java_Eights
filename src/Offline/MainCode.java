package Offline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainCode {
    // Instance variables
    static final File deckFile = new File("C:\\Users\\timmy\\Documents\\GitHub\\BeginningJava\\src\\CrazyEights\\Uno_deck.txt");
    private static boolean GAME_RUNNING = true;
    static final int CARDS_PER_PERSON = 4;
    static Card CURRENT_CARD;
    static int CURRENT_PLAYER;
    static String CURRENT_DIRECTION = "forward";
    static int CONSOLE_HEIGHT = 60;

    // Players
    static Player p1;
    static Player p2;
    static Player p3;
    static Player p4;

    // Methods
    static String askForColor() {
        Scanner sc = new Scanner(System.in);
        System.out.println("What color do you want to choose?");
        while(true) {
            String input = sc.next().toLowerCase();
            if(input.equals("green") || input.equals("red") || input.equals("yellow") || input.equals("blue")) {
                return input;
            }
            System.err.println("Invalid input.");
        }
    }
    static int askForValidCard(ArrayList<Card> cards) {
        Scanner sc = new Scanner(System.in);
        System.out.println("The cards in your hand are:");
        for(int i = 0; i<cards.size(); i++) {
            Card iCard = cards.get(i);
            System.out.println(i+1+") "+iCard.toString());
        }
        while(true) {
            System.out.println("Enter the number of the card you would like to play.");
            if(sc.hasNextInt()) {
                int cardNum = sc.nextInt()-1;
                if(cardNum<cards.size()&&cardNum>=0) {
                    if(Card.validCard(CURRENT_CARD,cards.get(cardNum))) {
                        return cardNum;
                    } else {
                        System.err.println("You can't play that! That's cheating!");
                    }
                } else {
                    System.err.println("Invalid input.");
                }
            } else {
                sc.next();
                System.err.println("Invalid input.");
            }
        }
    }
    private static Player wonGame() {
        if(p1.cards() == 0) {
            return p1;
        } else if (p2.cards() == 0) {
            return p2;
        } else if (p3.cards() == 0) {
            return p3;
        } else if (p4.cards() == 0) {
            return p4;
        } else {
            return null;
        }
    }
    private static void nextTurn() throws InterruptedException {
        if(CURRENT_PLAYER == 1) {
            System.out.println(p1.turn());
        } else if (CURRENT_PLAYER==2) {
            System.out.println(p2.turn());
        } else if (CURRENT_PLAYER==3) {
            System.out.println(p3.turn());
        } else if (CURRENT_PLAYER==4) {
            System.out.println(p4.turn());
        }
        if(wonGame() != null) {
            System.out.println(wonGame().identifier()+" won the game! Congratulations!");
            GAME_RUNNING = false;
        } else {
            if(Player.currentPlayer().isHuman()) {
                Thread.sleep(1000);
                clear();
            }
            if(CURRENT_DIRECTION.equals("reverse")) {
                if(CURRENT_PLAYER == 1) {
                    CURRENT_PLAYER = 4;
                } else {
                    CURRENT_PLAYER--;
                }
            }
            else {
                if(CURRENT_PLAYER == 4) {
                    CURRENT_PLAYER = 1;
                } else {
                    CURRENT_PLAYER++;
                }
            }
            Thread.sleep(500);
        }

    }
    static void playerTurnMessage(int id) {
        System.out.print("Player "+id+" 's turn ============> Current card: "+CURRENT_CARD);
        if(CURRENT_CARD.getType().equals("color") || CURRENT_CARD.getType().equals("plusFour")) {
            System.out.print("; "+CURRENT_CARD.getColor()+" color");
        }
        System.out.println();
    }
    static void clear() {
        for(int i = 0; i < CONSOLE_HEIGHT; i++) {
            System.out.println();
        }
    }

    // Main code
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Crazy Eights!");
        while(GAME_RUNNING) {
            // Setup of match
            CURRENT_PLAYER = 1;
            CURRENT_CARD = Card.generateCardNoSpecial();
            p1 = new Player(1,"human");
            p2 = new Player(2,"human");
            p3 = new Player(3,"computer");
            p4 = new Player(4,"computer");
            clear();
            // Run the game
            while(true) {
                // Do one turn
                nextTurn();
                // Check if the game is won
                if(!GAME_RUNNING) {
                    break;
                }
            }
            System.exit(0);
        }
    }
}
