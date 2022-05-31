package com.nikos.posts.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.nikos.posts.model.Comment;
import com.nikos.posts.model.Post;

@Repository
@CrossOrigin(origins = "http://localhost:4200")
public interface CommentsRepository extends JpaRepository<Comment, Long> {

	public Page<Comment> findAll(Pageable pageable);

	public Optional<Comment> findById(Long id);

	public Page<Comment> findByPost(Post post, Pageable page);

	Comment save(Comment comment);

	void deleteById(Long id);
}