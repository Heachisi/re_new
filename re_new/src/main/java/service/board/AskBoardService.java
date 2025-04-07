package service.board;

import java.util.List;

import javax.xml.stream.events.Comment;

import jakarta.servlet.http.HttpServletRequest;
import model.board.AskBoard;


public interface AskBoardService {
    
    public List<AskBoard> getBoardList(AskBoard board);

    public AskBoard getBoardById(String boardId);
    
    public boolean createBoard(AskBoard board, HttpServletRequest request);
    
    public boolean updateBoard(AskBoard board, HttpServletRequest request);
    
    public boolean deleteBoard(AskBoard board);
    
    public boolean createComment(Comment comment);
    
    public boolean updateComment(Comment comment);
    
    public boolean deleteComment(Comment comment);
    
    public void increaseViewCount(String boardId);
}