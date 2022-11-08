package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@ThreadSafe
@Repository
public class UserDBStore {
    private static final Logger LOG = Logger.getLogger(UserDBStore.class);

    private final BasicDataSource pool;
    private static final String ADD_USER = "INSERT INTO users(name) VALUES (?)";

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public User addUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_USER, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.execute();
            try (ResultSet it = ps.getGeneratedKeys()) {
                if (it.next()) {
                    user.setId(it.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return user;
    }
}
