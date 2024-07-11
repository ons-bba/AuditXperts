package esprit.experts.services;

import java.sql.SQLException;
import java.util.List;

public interface IService<O> {
    public void Create(O o) throws SQLException;
    public void Update( O o) throws SQLException;
    public List<O> read() throws SQLException;
    public void Delete(O o);
    public O getById(long id) throws SQLException;



}