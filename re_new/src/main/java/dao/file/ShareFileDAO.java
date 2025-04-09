package dao.file;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.share.ShareDAO;
import util.MybatisUtil;

public class ShareFileDAO {
	private static final Logger logger = LogManager.getLogger(ShareDAO.class);
	private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
	 
	public ShareFileDAO() {
	        try {
	            sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
	        } catch (Exception e) {
	            logger.error("Mybatis 오류", e);
	    }
	}



    
}
