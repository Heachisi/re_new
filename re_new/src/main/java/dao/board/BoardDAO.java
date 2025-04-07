package dao.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.board.Board;
import model.board.Comment;

public class BoardDAO {
	  private static final Logger logger = LogManager.getLogger(BoardDAO.class); // Logger 인스턴스 생성
	 
	  public List<Board> getBoardList(SqlSession session, Board board, String searchKey, String searchQuery ) {
		  List<Board> boardList = session.selectList("BoardMapper.getBoardList", board);
		  return boardList;
	  }
	  
	  public int getTotalBoardCount(SqlSession session, String searchKey, String searchQuery) {
		  //검색 조건 포함한 파라미터 map생성
		  Map<String, Object> params = new HashMap<>();
		  params.put("searchKey", searchKey);
		  params.put("searchquery", searchQuery);
		  
		  int totalCount = session.selectOne("BoardMapper.getTotalBoardCount",params);
		  return totalCount;
	  }
	  
	  public Board getBoardById(SqlSession session, String boardId) {
		  Board board = session.selectOne("BoardMapper.getBoardById", boardId);
		  return board;
	  }
	  
	  public boolean createBoard(SqlSession session, Board board) {
	        int result = session.insert("BoardMapper.create", board);
	        return result > 0; 
	    }
	  public boolean updateBoard(SqlSession session, Board board) {
		  int result=session.update("BoardMapper.update", board);
		  return result >0; //수정
	  }
	  public boolean deleteBoard(SqlSession session, Board board) {
		  int result=session.update("BoardMapper.delete", board);
		  return result >0; //삭제
	  }
	  
	  
	  /**
	  * 댓글 목록 조회
	  * @param boardId 게시글 ID
	  * @return 댓글 목록
	  * */
	  public List<Comment> getCommentList(SqlSession session, String boardId) {
		  return session.selectList("BoardMapper.getCommentsByBoardId", boardId);
	  }

	  /**
	  * 댓글 등록
	  * @param comment 댓글 객체
	  * @return 성공 여부
	  * */
	  public boolean insertComment(SqlSession session, Comment comment) {
		  int result = session.insert("BoardMapper.insertComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 수정
	  * @param comment 댓글 객체 (수정할 내용 포함)
	  * @return 성공 여부
	  * */
	  public boolean updateComment(SqlSession session, Comment comment) {
		  int result = session.update("BoardMapper.updateComment", comment);
	      return result > 0;
	  }

	  /**  
	  * 댓글 삭제
	  * @param commentId 댓글 ID
	  * @return 성공 여부
	  * */
	  public boolean deleteComment(SqlSession session, Comment comment) {
		  int result = session.update("BoardMapper.deleteComment", comment);
	      return result > 0;
	  }
}