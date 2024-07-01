package esprit.experts;

import esprit.experts.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ExpertAudit extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database connection
        DatabaseConnection.connectDB();

        // Load FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/register.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Set up the stage
        stage.setTitle("User Registration");
        stage.setScene(scene);

        // Set the stage to full screen
        stage.setMaximized(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
