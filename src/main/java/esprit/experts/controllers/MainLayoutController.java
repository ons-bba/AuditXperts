package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class MainLayoutController {
    private User user;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox sidebar;

    @FXML
    private BorderPane contentPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private ImageView userImageView;

    @FXML
    private void initialize() {
        // Load default view
        loadView("ProfilePage.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/" + fxmlFile));
            BorderPane view = loader.load();
            contentPane.setCenter(view);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + fxmlFile);
            System.out.println("Check if the file exists and the path is correct.");
        }
    }

    @FXML
    private void showUsers() {
        loadView("Users.fxml");
    }

    @FXML
    private void showProfile() {
        loadView("ProfilePage.fxml");
    }

    @FXML
    private void showPlanification() {
        loadView("Planification.fxml");
    }

    @FXML
    private void showAudits() {
        loadView("Audits.fxml");
    }

    @FXML
    private void showWorkspace() {
        loadView("WorkSpace.fxml");
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

}
