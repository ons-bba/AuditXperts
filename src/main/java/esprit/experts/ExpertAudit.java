package esprit.experts;

import esprit.experts.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ExpertAudit extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize database connection
        DatabaseConnection.connectDB();
        Parent root = FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Main Layout");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Set the stage to maximized mode

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
