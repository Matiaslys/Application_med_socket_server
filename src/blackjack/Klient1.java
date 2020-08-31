package blackjack;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Klient1 {
    private ArrayList<Card> myHand = new ArrayList<>();
    private ArrayList<Card> opponentsHand = new ArrayList<>();
    private ArrayList<Card> dealersHand = new ArrayList<>();
    private double myHandValue;
    private double opponentsHandValue;
    private double dealersHandValue;
    private int player;
    private Scanner scanner = new Scanner(System.in);
    private boolean myTurn;
    private boolean tabt;
    private boolean stand;
    // Input and output streams from/to server
    private ObjectInputStream fromServer;
    private ObjectOutputStream toServer;
    // Host name or ip
    private String host = "localhost";

    public static void main(String[] args) {
        Klient1 klient1 = new Klient1();
        klient1.connectToServer();
    }

    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket(host, 8000);

            // Create an input stream to receive data from the server
            fromServer = new ObjectInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new ObjectOutputStream(socket.getOutputStream());

            player = (int) fromServer.readObject();
            System.out.println("Du er spiller " + player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        new Thread(() -> {
            try {
                Start();
                Turn();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void Start() throws IOException, ClassNotFoundException {
        if (player == 1) {
            myHand.add((Card) fromServer.readObject());
            myHand.add((Card) fromServer.readObject());
            opponentsHand.add((Card) fromServer.readObject());
            opponentsHand.add((Card) fromServer.readObject());
        } else {
            opponentsHand.add((Card) fromServer.readObject());
            opponentsHand.add((Card) fromServer.readObject());
            myHand.add((Card) fromServer.readObject());
            myHand.add((Card) fromServer.readObject());
        }
        dealersHand.add((Card) fromServer.readObject());
        dealersHand.add((Card) fromServer.readObject());

        System.out.println("Min hånd består af: ");
        for (Card card : myHand) {
            System.out.println(card.getName() + " " + card.getSuit() + " " + card.getValue() + " ");
            myHandValue = myHandValue + card.getValue();
        }
        System.out.println("til sammen har jeg " + myHandValue);
        System.out.println("Min modstanders hånd består af: ");
        for (Card card : opponentsHand) {
            System.out.println(card.getName() + " " + card.getSuit() + " " + card.getValue());
            opponentsHandValue = opponentsHandValue + card.getValue();
        }
        System.out.println("til sammen min modstander " + opponentsHandValue);
        System.out.println("Dealerens hånd består af: ");
        System.out.println(dealersHand.get(0).getName() + " " + dealersHand.get(0).getSuit() + " " + dealersHand.get(0).getValue());
        System.out.println("Det andet kort kendes ikke endnu");
    }

    private void Turn() throws IOException, ClassNotFoundException {
        while (true) {
            String userinput = scanner.nextLine();
            Turn turn = new Turn(player, userinput);
            if (userinput.equalsIgnoreCase("hit")) {
                toServer.writeObject(player);
                toServer.writeObject(turn);
                Card newCard = (Card) fromServer.readObject();
                myHand.add(newCard);
                for (Card card : myHand) {
                    System.out.println(card.getName() + " " + card.getSuit() + " " + card.getValue());
                }
                myHandValue = myHandValue + newCard.getValue();
                System.out.println("til sammen har jeg " + myHandValue);
            }
            if (myHandValue > 21) {
                System.out.println("Du har tabt");
                tabt = true;
                break;
            }

            if (userinput.equalsIgnoreCase("stand")) {
                stand = true;
                break;
            }

        }
    }

}

