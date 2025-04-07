package service.board;

import java.util.List;

import javax.xml.stream.events.Comment;

import jakarta.servlet.http.HttpServletRequest;
import model.board.AskBoard;
import model.board.AskComment;


public interface AskBoardService {
    
    public List<AskBoard> getBoardList(AskBoard board);

    public AskBoard getBoardById(String boardId);
    
    public boolean createBoard(AskBoard board, HttpServletRequest request);
    
    public boolean updateBoard(AskBoard board, HttpServletRequest request);
    
    public boolean deleteBoard(AskBoard board);
    
    public boolean createComment(AskComment askComment);
    
    public boolean updateComment(AskComment askComment);
    
    public boolean deleteComment(AskComment askComment);
    
    public void increaseViewCount(String boardId);
}