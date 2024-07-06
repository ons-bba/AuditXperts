package esprit.experts.controllers;


import esprit.experts.entities.Audit;
import esprit.experts.services.AuditService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class AuditController {
    AuditService auditService = new AuditService();
    Audit selectedAudit = null;
    @FXML
    private TableColumn<Audit, String> columnapproach;

    @FXML
    private TableColumn<Audit, String> columndeficiency;

    @FXML
    private TableColumn<Audit, String> columnduration;

    @FXML
    private TableColumn<Audit, String> columnreport;

    @FXML
    private TableColumn<Audit, String> titlecolumn;

    @FXML
    private TableColumn<Audit, String> columnstatus;

    @FXML
    private TableView<Audit> auditsTable;
    @FXML
    private TextField approach;

    @FXML
    private TextField deficiency;

    @FXML
    private TextField duration;

    @FXML
    private TableColumn<Audit, Integer> id;

    @FXML
    private TextField report;

    @FXML
    private TextField returndate;

    @FXML
    private TextField sartdate;

    @FXML
    private TableColumn<Audit, String> startdate;

    @FXML
    private TextField status;

    @FXML
    private Button submit_btn;

    @FXML
    private TextField title;

    @FXML
    void goToCategory(ActionEvent event) {

    }

    @FXML
    void onDelete(ActionEvent event) {
        selectedAudit = auditsTable.getSelectionModel().getSelectedItem();
        try {
            auditService.delete(selectedAudit);
            loadAudits();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
     void clearInputFields() {
        title.clear();
        sartdate.clear();
        returndate.clear();
        deficiency.clear();
        report.clear();
        duration.clear();
        status.clear();
        approach.clear();
    }

    @FXML
    void onEdit(ActionEvent event) {
        selectedAudit = auditsTable.getSelectionModel().getSelectedItem();

        title.setText(selectedAudit.getTitle());
        sartdate.setText(selectedAudit.getStartdate());
        returndate.setText(selectedAudit.getReturndate());
        deficiency.setText(String.valueOf(selectedAudit.getDeficiency()));
        report.setText(String.valueOf(selectedAudit.getReport()));
        duration.setText(String.valueOf(selectedAudit.getDuration()));
        status.setText(String.valueOf(selectedAudit.getStatus()));
        approach.setText(selectedAudit.getApproach());
        submit_btn.setText("Modifier");
    }

    @FXML
    void onSubmit(ActionEvent event) {
        String state = submit_btn.getText();

            if (state.equals("Modifier")) {
                updateAuidot();
                submit_btn.setText("Ajouter");

                clearInputFields();
            } else {
                addaudit();
                clearInputFields();

            }
    }
    public void initialize() {
        try {
            loadAudits();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void addaudit() {
        try {


            String titleText = title.getText();
            String startdateTexte = sartdate.getText();
            String returndateText = returndate.getText();
            String deficiencyText = deficiency.getText();
            String reportText = report.getText();
            String durationText = duration.getText();
            String statusText = status.getText();
            String approachText = approach.getText();


            Audit audit = new Audit( titleText, startdateTexte, returndateText, deficiencyText, reportText, durationText, statusText, approachText);
            auditService.add(audit);
            loadAudits();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


        void loadAudits() throws SQLException {
        List<Audit> audits = auditService.findAll();
            System.out.println(audits);
        auditsTable.getItems().setAll(audits);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        startdate.setCellValueFactory(new PropertyValueFactory<>("startdate"));
            titlecolumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            columndeficiency.setCellValueFactory(new PropertyValueFactory<>("deficiency"));
            columnreport.setCellValueFactory(new PropertyValueFactory<>("report"));
            columnduration.setCellValueFactory(new PropertyValueFactory<>("duration"));
            columnstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            columnapproach.setCellValueFactory(new PropertyValueFactory<>("approach"));



        }



    void updateAuidot() {
        String titleText = title.getText();
        String startdateTexte = sartdate.getText();
        String returndateText = returndate.getText();
        String deficiencyText = deficiency.getText();
        String reportText = report.getText();
        String durationText = duration.getText();
        String statusText = status.getText();
        String approachText = approach.getText();


        Audit audit = new Audit(  selectedAudit.getId(),titleText, startdateTexte, returndateText, deficiencyText, reportText, durationText, statusText, approachText);

        try {
            auditService.update(audit);
            loadAudits();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
