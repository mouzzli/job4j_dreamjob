package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class PostStore {
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(3);

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Description for Junior", new City(1, "Москва")));
        posts.put(2, new Post(2, "Middle Java Job", "Description for Middle", new City(1, "Москва")));
        posts.put(3, new Post(3, "Senior Java Job", " Description for Senior", new City(2, "СПб")));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void addPost(Post post) {
        int postId = id.incrementAndGet();
        post.setId(postId);
        post.setCreated(LocalDateTime.now());
        posts.putIfAbsent(postId, post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        posts.replace(post.getId(), post);
    }
}