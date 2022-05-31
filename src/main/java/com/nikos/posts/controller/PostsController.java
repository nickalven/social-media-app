package com.nikos.posts.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikos.posts.dto.CommentDto;
import com.nikos.posts.dto.PostDto;
import com.nikos.posts.service.PostsService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/posts")
public class PostsController {
	
	private static final String DEFAULT_PAGE_NUMBER = "0";
	private static final String DEFAULT_PAGE_SIZE = "25";
	
	private final PostsService postsService;
	
	public PostsController(PostsService postsService) {
		this.postsService = postsService;
	}
	
	/*
	 * Get all posts with pagination
	 */
	@Cacheable(value = "pagedPostsUnsorted")
	@GetMapping
	public ResponseEntity<Page<PostDto>> findPosts(@RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
		log.info("Fetch posts from db");
		Page<PostDto> posts = postsService.findAllPosts(PageRequest.of(pageNumber, pageSize));
		return ResponseEntity.ok(posts);
	}
	
	/*
	 * Get all posts sorted by update Timestamp with pagination
	 */
	@GetMapping("/sorted")
	@Cacheable(value = "pagedPostsSorted")
	public ResponseEntity<Page<PostDto>> findPostsSorted(@RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
		log.info("Fetch sorted posts from db");
		Page<PostDto> posts = postsService.findAllPostsSorted(PageRequest.of(pageNumber, pageSize, Sort.by("creationTime").descending()));
		return ResponseEntity.ok(posts);
	}
	
	
	/*
	 * Get comments by post id with pagination
	 */
	@GetMapping("/{id}/comments")
	@Cacheable(value = "pagedCommentsByPost")
	public ResponseEntity<Page<CommentDto>> findPostComments(@PathVariable Long id, @RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
		log.info("Fetch comments by post from db");
		Page<CommentDto> comments = postsService.findCommentsByPostId(id, PageRequest.of(pageNumber, pageSize, Sort.by("creationTime").descending()));
		return ResponseEntity.ok(comments);
	}
	

	/*
	 * Get a user posts by its email with pagination
	 */
	@Cacheable(value = "pagedPostsByUsername")
	@GetMapping("/users/{username}")
	public ResponseEntity<Page<PostDto>> findUserPosts(@PathVariable String username, @RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
		log.info("Fetch posts by user from db");
		Page<PostDto> posts = postsService.findByUserName(username, PageRequest.of(pageNumber, pageSize, Sort.by("creationTime").descending()));
		return ResponseEntity.ok(posts);
	}
	

	/*
	 * Get my posts with pagination
	 */
	@Cacheable(value = "pagedPostsMine")
	@GetMapping("/mine")
	public ResponseEntity<Page<PostDto>> findMyPosts(@RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest request) throws Exception {
		log.info("Fetch logged in users posts");
		String username = request.getUserPrincipal().getName();
		Page<PostDto> posts = postsService.findByUserName(username, PageRequest.of(pageNumber, pageSize, Sort.by("creationTime")));
		return ResponseEntity.ok(posts);
	}
	
	
	/*
	 * Save a new Post
	 */
	@Caching(
		      evict = {
		            @CacheEvict(value = "pagedPostsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsByUsername", allEntries = true), 
		            @CacheEvict(value = "pagedPostsMine", allEntries = true)
		      }
			)
	@PostMapping
	@Transactional
	public ResponseEntity<PostDto> savePost(HttpServletRequest request, @RequestBody @Valid PostDto postDto) throws Exception {
		PostDto savedPost = postsService.savePost(request, postDto);
		
		return ResponseEntity.ok(savedPost); 
		
	}
	
	
	/*
	 * Update a post
	 */
	@Caching(
		      evict = {
		            @CacheEvict(value = "pagedPostsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsByUsername", allEntries = true), 
		            @CacheEvict(value = "pagedPostsMine", allEntries = true)
		      }
			)
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(HttpServletRequest request, @PathVariable Long id, @RequestBody @Valid PostDto postDto) throws Exception {
		PostDto savedPost = postsService.updatePost(request, id, postDto);
		return ResponseEntity.ok(savedPost);
	}

	
	/*
	 * Delete a post
	 */
	@Caching(
		      evict = {
		            @CacheEvict(value = "pagedPostsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsByUsername", allEntries = true), 
		            @CacheEvict(value = "pagedPostsMine", allEntries = true),
		            @CacheEvict(value = "pagedCommentsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedCommentsSorted", allEntries = true)
		      }
			)
	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(HttpServletRequest request, @PathVariable Long id) throws Exception {
		postsService.deleteById(request, id); 
		return ResponseEntity.noContent().build();
		
	}
	
}
