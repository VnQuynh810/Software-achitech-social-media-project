package socialMediaApp.services.impl;

import org.springframework.stereotype.Service;
import socialMediaApp.mappers.CommentMapper;
import socialMediaApp.models.Comment;
import socialMediaApp.repositories.CommentRepository;
import socialMediaApp.dto.requests.CommentAddRequest;
import socialMediaApp.dto.responses.comment.CommentGetResponse;
import socialMediaApp.services.ICommentService;

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
