package service.board;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.board.BulletinBoard;
import model.board.BulletinComment;


public interface BulletinBoardService {
    
    public List<BulletinBoard> getBoardList(BulletinBoard board);

    public BulletinBoard getBoardById(String boardId);
    
    public boolean createBoard(BulletinBoard board, HttpServletRequest request);
    
    public boolean updateBoard(BulletinBoard board, HttpServletRequest request);
    
    public boolean deleteBoard(BulletinBoard board);
    
    public boolean createComment(BulletinComment comment);
    
    public boolean updateComment(BulletinComment comment);
    
    public boolean deleteComment(BulletinComment comment);
    
    public void increaseViewCount(String boardId);
}