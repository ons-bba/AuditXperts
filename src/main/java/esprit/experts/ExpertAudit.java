package esprit.experts;

import esprit.experts.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ExpertAudit extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize database connection
        // Initialize database connection
        Parent root;
        try {
            DatabaseConnection.getConnection();
            root = FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/Document.fxml"));
            Scene scene = new  Scene(root , 1350 , 850);
            primaryStage.setTitle("Login Page");
            primaryStage.setScene(scene);
//            primaryStage.setFullScreen(true); // Set the stage to maximized mode

            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
