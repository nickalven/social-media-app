package com.nikos.posts.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.nikos.posts.model.Post;
import com.nikos.posts.model.User;


@Repository
@CrossOrigin(origins = "http://localhost:4200")
public interface PostsRepository extends JpaRepository<Post, Long> {

	
	public Page<Post> findAll(Pageable pageable);
	
	public Page<Post> findByUser(User user, Pageable page);
	
	public Optional<Post> findById(Long id);
	
	Post save(Post post);
	
	void deleteById(Long id);
}
