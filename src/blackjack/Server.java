package blackjack;

import tic_tac_toe.TicTacToeConstants;
import tic_tac_toe.TicTacToeServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Server{
    private static boolean stillplaying = true;
    private static ArrayList<ObjectInputStream> fromPlayers = new ArrayList<>();
    private static ArrayList<ObjectOutputStream> toPlayers = new ArrayList<>();
    private static ArrayList<Card> dealersHand = new ArrayList<>();
    private static ArrayList<Card> hand = new ArrayList<>();
    private static Deck deck = new Deck();
    public static void main(String[] args) {
        new Thread( () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                System.out.println(new Date() + ": Server started at socket 8000\n");
                while (true) {

                    // Connect to player 1
                    Socket player1 = serverSocket.accept();
                    System.out.println("Player 1's IP address " +
                            player1.getInetAddress().getHostAddress() + '\n');
                    // Notify that the player is Player 1
                    toPlayers.add(new ObjectOutputStream(player1.getOutputStream()));
                    fromPlayers.add(new ObjectInputStream(player1.getInputStream()));
                    toPlayers.get(0).writeObject(1);

                    Socket player2 = serverSocket.accept();

                    System.out.println("Player 2's IP address " +
                            player2.getInetAddress().getHostAddress() + '\n');

                    // Notify that the player is Player 2
                    toPlayers.add(new ObjectOutputStream(player2.getOutputStream()));
                    fromPlayers.add(new ObjectInputStream(player2.getInputStream()));
                    toPlayers.get(1).writeObject(2);

                    new Thread(new HandleASession(player1, player2)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static class HandleASession implements Runnable, TicTacToeConstants {
        private Socket player1;
        private Socket player2;

        public HandleASession(Socket player1, Socket player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        @Override
        public void run() {
            try {
                Game();
                visKort();
//                for (int i = 0; i < 2; i++) {
                træk();

//                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void Game() {
            for (int i = 0; i < 2; i++) {
                Card card = deck.Draw();
                dealersHand.add(card);
                for (int j = 0; j < 2; j++) {
                    Card card1 = deck.Draw();
                    hand.add(card1);
                }
            }
        }

        private void visKort() throws IOException {
            //send kort til player 1
            toPlayers.get(0).writeObject(hand.get(0));
            toPlayers.get(0).writeObject(hand.get(1));
            toPlayers.get(0).writeObject(hand.get(2));
            toPlayers.get(0).writeObject(hand.get(3));
            toPlayers.get(0).writeObject(dealersHand.get(0));
            toPlayers.get(0).writeObject(dealersHand.get(1));
            //send kort til player 2
            toPlayers.get(1).writeObject(hand.get(0));
            toPlayers.get(1).writeObject(hand.get(1));
            toPlayers.get(1).writeObject(hand.get(2));
            toPlayers.get(1).writeObject(hand.get(3));
            toPlayers.get(1).writeObject(dealersHand.get(0));
            toPlayers.get(1).writeObject(dealersHand.get(1));
        }

        private void træk() throws IOException, ClassNotFoundException {
//            for (int i = 1; i <= 2; i++) {
            int i = (int) fromPlayers.get(0).readObject();
                if (i == 1) {
                    Turn turn1 = (Turn) fromPlayers.get(0).readObject();
                    String userinput = turn1.getMessage();
                    if (userinput.equalsIgnoreCase("hit")) {
                        toPlayers.get(0).writeObject(deck.Draw());
                        toPlayers.get(1).writeObject(deck.Draw());
                        System.out.println("yea");
                    }
                }
                if (i == 2) {
                    Turn turn2 = (Turn) fromPlayers.get(1).readObject();
                    String userinput2 = turn2.getMessage();
                    if (userinput2.equalsIgnoreCase("hit")) {
                        toPlayers.get(0).writeObject(deck.Draw());
                        toPlayers.get(1).writeObject(deck.Draw());
                        System.out.println("yea2");
                    }
                }
//            }
        }
    }
}
