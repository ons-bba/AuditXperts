package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainLayoutController {
    private User user ;
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox menuVBox;

    @FXML
    private BorderPane contentPane;

    @FXML
    private void initialize() {
        loadView("ProfilePage.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/esprit/experts/controllers/" + fxmlFile));
            BorderPane view = loader.load();
            contentPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + fxmlFile);
            System.out.println("Check if the file exists and the path is correct.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An unexpected error occurred while loading the FXML file: " + fxmlFile);
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
            System.out.println(this.user);
        } else {
            System.out.println("User with email " + userEmail + " not found.");
        }
    }

}
