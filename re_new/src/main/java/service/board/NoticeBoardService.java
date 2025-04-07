package service.board;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.board.NoticeBoard;
import model.board.NoticeComment;


public interface NoticeBoardService {
    
    public List<NoticeBoard> getBoardList(NoticeBoard board);

    public NoticeBoard getBoardById(String boardId);
    
    public boolean createBoard(NoticeBoard board, HttpServletRequest request);
    
    public boolean updateBoard(NoticeBoard board, HttpServletRequest request);
    
    public boolean deleteBoard(NoticeBoard board);
    
    public boolean createComment(NoticeComment comment);
    
    public boolean updateComment(NoticeComment comment);
    
    public boolean deleteComment(NoticeComment comment);
    
    public void increaseViewCount(String boardId);
}