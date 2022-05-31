package com.nikos.posts.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

	private Long id;
	private String text;
	@NotNull
	private Long postId;
	
	@Email
	private String username;
	private LocalDateTime creationTime;
}
