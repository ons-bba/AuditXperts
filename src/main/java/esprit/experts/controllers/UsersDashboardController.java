package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsersDashboardController implements Initializable {

    @FXML
    private VBox userListContainer;

    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserService();
//        loadUsers();
    }

    private void loadUsers() {
        List<User> users = userService.read();

        for (User user : users) {
            HBox userCard = createUserCard(user);
            userListContainer.getChildren().add(userCard);
        }
    }

    private HBox createUserCard(User user) {
        HBox card = new HBox();
        card.getStyleClass().add("user-card");
        card.setSpacing(10);
        card.setPadding(new Insets(10));

        Label nameLabel = new Label(user.getFirstname() + " " + user.getLastname());
        Label statusLabel = new Label("Status: " + user.getStatus());
        Label roleLabel = new Label("Role: " + user.getRole());

        card.getChildren().addAll(nameLabel, statusLabel, roleLabel);
        return card;
    }
}
