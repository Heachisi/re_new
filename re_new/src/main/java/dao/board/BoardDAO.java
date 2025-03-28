package dao.board;

import java.util.List;



import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.board.Board;
import model.board.Comment;

public class BoardDAO {	
	private static final Logger logger = LogManager.getLogger(BoardDAO.class); // Logger 인스턴스 생성
	
	public int getTotalBoardCount(SqlSession session) {
        int board = session.selectOne("BoardMapper.getTotalBoardCount");
        return board;
    }
	
    public List getBoardList(SqlSession session, Board board) {
        List boardList = session.selectList("BoardMapper.getBoardList", board);
        return boardList;
    }
	
    public Board getBoardById(SqlSession session, String boardId) {
        Board board = session.selectOne("BoardMapper.getBoardById", boardId);
        return board;
    }
    
    public boolean createBoard(SqlSession session, Board board) {
    	int result = session.insert("BoardMapper.create", board);
    	return result > 0; // 삽입 성공 여부 반환
    }
    
    public boolean updateBoard(SqlSession session, Board board) {
    	int result = session.update("BoardMapper.update", board);
    	return result > 0; // 수정 성공 여부 반환
    }
    
    public boolean deleteBoard(SqlSession session, Board board) {
    	int result = session.update("BoardMapper.delete", board);
    	return result > 0; // 삭제 성공 여부 반환
    }
    
    /**
    댓글 목록 조회
    @param boardId 게시글 ID
    @return 댓글 목록*/
    		
    public List<Comment> getCommentList(SqlSession session, String boardId) {
            return session.selectList("BoardMapper.getCommentsByBoardId", boardId);}

    /**
    댓글 등록
    @param comment 댓글 객체
    @return 성공 여부*/
    public boolean insertComment(SqlSession session, Comment comment) {
            int result = session.insert("BoardMapper.insertComment", comment);
            return result > 0;}

    /**
    댓글 수정
    @param comment 댓글 객체 (수정할 내용 포함)
    @return 성공 여부*/
    public boolean updateComment(SqlSession session, Comment comment) {
            int result = session.update("BoardMapper.updateComment", comment);
            return result > 0;}

    /**
    댓글 삭제
    @param commentId 댓글 ID
    @return 성공 여부*/
    public boolean deleteComment(SqlSession session, Comment comment) {
            int result = session.delete("BoardMapper.deleteComment", comment);
            return result > 0;}
    
     }



