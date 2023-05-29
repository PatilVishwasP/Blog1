package com.blog.Exception;

import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException {
    public BlogAPIException(String message) {
        super(message);
    }
}
