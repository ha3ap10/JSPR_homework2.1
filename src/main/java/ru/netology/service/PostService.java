package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PostService {
    private final PostRepository repository;
    private final AtomicLong idValue = new AtomicLong();

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id).orElseThrow(NotFoundException::new);
    }

    public Post save(Post post) {
        final var id = post.getId();
        final var postOpt = repository.getById(id);
        if (id == 0 && postOpt.isEmpty()) {
            post.setId(idValue.incrementAndGet());
        }
        return repository.save(post);
    }

    public void removeById(long id) {
        final var postOpt = repository.getById(id);
        if (id != 0 && postOpt.isPresent()) {
            repository.removeById(id);
        }
    }
}

