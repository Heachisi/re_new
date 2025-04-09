package dao.product;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.product.Product;
import model.product.Comment;


public class ProductDAO {
    private static final Logger logger = LogManager.getLogger(ProductDAO.class); // Logger 인스턴스 생성
   
    public List getProductList(SqlSession session, Product product) {
        // selectList: 여러 개의 데이터를 조회하여 List 형태로 반환
        List productList = session.selectList("ProductMapper.getProductList", product); 
        return productList;
    }
    public List getMyProductList(SqlSession session, Product product) {
        // selectList: 여러 개의 데이터를 조회하여 List 형태로 반환
        List productList = session.selectList("ProductMapper.getMyProductList", product); 
        return productList;
    }
    public int getMinPrice(SqlSession session) {
        Integer minprice = session.selectOne("ProductMapper.getMinPrice");
        return (minprice != null) ? minprice : 0;
    }

    public int getMaxPrice(SqlSession session) {
        Integer maxprice = session.selectOne("ProductMapper.getMaxPrice");
        return (maxprice != null) ? maxprice : 0;
    }

    public int getTotalProductCount(SqlSession session) {
        // selectOne: 단일 데이터를 조회하여 하나의 객체로 반환
        int totalCount = session.selectOne("ProductMapper.getTotalProductCount");
        return totalCount;
    }

    public Product getProductById(SqlSession session, String productId) {
        // selectOne: 특정 게시글 ID로 게시글 하나를 조회하여 반환
        Product product = session.selectOne("ProductMapper.getProductById", productId);
        return product;
    }

    public boolean createProduct(SqlSession session, Product product) {
        // insert: 새로운 데이터를 DB에 삽입
        int result = session.insert("ProductMapper.create", product);
        return result > 0;
    }

    public boolean updateProduct(SqlSession session, Product product) {
        // insert (주의: 실제로는 update를 써야함): 기존 데이터를 수정
        int result = session.insert("ProductMapper.update", product);
        return result > 0;
    }

    public boolean deleteProduct(SqlSession session, Product product) {
        // insert (주의: 실제로는 delete를 써야함): 기존 데이터를 삭제
        int result = session.insert("ProductMapper.delete", product);
        return result > 0;
    }

    public List<Comment> getCommentList(SqlSession session, String productId) {
        // selectList: 특정 게시글에 연결된 댓글 목록을 조회
        return session.selectList("ProductMapper.getCommentsByProductId", productId);
    }

    public boolean insertComment(SqlSession session, Comment comment) {
        // insert: 새로운 댓글을 DB에 삽입
        int result = session.insert("ProductMapper.insertComment", comment);
        return result > 0;
    }

    public boolean updateComment(SqlSession session, Comment comment) {
        // update: 기존 댓글 데이터를 수정
        int result = session.update("ProductMapper.updateComment", comment);
        session.commit();
        return result > 0;
    }

    public boolean deleteComment(SqlSession session, Comment comment) {
        // update (주의: 실제로는 delete를 써야함): 댓글을 삭제하거나 상태 변경
        int result = session.update("ProductMapper.deleteComment", comment);
        return result > 0;
    }
}