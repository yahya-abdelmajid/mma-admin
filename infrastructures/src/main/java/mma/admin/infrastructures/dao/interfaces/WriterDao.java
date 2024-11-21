package mma.admin.infrastructures.dao.interfaces;

public interface WriterDao<T> {
    void insert(T object);
    void update(T object);
}
