package esprit.experts.controllers;
import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfilePageController implements Initializable {
    @FXML
    private ImageView profileImage;

    @FXML
    private Label nameLabel;


    @FXML
    private Button editButton;
    @FXML
    private TextField role;

    @FXML
    private TextField sex;

    @FXML
    private TextField status;
    @FXML
    private TextField email;
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fetch the user data from MainLayoutController
        this.user = MainLayoutController.getUser();

        if (user != null) {
            // Populate UI elements with user data
            nameLabel.setText(user.getFirstname() + " " + user.getLastname());
            email.setText(user.getEmail());
            role.setText(user.getRole());
            status.setText(user.getStatus());
            sex.setText(user.getSex());

            // Load profile image if available
            String imagePath = user.getImagePath();
            try {
                InputStream inputStream = new FileInputStream(new File(imagePath));
                Image image = new Image(inputStream);
                profileImage.setImage(image);
            } catch (FileNotFoundException e) {
                System.err.println("Image file not found: " + e.getMessage());
                // Handle file not found error
            }
        } else {
            System.err.println("User object is null.");
        }
    }
    @FXML
    public void editProfile() {
        try {
        // Load the MainLayout.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/MainLayout.fxml"));
        Parent root = loader.load();
        // Access the controller and call setLoggedInUser
        MainLayoutController mainController = loader.getController();

        Stage stage = (Stage) nameLabel.getScene().getWindow(); // Replace `button` with your actual button object
        Scene scene = new Scene(root);
        stage.setScene(scene);
        mainController.editProfile(this.user.getId());
        stage.show();

    } catch(IOException ex ) {
        System.out.println(ex.getMessage());
    }
}

}
