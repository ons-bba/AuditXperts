package esprit.experts.services;

import esprit.experts.entities.Document;
import esprit.experts.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;

public class DocumentService {

    public ObservableList<Document> getAllDocuments() {
        ObservableList<Document> documentList = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM documents";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Document doc = new Document();
                doc.setId(rs.getInt("id"));
                doc.setTitle(rs.getString("title"));
                doc.setAuthor(rs.getString("author"));
                doc.setAttachmentPath(rs.getString("attachment"));
                doc.setDateOfInsertion(rs.getTimestamp("date_of_insertion"));
                // Retrieve attachment if needed
                documentList.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return documentList;
    }

    public void addDocument(Document document) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO documents (title, author, attachment, date_of_insertion) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, document.getTitle());
            pstmt.setString(2, document.getAuthor());
            pstmt.setString(3, document.getAttachmentPath());
            pstmt.setTimestamp(4, (document.getDateOfInsertion()));

            pstmt.executeUpdate();

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                document.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, generatedKeys);
        }
    }

    public void updateDocument(Document document) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String query = "UPDATE documents SET title = ?, author = ?, attachment = ? WHERE id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, document.getTitle());
            pstmt.setString(2, document.getAuthor());
            pstmt.setString(3, document.getAttachmentPath());
            pstmt.setInt(4, document.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    public void deleteDocument(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String query = "DELETE FROM documents WHERE id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
