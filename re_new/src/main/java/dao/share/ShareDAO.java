package dao.share;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.share.Share;
import model.share.Comment;


public class ShareDAO {
    private static final Logger logger = LogManager.getLogger(ShareDAO.class); // Logger 인스턴스 생성
   
    public List getShareList(SqlSession session, Share share) {
        // selectList: 여러 개의 데이터를 조회하여 List 형태로 반환
        List shareList = session.selectList("ShareMapper.getShareList", share); 
        return shareList;
    }
    public List getMyShareList(SqlSession session, Share share) {
        // selectList: 여러 개의 데이터를 조회하여 List 형태로 반환
        List shareList = session.selectList("ShareMapper.getMyShareList", share); 
        return shareList;
    }
    public int getMinPrice(SqlSession session) {
        Integer minprice = session.selectOne("ShareMapper.getMinPrice");
        return (minprice != null) ? minprice : 0;
    }

    public int getMaxPrice(SqlSession session) {
        Integer maxprice = session.selectOne("ShareMapper.getMaxPrice");
        return (maxprice != null) ? maxprice : 0;
    }

    public int getTotalShareCount(SqlSession session) {
        // selectOne: 단일 데이터를 조회하여 하나의 객체로 반환
        int totalCount = session.selectOne("ShareMapper.getTotalShareCount");
        return totalCount;
    }

    public Share getShareById(SqlSession session, String shareId) {
        // selectOne: 특정 게시글 ID로 게시글 하나를 조회하여 반환
        Share share = session.selectOne("ShareMapper.getShareById", shareId);
        return share;
    }

    public boolean createShare(SqlSession session, Share share) {
        // insert: 새로운 데이터를 DB에 삽입
        int result = session.insert("ShareMapper.create", share);
        return result > 0;
    }

    public boolean updateShare(SqlSession session, Share share) {
        // insert (주의: 실제로는 update를 써야함): 기존 데이터를 수정
        int result = session.insert("ShareMapper.update", share);
        return result > 0;
    }

    public boolean deleteShare(SqlSession session, Share share) {
        // insert (주의: 실제로는 delete를 써야함): 기존 데이터를 삭제
        int result = session.insert("ShareMapper.delete", share);
        return result > 0;
    }

    public List<Comment> getCommentList(SqlSession session, String shareId) {
        // selectList: 특정 게시글에 연결된 댓글 목록을 조회
        return session.selectList("ShareMapper.getCommentsByShareId", shareId);
    }

    public boolean insertComment(SqlSession session, Comment comment) {
        // insert: 새로운 댓글을 DB에 삽입
        int result = session.insert("ShareMapper.insertComment", comment);
        return result > 0;
    }

    public boolean updateComment(SqlSession session, Comment comment) {
        // update: 기존 댓글 데이터를 수정
        int result = session.update("ShareMapper.updateComment", comment);
        session.commit();
        return result > 0;
    }

    public boolean deleteComment(SqlSession session, Comment comment) {
        // update (주의: 실제로는 delete를 써야함): 댓글을 삭제하거나 상태 변경
        int result = session.update("ShareMapper.deleteComment", comment);
        return result > 0;
    }
}