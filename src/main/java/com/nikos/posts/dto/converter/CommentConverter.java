package com.nikos.posts.dto.converter;


import com.nikos.posts.dto.CommentDto;
import com.nikos.posts.dto.PostDto;
import com.nikos.posts.model.Comment;

public class CommentConverter {
	public static CommentDto convertCommentToCommentDto(Comment comment) {
		CommentDto commentDto = new CommentDto();
		commentDto.setId(comment.getId());
		commentDto.setUsername(comment.getUser().getUsername());
		commentDto.setText(comment.getText());
		commentDto.setCreationTime(comment.getCreationTime());
		commentDto.setPostId(comment.getPost().getId());
		return commentDto;
	}

}
