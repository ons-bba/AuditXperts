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
        DatabaseConnection.connectDB();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("controllers/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}