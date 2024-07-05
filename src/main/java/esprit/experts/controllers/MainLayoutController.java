package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainLayoutController {
    private User user;

    @FXML
    AnchorPane holderPanel ;
    AnchorPane dynamicContent ;

    @FXML
    private Label welcomeLabel;

    @FXML
    private ImageView userImageView;

    @FXML
    private void initialize() {
        // Load default view
        showUsers();
    }


    private void setNode(Node node ) {
        holderPanel.getChildren().clear();
        holderPanel.getChildren().add( (Node)node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.5);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }





    public void setLoggedInUser(String userEmail) {
        UserService userService = new UserService();
        User loggedInUser = userService.getUserByEmail(userEmail);

        if (loggedInUser != null) {
            this.user = loggedInUser;
            welcomeLabel.setText("Welcome, " + loggedInUser.getFirstname() + " " + loggedInUser.getLastname());

            // Load user image if available
            if (loggedInUser.getImagePath() != null && !loggedInUser.getImagePath().isEmpty()) {
                try {
                    String imagePath = loggedInUser.getImagePath(); // Assuming imagePath is "src/main/resources/images/image_20240701222306.jpg"
                    InputStream inputStream = new FileInputStream(new File(imagePath));
                    Image image = new Image(inputStream);
                    userImageView.setImage(image);

                    // Style the user image as a rounded circle (example)
                    userImageView.setClip(new Circle(25, 25, 25));
                    userImageView.setFitWidth(50); // Adjust size as needed
                    userImageView.setFitHeight(50);
                    userImageView.setPreserveRatio(true);

                } catch (FileNotFoundException e) {
                    System.out.println("File not found: " + loggedInUser.getImagePath());
                    e.printStackTrace();
                    // Handle file not found exception
                } catch (Exception e) {
                    System.out.println("Error loading user image from path: " + loggedInUser.getImagePath());
                    e.printStackTrace();
                    // Handle other exceptions
                }
            } else {
                // Set a default image or handle the case where no image is available
            }
        } else {
            System.out.println("User with email " + userEmail + " not found.");
        }
    }
    @FXML
    public void showUsers() {
        try {
            dynamicContent =  FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/UsersDashboard.fxml"));
            setNode(dynamicContent);
        }catch( Exception e ) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void showProfile() {
        try {
            dynamicContent =  FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/ProfilePage.fxml"));
            setNode(dynamicContent);
        }catch( Exception e ) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void showPlanification(ActionEvent actionEvent) {
    }
    @FXML
    public void showAudits(ActionEvent actionEvent) {
    }
    @FXML
    public void showWorkspace(ActionEvent actionEvent) {
    }
}
