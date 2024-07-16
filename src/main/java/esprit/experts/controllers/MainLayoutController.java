package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainLayoutController {
    private static User user;
    @FXML
    public Button showUsersButton;
    @FXML
    public Button showProfileButton;
    @FXML
    public Button showPlanificationButton;
    @FXML
    public Button showAuditsButton;
    @FXML
    public Button showWorkspaceButton;
    @FXML
    public Button showDocumentButton;
    public Region spacer;
    @FXML
    private Button logoutButton;


    @FXML
    AnchorPane holderPanel;
    AnchorPane dynamicContent;

    @FXML
    private Label welcomeLabel;

    @FXML
    private ImageView userImageView;

    public static User getUser() {
        return user;
    }

    @FXML
    private void initialize() {
        // Load default view

        showUsers();
    }

    private void setNode(Node node) {
        holderPanel.getChildren().clear();
        holderPanel.getChildren().add(node);
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
        try {
            user = userService.getUserByEmail(userEmail);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getFirstname() + " " + user.getLastname());

            // Load user image if available
            if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
                try {
                    String imagePath = user.getImagePath();
                    InputStream inputStream = new FileInputStream(new File(imagePath));
                    Image image = new Image(inputStream);
                    userImageView.setImage(image);

                    // Style the user image as a rounded circle (example)
                    userImageView.setClip(new Circle(25, 25, 25));
                    userImageView.setFitWidth(50); // Adjust size as needed
                    userImageView.setFitHeight(50);
                    userImageView.setPreserveRatio(true);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found: " + user.getImagePath());
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Error loading user image from path: " + user.getImagePath());
                    e.printStackTrace();
                }
            } else {
                // Set a default image or handle the case where no image is available
            }
        } else {
            System.out.println("User with email " + userEmail + " not found.");
        }
    }

    @FXML
    private void handleLogout() {
        // Close current window
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        try {
            // Load login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.initStyle(StageStyle.UNDECORATED); // Optional, depending on your design
            loginStage.setScene(new Scene(root));
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error dialog)
        }
    }

    @FXML
    public void showUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/UserDashboard.fxml"));
            Parent root = loader.load();
            this.setButtonFocused(this.showUsersButton);
            setNode(root);
        } catch (Exception e) {
            System.out.println("Error loading UserDashboard.fxml: " + e.getMessage());

        }
    }

    @FXML
    public void showProfile() {
        try {
            //refreshUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/ProfilePage.fxml"));
            Parent root = loader.load();
            this.setButtonFocused(this.showProfileButton);
            setNode(root);
        } catch (Exception e) {
            System.out.println("Error loading ProfilePage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void showPlanification(ActionEvent actionEvent) {
    }

    @FXML
    public void showAudits(ActionEvent actionEvent) {
        try {
            refreshUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/audit.fxml"));
            Parent root = loader.load();
            this.setButtonFocused(this.showAuditsButton);
            setNode(root);
        } catch (Exception e) {
            System.out.println("Error loading ProfilePage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void ShowDocuments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/Document.fxml"));
            Parent root = loader.load();

            // Get the controller and set the logged-in user
            DocumentController documentController = loader.getController();
            documentController.setLoggedInUser(MainLayoutController.user);

            this.setButtonFocused(this.showDocumentButton);
            setNode(root);
        } catch (Exception e) {
            System.out.println("Error loading Document.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showWorkspace(ActionEvent actionEvent) {
    }

    @FXML

    private void setButtonFocused(Button focusedButton) {
        List<Button> allButtons = getAllButtons();

        for (Button button : allButtons) {
            if (button == focusedButton) {
                button.getStyleClass().add("focused");
                button.requestFocus(); // Optionally request focus programmatically
            } else {
                button.getStyleClass().remove("focused");
            }
        }
    }

    private List<Button> getAllButtons() {
        List<Button> allButtons = new ArrayList<>();
        allButtons.add(this.showWorkspaceButton);
        allButtons.add(this.showUsersButton);
        allButtons.add(this.showProfileButton);
        allButtons.add(this.showAuditsButton);
        allButtons.add(this.showPlanificationButton);
        allButtons.add(this.showDocumentButton);
        return allButtons;
    }

    public void editProfile(Long id) {
        try {
            refreshUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/editProfile.fxml"));
            Parent root = loader.load();

            // Access the controller and call setLoggedInUser
            EditProfile editProfileController = loader.getController();
            editProfileController.getUserProfile(id);
            this.setButtonFocused(this.showProfileButton);
            setNode(root);
        } catch (Exception e) {
            System.out.println("Error loading ProfilePage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshUser() {
        UserService us = new UserService();
        try {
            user = us.getById(user.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(user);
    }
}
