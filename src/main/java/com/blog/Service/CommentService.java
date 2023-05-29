package com.blog.Service;

import com.blog.Payload.CommentDto;

import java.util.List;

public interface CommentService {

    void deleteComment(long postId, long commentId);

    public CommentDto saveComment(Long postId, CommentDto commentDto);

    List<CommentDto> getCommentByPostId(long postId);

    CommentDto getCommentById(long postId, long commentId);

    CommentDto updateComment(long postId, long commentId, CommentDto commentDto);
}
