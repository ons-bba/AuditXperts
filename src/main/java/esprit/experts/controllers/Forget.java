package esprit.experts.controllers;

import esprit.experts.services.UserService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Forget {

    @FXML
    private Button email;
    @FXML
    TextField text ;
    @FXML
    void onforget(ActionEvent event) throws IOException {
            String emaill = text.getText().trim();
          boolean state =   new UserService().updatePasswordByEmail(emaill);
        if(!state) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/login.fxml"));
        Parent root = loader.load();

        // Fade transition
        FadeTransition ft = new FadeTransition(Duration.millis(1000), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        // Create a new stage for the main layout
        Stage stage = (Stage) email.getScene().getWindow(); // Replace `button` with your actual button object
        stage.setScene( new Scene(root , 1350 , 800));
        stage.setTitle("forget password ");
//            stage.setFullScreen(true); // Set the stage to maximized mode
        // Show the main stage
        stage.show();
    }
}
