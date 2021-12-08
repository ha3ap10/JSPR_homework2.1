package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PostRepository {

    private final Map<Long, Post> repository;

    public PostRepository() {
        this.repository = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(repository.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    public Post save(Post post) {
        repository.put(post.getId(), post);
        return post;
    }

    public void removeById(long id) {
        repository.remove(id);
    }
}
