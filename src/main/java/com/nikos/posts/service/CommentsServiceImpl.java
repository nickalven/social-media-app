package com.nikos.posts.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nikos.posts.dto.CommentDto;
import com.nikos.posts.dto.PostDto;
import com.nikos.posts.dto.converter.CommentConverter;
import com.nikos.posts.dto.converter.PostConverter;
import com.nikos.posts.exception.AuthorizationException;
import com.nikos.posts.exception.ResourceNotFoundException;
import com.nikos.posts.model.Comment;
import com.nikos.posts.model.Post;
import com.nikos.posts.model.User;
import com.nikos.posts.repository.CommentsRepository;
import com.nikos.posts.repository.PostsRepository;
import com.nikos.posts.repository.UserRepository;

@Service
public class CommentsServiceImpl implements CommentsService {

	@Autowired
	private PostsRepository postsRepository;
	
	@Autowired
	private CommentsRepository commentsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public Page<CommentDto> findAllComments(Pageable pageable) {
		return commentsRepository.findAll(pageable).map(CommentConverter::convertCommentToCommentDto);
	}

	@Override
	public Page<CommentDto> findAllCommentsSorted(Pageable pageable) {
		return commentsRepository.findAll(pageable).map(CommentConverter::convertCommentToCommentDto);
	}


	@Override
	public Optional<Comment> findCommentById(Long id) {
		return commentsRepository.findById(id);
	}

	@Override
	@Transactional
	public CommentDto saveComment(HttpServletRequest request, CommentDto commentDto) {
		Comment comment = new Comment();
		User user = userRepository.findByUsername(request.getUserPrincipal().getName()).get();
		comment.setUser(user);
		comment.setText(commentDto.getText());
		Post post = postsRepository.findById(commentDto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post with id " + commentDto.getPostId() + " not found"));
		comment.setPost(post);
		Comment savedComment = commentsRepository.saveAndFlush(comment);
		CommentDto result = CommentConverter.convertCommentToCommentDto(savedComment);
		return result;	
	}


	@Override
	@Transactional
	public CommentDto updateComment(HttpServletRequest request, Long id, CommentDto commentDto) {
		Comment comment = commentsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
		String userName = request.getUserPrincipal().getName();
		if (!comment.getUser().getUsername().equals(userName)) {
			throw new AuthorizationException("You can't edit this post");
		}
		comment.setId(id);
		User user = userRepository.findByUsername(userName).get();
		comment.setUser(user);
		comment.setText(commentDto.getText());
		Post post = postsRepository.findById(commentDto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post with id " + commentDto.getPostId() + " not found"));
		comment.setPost(post);
		Comment savedComment = commentsRepository.saveAndFlush(comment);
		CommentDto result = CommentConverter.convertCommentToCommentDto(savedComment);
		return result;
	}

	@Override
	@Transactional	
	public void deleteCommentById(HttpServletRequest request, Long id) {
		Comment comment = commentsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
		String username = request.getUserPrincipal().getName();
		if (!comment.getUser().getUsername().equals(username)) {
			throw new AuthorizationException("You cannot delete another's user comment");
		}
		commentsRepository.deleteById(id);
	}
	
}
