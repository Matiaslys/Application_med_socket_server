package blackjack;

import tic_tac_toe.TicTacToeConstants;
import tic_tac_toe.TicTacToeServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Server {
    private static boolean stillplaying = true;
    private static int numberOfPlayers = 1;
    private static int i;
    private static int otherplayer;
    private static ArrayList<ObjectInputStream> fromPlayers = new ArrayList<>();
    private static ArrayList<ObjectOutputStream> toPlayers = new ArrayList<>();
    private static ArrayList<Card> dealersHand = new ArrayList<>();
    private static ArrayList<Card> hand = new ArrayList<>();
    private static Deck deck = new Deck();

    public static void main(String[] args) {
        new Thread(() -> {
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
                træk();
                end();

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
            for (i = 0; i <= numberOfPlayers; i++) {
                if (i == numberOfPlayers) {
                    otherplayer = 0;
                } else {
                    otherplayer = i + 1;
                }
                String message = (String) fromPlayers.get(i).readObject();
                toPlayers.get(otherplayer).writeObject(message);
                System.out.println(message);
                String userinput = message;

                while (!userinput.equalsIgnoreCase("stand")) {
                    if (userinput.equalsIgnoreCase("hit")) {
                        Card card = deck.Draw();
                        toPlayers.get(i).writeObject(card);
                        toPlayers.get(otherplayer).writeObject(card);
                    }
                    double playerHandValue = (double) fromPlayers.get(i).readObject();
                    if (playerHandValue > 21) {
                        break;
                    }
                    message = (String) fromPlayers.get(i).readObject();
                    toPlayers.get(otherplayer).writeObject(message);
                    userinput = message;
                }
            }
        }

        public void end() {
            try {
//                fromPlayers.get(0).readObject();
//                fromPlayers.get(1).readObject();
                double dealershandvalue = (double) fromPlayers.get(0).readObject();
                while (dealershandvalue < 17) {
                    Card card = deck.Draw();
                    toPlayers.get(0).writeObject(card);
                    toPlayers.get(1).writeObject(card);
                    dealershandvalue = (double) fromPlayers.get(0).readObject();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
