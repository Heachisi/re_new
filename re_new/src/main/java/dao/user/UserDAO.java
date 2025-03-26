package dao.user;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.user.User;
import util.MybatisUtil;


public class UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAO.class); // Logger 인스턴스 생성


                 
		
	public boolean registerUser(SqlSession session, User user) {
	    int result = session.insert("UserMapper.registerUser", user); // 사용자 등록 쿼리 실행
	    return result > 0; // 삽입 성공 여부 반환}
	}
	
	  

	public User getUserById(SqlSession session, String userId) {
	    User user = session.selectOne("UserMapper.getUserById", userId); // 사용자 정보조회
	        return user;}
	
	  }