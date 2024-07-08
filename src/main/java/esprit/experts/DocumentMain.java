package esprit.experts;
import esprit.experts.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DocumentMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root;
        try {

           root = FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/Documents.fxml"));
            Scene scene = new  Scene(root , 1350 , 850);
            primaryStage.setTitle("Document Page");
            primaryStage.setScene(scene);
//            primaryStage.setFullScreen(true); // Set the stage to maximized mode

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}