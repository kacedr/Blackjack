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
        moneyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        play = new Button("Play");
        play.setStyle("-fx-font-size: 20px; -fx-padding: 5px 10px;");
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

        BackgroundSize backgroundSize = new BackgroundSize(100, cas.getHeight() * (1200 / cas.getWidth()), true, false, false, true);
        BackgroundImage backgroundImage = new BackgroundImage(cas, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        borderPane.setBackground(new Background(backgroundImage));

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
        int boo = 0;
        String yScoreLabel = String.format("Your Score\n      %d", boo);
        yScore = new Label(yScoreLabel);
        hit = new Button("HIT");

        leftGame = new VBox(exit, yScore, hit);



        dCards = new HBox();
        betInput = new TextField();
        centerPop = new Label("      boo        ");
//        not working correctly
        String moneyamtlabel = String.format("%.2f", money);
        moneyamt = new Label(moneyamtlabel);
        pCards = new HBox();

        centerGame = new VBox(dCards, betInput, centerPop, moneyamt, pCards);




        String dScoreLabel = String.format("Your Score\n      %d", boo);
        dScore = new Label(dScoreLabel);
        stay = new Button("STAY");
        rightGame = new VBox(dScore, stay);








        BorderPane gamePane = new BorderPane();
        gamePane.setLeft(leftGame);
        gamePane.setCenter(centerGame);
        gamePane.setRight(rightGame);
        return new Scene(gamePane, 1200, 600);
    }
}
