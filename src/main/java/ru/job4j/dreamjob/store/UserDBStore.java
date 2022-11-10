package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@ThreadSafe
@Repository
public class UserDBStore {
    private static final Logger LOG = Logger.getLogger(UserDBStore.class);

    private final BasicDataSource pool;
    private static final String ADD_USER = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";
    private static final String FIND_USER_BY_EMAIL_AND_PASSWORD = "SELECT * FROM users WHERE email = ? AND password =?";

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> addUser(User user) {
        Optional<User> userOptional = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_USER, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet it = ps.getGeneratedKeys()) {
                if (it.next()) {
                    user.setId(it.getInt(1));
                }
                userOptional = Optional.of(user);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return userOptional;
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_USER_BY_EMAIL_AND_PASSWORD)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    user = getUser(it);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return Optional.ofNullable(user);
    }

    private User getUser(ResultSet it) throws SQLException {
        return new User(it.getInt("id"),
                it.getString("name"),
                it.getString("email"),
                it.getString("password"));
    }
}
