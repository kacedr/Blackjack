import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class javaFxFront extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    VBox v1;
    HBox h1;
    Label blackjack;
    TextField moneyprompt;
    Button play, help;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Blackjack");
        Image bj = new Image("blackjack.png");
        ImageView blackjacktitle = new ImageView(bj);
        blackjacktitle.setFitWidth(100);
        blackjacktitle.setFitHeight(100);
        blackjacktitle.setPreserveRatio(true);
        blackjacktitle.setSmooth(true);

        blackjack = new Label("Blackjack");

        blackjack.setStyle("-fx-font-family: 'Constantia'; -fx-text-fill: black; -fx-font-size: 48px; -fx-font-weight: bold;");
        h1 = new HBox(blackjacktitle, blackjack);
        h1.setAlignment(Pos.CENTER);
        moneyprompt = new TextField("Enter money amount here");
//        moneyprompt.setPromptText("Enter money amount here");
        moneyprompt.setMaxWidth(200);

        play = new Button("Play");
        play.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");
        play.setMinWidth(120);
        play.setMinHeight(50);
        help = new Button("Help?");
        v1 = new VBox(20, h1, moneyprompt, play);
        v1.setPrefWidth(500);
        v1.setMaxWidth(500);
        v1.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(v1);
        borderPane.setRight(help);
        borderPane.setStyle("-fx-background-color: #005e30;");
//        BorderPane.setMargin(v1, new Insets(130,20,20,225));





        Scene scene = new Scene(borderPane, 1200,600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
