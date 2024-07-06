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
        // Initialize database connection
        Parent root;
        try {
            DatabaseConnection.connectDB();
            root = FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/login.fxml"));
            Scene scene = new  Scene(root , 1350 , 850);
            primaryStage.setTitle("Login Page");
            primaryStage.setScene(scene);
//            primaryStage.setFullScreen(true); // Set the stage to maximized mode

            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
