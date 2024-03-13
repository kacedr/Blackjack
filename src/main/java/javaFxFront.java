import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class javaFxFront extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // initialize blackjack game, this needs to be initialized like bGame = new BlackjackGame()
    BlackjackGame bGame;
    HashMap<String, Scene> sceneMap;
    Stage primary;
    TextField moneyprompt;
    double money;
    Image cas = new Image("casinobackground.jpg");
    ImageView casBack = new ImageView(cas);

    // pulled from getScene
    Button exit, start, hit, stay;
    Button betlabel; // sets the bet at the start of each hand
    TextField betInput;
    Label yScore, dScore, centerPop, moneyamt;
    HBox pCards, dCards;
    VBox centerGame, leftGame, rightGame;

    String moneyamtlabel;

    // pulled from gameScene
    VBox v1;
    HBox h1;
    Label blackjack, moneyLabel;
    Button play, help;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primary = primaryStage;
        sceneMap = new HashMap<>();
        sceneMap.put("setup", startScene());
        sceneMap.put("game", gameScene());

        primaryStage.setTitle("Blackjack");
        primaryStage.setScene(sceneMap.get("setup"));
        primaryStage.show();

    }
    private Scene startScene() {


        Image bj = new Image("blackjack.png");
        ImageView blackjacktitle = new ImageView(bj);
        blackjacktitle.setFitWidth(120);
        blackjacktitle.setFitHeight(120);
        blackjacktitle.setPreserveRatio(true);
        blackjacktitle.setSmooth(true);

        blackjack = new Label("Blackjack");

        blackjack.setStyle("-fx-font-family: 'Constantia'; -fx-text-fill: black; -fx-font-size: 60px; -fx-font-weight: bold;");
        h1 = new HBox(20, blackjacktitle, blackjack);
        h1.setAlignment(Pos.CENTER);
        moneyprompt = new TextField();

        moneyprompt.setMaxWidth(200);
//        moneyprompt.setOnKeyPressed(e -> {
//            if(e.getCode() == KeyCode.ENTER) {
//                validateMoneyInput();
//            }
//        });
        moneyLabel = new Label("Enter starting money amount:");


        moneyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        play = new Button("Play");
        play.setStyle("-fx-font-size: 20px; -fx-padding: 5px 10px; -fx-border-radius: 15px; -fx-background-radius: 15px;");
        play.setMinWidth(100);
        play.setMinHeight(45);
        play.setOnAction(e -> handlePlayAction());

        help = new Button("Help?");
        help.setStyle("-fx-font-size: 14px;");
        help.setMinWidth(30);
        help.setMinHeight(15);

        Button spaceButton = new Button("Help?");
        spaceButton.setStyle("-fx-text-fill: transparent;-fx-background-color: transparent;-fx-font-size: 14px;");
        spaceButton.setMinWidth(30);
        spaceButton.setMinHeight(15);
        spaceButton.setOpacity(1);

        v1 = new VBox(20, h1, moneyLabel, moneyprompt, play);
        v1.setPrefWidth(500);
        v1.setMaxWidth(500);
        v1.setAlignment(Pos.CENTER);
        VBox.setMargin(h1, new Insets(0, 0, 0, -35));
        VBox.setMargin(moneyLabel, new Insets(0, 0, -15, 0));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(v1);
        borderPane.setRight(help);
        borderPane.setLeft(spaceButton);


        borderPane.setStyle("-fx-background-color: #005e30;");
        BorderPane.setMargin(help, new Insets(560, 10, 0, 0));

        return new Scene(borderPane, 1200, 600);
    }




    private void handlePlayAction() {
        validateMoneyInput();
    }

    private void validateMoneyInput() {
        try {
            money = Double.parseDouble(moneyprompt.getText());
            if (money > 0) {
                primary.setScene(sceneMap.get("game"));
                primary.show();
                gameplay(); // this will start the game

            } else {
                showAlert("Please try a Valid Money input");
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid starting money amount (amount>0)");
        }

    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public Scene gameScene() {



        Image ebut = new Image("exit.png");
        ImageView exitpic = new ImageView(ebut);

        exit = new Button();
        exitpic.setFitHeight(28);
        exitpic.setFitWidth(28);
        exitpic.setPreserveRatio(true);
        exit.setGraphic(exitpic);
        HBox exitBox = new HBox(exit);
        VBox.setMargin(exitBox, new Insets(-56, 0, 0, 0));
        exit.setOnAction(e -> primary.setScene(sceneMap.get("setup")));

        int boo = 0;
        String yScoreLabel = String.format("Your Score\n         %d         ", boo);
        yScore = new Label(yScoreLabel);
        VBox.setMargin(yScore, new Insets(250, 0, 0, 0));
        yScore.setStyle("-fx-font-family: 'Constantia'; -fx-text-fill: white; -fx-font-size: 21px; -fx-font-weight: bold;");
        hit = new Button("HIT");
        hit.setStyle("-fx-font-size: 30px; -fx-padding: 10px 30px;-fx-border-radius: 15px; -fx-background-radius: 15px;");
        VBox.setMargin(hit, new Insets(145, 0, 0, 20));
        leftGame = new VBox(exitBox, yScore, hit);




        dCards = new HBox(16);
        dCards.setAlignment(Pos.CENTER);
        dCards.setMinWidth(800);
        dCards.setMaxWidth(800);
        dCards.setMinHeight(138);
        dCards.setPadding(new Insets(15, 15, 15, 15));
        VBox.setMargin(dCards, new Insets(0, 0, 0, 0));
        dCards.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");

        // instead of this being a label I changed it to a button to get the text from the text box
        // Label betlabel = new Label("Enter bet amount:"); did not remove in case still needed
        betlabel = new Button("Set Bet"); // kept the same name "betLable" should be changed to setBet for readibility

        betlabel.setStyle("-fx-font-weight: 600;-fx-text-fill: black");
        VBox.setMargin(betlabel, new Insets(120, 15, 15, 15));
        betInput = new TextField();
        betInput.setMaxWidth(100);

        centerPop = new Label("");
//        not working correctly
        moneyamtlabel = String.format("%.2f", money);

        moneyamt = new Label(moneyamtlabel);
        moneyamt.setStyle("-fx-text-fill: white;-fx-font-size: 14;");
        VBox.setMargin(moneyamt, new Insets(70, 0, 0, 0));

        pCards = new HBox(16);
        pCards.setAlignment(Pos.CENTER);
        pCards.setMinWidth(800);
        pCards.setMaxWidth(800);
        pCards.setMinHeight(138);
        pCards.setPadding(new Insets(15, 15, 15, 15));
        VBox.setMargin(pCards, new Insets(10, 0, 0, 0));
        pCards.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");


        centerGame = new VBox(dCards, betlabel, betInput, centerPop, moneyamt, pCards);




        String dScoreLabel = String.format("Dealer Score\n            %d            ", boo);
        dScore = new Label(dScoreLabel);
        VBox.setMargin(dScore, new Insets(226, 0, 0, 0));
        dScore.setStyle("-fx-font-family: 'Constantia'; -fx-text-fill: white; -fx-font-size: 21px; -fx-font-weight: bold;");

        stay = new Button("STAY");
        VBox.setMargin(stay, new Insets(144, 0, 0, 0));
        stay.setStyle("-fx-font-size: 30px; -fx-padding: 10px 15px;-fx-border-radius: 15px; -fx-background-radius: 15px;");

        rightGame = new VBox(dScore, stay);








        BorderPane gamePane = new BorderPane();
        centerGame.setAlignment(Pos.CENTER);
        leftGame.setAlignment(Pos.CENTER);
        rightGame.setAlignment(Pos.CENTER);
        gamePane.setLeft(leftGame);
        gamePane.setCenter(centerGame);
        gamePane.setRight(rightGame);
        gamePane.setStyle("-fx-background-color: #005e30;");

        return new Scene(gamePane, 1200, 600);
    }

    // this method is what controls the gameplay, this must be called once the starting money amount is set
    // this method should only work with button actions and grab input from text fields, it should not create elements
    // name, return type, and inputs might need to be changed
    private void gameplay() {
        // these methods pertaining to gameplay can be moved, not sure where they should be inside of this method

        // a variable in scope of javaFxFront class needs to be made for deckAmount and cutCard percentage and
        // input needs to be taken somewhere for those before gameScene method is called. Since im not sure
        // how this wants to be handled, I will initiate the variables here.

        // set money amount for ui

        int deckAmount = 1; // default values, must be moved only for development
        double cutCard = 0.30; // default values, must be moved only for development

        // boolean for if the deck was shuffled
        final boolean[] wasShuffled = {false}; // todo might not be needed

        final boolean[] isNewHand = {true};

        // starts a new game with the desired deck amount and shuffle point (cutCard)
        bGame = new BlackjackGame(deckAmount, cutCard);

        // double for bet, string for bet (when taken from text field)
        final String[] betAsString = new String[1];

        // sets the starting amount of money, should only happen at start of the game (not the start of each hand)
        bGame.totalWinnings = money;

        // set the initial amount of money
        moneyamt.setText(String.format("%.2f", bGame.totalWinnings));


        // the game will run until either the player exits, or their money reaches zero
        // todo Make sure a new game is started every time the player exits, If they reach zero, they should be
        //  able to play a new game (not a new hand) without restarting the application.

        // set the bet, if the user does not set a new bet, the same bet should be used (UNLESS THE PLAYER DOES NOT
        // HAVE ENOUGH TO BET THAT AMOUNT, then prompt user for new bet)
        // todo While the hand is running, the user should not be able to change their bet or even edit the bet text
        //  box. It should be greyed out and non traversable.

        // Basically this signifies that there is a new hand thus this button needs to be disabled unless a new
        // hand can happen
        betlabel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // get text
                betAsString[0] = betInput.getText();

                // this will throw an exception if the input is in valid
                try {
                    betAsString[0] = betInput.getText();
                    if (!isNewHand[0]) {
                        showAlert("Can not change bet during hand");
                    }
                    else if (Double.parseDouble(betAsString[0]) > bGame.totalWinnings) {
                        showAlert("Must change bet amount, not enough winnings");
                    } else {
                        bGame.currentBet = Double.parseDouble(betAsString[0]);


                        // start new hand
                        if (bGame.newHand()) {
                            showAlert("Deck was shuffled");
                        }

                        // set boolean too false to not allow new hand until current hand is over
                        isNewHand[0] = false;

                        // we are going to minus the bet from the money total visually
                        moneyamt.setText(String.format("%.2f", bGame.totalWinnings - bGame.currentBet));

                        // set dealers cards pictures
                        for (int i = 0; i < bGame.bankerHand.size(); i++) {
                            // hides last card
                            if (i == bGame.bankerHand.size() - 1) {
                                Image bCard = new Image("theseCardsMightBeBetter/Large/Back Blue 1.png");
                                ImageView bCardView = new ImageView(bCard);
                                dCards.getChildren().add(bCardView);
                            } else {

                                // set path name
                                String cardImageName = "theseCardsMightBeBetter/Large/" + bGame.bankerHand.get(i).suit
                                        + " " + bGame.bankerHand.get(i).face + ".png";
                                System.out.println(cardImageName);

                                Image bCard = new Image(cardImageName);
                                ImageView bCardView = new ImageView(bCard);
                                dCards.getChildren().add(bCardView);
                            }
                        }

                        // set players cards pictures
                        for (int i = 0; i < bGame.playerHand.size(); i++) {
                            // set path name
                            String cardImageName = "theseCardsMightBeBetter/Large/" + bGame.playerHand.get(i).suit
                                    + " " + bGame.playerHand.get(i).face + ".png";
                            System.out.println(cardImageName);

                            Image pCard = new Image(cardImageName);
                            ImageView pCardView = new ImageView(pCard);
                            pCards.getChildren().add(pCardView);
                        }

                    }
                } catch (NumberFormatException e) {
                    showAlert("Must enter a valid bet");
                }
            }
        });

        hit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean playerHitRes = bGame.playerHit();

                // set card image for player
                String cardImageName = "theseCardsMightBeBetter/Large/" + bGame.playerHand.getLast().suit
                        + " " + bGame.playerHand.getLast().face + ".png";
                System.out.println(cardImageName);

                Image pCard = new Image(cardImageName);
                ImageView pCardView = new ImageView(pCard);
                pCards.getChildren().add(pCardView);

                if(!playerHitRes) {
                    showAlert("You Busted!!!");
                    // evaluate winnings
                    bGame.evaluateWinnings();

                    isNewHand[0] = true;

                    // update winnings
                    moneyamt.setText(String.format("%.2f", bGame.totalWinnings));
                }
            }
        });

        stay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                double evWiningReturn = bGame.evaluateWinnings();

                if (!bGame.playerStay()) {
                    showAlert("Banker Busted, You Win!!!");
                } else {
                    // evaluate winnings
                    if (evWiningReturn == bGame.currentBet) {
                        // player lost
                        showAlert("You Lost!!!");
                    } else if (evWiningReturn > bGame.currentBet) {
                        // player won
                        showAlert("You Won!!!");
                    } else {
                        // push
                        showAlert("Push!!!");
                    }
                }
                isNewHand[0] = true;

                // update winnings
                moneyamt.setText(String.format("%.2f", bGame.totalWinnings));
            }
        });
    }
}
