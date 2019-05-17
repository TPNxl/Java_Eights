package Online;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

public class Server extends Thread {

    static DataInputStream in;
    static DataOutputStream out;
    private ServerSocket serverSocket;
    // Instance variables
    static final File deckFile = new File("C:\\Users\\timmy\\Documents\\GitHub\\BeginningJava\\src\\CrazyEights\\Uno_deck.txt");
    private static boolean GAME_RUNNING = true;
    static final int CARDS_PER_PERSON = 4;
    static Card CURRENT_CARD;
    static int CURRENT_PLAYER;
    static String CURRENT_DIRECTION = "forward";

    // Players
    static Player p1;
    static Player p2;
    static Player p3;
    static Player p4;

    // Methods
    static String askForColor() throws IOException {
        Reader rd = new Reader();;
        out.writeUTF("What color do you want to choose?\n");
        while(true) {
            String input = rd.next().toLowerCase();
            if(input.equals("green") || input.equals("red") || input.equals("yellow") || input.equals("blue")) {
                return input;
            }
            out.writeUTF("Invalid input.\n");
        }
    }
    static int askForValidCard(ArrayList<Card> cards) throws IOException {
        Reader rd = new Reader();;
        out.writeUTF("The cards in your hand are:\n");
        for(int i = 0; i<cards.size(); i++) {
            Card iCard = cards.get(i);
            out.writeUTF(i+1+") "+iCard.toString()+"\n");
        }
        while(true) {
            out.writeUTF("Enter which card you would like to play.\n");
            while (true) {
                int cardNum = rd.nextInt()-1;
                if(cardNum<=cards.size()&&cardNum>=0) {
                    if(Card.validCard(CURRENT_CARD,cards.get(cardNum))) {
                        return cardNum;
                    } else {
                        out.writeUTF("You can't play that! That's cheating!\n");
                    }
                } else {
                    out.writeUTF("Invalid input.\n");
                }
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
    private static void nextTurn() throws IOException {
        if(CURRENT_PLAYER == 1) {
            out.writeUTF(p1.turn()+"\n");
        } else if (CURRENT_PLAYER==2) {
            out.writeUTF(p2.turn()+"\n");
        } else if (CURRENT_PLAYER==3) {
            out.writeUTF(p3.turn()+"\n");
        } else if (CURRENT_PLAYER==4) {
            out.writeUTF(p4.turn()+"\n");
        }
        if(wonGame() != null) {
            out.writeUTF(wonGame().identifier()+" won the game! Congratulations!\n");
            GAME_RUNNING = false;
        } else {
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
        }

    }
    static void playerTurnMessage(int id) throws IOException {
        out.writeUTF("Player "+id+" 's turn ============> Current card: "+CURRENT_CARD+"\n");
    }

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(60000);
    }
    public void run() {
        while(true) {
            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                in = new DataInputStream(server.getInputStream());
                out = new DataOutputStream(server.getOutputStream());
                while(GAME_RUNNING) {
                    // Setup of match
                    System.out.println("Setting up a standard game with 4 players and "+CARDS_PER_PERSON+" cards each...");
                    CURRENT_PLAYER = 1;
                    CURRENT_CARD = Card.generateCardNoSpecial();
                    p1 = new Player(1,"computer");
                    p2 = new Player(2,"computer");
                    p3 = new Player(3,"computer");
                    p4 = new Player(4,"computer");
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

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    // Main code
    public static void main(String[] args) throws IOException {
        Reader rd = new Reader();;
        System.out.println("Welcome to Crazy Eights!");
        int port = 6066;
        try {
            Thread t = new Server(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}