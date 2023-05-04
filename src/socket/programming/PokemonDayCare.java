package socket.programming;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.net.ServerSocket;
import Utilities.Pokemon;

/**@author Jasmine Rodriguez
 * @id U0000010605
 * @date 4/17/23
 * The server class that will be sent the Pokemon's information
 * so it can display if the Pokemon is in the server or not
 */
public class PokemonDayCare extends Application
{
    private TextArea ta = new TextArea();

    int connectCount = 0; //int that will keep track of how many clients connect to the server

    String ipAddress;

    @Override
    //Create the UI which consists of a textField and scroll bar
    public void start(Stage primaryStage) {

        ta.setWrapText(true);

        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Pokemon Daycare");

        primaryStage.setScene(scene);
        primaryStage.show();


        new Thread(() -> startServer()).start();
    }

    // When connected to a client
    private void connectToClient(Socket connectToClient) {

        try {
            // Retrieves the IP address of newly connected client
            ipAddress = connectToClient.getInetAddress().getHostAddress();

            Platform.runLater(() ->
                    ta.appendText("Trainer's IP Address: " + ipAddress + '\n'));

            connectCount++;

            Platform.runLater(() -> ta.appendText("New connection made with Trainer. Total connections = " + connectCount + '\n'));

            ObjectInputStream isFromClient = new ObjectInputStream(connectToClient.getInputStream());

            while (true) {
                Pokemon pokemonInCare = (Pokemon) isFromClient.readObject();

                // Displays the Pokemon's current status in the textField ta
                if (pokemonInCare.isCheckedIn())
                    ta.appendText("Pokemon " + pokemonInCare.getName() + " has been checked in." + '\n');
                else
                    ta.appendText("Pokemon " + pokemonInCare.getName() + " has been checked out." + '\n');
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex);
        }
    }

    // Starts the server and allows for multiple clients to connect
    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);

            Platform.runLater(() ->
                    ta.appendText("Server has started at " + new Date() + '\n'));

            while (true) // New sockets can be accepted by starting a new thread
            {
                Socket socket = serverSocket.accept();
                new Thread(() -> connectToClient(socket)).start();
            }
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}