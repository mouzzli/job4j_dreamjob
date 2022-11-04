package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostDBStore {
    private static final Logger LOG = Logger.getLogger(PostDBStore.class);
    private static final String FIND_ALL = "SELECT * FROM post ORDER BY id";
    private static final String ADD_POST = "INSERT INTO post(name, description, created, visible, city_id) VALUES (?, ?, now(), ?, ?)";
    private static final String UPDATE = "UPDATE post SET name = ?, description = ?, visible = ?, city_id = ? WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM post WHERE id = ?";
    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(getPost(it));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return posts;
    }

    public Post addPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_POST,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setBoolean(3, post.isVisible());
            ps.setInt(4, post.getCity().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return post;
    }

    public boolean update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setBoolean(3, post.isVisible());
            ps.setInt(4, post.getCity().getId());
            ps.setInt(5, post.getId());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

    public Optional<Post> findById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    post = getPost(it);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return Optional.ofNullable(post);
    }

    private Post getPost(ResultSet it) throws SQLException {
        return new Post(it.getInt("id"),
                it.getString("name"),
                it.getString("description"),
                new City(it.getInt("city_id")),
                it.getTimestamp("created").toLocalDateTime(),
                it.getBoolean("visible"));
    }
}
