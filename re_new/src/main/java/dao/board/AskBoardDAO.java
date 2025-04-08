package dao.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.board.AskBoard;
import model.board.AskComment;


public class AskBoardDAO {
	  private static final Logger logger = LogManager.getLogger(AskBoardDAO.class); // Logger 인스턴스 생성
	 
	  public List<AskBoard> getBoardList(SqlSession session, AskBoard board) {
		  List<AskBoard> boardList = session.selectList("AskBoardMapper.getBoardList", board);
		  return boardList;
	  }
	  
	  public int getTotalBoardCount(SqlSession session, AskBoard board) {
		  int totalCount = session.selectOne("AskBoardMapper.getTotalBoardCount", board);
		  return totalCount;
	  }
	  
	  public AskBoard getBoardById(SqlSession session, String boardId) {
		  AskBoard board = session.selectOne("AskBoardMapper.getBoardById", boardId);
		  return board;
	  }
	  
	  public boolean createBoard(SqlSession session, AskBoard board) {
	        int result = session.insert("AskBoardMapper.create", board);
	        return result > 0; // 삽입 성공 여부 반환
	        
	        
	    }
	  public boolean updateBoard(SqlSession session, AskBoard board) {
		  int result=session.update("AskBoardMapper.update", board);
		  return result >0; //수정
	  }
	  public boolean deleteBoard(SqlSession session, AskBoard board) {
		  int result=session.update("AskBoardMapper.delete", board);
		  return result >0; //삭제
	  }
	  
	  
	  /**
	  * 댓글 목록 조회
	  * @param boardId 게시글 ID
	  * @return 댓글 목록
	  * */
	  public List<AskComment> getCommentList(SqlSession session, String boardId) {
		  return session.selectList("AskBoardMapper.getCommentsByBoardId", boardId);
	  }

	  /**
	  * 댓글 등록
	  * @param comment 댓글 객체
	  * @return 성공 여부
	  * */
	  public boolean insertComment(SqlSession session, AskComment comment) {
		  int result = session.insert("AskBoardMapper.insertComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 수정
	  * @param comment 댓글 객체 (수정할 내용 포함)
	  * @return 성공 여부
	  * */
	  public boolean updateComment(SqlSession session, AskComment comment) {
		  int result = session.update("AskBoardMapper.updateComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 삭제
	  * @param commentId 댓글 ID
	  * @return 성공 여부
	  * */
	  public boolean deleteComment(SqlSession session, AskComment comment) {
		  int result = session.update("AskBoardMapper.deleteComment", comment);
	      return result > 0;
	  }
	  
	  public void increaseViewCount(SqlSession session, String boardId) {
		  session.update("AskBoardMapper.increaseViewCount", boardId);
	  }
}