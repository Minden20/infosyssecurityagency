package repository;

import java.io.IOException;
import java.util.List;

public interface CrudRepository<T, ID> {
    
    boolean create(T entity) throws IOException;
    
    List<T> findAll() throws IOException;
    
    boolean update(T entity) throws IOException;
    
    boolean delete(ID id) throws IOException;
}
