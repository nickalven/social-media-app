package com.nikos.posts.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import com.nikos.posts.model.Post;
import com.nikos.posts.model.User;
import com.nikos.posts.repository.CommentsRepository;
import com.nikos.posts.repository.PostsRepository;
import com.nikos.posts.repository.UserRepository;

@Service
public class PostsServiceImpl implements PostsService {

	private final PostsRepository postsRepository;

	private final CommentsRepository commentsRepository;

	private final UserRepository userRepository;

	public PostsServiceImpl(PostsRepository postsRepository, CommentsRepository commentsRepository,
			UserRepository userRepository) {
		this.postsRepository = postsRepository;
		this.commentsRepository = commentsRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Page<PostDto> findAllPosts(Pageable pageable) {
		return postsRepository.findAll(pageable).map(PostConverter::convertPostToPOstDto);
	}

	@Override
	public Page<PostDto> findAllPostsSorted(Pageable pageable) {
		return postsRepository.findAll(pageable).map(PostConverter::convertPostToPOstDto);
	}

	@Override
	public Page<PostDto> findByUserName(String username, Pageable page) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Username " + username + " not found"));
		Page<PostDto> posts = postsRepository.findByUser(user, page).map(PostConverter::convertPostToPOstDto);
		return posts;
	}

	@Override
	public Optional<Post> findById(Long id) {
		return postsRepository.findById(id);
	}

	@Override
	@Transactional
	public PostDto savePost(HttpServletRequest request, PostDto postDto) {
		Post post = new Post();
		User user = userRepository.findByUsername(request.getUserPrincipal().getName()).get();
		post.setUser(user);
		post.setText(postDto.getText());
		Post savedPost = postsRepository.saveAndFlush(post);
		PostDto result = PostConverter.convertPostToPOstDto(savedPost);
		return result;
	}

	@Override
	@Transactional
	public void deleteById(HttpServletRequest request, Long id) {
		Post post = postsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
		if (!post.getUser().getUsername().equals(request.getUserPrincipal().getName())) {
			throw new AuthorizationException("You cannot delete another user's post");
		}
		postsRepository.deleteById(id);

	}

	@Override
	public Page<CommentDto> findCommentsByPostId(Long id, Pageable page) {
		System.out.println("Fetch comment by post id from db");
		Post post = postsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
		return commentsRepository.findByPost(post, page).map(CommentConverter::convertCommentToCommentDto);
	}

	@Override
	@Transactional
	public PostDto updatePost(HttpServletRequest request, Long id, PostDto postDto) throws Exception {
		Post originalpost = postsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
		String username = request.getUserPrincipal().getName();
		if (!originalpost.getUser().getUsername().equals(username)) {
			throw new AuthorizationException("You can't edit this post");
		}
		Post post = new Post();
		User user = userRepository.findByUsername(username).get();
		post.setId(id);
		post.setUser(user);
		post.setText(postDto.getText());
		Post savedPost = postsRepository.saveAndFlush(post);
		PostDto result = PostConverter.convertPostToPOstDto(savedPost);
		return result;

	}

}
