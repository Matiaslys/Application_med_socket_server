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
                for (int i = 1; i <= 2; i++) {
                    myTurn = i == player;
                    Turn();
                }
                end();
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
        System.out.println("tilsammen har jeg " + myHandValue);
        System.out.println("Min modstanders hånd består af: ");
        for (Card card : opponentsHand) {
            System.out.println(card.getName() + " " + card.getSuit() + " " + card.getValue());
            opponentsHandValue = opponentsHandValue + card.getValue();
        }
        System.out.println("tilsammen min modstander " + opponentsHandValue);
        System.out.println("Dealerens hånd består af: ");
        System.out.println(dealersHand.get(0).getName() + " " + dealersHand.get(0).getSuit() + " " + dealersHand.get(0).getValue());
        System.out.println("Det andet kort kendes ikke endnu");
    }

    private void Turn() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        String userinput = " ";
        toServer.writeObject(userinput);
        if (myTurn) {
            System.out.println("det er min tur");
        } else {
            System.out.println("det er min modstanders tur");
        }
        while (!userinput.equalsIgnoreCase("stand")) {
            if (!myTurn) {
                userinput = (String) fromServer.readObject();
                if (userinput.equalsIgnoreCase("hit")) {
                    try {
                        Card newCard = (Card) fromServer.readObject();
                        opponentsHand.add(newCard);
                        System.out.println("Min modstander fik");
                        for (Card card : opponentsHand) {
                            System.out.println(card.getName() + " " + card.getSuit() + " " + card.getValue());
                        }
                        opponentsHandValue = opponentsHandValue + newCard.getValue();
                        System.out.println("tilsammen har personen nu " + opponentsHandValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (opponentsHandValue > 21) {
                    System.out.println("din modstander har tabt");
                    break;
                }
            }
            if (myTurn) {
                userinput = scanner.nextLine();
                toServer.writeObject(userinput);
                if (userinput.equalsIgnoreCase("hit")) {
                    Card newCard = (Card) fromServer.readObject();
                    myHand.add(newCard);
                    for (Card card : myHand) {
                        System.out.println(card.getName() + " " + card.getSuit() + " " + card.getValue());
                    }
                    myHandValue = myHandValue + newCard.getValue();
                    System.out.println("tilsammen har jeg " + myHandValue);
                }
            }
            if (myHandValue > 21) {
                System.out.println("Du har tabt ");
                break;
            }
        }
    }

    public void end() {
        System.out.println("Dealeren har ");
        for (Card card : dealersHand) {
            System.out.println(card.getName() + " " + card.getSuit() + " " + card.getValue());
            dealersHandValue = dealersHandValue + card.getValue();
        }
        System.out.println("tilsammen har dealeren " + dealersHandValue);
        while (dealersHandValue < 17) {
            try {
                toServer.writeObject(dealersHandValue);
                Card newCard = (Card) fromServer.readObject();
                dealersHand.add(newCard);
                for (Card card : dealersHand) {
                    System.out.println(card.getName() + " " + card.getSuit() + " " + card.getValue());
                }
                dealersHandValue = dealersHandValue + newCard.getValue();
                System.out.println("til sammen har dealeren " + dealersHandValue);
                if (dealersHandValue > 21) {
                    System.out.println("Dealeren har tabt");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("du fik " + myHandValue);
        System.out.println("din modstander fik " + opponentsHandValue);

        if (myHandValue > dealersHandValue && myHandValue > opponentsHandValue && myHandValue <= 21 && dealersHandValue <= 21 && opponentsHandValue <= 21 || myHandValue <= 21 && dealersHandValue > 21 && opponentsHandValue > 21
                || myHandValue <= 21 && myHandValue > dealersHandValue && opponentsHandValue > 21 || myHandValue > opponentsHandValue && dealersHandValue > 21) {
            System.out.println("Du har vundet!!!!!");
        } else if (opponentsHandValue > dealersHandValue && myHandValue < opponentsHandValue && myHandValue <= 21 && dealersHandValue <= 21 && opponentsHandValue <= 21 || opponentsHandValue <= 21 && dealersHandValue > 21 && opponentsHandValue > 21
                || opponentsHandValue <= 21 && opponentsHandValue > dealersHandValue && myHandValue > 21 || opponentsHandValue > myHandValue && dealersHandValue > 21) {
            System.out.println("Din modstander vandt");
        } else if (dealersHandValue > myHandValue && dealersHandValue > opponentsHandValue && myHandValue <= 21 && dealersHandValue <= 21 && opponentsHandValue <= 21 || dealersHandValue <= 21 && myHandValue > 21 && opponentsHandValue > 21
                || dealersHandValue <= 21 && dealersHandValue > myHandValue && opponentsHandValue > 21 || dealersHandValue > opponentsHandValue && myHandValue > 21) {
            System.out.println("dealer vandt");
        } else if (myHandValue == dealersHandValue && opponentsHandValue < myHandValue) {
            System.out.println("der er push imellem mig og dealeren");
        } else if (myHandValue == opponentsHandValue && dealersHandValue < myHandValue) {
            System.out.println("der er push imellem mig og min modstander");
        } else if (opponentsHandValue == dealersHandValue && opponentsHandValue > myHandValue) {
            System.out.println("der er push imellem min modstander og dealeren");
        }
    }

}

