package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDBStore {
    private static final Logger LOG = Logger.getLogger(CandidateDBStore.class);
    private static final String FIND_ALL = "SELECT * FROM candidate ORDER BY id";
    private static final String UPDATE = "UPDATE candidate "
           + "SET name =?, description =?, photo = ?, city_id = ? WHERE id = ?";
    private static final String ADD_CANDIDATE = "INSERT INTO candidate(name, description, created, photo, city_id)"
            + "VALUES (?, ?, now(), ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM candidate WHERE id = ?";
    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(getCandidate(it));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return candidates;
    }

    public Candidate addCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_CANDIDATE, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setBytes(3, candidate.getPhoto());
            ps.setInt(4, candidate.getCity().getId());
            ps.execute();
            try (ResultSet it = ps.getGeneratedKeys()) {
                if (it.next()) {
                    candidate.setId(it.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return candidate;
    }

    public boolean update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setBytes(3, candidate.getPhoto());
            ps.setInt(4, candidate.getCity().getId());
            ps.setInt(5, candidate.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

    public Candidate findById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = getCandidate(it);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return candidate;
    }

    private Candidate getCandidate(ResultSet it) throws SQLException {
        return new Candidate(it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                new City(it.getInt("city_id")),
                it.getBytes("photo"),
                it.getTimestamp("created").toLocalDateTime());
    }
}
