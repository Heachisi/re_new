package dao.file;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.product.ProductDAO;
import model.common.PostFile;
import util.MybatisUtil;

public class ProductFileDAO {
	private static final Logger logger = LogManager.getLogger(ProductDAO.class);
	private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
	 
	public ProductFileDAO() {
	        try {
	            sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
	        } catch (Exception e) {
	            logger.error("Mybatis 오류", e);
	    }
	}

    // 게시글에 첨부된 파일 목록을 저장
    public boolean insertProductFile(SqlSession session, PostFile file) {
        try {
            session.insert("FileMapper.insertFile", file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 게시글 ID와 파일 ID로 첨부된 파일 조회
    public PostFile getFileByProductIdAndFileId(SqlSession session, PostFile file) {
    	  return session.selectOne("FileMapper.getFileByProductIdAndFileId",file);  // 파일을 찾지 못한 경우
         } 
    
    // 게시글 ID로 첨부된 파일 목록 조회
    public List<PostFile> getFilesByProductId(SqlSession session, String productId) {
            return session.selectList("FileMapper.getFilesByProductId", productId);
    }

    public boolean deleteFile(SqlSession session, PostFile file) {
        try {
            int result = session.delete("FileMapper.deleteFile", file);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
}
