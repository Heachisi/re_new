package service.share;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.share.ShareDAO;
import jakarta.servlet.http.HttpServletRequest;
import model.share.Comment;
import model.share.Share;
import util.MybatisUtil;

public class ShareServiceImpl implements ShareService {
    private static final Logger logger = LogManager.getLogger(ShareServiceImpl.class);
    private ShareDAO shareDAO;

    private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
   
    public ShareServiceImpl() {
        this.shareDAO = new ShareDAO();
        try {
            sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
        } catch (Exception e) {
            logger.error("Mybatis 오류", e); // 오류 발생 시 로그 출력
        }
    }

    // 특정 게시글 ID로 게시글 상세 정보를 가져옴
    public Share getShareById(String shareId) {
        SqlSession session = sqlSessionFactory.openSession(); // 데이터베이스 세션 열기
        Share selectShare = shareDAO.getShareById(session, shareId); // 게시글 기본 정보 조회
     // DB에서 댓글 목록을 가져와서(getCommentList) 게시글 객체(selectShare)에 세팅
        selectShare.setComments(shareDAO.getCommentList(session, shareId));
     // 실제 화면 출력은 Controller가 View(JSP)에 selectShare를 전달할 때 이루어짐
        return selectShare;
    }
    
    // 새 게시글 생성
    public boolean createShare(Share share, HttpServletRequest request) {
        SqlSession session = sqlSessionFactory.openSession(); // DB 세션 열기
        boolean result = false; 
        try {
            result = shareDAO.createShare(session, share); // 게시글 생성
            session.commit(); // DB 변경사항 반영
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback(); // 오류 발생 시 DB 작업 취소
        }
        return result;
    }
 // 기존 게시글 수정
    public boolean updateShare(Share share, HttpServletRequest request) {
        SqlSession session = sqlSessionFactory.openSession(); // DB 세션 열기
        boolean result = false; 
        try {
            result = shareDAO.updateShare(session, share); // 게시글 수정
            session.commit(); // DB 변경사항 반영
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback(); // 오류 발생 시 DB 작업 취소
        }
        return result;
    }
    public boolean deleteShare(Share share) {
    	SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	try {
    		// DAO를 통해 회원가입 진행
            result = shareDAO.deleteShare(session, share);
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
    } 
	
    public List getShareList(Share share) {
    	//얘는 DB와 연결시켜주는 통로같은 역할
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	int page = share.getPage();
    	int size = share.getSize();
    	
    	int totalCount = shareDAO.getTotalShareCount(session);
    	int totalPages = (int) Math.ceil((double) totalCount/size);
    	
    	int startRow = (page - 1)*size + 1;
    	int endRow = page*size;
    	
    	share.setTotalCount(totalCount);
    	share.setTotalPages(totalPages);
    	share.setStartRow(startRow);
    	share.setEndRow(endRow);
    	
    	List list = shareDAO.getShareList(session, share);
    	return list;
    }
   

	@Override
	public boolean createComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	try {
    		result = shareDAO.insertComment(session, comment);
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
	}

	@Override
	public boolean updateComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	try {
    		result = shareDAO.updateComment(session, comment);
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
	}

	@Override
	public boolean deleteComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	try {
    		result = shareDAO.deleteComment(session, comment);
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
	}

	
	

	
}
