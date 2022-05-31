package com.nikos.posts.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
import com.nikos.posts.service.CommentsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comments")
public class CommentsController {
	
	private static final String DEFAULT_PAGE_NUMBER = "0";
	private static final String DEFAULT_PAGE_SIZE = "25";
	
	private final CommentsService commentsService;
	
	public CommentsController(CommentsService commentsService) {
		this.commentsService = commentsService;
	}

	/*
	 * Get all Comments with pagination
	 */
	@Cacheable(value = "pagedCommentsUnsorted")
	@GetMapping
	public ResponseEntity<Page<CommentDto>> findComments(@RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
	   
		log.info("Fetch comments from db");
		Page<CommentDto> comments = commentsService.findAllComments(PageRequest.of(pageNumber, pageSize));
		return ResponseEntity.ok(comments);
	}
	
	
	/*
	 * Get all comments sorted by time updated descending with pagination
	 */
	@Cacheable(value = "pagedCommentsSorted", key = "'sorted'.concat(#pageable.pageNumber)")
	@GetMapping("/sorted")
	public ResponseEntity<Page<CommentDto>> findCommentsSorted(@RequestParam(value = "pageNumber", defaultValue = DEFAULT_PAGE_NUMBER) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) {
		log.info("Fetch sorted comments from db");
		Page<CommentDto> comments = commentsService.findAllCommentsSorted(PageRequest.of(pageNumber, pageSize, Sort.by("text").descending()));
		return ResponseEntity.ok(comments);
	}
	

	/*
	 *  Save a new comment in the database
	 */
	@Caching(
		      evict = {
		            @CacheEvict(value = "pagedCommentsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedCommentsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsByUsername", allEntries = true), 
		            @CacheEvict(value = "pagedPostsMine", allEntries = true),
		            @CacheEvict(value = "pagedCommentsByPost", allEntries = true)
		      }
			)
	@Transactional
	@PostMapping
	public ResponseEntity<CommentDto> saveComment(@RequestBody @Valid CommentDto commentDto, HttpServletRequest request) {

		CommentDto savedComment = commentsService.saveComment(request, commentDto);
		return ResponseEntity.ok(savedComment); 
		
	}
	
	/*
	 * Update a comment
	 */

	@Caching(
		      evict = {
		            @CacheEvict(value = "pagedCommentsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedCommentsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsByUsername", allEntries = true), 
		            @CacheEvict(value = "pagedPostsMine", allEntries = true),
		            @CacheEvict(value = "pagedCommentsByPost", allEntries = true),
		      }
			)
	@Transactional
	@PutMapping("/{id}")
	public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody @Valid CommentDto commentDto, HttpServletRequest request) {
				
		CommentDto savedCommentDto = commentsService.updateComment(request, id, commentDto);
		return ResponseEntity.ok(savedCommentDto);
	}
	
	
	/*
	 * Delete a comment
	 */
	@Caching(
		      evict = {
		            @CacheEvict(value = "pagedCommentsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedCommentsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsUnsorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsSorted", allEntries = true),
		            @CacheEvict(value = "pagedPostsByUsername", allEntries = true), 
		            @CacheEvict(value = "pagedPostsMine", allEntries = true),
		            @CacheEvict(value = "pagedCommentsByPost", allEntries = true)
		      }
			)
	@Transactional
	@DeleteMapping("/{id}")
	public void deleteComment(HttpServletRequest request, @PathVariable Long id) {

		commentsService.deleteCommentById(request, id);
		
	}

}
