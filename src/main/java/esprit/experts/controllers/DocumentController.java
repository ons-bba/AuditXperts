package esprit.experts.controllers;

import esprit.experts.entities.Document;
import esprit.experts.entities.User;
import esprit.experts.services.DocumentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField searchField;
    @FXML
    private TextField authorField;

    @FXML
    private ListView<Document> documentListView;
    @FXML
    private TableView<Document> documentTable;
    @FXML
    private TextArea contentArea;
    @FXML
    private Label attachmentLabel;

    // Additional labels for document details
    @FXML
    private Label idLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label attachmentStatusLabel;

    private File attachedFile;
    private User loggedInUser;

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    private final DocumentService documentService = new DocumentService();

    @FXML
    public void initialize() {
        configureDocumentTable();
        loadDocuments();
    }

    private void configureDocumentTable() {
        TableColumn<Document, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Document, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Document, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Document, String> attachmentColumn = new TableColumn<>("Attachment");
        attachmentColumn.setCellValueFactory(new PropertyValueFactory<>("attachmentPath"));

        TableColumn<Document, Timestamp> dateColumn = new TableColumn<>("Date of Insertion");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfInsertion"));


        documentTable.getColumns().setAll(idColumn, titleColumn, authorColumn, attachmentColumn,dateColumn );

        documentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titleField.setText(newSelection.getTitle());
                attachmentLabel.setText(newSelection.getAttachmentPath() != null ? "PDF Attached" : "No Attachment");
            }
        });
    }

    @FXML
    private void handleAddDocument() {
        if (loggedInUser == null) {
            System.out.println("Error: No logged-in user.");
            return;
        }

        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Title Required");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a title for the document.");
            alert.showAndWait();
            return;
        }

        if (attachedFile == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Attachment Required");
            alert.setHeaderText(null);
            alert.setContentText("Please attach a PDF document before adding.");
            alert.showAndWait();
            return;
        }

        title = titleField.getText();
        // Use the logged-in user's name as the author
        String author = loggedInUser.getFirstname() + " " + loggedInUser.getLastname();
        // Get current system timestamp
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        String attachmentPath = null;
        if (attachedFile != null) {
            attachmentPath = saveAttachment(attachedFile);
            if (attachmentPath == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Attachment Required");
                alert.setHeaderText(null);
                alert.setContentText("Please attach a PDF document before adding.");
                alert.showAndWait();
                // Handle error saving attachment
                System.out.println("Error saving attachment.");
                return;
            }
        }

        Document document = new Document(title, author, attachmentPath);
        document.setDateOfInsertion(timestamp);

        documentService.addDocument(document);

        // Reload documents table
        loadDocuments();

        // Clear input fields after adding document
        clearFields();
    }

    private String saveAttachment(File file) {
        // Example method to save the file to a folder and return its path
        // Modify this based on your actual file storage setup
        String storagePath = "C:/attachments/"; // Example path
        String fileName = file.getName();

        try {
            Files.copy(file.toPath(), Paths.get(storagePath + fileName), StandardCopyOption.REPLACE_EXISTING);
            return storagePath + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearFields() {
        titleField.clear();

    }

    private void loadDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        documentTable.getItems().setAll(documents);
    }


    @FXML
    private void handleUpdateDocument() {
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            String title = titleField.getText();
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

            selectedDocument.setTitle(title);
            selectedDocument.setDateOfInsertion(timestamp);

            documentService.updateDocument(selectedDocument);
            loadDocuments();
            clearFields();
        }
    }

    @FXML
    private void handleDeleteDocument() {
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            documentService.deleteDocument(selectedDocument.getId());
            loadDocuments(); // Refresh the table after deletion
        } else {
            // Handle case when no document is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Document Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a document to delete.");
            alert.showAndWait();
        }
    }


    @FXML
    private void handleSelectDocument() {
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            titleField.setText(selectedDocument.getTitle());
        } else {
            // Clear fields if no document is selected
            titleField.setText("");
        }
    }

    @FXML
    private void handleExportToPdf() {
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File saveFile = fileChooser.showSaveDialog(new Stage());

            if (saveFile != null) {
                try (PDDocument pdfDocument = new PDDocument()) {
                    PDPage page = new PDPage();
                    pdfDocument.addPage(page);

                    try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                        contentStream.setLeading(14.5f);
                        contentStream.newLineAtOffset(25, 700);

                        contentStream.showText("Document Details");
                        contentStream.newLine();
                        contentStream.newLine();

                        contentStream.setFont(PDType1Font.HELVETICA, 12);
                        contentStream.showText("ID: " + selectedDocument.getId());
                        contentStream.newLine();
                        contentStream.showText("Title: " + selectedDocument.getTitle());
                        contentStream.newLine();
                        contentStream.showText("Author: " + selectedDocument.getAuthor());
                        contentStream.newLine();
                        contentStream.showText("Date of Insertion: " + selectedDocument.getDateOfInsertion());
                        contentStream.newLine();
                        contentStream.showText("Attachment: " + (selectedDocument.getAttachmentPath() != null ? "Yes" : "No"));
                        contentStream.newLine();

                        contentStream.endText();
                    }

                    pdfDocument.save(saveFile);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Export to PDF");
                    alert.setHeaderText(null);
                    alert.setContentText("Document " + selectedDocument.getTitle() + " exported to PDF successfully.");
                    alert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Export to PDF");
                    alert.setHeaderText(null);
                    alert.setContentText("An error occurred while exporting the document to PDF.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Document Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a document to export.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSearchDocument() {
        String searchText = searchField.getText().toLowerCase();
        List<Document> documents = documentService.getAllDocuments();
        List<Document> filteredDocuments = documents.stream()
                .filter(doc -> String.valueOf(doc.getId()).toLowerCase().contains(searchText)
                        || doc.getTitle().toLowerCase().contains(searchText)
                        || doc.getAuthor().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        documentTable.getItems().setAll(filteredDocuments);
    }


    @FXML
    private void handleAttachFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Attach PDF Document");
        attachedFile = fileChooser.showOpenDialog(new Stage()); // Open file dialog
        if (attachedFile != null) {
            attachmentLabel.setText("PDF Attached: " + attachedFile.getName());
        }
    }

    @FXML
    private void handleDownloadAttachment() {
        Document selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null && selectedDocument.getAttachmentPath() != null) {
            try {
                File file = new File(selectedDocument.getAttachmentPath());
                if (file.exists()) {
                    // Open file using default system program
                    Desktop.getDesktop().open(file);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("File Not Found");
                    alert.setHeaderText(null);
                    alert.setContentText("Attachment file not found.");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Opening Attachment");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while opening the attachment.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Attachment Found");
            alert.setHeaderText(null);
            alert.setContentText("Please select a document with an attachment to download.");
            alert.showAndWait();
        }
    }
}
