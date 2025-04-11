package dao.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.board.NoticeBoard;
import model.board.NoticeComment;


public class NoticeBoardDAO {
	  private static final Logger logger = LogManager.getLogger(NoticeBoardDAO.class); // Logger 인스턴스 생성
	 
	  public List<NoticeBoard> getBoardList(SqlSession session, NoticeBoard board) {
		  List<NoticeBoard> boardList = session.selectList("NoticeBoardMapper.getBoardList", board);
		  return boardList;
	  }
	  
	  public int getTotalBoardCount(SqlSession session, NoticeBoard board) {
		  int totalCount = session.selectOne("NoticeBoardMapper.getTotalBoardCount");
		  return totalCount;
	  }
	  
	  public NoticeBoard getBoardById(SqlSession session, String boardId) {
		  NoticeBoard board = session.selectOne("NoticeBoardMapper.getBoardById", boardId);
		  return board;
	  }
	  
	  public boolean createBoard(SqlSession session, NoticeBoard board) {
	        int result = session.insert("NoticeBoardMapper.create", board);
	        return result > 0; // 삽입 성공 여부 반환
	        
	        
	    }
	  public boolean updateBoard(SqlSession session, NoticeBoard board) {
		  int result=session.update("NoticeBoardMapper.update", board);
		  return result >0; //수정
	  }
	  public boolean deleteBoard(SqlSession session, NoticeBoard board) {
		  int result=session.update("NoticeBoardMapper.delete", board);
		  return result >0; //삭제
	  }
	  
	  
	  /**
	  * 댓글 목록 조회
	  * @param boardId 게시글 ID
	  * @return 댓글 목록
	  * */
	  public List<NoticeComment> getCommentList(SqlSession session, String boardId) {
		  return session.selectList("NoticeBoardMapper.getCommentsByBoardId", boardId);
	  }

	  /**
	  * 댓글 등록
	  * @param comment 댓글 객체
	  * @return 성공 여부
	  * */
	  public boolean insertComment(SqlSession session, NoticeComment comment) {
		  int result = session.insert("NoticeBoardMapper.insertComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 수정
	  * @param comment 댓글 객체 (수정할 내용 포함)
	  * @return 성공 여부
	  * */
	  public boolean updateComment(SqlSession session, NoticeComment comment) {
		  int result = session.update("NoticeBoardMapper.updateComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 삭제
	  * @param commentId 댓글 ID
	  * @return 성공 여부
	  * */
	  public boolean deleteComment(SqlSession session, NoticeComment comment) {
		  int result = session.update("NoticeBoardMapper.deleteComment", comment);
	      return result > 0;
	  }
	  
	  public void increaseViewCount(SqlSession session, String boardId) {
		  session.update("NoticeBoardMapper.increaseViewCount", boardId);
	  }
}