package com.example.demo3;

import Project2.Quiz;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javafx.scene.paint.Color.WHITE;

public class Server extends Application {
    private static Timeline TIMER;
    private static Label showTimer;
    private static int questions_NUMBERS =0;
    private static Quiz FromQuiz;
    private  static Stage stage;
    private static int second_Timer = 4;
    private static int START_TRUE= 0;
    private static int PASSWORD_CODE ;
    private static List<String> PLAYERS_OPTIONS = new ArrayList<>();
    private static List<String> PLAYERS_NAMES = new ArrayList<>();
    private static List<Integer> PLAYERS_POINTS = new ArrayList<>();

    public static StackPane MainPane() throws IOException, InterruptedException {
        StackPane stackpane = new StackPane();
        BorderPane borderPane = new BorderPane();
        stackpane.setStyle("-fx-background-color: #3e147f");
        Label background = new Label("PIN : " + PASSWORD_CODE);
        background.setFont(Font.font("Veranda", FontWeight.BOLD, FontPosture.ITALIC, 20));
        background.setTextFill(WHITE);
        Button startButton = new Button("START");
        startButton.setMinSize(8,15);
        startButton.setFont(Font.font("Veranda", FontWeight.BOLD, FontPosture.ITALIC,14));
        background.setAlignment(Pos.CENTER);
        background.setMinWidth(500);
        Pane pane = new Pane();
        pane.getChildren().addAll(background);
        borderPane.setTop(background);
        borderPane.setCenter(startButton);
        stackpane.getChildren().addAll(borderPane);
        startButton.setOnAction(e->{
            try {
                START_TRUE = 1;
                stage.setScene(new Scene(Bastau(),500.,500.));
            } catch (IOException ignored) {
            } catch (InterruptedException ignored) {
            }
        });
        return stackpane;
    }
    private static String TIMER_GAME(int second) throws IOException, InterruptedException {
        if (second_Timer == -1) {
            TIMER.stop();
            second_Timer = 4;
            questions_NUMBERS++;
            if (FromQuiz.suraqtar.size() >= questions_NUMBERS) {
                stage.setScene(new Scene(Bastau(), 500.0, 500.0));
                TIMER.play();
            }
        }
        return ""+second;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        PASSWORD_CODE = (int)(Math.random()*1000000);
        new Thread(() -> {
            try {
                ServerSocket SERVERS = new ServerSocket(11111);
                int clientNo = 1;
                while (true) {
                    try {
                        System.out.println("Waiting for incomes");
                        Socket SOCKET = SERVERS.accept();
                        System.out.println(clientNo + " Client is Connected!");
                        new Thread(() -> {
                            try {
                                DataInputStream fromClient = new DataInputStream(SOCKET.getInputStream());
                                DataOutputStream toClient = new DataOutputStream(SOCKET.getOutputStream());
                                while (true) {
                                    int clientPin = fromClient.readInt();
                                    if (clientPin != PASSWORD_CODE) {
                                        toClient.writeUTF("Wrong PIN!");
                                    } else {
                                        toClient.writeUTF("CONNECT!");
                                    }

                                    while (START_TRUE!=1){
                                        Thread.onSpinWait();
                                    }
                                    toClient.writeInt(START_TRUE);
                                    toClient.writeInt(FromQuiz.suraqtar.size());
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }).start();
                        clientNo++;
                    } catch (IOException e) {

                    }
                }
            } catch (IOException e) {

            }

        }).start();
        stage = primaryStage;
        stage.setScene(new Scene(From_FILE(), 500., 500.));
        stage.setTitle("Server");
        stage.show();
    }

    private static void Variants(int k) {
        FromQuiz.s();
        for (k = 0;  k < FromQuiz.suraqtar.size(); k++) {
            FromQuiz.Suraqtardy_Qosu(k);
            (FromQuiz.TRUE_FALSE ? FromQuiz.T : FromQuiz.F).toString();
        }
    }
    public static BorderPane Resuilts() throws FileNotFoundException {
        BorderPane borderPane = new BorderPane();
        Image image = new Image(new FileInputStream("BACKGRUND PHOTO"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(500);
        imageView.setFitHeight(500);
        borderPane.setCenter(imageView);
        return borderPane;
    }
    private  static BorderPane Bastau() throws IOException, InterruptedException {
        Variants(0);
        int sizes = FromQuiz.suraqtar.size();
        if(sizes == questions_NUMBERS) {
            TIMER.stop();
            return Resuilts();
        }
        else{
            TIMER = new Timeline();
            showTimer = new Label();
            showTimer.setText(TIMER_GAME(second_Timer));
            TIMER.setCycleCount(Timeline.INDEFINITE);
            TIMER.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
                try {
                    showTimer.setText(TIMER_GAME(--second_Timer));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }));
            TIMER.play();
            VBox verticalAC = new VBox(4);
            VBox verticalBD = new VBox(4);
            BorderPane borderPane = new BorderPane();
            Label label = new Label(FromQuiz.suraqtar.get(questions_NUMBERS));
            label.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 14));
            label.setWrapText(true);

            Image image = new Image(new FileInputStream("C:\\Users\\Dias\\IdeaProjects\\Project_2_real\\src\\main\\java\\com\\example\\demo1\\logo.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(240);
            imageView.setFitHeight(150);
            imageView.setX(130);
            imageView.setY(80);
            borderPane.getChildren().addAll(imageView);
            Button[] buttons = new Button[4];
            for (int i = 0; i < 4; i++) {
                Button button = new Button();
                button.setText(String.valueOf(FromQuiz.Variants.get(questions_NUMBERS).get(i)));
                button.setFont(Font.font("Veranda", FontWeight.BLACK, FontPosture.REGULAR, 13));
                button.setTextFill(WHITE);
                button.setMinSize(245, 50);
                buttons[i] = button;
            }
            buttons[0].setStyle("-fx-background-color : RED");
            buttons[1].setStyle("-fx-background-color : BLUE");
            buttons[2].setStyle("-fx-background-color : YELLOW");
            buttons[3].setStyle("-fx-background-color : GREEN");
            HBox Horizontal = new HBox(4);
            verticalAC.getChildren().addAll(buttons[0], buttons[2]);
            verticalBD.getChildren().addAll(buttons[1], buttons[3]);
            Horizontal.getChildren().addAll(verticalAC, verticalBD);
            borderPane.setTop(new StackPane(label));
            Pane pane = new Pane();
            showTimer.setLayoutX(40);
            showTimer.setLayoutY(200);
            Horizontal.setPadding(new Insets(4));
            pane.getChildren().addAll(showTimer);
            borderPane.getChildren().addAll(pane);
            borderPane.setBottom(new StackPane(Horizontal));
            return borderPane;
        }
    }
    public static StackPane From_FILE()throws IOException {
        Pane vremenno = new Pane();
        Button buttonChoose = new Button("Choose a file");
        buttonChoose.setStyle("-fx-background-color : white");
        buttonChoose.setFont(Font.font("Times New Roman",FontWeight.BOLD,FontPosture.ITALIC,16));
        buttonChoose.setLayoutX(180);
        buttonChoose.setLayoutY(380);
        buttonChoose.setTextFill(Color.BLUEVIOLET);

        Image background = new Image(new FileInputStream("C:\\Users\\Dias\\Downloads\\Background_kahoot.png"));
        ImageView imageView = new ImageView(background);
        StackPane stackPane = new StackPane();
        vremenno.getChildren().addAll(buttonChoose);
        stackPane.getChildren().addAll(imageView,vremenno);
        buttonChoose.setOnAction(e -> {
            FromQuiz = new Quiz();
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            try {
                FromQuiz.filedan(file.getPath());
                for (int i = 0; i < FromQuiz.suraqtar.size(); i++) {
                    Collections.shuffle(FromQuiz.Variants.get(i));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                stage.setScene(new Scene(MainPane(), 500., 500.));
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        return stackPane;
    }
    public static void main(String[] args) {
        launch();
    }
}