package esprit.experts.services;

import java.util.List;

public interface IService<O> {
    public void Create(O o);
    public void Update( O o);
    public List<O> Read();
    public void Delete(O o);
    public O getById(long id) ;



}