package dao.board;

import java.util.List;



import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.board.Board;
import model.board.Comment;


public class BoardDAO {
    private static final Logger logger = LogManager.getLogger(BoardDAO.class); // Logger 인스턴스 생성
   
    public List getBoardList(SqlSession session,Board board) {
 	   
 	   List boardList = session.selectList("BoardMapper.getBoardList", board);
 	   return boardList;
    } 
    
    public int getTotalBoardCount(SqlSession session) {
 	   
 	   int totalCount = session.selectOne("BoardMapper.getTotalBoardCount");
 	   return totalCount;
    }
    
   public Board getBoardById(SqlSession session, String boardId) {
	   
	   Board board = 
	   session.selectOne("BoardMapper.getBoardById", boardId);
	   return board;
   }
   
   public boolean createBoard(SqlSession session, Board board) {
	    int result = session.insert("BoardMapper.create", board); // 사용자 등록 쿼리 실행
	    return result > 0; // 삽입 성공 여부 반환}
	}
	
   
   public boolean updateBoard(SqlSession session, Board board) {
	    int result = session.update("BoardMapper.update", board); // 사용자 등록 쿼리 실행
	    return result > 0; // 삽입 성공 여부 반환}
	}
   
   
   public boolean deleteBoard(SqlSession session, Board board) {
	    int result = session.update("BoardMapper.delete", board); // 사용자 등록 쿼리 실행
	    return result > 0; // 삽입 성공 여부 반환}
	}
	
  
   
 
  
   public List<Comment> getCommentList(SqlSession session, String boardId) {
           return session.selectList("BoardMapper.getCommentsByBoardId", boardId);

   }  
  
   public boolean insertComment(SqlSession session, Comment comment) {
           int result = session.insert("BoardMapper.insertComment", comment);
           return result > 0;

   }
   public boolean updateComment(SqlSession session, Comment comment) {
           int result = session.update("BoardMapper.updateComment", comment);
           return result > 0;

   }    
   public boolean deleteComment(SqlSession session, Comment commentId) {
           int result = session.delete("BoardMapper.deleteComment", commentId);
           return result > 0;
   }
   
}