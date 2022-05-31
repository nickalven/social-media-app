package com.nikos.posts.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nikos.posts.dto.CommentDto;
import com.nikos.posts.model.Comment;

public interface CommentsService {

	public Page<CommentDto> findAllComments(Pageable pageable);

	public Page<CommentDto> findAllCommentsSorted(Pageable pageable);
	
//	public Page<CommentDto> findCommentsByUserId(Long id, Pageable page);
	
	public Optional<Comment> findCommentById(Long id);
	
//	public Page<Comment> findCommentsByPostId(Long id, Pageable page);
	
	CommentDto saveComment(HttpServletRequest request, CommentDto comment);

	CommentDto updateComment(HttpServletRequest request, Long id, CommentDto commentDto);
	
	void deleteCommentById(HttpServletRequest request, Long id);

}
