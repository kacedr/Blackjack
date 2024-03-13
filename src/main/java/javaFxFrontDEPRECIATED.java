/*
* Author: Kyle Gleason, Conor West
* This class is what runs the front end UI of the black jack game.
*
* */
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import java.util.HashMap;
import java.util.Optional;


// this class has been depreciated for the cleaned up class
// todo: When all mechanics are confirmed working in the new front end file, delete this file
public class javaFxFrontDEPRECIATED extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // Core game components
    private BlackjackGame bGame; // Initialize like: bGame = new BlackjackGame()
    private HashMap<String, Scene> sceneMap = new HashMap<>();
    private Stage primary;

    // Background elements
    private Image cas = new Image("casinobackground.jpg");
    private ImageView casBack = new ImageView(cas);

    // User input
    private TextField moneyprompt;
    private double money;

    // Scene components: setup scene
    private Button start;
    private TextField betInput;
    private Label moneyLabel;
    private VBox v1;
    private HBox h1;

    // Scene components: game scene
    private Button exit, hit, stay, betlabel; // betlabel sets the bet at the start of each hand
    private Label yScore, dScore, centerPop, moneyamt;
    private HBox pCards, dCards;
    private VBox centerGame, leftGame, rightGame;
    private Button play, help;
    private Label blackjack;

    // Game settings
    private int deckAmount = 1; // Default value
    private double cutCard = 0.30; // Default value

    // Additional fields
    private String moneyamtlabel;

    // start scene
    @Override
    public void start(Stage primaryStage) throws Exception {
        primary = primaryStage;
        sceneMap = new HashMap<>();
        sceneMap.put("setup", startScene());
        sceneMap.put("game", gameScene());
        sceneMap.put("help", helpScene());

        primaryStage.setTitle("Blackjack");
        primaryStage.setScene(sceneMap.get("setup"));
        primaryStage.show();
    }
    private Scene startScene() {
        
        // TODO: For help button, explain to the user what the shuffle percentage/cut card does (it is the percentage
        //  of the deck('s) left for when the dealer shuffles.) other than that just find blackjack rules somewhere
        //  also explain to the user how to accesses the secret settings (click blackjack label)


        ImageView blackjacktitle = getImageView();

        blackjack = new Label("Blackjack");
        blackjack.setStyle("-fx-cursor: hand; -fx-font-family: 'Constantia'; -fx-text-fill: black; " +
                "-fx-font-size: 60px; -fx-font-weight: bold;"); // css styling to allow clickable label

        // hover text
        Tooltip blackjackTooltip = new Tooltip("Click for advanced settings");
        blackjackTooltip.setStyle("-fx-font-size: 10pt;");
        blackjackTooltip.setShowDelay(Duration.seconds(0.01));
        Tooltip.install(blackjack, blackjackTooltip);

        // this took way to long, this handles the secret settings for setting deck amount and cut card
        blackjack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // create dialog
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Advanced Settings");
            dialog.setHeaderText("Defaults Values Are 30% and 1 deck");

            // set the button types
            ButtonType applyButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

            // create the cut card and deck amount inputs
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField cutCardInput = new TextField();
            cutCardInput.setPromptText("Between 30-90");
            TextField deckAmountInput = new TextField();
            deckAmountInput.setPromptText("Must be > 0");
            CheckBox hiLowCountShow = new CheckBox("Enable Hi-Low Counter");
            CheckBox showStrategyChart = new CheckBox("Show Strategy Chart");

            grid.add(new Label("Deck Shuffle %:"), 0, 0);
            grid.add(cutCardInput, 1, 0);
            grid.add(new Label("Deck Amount:"), 0, 1);
            grid.add(deckAmountInput, 1, 1);
            grid.add(hiLowCountShow, 1, 2);
            grid.add(showStrategyChart, 1, 3);

            // enable/disable the apply button depending on whether both inputs are used
            Node applyButton = dialog.getDialogPane().lookupButton(applyButtonType);
            applyButton.setDisable(true);

            // validates input
            ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
                boolean isCutCardValid;
                boolean isDeckAmountValid;

                try {
                    int cutCardValue = Integer.parseInt(cutCardInput.getText().trim());
                    isCutCardValid = cutCardValue >= 30 && cutCardValue <= 90;
                } catch (NumberFormatException e) {
                    isCutCardValid = false;
                }

                try {
                    int deckAmountValue = Integer.parseInt(deckAmountInput.getText().trim());
                    isDeckAmountValid = deckAmountValue > 0;
                } catch (NumberFormatException e) {
                    isDeckAmountValid = false;
                }

                applyButton.setDisable(!isCutCardValid || !isDeckAmountValid);
            };

            cutCardInput.textProperty().addListener(validationListener);
            deckAmountInput.textProperty().addListener(validationListener);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to a pair of cut card and deck amount when the apply button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == applyButtonType) {
                    return new Pair<>(cutCardInput.getText(), deckAmountInput.getText());
                }
                return null;
            });

            // Show the dialog and wait for the user to click apply or cancel
            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(cutDeckAmount -> {
                cutCard = Double.parseDouble(cutDeckAmount.getKey());
                deckAmount = Integer.parseInt(cutDeckAmount.getValue());
            });
        });

        h1 = new HBox(20, blackjacktitle, blackjack);
        h1.setAlignment(Pos.CENTER);
        moneyprompt = new TextField();

        moneyprompt.setMaxWidth(200);
        moneyLabel = new Label("Enter starting money amount:");


        moneyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        play = new Button("Play");
        play.setStyle("-fx-font-size: 20px; -fx-padding: 5px 10px; -fx-border-radius: 15px; -fx-background-radius: 1" +
                "5px; -fx-background-color: black; -fx-text-fill: white;");
        play.setMinWidth(100);
        play.setMinHeight(45);
        play.setOnAction(e -> handlePlayAction());

        help = new Button("Help?");
        help.setStyle("-fx-font-size: 14px; -fx-background-color: black; -fx-text-fill: white;");
        help.setMinWidth(30);
        help.setMinHeight(15);
        help.setOnAction(e -> primary.setScene(sceneMap.get("help")));

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

    private static ImageView getImageView() {
        Image bj = new Image("blackjack.png");
        ImageView blackjacktitle = new ImageView(bj);
        blackjacktitle.setFitWidth(120);
        blackjacktitle.setFitHeight(120);
        blackjacktitle.setPreserveRatio(true);
        blackjacktitle.setSmooth(true);
        return blackjacktitle;
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
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public Scene gameScene() {
        if (bGame == null) {
            bGame = new BlackjackGame();
        }
        Image ebut = new Image("exit.png");
        ImageView exitpic = new ImageView(ebut);

        exit = new Button();
        exitpic.setFitHeight(28);
        exitpic.setFitWidth(28);
        exitpic.setPreserveRatio(true);
        exit.setGraphic(exitpic);
        HBox exitBox = new HBox(exit);
        VBox.setMargin(exitBox, new Insets(-56, 0, 0, 0));
        exit.setOnAction(e -> {
            primary.setScene(sceneMap.get("setup"));
            betInput.clear();
            resetGame();
        });

        int playerScore = bGame.gameLogic.handTotal(bGame.playerHand);

        hit = new Button("HIT");
        hit.setStyle("-fx-font-size: 30px; -fx-padding: 10px 30px; -fx-border-radius: 15px;" +
                " -fx-background-radius: 15px; -fx-background-color: black; -fx-text-fill: white;");
        VBox.setMargin(hit, new Insets(145, 0, 0, 20));
        leftGame = new VBox(exitBox, hit);

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
        betlabel = new Button("Set Bet And Start Hand"); // kept the same name "betLable" should be changed to setBet for readibility

        betlabel.setStyle("-fx-font-weight: 600; -fx-background-color: black; -fx-text-fill: white;");
        VBox.setMargin(betlabel, new Insets(120, 15, 15, 15));
        betInput = new TextField();
        betInput.setMaxWidth(100);

        centerPop = new Label("");
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
        pCards.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1px; " +
                "-fx-border-radius: 5px;");

        centerGame = new VBox(dCards, betlabel, betInput, centerPop, moneyamt, pCards);

        stay = new Button("STAY");
        VBox.setMargin(stay, new Insets(144, 20, 20, 20));
        stay.setStyle("-fx-font-size: 30px; -fx-padding: 10px 15px; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-background-color: black; -fx-text-fill: white;");

        rightGame = new VBox(stay);

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
    // this method should only work with button actions and grab input from text fields, it should not create elements.
    private void gameplay() {
        final boolean[] isNewHand = {true};

        // starts a new game with the desired deck amount and shuffle point (cutCard)
        bGame = new BlackjackGame(deckAmount, cutCard);

        // double for bet, string for bet (when taken from text field)
        final String[] betAsString = new String[1];

        // sets the starting amount of money, should only happen at start of the game (not the start of each hand)
        bGame.totalWinnings = money;

        // set the initial amount of money
        moneyamt.setText(String.format("%.2f", bGame.totalWinnings));

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

                        // clear old hand
                        dCards.getChildren().clear();
                        pCards.getChildren().clear();

                        // set dealers cards pictures
                        for (int i = 0; i < bGame.bankerHand.size(); i++) {
                            Image bCard;
                            ImageView bCardView;

                            // hides last card
                            if (i == bGame.bankerHand.size() - 1) {
                                bCard = new Image("theseCardsMightBeBetter/Medium/Back Blue 1.png");
                                bCardView = new ImageView(bCard);
                                bCardView.setId("backCard"); // reference so we know which one to flip
                            } else {

                                // set path name
                                String cardImageName = "theseCardsMightBeBetter/Medium/" + bGame.bankerHand.get(i).suit
                                        + " " + bGame.bankerHand.get(i).face + ".png";
                                bCard = new Image(cardImageName);
                                bCardView = new ImageView(bCard);
                            }

                            dCards.getChildren().add(bCardView);
                        }

                        // set players cards pictures
                        for (int i = 0; i < bGame.playerHand.size(); i++) {
                            // set path name
                            String cardImageName = "theseCardsMightBeBetter/Medium/" + bGame.playerHand.get(i).suit
                                    + " " + bGame.playerHand.get(i).face + ".png";

                            Image pCard = new Image(cardImageName);
                            ImageView pCardView = new ImageView(pCard);
                            pCards.getChildren().add(pCardView);
                            int newPlayerScore = bGame.gameLogic.handTotal(bGame.playerHand);
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
                String cardImageName = "theseCardsMightBeBetter/Medium/" + bGame.playerHand.getLast().suit
                        + " " + bGame.playerHand.getLast().face + ".png";
                System.out.println(cardImageName);

                Image pCard = new Image(cardImageName);
                ImageView pCardView = new ImageView(pCard);
                pCards.getChildren().add(pCardView);
                int newPlayerScore = bGame.gameLogic.handTotal(bGame.playerHand);

                if(!playerHitRes) {
                    showAlert("You Busted!!!");
                    // evaluate winnings
                    bGame.evaluateWinnings();
                    if (Math.abs(bGame.totalWinnings) < 0.01) {
                        showAlert("Out of money, sending you back to the title screen.");
                        primary.setScene(sceneMap.get("setup"));
                        betInput.clear();
                        resetGame();

                    }
                    isNewHand[0] = true;

                    // update winnings
                    moneyamt.setText(String.format("%.2f", bGame.totalWinnings));

                    // clear old hand
                    dCards.getChildren().clear();
                    pCards.getChildren().clear();
                }
            }
        });

        stay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean bankerReturn = bGame.playerStay();
                double evWiningReturn = bGame.evaluateWinnings();

                // flip the flip card
                Card flippedCard = bGame.bankerHand.get(1); // get the second card
                String flippedCardImageName = "theseCardsMightBeBetter/Medium/" +
                        flippedCard.suit + " " + flippedCard.face + ".png";
                Image flippedCardImage = new Image(flippedCardImageName);
                ImageView flippedCardView = (ImageView) dCards.lookup("#backCard");
                flippedCardView.setImage(flippedCardImage); // Update the image of the flipped card

                // shows the dealers cards but with a delay TODO: delay does not work, worry about that if time
                // loop through the rest of the cards if there are any
                for (int i = 2; i < bGame.bankerHand.size(); i++) {
                    // just swap the hidden card
                    String cardImageName = "theseCardsMightBeBetter/Medium/" + bGame.bankerHand.get(i).suit
                            + " " + bGame.bankerHand.get(i).face + ".png";
                    Image bCard = new Image(cardImageName);
                    ImageView bCardView = new ImageView(bCard);
                    dCards.getChildren().add(bCardView);
                }

                if (!bankerReturn) {
                    showAlert("Banker Busted, You Win!!!");
                } else {
                    // evaluate winnings (evaluateWinnings returns negative if player lost)
                    if (evWiningReturn < 0) {
                        System.out.println(evWiningReturn);
                        System.out.println(bGame.currentBet);
                        // player lost
                        showAlert("You Lost!!!");
                    } else if (evWiningReturn > 0) {
                        System.out.println(evWiningReturn);
                        System.out.println(bGame.currentBet);
                        // player won
                        showAlert("You Won!!!");
                    } else {
                        System.out.println(evWiningReturn);
                        System.out.println(bGame.currentBet);
                        // push
                        showAlert("Push!!!");
                    }
                }
                if (Math.abs(bGame.totalWinnings) < 0.01) {
                    showAlert("Out of money, sending you back to the title screen.");
                    primary.setScene(sceneMap.get("setup"));
                    betInput.clear();
                    resetGame();

                }
                isNewHand[0] = true;

                // update winnings
                moneyamt.setText(String.format("%.2f", bGame.totalWinnings));

                // clear old hand
                dCards.getChildren().clear();
                pCards.getChildren().clear();
            }
        });
    }

    // Method to reset the game
    private void resetGame() {
        bGame = new BlackjackGame();
        money = 0;
        moneyprompt.clear();
        dCards.getChildren().clear();
        pCards.getChildren().clear();
        deckAmount = 1;
        cutCard = 0.30;
    }
    private Scene helpScene() {
        // create a back button to return to the setup scene
        Image ebut = new Image("exit.png");
        ImageView exitpic = new ImageView(ebut);

        exit = new Button();
        exitpic.setFitHeight(28);
        exitpic.setFitWidth(28);
        exitpic.setPreserveRatio(true);
        exit.setGraphic(exitpic);
        HBox exitBox = new HBox(exit);
//        VBox.setMargin(exitBox, new Insets(-56, 0, 0, 0));
        exit.setOnAction(e -> {
                    primary.setScene(sceneMap.get("setup"));
        });
        VBox centerV = new VBox();
        Label rulesLabel = new Label("Blackjack Rules");
        rulesLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Constantia'");
        centerV = new VBox(10, rulesLabel);


        BorderPane helpBorder = new BorderPane();
        helpBorder.setLeft(exitBox);
        helpBorder.setCenter(centerV);
        helpBorder.setStyle("-fx-background-color: #005e30;");
        //comment

        return new Scene(helpBorder, 1200, 600);
    }
}