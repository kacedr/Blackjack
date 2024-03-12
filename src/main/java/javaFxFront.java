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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;

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
        VBox v1;
        HBox h1;
        Label blackjack, moneyLabel;
        Button play, help;

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
        moneyprompt.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                validateMoneyInput();
            }
        });
        moneyLabel = new Label("Enter starting money amount:");

        // starts game, no deck amount, no cut shuffle percentage, might have to be moved because
        // this should not start until the user inputs correct input. The input also has to be directed
        // to the variables to properly set up the game. If the user does not select any input for cut/deck,
        // they can either be prompted to or values can be set to default. Any actions on bGame must be in scope
        // of this function. So direct modification of bGame can only happen inside of this function.
        int deckAmount = 1; // default values
        double cutCard = 0.30; // default values
        bGame = new BlackjackGame(deckAmount, cutCard);

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

        Button exit, start, hit, stay;
        TextField betInput;
        Label yScore, dScore, centerPop, moneyamt;
        HBox pCards, dCards;
        VBox centerGame, leftGame, rightGame;

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

        Label betlabel = new Label("Enter bet amount:");
        betlabel.setStyle("-fx-font-weight: 600;-fx-text-fill: white");
        VBox.setMargin(betlabel, new Insets(120, 0, 0, 0));
        betInput = new TextField();
        betInput.setMaxWidth(100);

        centerPop = new Label("");
//        not working correctly
        String moneyamtlabel = String.format("%.2f", money);

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
}
