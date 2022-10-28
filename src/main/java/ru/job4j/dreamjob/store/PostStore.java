package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {

    private static final PostStore INST = new PostStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    private PostStore() {
        posts.put(1, new Post(id.incrementAndGet(), "Junior Java Job", "Description for Junior"));
        posts.put(2, new Post(id.incrementAndGet(), "Middle Java Job", "Description for Middle"));
        posts.put(3, new Post(id.incrementAndGet(), "Senior Java Job", " Description for Senior"));
    }

    public static PostStore instOf() {
        return INST;
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
}