package lms;

import lms.connector.db.SQLConnector; // Import the DB class
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize DB Tables
        SQLConnector.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("library_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("LMS - Library Management System (Postgres Edition)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}