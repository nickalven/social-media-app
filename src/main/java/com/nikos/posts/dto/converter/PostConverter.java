package com.nikos.posts.dto.converter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.nikos.posts.dto.CommentDto;
import com.nikos.posts.dto.PostDto;
import com.nikos.posts.model.Post;

public class PostConverter {
	
	public static PostDto convertPostToPOstDto(Post post) {
		PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		postDto.setUsername(post.getUser().getUsername());
		postDto.setText(post.getText());
		postDto.setCreationTime(post.getCreationTime());
		List<CommentDto> comments = post.getComments().stream().map(comment -> CommentConverter.convertCommentToCommentDto(comment)).sorted(Comparator.comparing(CommentDto::getCreationTime).reversed()).collect(Collectors.toList());
		postDto.setComments(comments);
		return postDto;
	}

}
