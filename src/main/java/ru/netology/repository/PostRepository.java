package ru.netology.repository;

import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {

    private ConcurrentHashMap<Long, Post> repository = new ConcurrentHashMap<>();

    private AtomicLong idCounter = new AtomicLong(1);

    public List<Post> all() {
        return repository
                .values()
                .stream()
                .toList();
    }

    public Optional<Post> getById(long id) {
        Post post = repository.get(id);
        return Optional.ofNullable(post);
    }

    public Optional<Post> save(Post post) {
        long id = post.getId();

        if (id == 0) {
            long newId = idCounter.getAndIncrement();
            post.setId(newId);
            repository.put(newId, post);

            return Optional.of(repository.get(newId));
        } else {

          if (repository.containsKey(id)) {
            repository.put(id, post);
            return Optional.of(repository.get(id));
          } else {
            return Optional.empty();
          }
        }
    }

    public void removeById(long id) {
      repository.remove(id);
    }
}
