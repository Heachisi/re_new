package dao.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.board.BulletinBoard;
import model.board.BulletinComment;


public class BulletinBoardDAO {
	  private static final Logger logger = LogManager.getLogger(BulletinBoardDAO.class); // Logger 인스턴스 생성
	 
	  public List<BulletinBoard> getBoardList(SqlSession session, BulletinBoard board) {
		  List<BulletinBoard> boardList = session.selectList("BulletinBoardMapper.getBoardList", board);
		  return boardList;
	  }
	  
	  public int getTotalBoardCount(SqlSession session, BulletinBoard board) {
		  int totalCount = session.selectOne("BulletinBoardMapper.getTotalBoardCount");
		  return totalCount;
	  }
	  
	  public BulletinBoard getBoardById(SqlSession session, String boardId) {
		  BulletinBoard board = session.selectOne("BulletinBoardMapper.getBoardById", boardId);
		  return board;
	  }
	  
	  public boolean createBoard(SqlSession session, BulletinBoard board) {
	        int result = session.insert("BulletinBoardMapper.create", board);
	        return result > 0; // 삽입 성공 여부 반환
	        
	        
	    }
	  public boolean updateBoard(SqlSession session, BulletinBoard board) {
		  int result=session.update("BulletinBoardMapper.update", board);
		  return result >0; //수정
	  }
	  public boolean deleteBoard(SqlSession session, BulletinBoard board) {
		  int result=session.update("BulletinBoardMapper.delete", board);
		  return result >0; //삭제
	  }
	  
	  
	  /**
	  * 댓글 목록 조회
	  * @param boardId 게시글 ID
	  * @return 댓글 목록
	  * */
	  public List<BulletinComment> getCommentList(SqlSession session, String boardId) {
		  return session.selectList("BulletinBoardMapper.getCommentsByBoardId", boardId);
	  }

	  /**
	  * 댓글 등록
	  * @param comment 댓글 객체
	  * @return 성공 여부
	  * */
	  public boolean insertComment(SqlSession session, BulletinComment comment) {
		  int result = session.insert("BulletinBoardMapper.insertComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 수정
	  * @param comment 댓글 객체 (수정할 내용 포함)
	  * @return 성공 여부
	  * */
	  public boolean updateComment(SqlSession session, BulletinComment comment) {
		  int result = session.update("BulletinBoardMapper.updateComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 삭제
	  * @param commentId 댓글 ID
	  * @return 성공 여부
	  * */
	  public boolean deleteComment(SqlSession session, BulletinComment comment) {
		  int result = session.update("BulletinBoardMapper.deleteComment", comment);
	      return result > 0;
	  }
	  
	  public void increaseViewCount(SqlSession session, String boardId) {
		  session.update("BulletinBoardMapper.increaseViewCount", boardId);
	  }
}