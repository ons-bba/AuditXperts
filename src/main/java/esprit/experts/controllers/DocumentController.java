package esprit.experts.controllers;


import esprit.experts.entities.Document;
import esprit.experts.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.sql.*;
import java.time.LocalDate;

public class DocumentController
{
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private ListView<Document> documentListView;
    @FXML private TextArea contentArea;

    private Document selectedDocument;

    private ObservableList<Document> documentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadDocuments();
    }

    private void loadDocuments() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM documents")) {

            while (rs.next()) {
                Document doc = new Document(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("content")
                );
                documentList.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        documentListView.setItems(documentList);
    }

    @FXML
    private void handleAddDocument() {
        String title = titleField.getText();
        String author = authorField.getText();
        String content = contentArea.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO documents (title, author, date, content) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setString(4, content);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    Document document = new Document(id, title, author, LocalDate.now(), content);
                    documentList.add(document);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clearFields();
    }

    @FXML
    private void handleUpdateDocument() {
        if (selectedDocument != null) {
            selectedDocument.setTitle(titleField.getText());
            selectedDocument.setAuthor(authorField.getText());
            selectedDocument.setContent(contentArea.getText());

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "UPDATE documents SET title = ?, author = ?, content = ? WHERE id = ?")) {

                pstmt.setString(1, selectedDocument.getTitle());
                pstmt.setString(2, selectedDocument.getAuthor());
                pstmt.setString(3, selectedDocument.getContent());
                pstmt.setInt(4, selectedDocument.getId());
                pstmt.executeUpdate();

                documentListView.refresh();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteDocument() {
        if (selectedDocument != null) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM documents WHERE id = ?")) {

                pstmt.setInt(1, selectedDocument.getId());
                pstmt.executeUpdate();

                documentList.remove(selectedDocument);
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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





