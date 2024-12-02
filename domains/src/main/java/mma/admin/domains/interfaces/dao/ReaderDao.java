package mma.admin.domains.interfaces.dao;

import java.util.List;

public interface ReaderDao<T> {

        List<T> findAll();
}
