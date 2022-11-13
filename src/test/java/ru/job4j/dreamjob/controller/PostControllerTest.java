package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PostControllerTest {

    private PostService postService;
    private CityService cityService;
    private PostController postController;

    @BeforeEach
    public void initPostController() {
        postService = mock(PostService.class);
        cityService = mock(CityService.class);
        postController = new PostController(
                postService,
                cityService
        );
    }

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post", "New Description", new City(1), LocalDateTime.now(), true),
                new Post(2, "New post", "New Description", new City(1), LocalDateTime.now(), true)
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        when(postService.findAll()).thenReturn(posts);
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("posts"));
    }

    @Test
    public void whenFormAddPost() {
        List<City> cities = Arrays.asList(new City(1), new City(2));
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        when(cityService.getAllCities()).thenReturn(cities);
        String page = postController.formAddPost(model, session);
        verify(model).addAttribute("cities", cities);
        assertThat(page, is("addPost"));
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "New post", "New Description", new City(2, "Москва"), LocalDateTime.now(), true);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("city.id")).thenReturn("2");
        String page = postController.createPost(input, request);
        verify(cityService).findById(2);
        verify(postService).addPost(input);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenFormUpdatePost() {
        List<City> cities = Arrays.asList(new City(1), new City(2));
        Post post = new Post(1, "New post", "New Description", new City(1), LocalDateTime.now(), true);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        when(cityService.getAllCities()).thenReturn(cities);
        when(postService.findById(1)).thenReturn(Optional.of(post));
        String page = postController.formUpdatePost(model, session, 1);
        verify(model).addAttribute("cities", cities);
        verify(model).addAttribute("post", Optional.of(post));
        assertThat(page, is("updatePost"));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(1, "New post", "New Description", new City(2), LocalDateTime.now(), true);
        String page = postController.updatePost(post);
        verify(cityService).findById(2);
        verify(postService).update(post);
        assertThat(page, is("redirect:/posts"));

    }
}