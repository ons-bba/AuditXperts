package esprit.experts.controllers;

import esprit.experts.entities.Document;
import esprit.experts.services.DocumentService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DocumentController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private ListView<Document> documentListView;
    @FXML
    private TextArea contentArea;

    private Document selectedDocument;

    private DocumentService documentService = new DocumentService();
    private ObservableList<Document> documentList;

    @FXML
    public void initialize() {
        loadDocuments();
        documentListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleSelectDocument();
            }
        });
    }

    private void loadDocuments() {
        documentList = documentService.getAllDocuments();
        documentListView.setItems(documentList);
    }

    @FXML
    private void handleAddDocument() {
        String title = titleField.getText();
        String author = authorField.getText();
        String content = contentArea.getText();
        Document document = new Document(0, title, author, LocalDate.now(), content);

        documentService.addDocument(document);
        documentList.add(document);
        clearFields();
    }

    @FXML
    private void handleUpdateDocument() {
        if (selectedDocument != null) {
            selectedDocument.setTitle(titleField.getText());
            selectedDocument.setAuthor(authorField.getText());
            selectedDocument.setContent(contentArea.getText());

            documentService.updateDocument(selectedDocument);
            documentListView.refresh();
            clearFields();
        }
    }

    @FXML
    private void handleDeleteDocument() {
        if (selectedDocument != null) {
            documentService.deleteDocument(selectedDocument.getId());
            documentList.remove(selectedDocument);
            clearFields();
        }
    }

    @FXML
    private void handleSelectDocument() {
        selectedDocument = documentListView.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            titleField.setText(selectedDocument.getTitle());
            authorField.setText(selectedDocument.getAuthor());
            contentArea.setText(selectedDocument.getContent());
        }
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        contentArea.clear();
        selectedDocument = null;
    }
}
