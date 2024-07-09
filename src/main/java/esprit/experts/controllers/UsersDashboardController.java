package esprit.experts.controllers;

import esprit.experts.entities.User;
import esprit.experts.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private ComboBox<?> addsex;

    @FXML
    private ComboBox<?> addstatus;

    @FXML
    private Button clearbtn;

    @FXML
    private Button deletebtn;

    @FXML
    private Button importBtn;

    @FXML
    private TableColumn<User, String> t_email;

    @FXML
    private TableColumn<User, String> t_firstname;

    @FXML
    private TableColumn<User, Long> t_id;

    @FXML
    private TableColumn<User, String> t_lastname;

    @FXML
    private TableColumn<User, String> t_role;

    @FXML
    private TableColumn<User, String> t_sex;

    @FXML
    private TableColumn<User, String> t_status;

    @FXML
    private TableView<User> t_tableView;

    @FXML
    private TextField tableSearch;

    @FXML
    private Button updatebtn;

    @FXML
    private AnchorPane usersBg;
    private String[] roles = {"SUPERVISOR", "ADMIN", "AUDITEUR"};
    private  String[] sexs = {"Male", "Female"};
    private  String[] statuss = {"ACTIVE", "INACTIVE"};


    List<User> users ;
    UserService userService ;
    Image image ;
    File selectedImageFile ;

    private ObservableList<User> usersObservableList;
    public void  showListData (){
        usersObservableList = listUserData();
        t_id.setCellValueFactory( new PropertyValueFactory<>("id"));
        t_firstname.setCellValueFactory( new PropertyValueFactory<>("firstname"));
        t_lastname.setCellValueFactory( new PropertyValueFactory<>("lastname"));
        t_email.setCellValueFactory( new PropertyValueFactory<>("email"));
        t_sex.setCellValueFactory( new PropertyValueFactory<>("sex"));
        t_status.setCellValueFactory( new PropertyValueFactory<>("status"));
        t_role.setCellValueFactory( new PropertyValueFactory<>("role"));
        t_tableView.setItems(usersObservableList);
    }

    public ObservableList<User> listUserData(){
        return  userService.userListData();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserService();
        showListData();
        addUserRole();
        addStatus();
        addSex();

    }
    public void addUserSelect(){
        User user = t_tableView.getSelectionModel().getSelectedItem();
        int num = t_tableView.getSelectionModel().getSelectedIndex();
        if((num-1) < -1){return;}
        addfirstname.setText(user.getFirstname());
        addlastname.setText(user.getLastname());
        addemail.setText(user.getEmail());
        String imagePath = user.getImagePath();
        try {
            InputStream inputStream = new FileInputStream(new File(imagePath));
            Image imagee = new Image(inputStream);
            addImage.setImage(imagee);
        } catch (FileNotFoundException e) {
            System.err.println("Image file not found: " + e.getMessage());
            // Handle file not found error
        }
        addrole.getSelectionModel().select(getRolePosition(user.getRole()));
        addsex.getSelectionModel().select(getgenderPosition(user.getSex()));
        addstatus.getSelectionModel().select(getstatusPosition(user.getStatus()));

    }
    public  void  userInsetImage(){
            FileChooser fileChooser = new FileChooser();
            selectedImageFile = fileChooser.showOpenDialog(addlastname.getScene().getWindow());
            if(selectedImageFile != null){
                 image = new Image(selectedImageFile.toURI().toString());
                    addImage.setImage(image);

            }
    }


    public void  addUser(){
        User user = new User();
        try {
            Alert alert;
            if (!areFieldsValid()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else {
                user.setFirstname(addfirstname.getText());
                user.setLastname(addlastname.getText());
                user.setEmail(addemail.getText());
                user.setSex(addsex.getSelectionModel().getSelectedItem().toString());
                user.setRole(addrole.getSelectionModel().getSelectedItem().toString());
                user.setStatus(addstatus.getSelectionModel().getSelectedItem().toString());
                String imagePath = null;
                if (selectedImageFile != null) {
                    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String extension = getFileExtension(selectedImageFile);
                    String newImageName = "image_" + timestamp + "." + extension;

                    File dest = new File("src/main/resources/images/" + newImageName);
                    try {
                        // Ensure the directory exists
                        Files.createDirectories(dest.getParentFile().toPath());
                        Files.copy(selectedImageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        imagePath = "src/main/resources/images/" + newImageName;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                user.setImagePath(imagePath);
                boolean icreated =  this.userService.createUser(user);
                if(icreated) {

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();
                    showListData();
                    addUserReset();
                };
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void addUserReset(){
        addfirstname.setText("");
        addlastname.setText("");
        addemail.setText("");
        addemail.setText("");
        addrole.getSelectionModel().clearSelection();
        addsex.getSelectionModel().clearSelection();
        addstatus.getSelectionModel().clearSelection();
        selectedImageFile= null ;
        addImage.setImage(null);
    }
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        if (lastIndex == -1) {
            return "";
        }
        return name.substring(lastIndex + 1);
    }
    private boolean areFieldsValid() {
        return !addfirstname.getText().isEmpty() &&
                !addlastname.getText().isEmpty() &&
                !addemail.getText().isEmpty() &&
                addsex.getSelectionModel().getSelectedItem() != null &&
                addrole.getSelectionModel().getSelectedItem() != null &&
                addstatus.getSelectionModel().getSelectedItem() != null;
    }
    private void  addUserRole(){
        List<String> listP = new ArrayList<>();

        for (String data : roles) {
            listP.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listP);
        addrole.setItems(listData);
    }
    private void  addStatus(){
        List<String> listP = new ArrayList<>();

        for (String data : statuss) {
            listP.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listP);
        addstatus.setItems(listData);
    }
    private void  addSex(){
        List<String> listP = new ArrayList<>();

        for (String data : sexs) {
            listP.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listP);
        addsex.setItems(listData);
    }

    public int getRolePosition(String role) {
        for (int i = 0; i < roles.length; i++) {
            if (roles[i].equals(role)) {
                return i;
            }
        }
        // Return -1 if the role is not found
        return 0;
    }
    public int getgenderPosition(String sex) {
        System.out.println(sex);
        for (int i = 0; i < sexs.length; i++) {
            if (sexs[i].equals(sex)) {
                return i;
            }
        }
        // Return -1 if the role is not found
        return 0;
    }
    public int getstatusPosition(String state) {
        for (int i = 0; i < statuss.length; i++) {
            if (roles[i].equals(state)) {
                return i;
            }
        }
        // Return -1 if the role is not found
        return 0;
    }


    public void deleteuser(javafx.event.ActionEvent actionEvent) {
        System.out.println("Start");
        User user = t_tableView.getSelectionModel().getSelectedItem();
        if (user != null) {
            boolean isDeleted = this.userService.DeleteUser(user);
            if (isDeleted) {
                // Show information alert if deletion was successful
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Deleted");
                alert.setHeaderText(null);
                alert.setContentText("User deleted successfully!");
                alert.showAndWait();
            } else {
                // Show error alert if deletion failed
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Deletion Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete user.");
                alert.showAndWait();
            }
        } else {
            // Show warning alert if no user is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No User Selected");
            alert.setHeaderText(null);
            alert.setContentText("No user selected for deletion.");
            alert.showAndWait();
        }
        showListData();
        addUserReset();
    }

    public void updateUser(ActionEvent actionEvent) {
        User user = t_tableView.getSelectionModel().getSelectedItem();
        try {
            Alert alert;
            if (!areFieldsValid()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else {
                user.setFirstname(addfirstname.getText());
                user.setLastname(addlastname.getText());
                user.setEmail(addemail.getText());
                user.setSex(addsex.getSelectionModel().getSelectedItem().toString());
                user.setRole(addrole.getSelectionModel().getSelectedItem().toString());
                user.setStatus(addstatus.getSelectionModel().getSelectedItem().toString());
                String imagePath = null;
                if (selectedImageFile != null) {
                    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String extension = getFileExtension(selectedImageFile);
                    String newImageName = "image_" + timestamp + "." + extension;

                    File dest = new File("src/main/resources/images/" + newImageName);
                    try {
                        // Ensure the directory exists
                        Files.createDirectories(dest.getParentFile().toPath());
                        Files.copy(selectedImageFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        imagePath = "src/main/resources/images/" + newImageName;
                        user.setImagePath(imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.userService.Update(user);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();
                showListData();
                addUserReset();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void addEmployeeSearch() {
        // Create a filtered list with initial data
        FilteredList<User> filteredData = new FilteredList<>(usersObservableList, p -> true);

        // Set the filter Predicate whenever the filter changes
        tableSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                // If filter text is empty, display all users
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every user with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getFirstname().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (user.getLastname().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // Wrap the FilteredList in a SortedList.
        SortedList<User> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(t_tableView.comparatorProperty());
        System.out.println(sortedData.size());
        // Add sorted (and filtered) data to the table.
        t_tableView.setItems(sortedData);

        // Refresh TableView to update the display.
        t_tableView.refresh();
    }
}
