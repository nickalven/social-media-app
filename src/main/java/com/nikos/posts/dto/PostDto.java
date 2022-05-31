package com.nikos.posts.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

	private Long id;
	
	private String text;
	
	@Email
	private String username; 
	
	private LocalDateTime creationTime;

	private List<CommentDto> comments;	
}
