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

public class JavaFxFront extends Application {

    private Stage primary;
    private final HashMap<String, Scene> sceneMap = new HashMap<>();
    private BlackjackGame bGame;
    private TextField moneyPrompt, betInput;
    private double money;
    private Label blackjack;
    private Label moneyAmount;
    private Button hit;
    private Button stay;
    private Button betLabel;
    private HBox dCards, pCards;
    private int deckAmount = 1;
    private double cutCard = 0.30;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primary = primaryStage;
        sceneMap.put("setup", startScene());
        sceneMap.put("game", gameScene());
        sceneMap.put("help", helpScene());

        primaryStage.setTitle("Blackjack");
        primaryStage.setScene(sceneMap.get("setup"));
        primaryStage.show();
    }

    private Scene startScene() {
        ImageView blackjackTitle = getImageView();
        blackjack = new Label("Blackjack");
        blackjack.setStyle("-fx-cursor: hand; -fx-font-family: 'Constantia'; -fx-text-fill: black; " +
                "-fx-font-size: 60px; -fx-font-weight: bold;");
        Tooltip blackjackTooltip = new Tooltip("Click for advanced settings");
        blackjackTooltip.setStyle("-fx-font-size: 10pt;");
        blackjackTooltip.setShowDelay(Duration.seconds(0.01));
        Tooltip.install(blackjack, blackjackTooltip);
        blackjack.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> advancedSettingsDialog());

        HBox h1 = new HBox(20, blackjackTitle, blackjack);
        h1.setAlignment(Pos.CENTER);
        moneyPrompt = new TextField();
        moneyPrompt.setMaxWidth(200);
        Label moneyLabel = new Label("Enter starting money amount:");
        moneyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        Button play = new Button("Play");
        play.setStyle("-fx-font-size: 20px; -fx-background-color: black; -fx-text-fill: white;");
        play.setMinSize(100, 45);
        play.setOnAction(e -> handlePlayAction());
        Button help = new Button("Help?");
        help.setStyle("-fx-font-size: 14px; -fx-background-color: black; -fx-text-fill: white;");
        help.setMinSize(30, 15);
        help.setOnAction(e -> primary.setScene(sceneMap.get("help")));

        VBox v1 = new VBox(20, h1, moneyLabel, moneyPrompt, play);
        v1.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(v1);
        borderPane.setRight(help);
        borderPane.setStyle("-fx-background-color: #005e30;");
        BorderPane.setMargin(help, new Insets(560, 10, 0, 0));

        return new Scene(borderPane, 1200, 600);
    }

    private static ImageView getImageView() {
        Image bj = new Image("blackjack.png");
        ImageView blackjackTitle = new ImageView(bj);
        //blackjackTitle.setFitDimensions(120, 120);
        blackjackTitle.setPreserveRatio(true);
        blackjackTitle.setSmooth(true);
        return blackjackTitle;
    }

    private void handlePlayAction() {
        validateMoneyInput();
    }

    private void validateMoneyInput() {
        try {
            money = Double.parseDouble(moneyPrompt.getText());
            if (money > 0) {
                primary.setScene(sceneMap.get("game"));
                primary.show();
                gameplay();
            } else {
                showAlert("Please try a Valid Money input");
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid starting money amount (amount>0)");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public Scene gameScene() {
        if (bGame == null) bGame = new BlackjackGame();
        Button exit = new Button();
        exit.setGraphic(new ImageView(new Image("exit.png")));
        exit.setOnAction(e -> resetGameScene());
        hit = setupGameButton("HIT");
        stay = setupGameButton("STAY");
        betLabel = new Button("Set Bet And Start Hand");
        betLabel.setStyle("-fx-font-weight: 600; -fx-background-color: black; -fx-text-fill: white;");
        betInput = new TextField();
        betInput.setMaxWidth(100);
        Label centerPop = new Label("");
        moneyAmount = new Label(String.format("%.2f", money));
        moneyAmount.setStyle("-fx-text-fill: white;-fx-font-size: 14;");
        dCards = setupCardHBox();
        pCards = setupCardHBox();
        VBox centerGame = new VBox(dCards, betLabel, betInput, centerPop, moneyAmount, pCards);
        centerGame.setAlignment(Pos.CENTER);

        BorderPane gamePane = new BorderPane();
        gamePane.setLeft(new VBox(exit, hit));
        gamePane.setCenter(centerGame);
        gamePane.setRight(new VBox(stay));
        gamePane.setStyle("-fx-background-color: #005e30;");

        return new Scene(gamePane, 1200, 600);
    }

    private Button setupGameButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 30px; -fx-background-color: black; -fx-text-fill: white;");
        return button;
    }

    private HBox setupCardHBox() {
        HBox hBox = new HBox(16);
        hBox.setAlignment(Pos.CENTER);
        hBox.setMinSize(800, 138);
        hBox.setPadding(new Insets(15));
        hBox.setStyle("-fx-background-color: transparent; -fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 5px;");
        return hBox;
    }

    private void resetGameScene() {
        primary.setScene(sceneMap.get("setup"));
        betInput.clear();
        resetGame();
    }

    private void resetGame() {
        bGame = new BlackjackGame();
        money = 0;
        moneyPrompt.clear();
        deckAmount = 1;
        cutCard = 0.30;
    }

    private Scene helpScene() {
        Button exit = new Button();
        exit.setGraphic(new ImageView(new Image("exit.png")));
        exit.setOnAction(e -> primary.setScene(sceneMap.get("setup")));
        VBox centerV = new VBox(10, new Label("Blackjack Rules"));
        centerV.getChildren().getFirst().setStyle("-fx-font-size: 30px; -fx-text-fill: black; " +
                "-fx-font-weight: bold; -fx-font-family: 'Constantia'");

        BorderPane helpBorder = new BorderPane();
        helpBorder.setLeft(new HBox(exit));
        helpBorder.setCenter(centerV);
        helpBorder.setStyle("-fx-background-color: #005e30;");

        return new Scene(helpBorder, 1200, 600);
    }

    private void advancedSettingsDialog() {
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
    }

    private void gameplay() {
        final boolean[] isNewHand = {true};

        // starts a new game with the desired deck amount and shuffle point (cutCard)
        bGame = new BlackjackGame(deckAmount, cutCard);

        // double for bet, string for bet (when taken from text field)
        final String[] betAsString = new String[1];

        // sets the starting amount of money, should only happen at start of the game (not the start of each hand)
        bGame.totalWinnings = money;

        // set the initial amount of money
        moneyAmount.setText(String.format("%.2f", bGame.totalWinnings));

        // Basically this signifies that there is a new hand thus this button needs to be disabled unless a new
        // hand can happen
        betLabel.setOnAction(new EventHandler<ActionEvent>() {
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
                        moneyAmount.setText(String.format("%.2f", bGame.totalWinnings - bGame.currentBet));

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
                        }
                    }
                } catch (NumberFormatException e) {
                    showAlert("Must enter a valid bet");
                }
            }
        });

        hit.setOnAction(actionEvent -> {
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
                moneyAmount.setText(String.format("%.2f", bGame.totalWinnings));

                // clear old hand
                dCards.getChildren().clear();
                pCards.getChildren().clear();
            }
        });

        stay.setOnAction(new EventHandler<>() {
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

                // shows the dealers cards but with a delay
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
                moneyAmount.setText(String.format("%.2f", bGame.totalWinnings));

                // clear old hand
                dCards.getChildren().clear();
                pCards.getChildren().clear();
            }
        });
    }
}
