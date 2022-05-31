package com.nikos.posts.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nikos.posts.dto.CommentDto;
import com.nikos.posts.dto.PostDto;
import com.nikos.posts.model.Post;

public interface PostsService {
	
	public Page<PostDto> findAllPosts(Pageable pageable);

	public Page<PostDto> findAllPostsSorted(Pageable pageable);
	
	public Page<PostDto> findByUserName(String username, Pageable page);
	
	public Page<CommentDto> findCommentsByPostId(Long id, Pageable page);
	
	public Optional<Post> findById(Long id);
	
	PostDto savePost(HttpServletRequest request, PostDto postDto);
	
	PostDto updatePost(HttpServletRequest request, Long id, PostDto postDto) throws Exception;
	
	void deleteById(HttpServletRequest request, Long id);
	 
}
