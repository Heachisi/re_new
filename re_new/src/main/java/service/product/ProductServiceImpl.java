package service.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.product.ProductDAO;
import dao.file.FileDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import model.product.Product;
import model.product.Comment;
import model.common.PostFile;
import util.FileUploadUtil;
import util.MybatisUtil;

public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);
    private ProductDAO productDAO;
    private FileDAO fileDAO;

    private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
   
    public ProductServiceImpl() {
        this.productDAO = new ProductDAO();
        this.fileDAO = new FileDAO();
        try {
            sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
        } catch (Exception e) {
            logger.error("Mybatis 오류", e); // 오류 발생 시 로그 출력
        }
    }

    // 특정 게시글 ID로 게시글 상세 정보를 가져옴
    public Product getProductById(String productId) {
        SqlSession session = sqlSessionFactory.openSession(); // 데이터베이스 세션 열기
        Product selectProduct = productDAO.getProductById(session, productId); // 게시글 기본 정보 조회
     // DB에서 파일 목록을 가져와서(getFilesByProductId) 게시글 객체(selectProduct)에 세팅
        selectProduct.setPostFiles(fileDAO.getFilesByProductId(session, productId)); 
     // DB에서 댓글 목록을 가져와서(getCommentList) 게시글 객체(selectProduct)에 세팅
        selectProduct.setComments(productDAO.getCommentList(session, productId));
     // 실제 화면 출력은 Controller가 View(JSP)에 selectProduct를 전달할 때 이루어짐
        return selectProduct;
    }
    
    // 새 게시글 생성
    public boolean createProduct(Product product, HttpServletRequest request) {
        SqlSession session = sqlSessionFactory.openSession(); // DB 세션 열기
        boolean result = false; 
        try {
            result = productDAO.createProduct(session, product); // 게시글 생성

            // 업로드된 파일 처리
            List<Part> fileParts = new ArrayList();
            for (Part part : request.getParts()) {
                if("files".equals(part.getName()) && part.getSize() > 0) {
                    fileParts.add(part);
                }
            }
            List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts,"product",
                    Integer.parseInt(product.getProductId()), product.getCreateId());

            // 첨부파일 정보를 DB에 저장
            for (PostFile postFile : fileList) {
                fileDAO.insertProductFile(session, postFile);
            }
            session.commit(); // DB 변경사항 반영
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback(); // 오류 발생 시 DB 작업 취소
        }
        return result;
    }
 // 기존 게시글 수정
    public boolean updateProduct(Product product, HttpServletRequest request) {
        SqlSession session = sqlSessionFactory.openSession(); // DB 세션 열기
        boolean result = false; 
        try {
            result = productDAO.updateProduct(session, product); // 게시글 수정
            if(result) {
                String postFilesParam = request.getParameter("remainingFileIds");
                List<String> postFiles = new ArrayList<String>();
                if(postFilesParam != null && !postFilesParam.trim().isEmpty()) {
                    postFiles = Arrays.asList(postFilesParam.split(",")); // 유지할 파일 목록
                }
                List<PostFile> existingFiles = fileDAO.getFilesByProductId(session, product.getProductId()); // 기존 첨부파일 조회

                // 삭제된 파일 처리
                if(existingFiles!=null && existingFiles.size() > 0) {
                    boolean fileExists = false;
                    for (PostFile existingFile : existingFiles) {
                        fileExists = false;
                        for (String fileId : postFiles) {
                            if(existingFile.getFileId() == Integer.parseInt(fileId)) {
                                fileExists = true;
                                break;
                            }
                        }
                        if(!fileExists) {
                            existingFile.setUpdateId(product.getUpdateId());
                            boolean deleteSuccess = fileDAO.deleteFile(session, existingFile);
                            if (!deleteSuccess) {
                                session.rollback();
                                return false;
                            }
                        }
                    }   
                }
            	
            	
             // 새로운 첨부파일 업로드
                List<Part> fileParts = new ArrayList();
                for (Part part : request.getParts()) {
                    if("files".equals(part.getName()) && part.getSize() > 0) {
                        fileParts.add(part);
                    }
                }
                List<PostFile> fileList = FileUploadUtil.uploadFiles(fileParts,"product",
                        Integer.parseInt(product.getProductId()), product.getUpdateId());

                // 새 첨부파일 정보 DB 저장
                for (PostFile postFile : fileList) {
                    fileDAO.insertProductFile(session, postFile);
                }
            }
            session.commit(); // DB 변경사항 반영
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback(); // 오류 발생 시 DB 작업 취소
        }
        return result;
    }
    public boolean deleteProduct(Product product) {
    	SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	try {
    		// DAO를 통해 회원가입 진행
            result = productDAO.deleteProduct(session, product);
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
    } 
	
    public List getProductList(Product product) {
    	//얘는 DB와 연결시켜주는 통로같은 역할
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	int page = product.getPage();
    	int size = product.getSize();
    	
    	int totalCount = productDAO.getTotalProductCount(session);
    	int totalPages = (int) Math.ceil((double) totalCount/size);
    	
    	int startRow = (page - 1)*size + 1;
    	int endRow = page*size;
    	
    	product.setTotalCount(totalCount);
    	product.setTotalPages(totalPages);
    	product.setStartRow(startRow);
    	product.setEndRow(endRow);
    	
    	List list = productDAO.getProductList(session, product);
    	product.setMinprice(String.valueOf(productDAO.getMinPrice(session)));
        product.setMaxprice(String.valueOf(productDAO.getMaxPrice(session)));
    	return list;
    }
    
    public List getMyProductList(Product product) {
    	//얘는 DB와 연결시켜주는 통로같은 역할
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	int page = product.getPage();
    	int size = product.getSize();
    	
    	int totalCount = productDAO.getTotalProductCount(session);
    	int totalPages = (int) Math.ceil((double) totalCount/size);
    	
    	int startRow = (page - 1)*size + 1;
    	int endRow = page*size;
    	
    	
    	product.setTotalCount(totalCount);
    	product.setTotalPages(totalPages);
    	product.setStartRow(startRow);
    	product.setEndRow(endRow);
    	
    	List list = productDAO.getMyProductList(session, product);
    	product.setMinprice(String.valueOf(productDAO.getMinPrice(session)));
        product.setMaxprice(String.valueOf(productDAO.getMaxPrice(session)));
    	return list;
    }

	@Override
	public boolean createComment(Comment comment) {
		SqlSession session = sqlSessionFactory.openSession();
    	boolean result = false; 
    	try {
    		result = productDAO.insertComment(session, comment);
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
    		result = productDAO.updateComment(session, comment);
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
    		result = productDAO.deleteComment(session, comment);
            session.commit(); // 트랜잭션 커밋
    	} catch (Exception e) {
    		e.printStackTrace();
    		session.rollback();
		}
        return result;
	}

	@Override
	public boolean updateProductLike(Product product) {
		SqlSession session = sqlSessionFactory.openSession();
		int result = session.update("ProductMapper.updateProductLike", product);
		session.commit();
	    return result > 0;
	}

	

	
}
