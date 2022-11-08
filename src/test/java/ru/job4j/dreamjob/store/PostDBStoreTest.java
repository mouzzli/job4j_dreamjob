package ru.job4j.dreamjob.store;


import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostDBStoreTest {
    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", "Description", new City(1, "Moscow"), LocalDateTime.now(), true);
        store.addPost(post);
        Post postInDb = store.findById(post.getId()).get();
        assertThat(postInDb.getName(), is(post.getName()));
        assertThat(postInDb.getDescription(), is(post.getDescription()));
        assertThat(postInDb.getCity().getId(), is(post.getCity().getId()));
        assertThat(postInDb.isVisible(), is(post.isVisible()));
    }

    @Test
    public void whenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", "Description", new City(1, "Moscow"), LocalDateTime.now(), true);
        store.addPost(post);
        post.setName("New Name");
        store.update(post);
        Post postInDb = store.findById(post.getId()).get();
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenFindAllPost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post1 = new Post(0, "Java Job1", "Description1", new City(1, "Moscow"), LocalDateTime.now(), true);
        Post post2 = new Post(1, "Java Job2", "Description2", new City(1, "Moscow"), LocalDateTime.now(), true);
        Post post3 = new Post(2, "Java Job3", "Description3", new City(1, "Moscow"), LocalDateTime.now(), true);
        List<Post> postsBefore = store.findAll();
        store.addPost(post1);
        store.addPost(post2);
        store.addPost(post3);
        List<Post> posts = store.findAll();
        assertThat(posts.size() - postsBefore.size(), is(3));
    }
}