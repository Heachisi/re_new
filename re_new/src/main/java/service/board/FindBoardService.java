package service.board;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.board.BulletinBoard;
import model.board.BulletinComment;
import model.board.FindBoard;
import model.board.FindComment;
import model.product.Product;


public interface FindBoardService {
    
    public List<FindBoard> getBoardList(FindBoard board);

    public FindBoard getBoardById(String boardId);
    
    public boolean createBoard(FindBoard board, HttpServletRequest request);
    
    public boolean updateBoard(FindBoard board, HttpServletRequest request);
    
    public boolean deleteBoard(FindBoard board);
    
    public boolean createComment(FindComment comment);
    
    public boolean updateComment(FindComment comment);
    
    public boolean deleteComment(FindComment comment);
    
    public void increaseViewCount(String boardId);
    
    public List<Product> getMyProductList(String userId);
}