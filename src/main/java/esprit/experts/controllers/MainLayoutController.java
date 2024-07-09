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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    AnchorPane holderPanel ;
    AnchorPane dynamicContent ;

    @FXML
    private Label welcomeLabel;

    @FXML
    private ImageView userImageView;

    @FXML
    private void initialize() {// Load default view
        showlanding();
    }


    public static User getUser() {
        return user;
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
                    System.out.println("set up  ;" + user.getId());

                    this.buttonSetting();

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
            System.out.println("Error loading UsersDashboard.fxml: " + e.getMessage());

        }
    }
    @FXML
    public void showProfile() {
        try {
            refreshUser();
            dynamicContent =  FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/ProfilePage.fxml"));
            this.setButtonFocused(this.showProfileButton);
            setNode(dynamicContent);
        }catch( Exception e ) {
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
            dynamicContent =  FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/audit.fxml"));
            this.setButtonFocused(this.showAuditsButton);
            setNode(dynamicContent);
        }catch( Exception e ) {
            System.out.println("Error loading ProfilePage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    public void showWorkspace(ActionEvent actionEvent) {
    }

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
        return  allButtons;
    }
    public void  afficheraudituser(){
        try {
            refreshUser();
            dynamicContent =  FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/audituser.fxml"));
            this.setButtonFocused(this.showAuditsButton);
            setNode(dynamicContent);
        }catch( Exception e ) {
            System.out.println("Error loading ProfilePage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void editProfile(Long id) {
        try {
            refreshUser();
            FXMLLoader loader =   new FXMLLoader(getClass().getResource("/esprit/experts/controllers/editProfile.fxml"));
            Parent root = loader.load();

            // Access the controller and call setLoggedInUser
            EditProfile editProfileController = loader.getController();
            editProfileController.getUserProfile(id);
            this.setButtonFocused(this.showProfileButton);
            setNode(root);
        }catch( Exception e ) {
            System.out.println("Error loading ProfilePage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void  refreshUser(){
        if(user == null) {return;}
        UserService us = new UserService();
        User newUser = us.getById(user.getId());
        System.out.println(newUser);
        user = newUser;
        this.buttonSetting();
    }

    private void buttonSetting(){
        // Hide all buttons initially
        showUsersButton.setVisible(false);
        showProfileButton.setVisible(false);
        showPlanificationButton.setVisible(false);
        showAuditsButton.setVisible(false);
        showWorkspaceButton.setVisible(false);
        switch (user.getRole()) {
            case "AUDITEUR":
                System.out.println("Setting buttons for AUDITEUR");
                showProfileButton.setVisible(true);
                showWorkspaceButton.setVisible(true);
                break;
            case "ADMIN":
                System.out.println("Setting buttons for ADMIN");
                showUsersButton.setVisible(true);
                showAuditsButton.setVisible(true);
                showProfileButton.setVisible(true);
                break;
            case "SUPERVISOR":
                System.out.println("Setting buttons for SUPERVISOR");
                showProfileButton.setVisible(true);
                showPlanificationButton.setVisible(true);
                showWorkspaceButton.setVisible(true);
                break;
            default:
                System.out.println("No role matched.");
                break;
        }

    }
    public void logout(){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();
            try {
                if (option.get().equals(ButtonType.OK)) {

                    holderPanel.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/login.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        private  void  showlanding(){
            try {
                dynamicContent =  FXMLLoader.load(getClass().getResource("/esprit/experts/controllers/landing.fxml"));
                setNode(dynamicContent);
            }catch( Exception e ) {
                System.out.println("Error loading UsersDashboard.fxml: " + e.getMessage());
                System.out.println(e);

            }
        }
}
