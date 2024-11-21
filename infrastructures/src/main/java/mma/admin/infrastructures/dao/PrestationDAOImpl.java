package mma.admin.infrastructures.dao;

import mma.admin.domains.Prestation;
import mma.admin.infrastructures.dao.interfaces.ReaderDao;

import java.sql.Connection;
import java.util.List;

public class PrestationDAOImpl implements ReaderDao<Prestation> {
    private Connection connection;
    public PrestationDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Prestation findById(String id) {
        return null;
    }

    @Override
    public List<Prestation> findAll() {
        return List.of();
    }
}
