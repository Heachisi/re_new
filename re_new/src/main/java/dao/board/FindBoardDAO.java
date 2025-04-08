package dao.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.board.BulletinBoard;
import model.board.BulletinComment;
import model.board.FindBoard;
import model.board.FindComment;
import model.product.Product;


public class FindBoardDAO {
	  private static final Logger logger = LogManager.getLogger(FindBoardDAO.class); // Logger 인스턴스 생성
	 
	  public List<FindBoard> getBoardList(SqlSession session, FindBoard board) {
		  List<FindBoard> boardList = session.selectList("FindBoardMapper.getBoardList", board);
		  return boardList;
	  }
	  
	  public int getTotalBoardCount(SqlSession session, FindBoard board) {
		  int totalCount = session.selectOne("FindBoardMapper.getTotalBoardCount");
		  return totalCount;
	  }
	  
	  public FindBoard getBoardById(SqlSession session, String boardId) {
		  FindBoard board = session.selectOne("FindBoardMapper.getBoardById", boardId);
		  return board;
	  }
	  
	  public boolean createBoard(SqlSession session, FindBoard board) {
	        int result = session.insert("FindBoardMapper.create", board);
	        return result > 0; // 삽입 성공 여부 반환
	        
	        
	    }
	  public boolean updateBoard(SqlSession session, FindBoard board) {
		  int result=session.update("FindBoardMapper.update", board);
		  return result >0; //수정
	  }
	  public boolean deleteBoard(SqlSession session, FindBoard board) {
		  int result=session.update("FindBoardMapper.delete", board);
		  return result >0; //삭제
	  }
	  
	  
	  /**
	  * 댓글 목록 조회
	  * @param boardId 게시글 ID
	  * @return 댓글 목록
	  * */
	  public List<FindComment> getCommentList(SqlSession session, String boardId) {
		  return session.selectList("FindBoardMapper.getCommentsByBoardId", boardId);
	  }

	  /**
	  * 댓글 등록
	  * @param comment 댓글 객체
	  * @return 성공 여부
	  * */
	  public boolean insertComment(SqlSession session, FindComment comment) {
		  int result = session.insert("FindBoardMapper.insertComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 수정
	  * @param comment 댓글 객체 (수정할 내용 포함)
	  * @return 성공 여부
	  * */
	  public boolean updateComment(SqlSession session, FindComment comment) {
		  int result = session.update("FindBoardMapper.updateComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 삭제
	  * @param commentId 댓글 ID
	  * @return 성공 여부
	  * */
	  public boolean deleteComment(SqlSession session, FindComment comment) {
		  int result = session.update("FindBoardMapper.deleteComment", comment);
	      return result > 0;
	  }
	  
	  public void increaseViewCount(SqlSession session, String boardId) {
		  session.update("FindBoardMapper.increaseViewCount", boardId);
	  }
	  
	  public List<Product> getMyProductList(SqlSession session, String userId) {
		    return session.selectList("FindBoardMapper.getMyProducts", userId);
		}
}