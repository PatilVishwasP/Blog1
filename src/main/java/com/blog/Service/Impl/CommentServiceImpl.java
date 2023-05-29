package com.blog.Service.Impl;


import com.blog.Entities.Comments;
import com.blog.Entities.Post;
import com.blog.Exception.BlogAPIException;
import com.blog.Exception.ResourceNotFoundException;
import com.blog.Payload.CommentDto;
import com.blog.Repositories.CommentRepository;
import com.blog.Repositories.PostRepository;
import com.blog.Service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;

    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    private CommentRepository commentRepository;

    private ModelMapper mapper;

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id: " + postId)
        );

        Comments comments = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment not found with id: " + commentId)
        );

        if (!Objects.equals(comments.getPost().getId(), post.getId())){
            throw new BlogAPIException("Comment Does not belong to the post");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto saveComment(Long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id: " + postId)
        );
        Comments comments = mapToEntity(commentDto);
        comments.setPost(post);
        Comments newComment = commentRepository.save(comments);
        return mapToDto(newComment);

    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        List<Comments> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id: " + postId)
        );
        Comments comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment not found with id: " + commentId)
        );

        if(!Objects.equals(comment.getPost().getId(), post.getId())){
            throw new BlogAPIException( "Comment does not belong to post");
        }


        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post is not found with id: " + postId)
        );

        Comments comments = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment is not with CommentId: " + commentId)
        );

        if(!Objects.equals(comments.getPost().getId(), post.getId())){
           throw new BlogAPIException("Comment does not belong to the post");
        }

        comments.setName(commentDto.getName());
        comments.setEmail(commentDto.getEmail());
        comments.setBody(commentDto.getBody());

        Comments save = commentRepository.save(comments);
        CommentDto dto = mapToDto(save);

        return dto;
    }

    Comments mapToEntity(CommentDto commentDto){
        Comments comments = mapper.map(commentDto, Comments.class);
//        Comments comments = new Comments();
//        comments.setBody(commentDto.getBody());
//        comments.setEmail(commentDto.getEmail());
//        comments.setName(commentDto.getName());
        return comments;
    }

    CommentDto mapToDto(Comments comments){
        CommentDto dto = mapper.map(comments, CommentDto.class);

//        CommentDto dto = new CommentDto();
//        dto.setId(comments.getId());
//        dto.setBody(comments.getBody());
//        dto.setEmail(comments.getEmail());
//        dto.setName(comments.getName());
        return dto;
    }

}
