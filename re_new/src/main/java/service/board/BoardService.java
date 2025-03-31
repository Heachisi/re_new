package service.board;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.board.Board;
import model.board.Comment;

public interface BoardService {
    
    public List<Board> getBoardList(Board board);

    public Board getBoardById(String boardId);
    
    public boolean createBoard(Board board, HttpServletRequest request);
    
    public boolean updateBoard(Board board, HttpServletRequest request);
    
    public boolean deleteBoard(Board board);
    
    public boolean createComment(Comment comment);
    
    public boolean updateComment(Comment comment);
    
    public boolean deleteComment(Comment comment);
}