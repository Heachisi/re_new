package dao.file;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.common.NoticePostFile;
import util.MybatisUtil;

public class NoticeFileDAO {
	private static final Logger logger = LogManager.getLogger(NoticeFileDAO.class);
	private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
	 
	public NoticeFileDAO() {
	        try {
	            sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
	        } catch (Exception e) {
	            logger.error("Mybatis 오류", e);
	    }
	}

    // 게시글에 첨부된 파일을 저장
    public boolean insertBoardFile(SqlSession session, NoticePostFile file) {
    	int result = session.insert("NoticeFileMapper.insertFile", file);
        return result > 0;
    }
    
    // 파일 ID로 첨부된 파일 조회
    public NoticePostFile getFileByFileId(SqlSession session, NoticePostFile file) {
    	return session.selectOne("NoticeFileMapper.getFileByFileId", file);
    }
    
    // 게시글 ID로 첨부된 파일 목록 조회
    public List<NoticePostFile> getFilesByBoardId(SqlSession session, String boardId) {
        return session.selectList("NoticeFileMapper.getFilesByBoardId", boardId);
    }

    /**
     * 게시글에 첨부된 파일 삭제
     * @param session MyBatis의 SqlSession 객체
     * @param fileId 삭제할 파일 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteFile(SqlSession session, NoticePostFile fileId) {
        int result = session.update("NoticeFileMapper.deleteFile", fileId);
        return result > 0;
    }

    
}