package mma.admin.infrastructures.dao.interfaces;

import java.util.List;

public interface ReaderDao<T> {

        T findById(String id);
        List<T> findAll();
}
