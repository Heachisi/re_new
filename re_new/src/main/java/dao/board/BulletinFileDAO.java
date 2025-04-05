package dao.file;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.common.BulletinPostFile;
import model.common.PostFile;
import util.MybatisUtil;

public class BulletinFileDAO {
	private static final Logger logger = LogManager.getLogger(BulletinFileDAO.class);
	private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
	 
	public BulletinFileDAO() {
	        try {
	            sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
	        } catch (Exception e) {
	            logger.error("Mybatis 오류", e);
	    }
	}

    // 게시글에 첨부된 파일을 저장
    public boolean insertBoardFile(SqlSession session, BulletinPostFile file) {
    	int result = session.insert("BulletinFileMapper.insertFile", file);
        return result > 0;
    }
    
    // 파일 ID로 첨부된 파일 조회
    public BulletinPostFile getFileByFileId(SqlSession session, BulletinPostFile file) {
    	return session.selectOne("BulletinFileMapper.getFileByFileId", file);
    }
    
    // 게시글 ID로 첨부된 파일 목록 조회
    public List<BulletinPostFile> getFilesByBoardId(SqlSession session, String boardId) {
        return session.selectList("BulletinFileMapper.getFilesByBoardId", boardId);
    }

    /**
     * 게시글에 첨부된 파일 삭제
     * @param session MyBatis의 SqlSession 객체
     * @param fileId 삭제할 파일 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteFile(SqlSession session, BulletinPostFile fileId) {
        int result = session.update("BulletinFileMapper.deleteFile", fileId);
        return result > 0;
    }

    
}