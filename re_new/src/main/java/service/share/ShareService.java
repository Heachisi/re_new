package service.share;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.share.Comment;
import model.share.Share;

public interface ShareService {
	public List getShareList(Share board);
	
    public Share getShareById(String boardId);
    
    public boolean createShare(Share board, HttpServletRequest request);
    
    public boolean updateShare(Share board, HttpServletRequest request);
    
    public boolean deleteShare(Share board);
    
    public boolean createComment(Comment comment);
    
    public boolean updateComment(Comment comment);
    
    public boolean deleteComment(Comment comment);
 
    
}
