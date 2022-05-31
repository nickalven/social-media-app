package com.nikos.posts.payload.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

	@NotBlank
	@Size(max = 50)
	@Email
	private String username;

	private Set<String> role;

	@NotBlank
	@Size(min = 6, max = 120)
	private String password;
}
