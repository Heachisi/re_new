package service.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.user.UserDAO;
import model.board.AskBoard;
import model.user.User;
import util.MybatisUtil;
import util.SHA256Util;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private UserDAO userDAO;

    private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
    
    /**
     * UserServiceImpl 생성자
     */
    public UserServiceImpl() {
        this.userDAO = new UserDAO();
        try {
            sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
        } catch (Exception e) {
            logger.error("Mybatis 오류", e); // 오류 발생 시 로그 출력
        }
    }
    @Override
	public List<User> getUserList(User user) {
		SqlSession session = sqlSessionFactory.openSession();

		int page = user.getPage();
		int size = user.getSize();

		
		int totalCount = userDAO.getUserCount(session, user);
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		int startRow = (page - 1) * size + 1;
		int endRow = page * size;

		user.setTotalCount(totalCount);
		user.setTotalPages(totalPages);
		user.setStartRow(startRow);
		user.setEndRow(endRow);

		List<User> list = userDAO.getUserList(session, user);
		
		return list;
	}

    /**
     * 사용자 회원가입 서비스
     * @param userId 사용자 ID
     * @param username 사용자 이름
     * @param password 비밀번호 (SHA-256 암호화 적용)
     * @param email 이메일
     * @return 성공 여부
     */
    @Override
    public boolean registerUser(User user) {
    	SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	try {
    		
    		String password = user.getPassword();
    		String encryptedPassword = password != null ? SHA256Util.encrypt(password) : null;
    		user.setPassword(encryptedPassword);
    		
    		// DAO를 통해 회원가입 진행
            result = userDAO.registerUser(session, user);
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
    }
    

    public boolean validateUser(User user) {
    	SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false;
    	try {
    		User selectUser =
    		userDAO.getUserById(session, user.getUserId());
    		// 사용자 정보가 없으면 false 반환
    		if (selectUser == null) {
    			return false; // 사용자 ID가 존재하지 않을 경우
    		}
    		String password = user.getPassword();
    		String encryptedPassword = password != null ? SHA256Util.encrypt(password) : null;
    		
    		logger.info(encryptedPassword+":"+selectUser.getPassword());
    		
    		// 입력된 비밀번호와 DB에 저장된 비밀번호 비교
    		result = encryptedPassword.equals(selectUser.getPassword()); // 비밀번호 비교
    		
    		session.commit(); //트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
    	}
    	return result;
    }
    
    public User getUserById(String userId) {
        SqlSession session = sqlSessionFactory.openSession();
        User selectUser = userDAO.getUserById(session, userId);
        return selectUser;
    }


  	
    @Override
    public boolean checkUserIdDuplicate(String userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int count = session.selectOne("UserMapper.checkUserIdDuplicate", userId);
            return count > 0; // 존재하면 true 반환
        } catch (Exception e) {
            logger.error("아이디 중복 체크 중 오류 발생!", e);
            return false;
        }
    }

	@Override
	public boolean updateUser(User user) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = userDAO.updateUser(session, user);
			session.commit();
		}catch (Exception e) {
			logger.error("회원 정보 업데이트 중 오류 발생", e); // 🚀 로깅 추가
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	@Override
	public boolean deleteUser(User user) {
		SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	try {
    		
    		// DAO를 통해 회원탈퇴 진행
            result = userDAO.deleteUser(session, user);
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
	}

	 @Override
	    public List<Map<String, Object>> getAgeGroupCounts() {
	        try (SqlSession session = sqlSessionFactory.openSession()) {
	            return session.selectList("UserMapper.getAgeGroupCounts");
	        } catch (Exception e) {
	            logger.error("연령대 통계 조회 중 오류 발생", e);
	            return null;
	        }
	    }

	    @Override
	    public List<Map<String, Object>> getGenderCounts() {
	        try (SqlSession session = sqlSessionFactory.openSession()) {
	            return session.selectList("UserMapper.getGenderCounts");
	        } catch (Exception e) {
	            logger.error("성별 통계 조회 중 오류 발생", e);
	            return null;
	        }
	    }

		@Override
		public boolean toggleUserDeletion(User user) {
			  try (SqlSession session = sqlSessionFactory.openSession()) {
			        boolean result = session.update("UserMapper.toggleUserDeletion", user) > 0;
			        if (result) session.commit(); // 변경 성공 시 commit 수행
			        return result;
			    } catch (Exception e) {
			        logger.error("회원 상태 변경 중 오류 발생", e);
			        return false;
			    }
		}
}
