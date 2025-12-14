package com.example.commentservice.services.impl;

import com.example.commentservice.dto.requests.CommentAddRequest;
import com.example.commentservice.dto.responses.CommentGetResponse;
import com.example.commentservice.mappers.CommentMapper;
import com.example.commentservice.models.Comment;
import com.example.commentservice.repositories.CommentRepository;
import com.example.commentservice.services.ICommentService;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }
    @Override
    public void add(CommentAddRequest commentAddRequest) {
        Comment comment = commentMapper.addRequestToComment(commentAddRequest);
        commentRepository.save(comment);
    }
    @Override
    public List<CommentGetResponse> getAll() {
        List<Comment> comments = commentRepository.findAll();
        return commentMapper.commentsToResponses(comments);
    }

    @Override
    public List<CommentGetResponse> getAllByPost(int postId) {
        List<Comment> comments = commentRepository.findAllByPost_Id(postId);
        return commentMapper.commentsToResponses(comments);
    }
    @Override
    public List<CommentGetResponse> getAllByUser(int userId) {
        List<Comment> comments = commentRepository.findAllByUser_Id(userId);
        return commentMapper.commentsToResponses(comments);
    }

    @Override
    public void delete(int id) {
        commentRepository.deleteById(id);
    }

}
