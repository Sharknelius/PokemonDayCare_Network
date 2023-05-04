package socket.programming;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import Utilities.Pokemon;

/**@author Jasmine Rodriguez
 * @id U0000010605
 * @date 4/17/23
 * A client class that sends the Pokemon's name, type, and checked-in status to the server
 * The user will be prompted to enter these variables into a textfield and comboBox
 * A send and pick-up button exist but only one will be enabled at a time
 */
public class TrainerClient extends Application {

    private TextField tfName = new TextField();

    String[] pokemonTypes = {"Bulbasaur", "Charmander", "Pikachu", "Squirtle"};

    private ComboBox cbType = new ComboBox(FXCollections
            .observableArrayList(pokemonTypes));

    private Button sendBtn = new Button("Send to daycare");


    private Button pickUpBtn = new Button("Pick up from daycare");
    ObjectOutputStream osToServer = null;

    @Override

    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Choose a Pokemon Type"), 0, 0);
        gridPane.add(new Label("Pokemon's name"), 0, 1);
        gridPane.add(cbType, 1, 0);
        gridPane.add(tfName, 1, 1);
        gridPane.add(sendBtn, 0, 2);
        gridPane.add(pickUpBtn, 1, 2);

        //Only the pick up button is disabled when first prompted
        sendBtn.setDisable(false);
        pickUpBtn.setDisable(true);
        tfName.setAlignment(Pos.BASELINE_RIGHT);


        Scene scene = new Scene(gridPane,450, 200);
        primaryStage.setTitle("Trainer");
        primaryStage.setScene(scene);
        primaryStage.show();


        sendBtn.setOnAction(e -> {
            try
            {
                sendToServer();
            }
            catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });

        pickUpBtn.setOnAction(e -> takeFromServer());


        try
        {
            //initial connection to server
            Socket serverConnection = new Socket("localhost",8000);
            //isFromServer = new ObjectInputStream(serverConnection.getInputStream());
            osToServer = new ObjectOutputStream(serverConnection.getOutputStream());
        } catch (IOException ex)
        {
            System.err.println(ex);
        }

    }


    public void sendToServer() throws IOException {
        try {
            String pokemonType = cbType.getValue().toString();
            String name = tfName.getText().trim();
            Pokemon pokemonInCare = new Pokemon(pokemonType, name);
            pokemonInCare.checkIn();
            sendBtn.setDisable(true);
            pickUpBtn.setDisable(false);
            cbType.setDisable(false);
            tfName.setDisable(false);

            osToServer.writeObject(pokemonInCare);

            osToServer.flush();
            osToServer.reset();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void takeFromServer() {

        try {
            //Retrieve pokemon from daycare
            sendBtn.setDisable(false);
            pickUpBtn.setDisable(true);
            cbType.setDisable(false);
            tfName.setDisable(false);

            String pokemonType = cbType.getValue().toString();
            String name = tfName.getText().trim();
            Pokemon pokemonInCare = new Pokemon(pokemonType, name);
            pokemonInCare.checkOut();

            osToServer.writeObject(pokemonInCare);

            osToServer.flush();
        } catch (IOException ex) {

            System.err.println(ex);


        }
    }
    public static void main (String[]args){
        launch(args);
    }
}
