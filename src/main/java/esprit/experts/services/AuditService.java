package esprit.experts.services;

import esprit.experts.entities.Audit;
import esprit.experts.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuditService implements IService<Audit> {
    Connection db = DatabaseConnection.getConnection();
    public void add(Audit audit) throws SQLException {
        String query = "INSERT INTO audit (Title, Startdate, Returndate, Deficiency, Report, Duration, Status, Approach) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = db.prepareStatement(query);
        preparedStatement.setString(1, audit.getTitle());
        preparedStatement.setString(2, audit.getStartdate());
        preparedStatement.setString(3, audit.getReturndate());
        preparedStatement.setString(4, audit.getDeficiency());
        preparedStatement.setString(5, audit.getReport());
        preparedStatement.setString(6, audit.getDuration());
        preparedStatement.setString(7, audit.getStatus());
        preparedStatement.setString(8, audit.getApproach());

        preparedStatement.executeUpdate();
    }

    public void update(Audit audit) throws SQLException {
        String query = "UPDATE audit SET Title = ?, Startdate = ?, Returndate = ?, Deficiency = ?, Report = ?, Duration = ?, Status = ?, Approach = ? WHERE id = ?";
        PreparedStatement preparedStatement = db.prepareStatement(query);
        preparedStatement.setString(1, audit.getTitle());
        preparedStatement.setString(2, audit.getStartdate());
        preparedStatement.setString(3, audit.getReturndate());
        preparedStatement.setString(4, audit.getDeficiency());
        preparedStatement.setString(5, audit.getReport());
        preparedStatement.setString(6, audit.getDuration());
        preparedStatement.setString(7, audit.getStatus());
        preparedStatement.setString(8, audit.getApproach());
        preparedStatement.setInt(9, audit.getId());


        preparedStatement.executeUpdate();
    }

    public void delete(Audit audit) throws SQLException {
        String query = "DELETE FROM audit WHERE id = ?";
        PreparedStatement preparedStatement = db.prepareStatement(query);
        preparedStatement.setInt(1, audit.getId());

        preparedStatement.executeUpdate();
    }

    public Audit findById(int id) throws SQLException {
        return null;
    }

    public List<Audit> findAll() throws SQLException {
        ObservableList<Audit> audits = null; 
            try{
                String query = "SELECT * FROM audit";
                PreparedStatement preparedStatement = db.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                 audits = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    audits.add(new Audit(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("startdate"),
                            resultSet.getString("returndate"),
                            resultSet.getString("deficiency"),
                            resultSet.getString("report"),
                            resultSet.getString("duration"),
                            resultSet.getString("status"),
                            resultSet.getString("approach")

                    ));
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        return audits;    }

    @Override
    public void Create(Audit o) {

    }

    @Override
    public void Update(Audit o) {

    }

    @Override
    public List<Audit> read() {
        return List.of();
    }

    @Override
    public void Delete(Audit o) {

    }

    @Override
    public Audit getById(long id) {
        return null;
    }
}
