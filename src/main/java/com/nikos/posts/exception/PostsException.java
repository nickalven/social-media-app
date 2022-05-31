package com.nikos.posts.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class of any SECUNDA exception
 * 
 * @author Centaur
 */
@Setter
@Getter
public class PostsException extends RuntimeException {
  private static final long serialVersionUID = -4605948820540110252L;
  private String domainName;
  
  
  public PostsException() {
    super();
  }

  public PostsException(String msg) {
    super(msg);
  }

  public PostsException(String msg, Throwable e) {
    super(msg, e);
  }
}
