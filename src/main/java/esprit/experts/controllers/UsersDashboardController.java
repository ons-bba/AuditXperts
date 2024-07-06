package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsersDashboardController implements Initializable {
    @FXML
    private ImageView addImage;

    @FXML
    private Button addbtn;

    @FXML
    private TextField addemail;

    @FXML
    private TextField addfirstname;

    @FXML
    private TextField addlastname;

    @FXML
    private ComboBox<?> addrole;

    @FXML
    private TextField addsex;

    @FXML
    private ComboBox<?> addstatus;

    @FXML
    private Button clearbtn;

    @FXML
    private Button deletebtn;

    @FXML
    private Button importBtn;

    @FXML
    private TableColumn<?, ?> t_email;

    @FXML
    private TableColumn<?, ?> t_firstname;

    @FXML
    private TableColumn<?, ?> t_id;

    @FXML
    private TableColumn<?, ?> t_lastname;

    @FXML
    private TableColumn<?, ?> t_role;

    @FXML
    private TableColumn<?, ?> t_sex;

    @FXML
    private TableColumn<?, ?> t_status;

    @FXML
    private TableView<?> t_tableView;

    @FXML
    private TextField tableSearch;

    @FXML
    private Button updatebtn;

    @FXML
    private AnchorPane usersBg;

    List<User> users ;
    UserService userService ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserService();
        users =  loadUsers();
    }

    private  List<User> loadUsers() {
      return  userService.read();

    }


}
