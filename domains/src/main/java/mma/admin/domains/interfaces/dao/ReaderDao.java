package mma.admin.domains.interfaces.dao;

import java.util.List;

public interface ReaderDao<T> {

        T findById(String id);
        List<T> findAll();
}
