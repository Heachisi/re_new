package controller.product;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.board.NoticeBoard;
import model.product.Comment;
import model.product.Product;
import model.user.User;
import service.board.NoticeBoardService;
import service.board.NoticeBoardServiceImpl;
import service.product.ProductService;
import service.product.ProductServiceImpl;


@WebServlet("/product/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class ProductController extends HttpServlet {
 
	private static final long serialVersionUID = 5726041575375068755L;
	private static final Logger logger = LogManager.getLogger(ProductController.class); 
	private ProductService productService;
	private NoticeBoardService noticeService;
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 12;
	
	public ProductController() {
        super();
        productService = new ProductServiceImpl(); 
        noticeService = new NoticeBoardServiceImpl(); 
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  logger.info("ProductController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("ProductController doGet path" + path); 
	      
	      //나중에 게시글 목록 나오면 사용될거임
	      if ("/product/view.do".equals(path)) {
	    	    String productId = request.getParameter("id");
	    	    Product product = productService.getProductById(productId);
	    	    
	    	    request.setAttribute("product", product);
	            request.getRequestDispatcher("/WEB-INF/jsp/product/view.jsp").forward(request, response);
	      } else if ("/product/myview.do".equals(path)) {
	    	    String productId = request.getParameter("id");
	    	    Product product = productService.getProductById(productId);
	    	    
	    	    request.setAttribute("product", product);
	            request.getRequestDispatcher("/WEB-INF/jsp/product/myview.jsp").forward(request, response);
	      } else if ("/product/create.do".equals(path)) {
	    	  request.getRequestDispatcher("/WEB-INF/jsp/product/create.jsp").forward(request, response);
	      } else if ("/product/update.do".equals(path)) {
	    	   
	    	  String productId = request.getParameter("id");
	    	    Product product = productService.getProductById(productId);
	    	    request.setAttribute("product", product);
	    	    
	            request.getRequestDispatcher("/WEB-INF/jsp/product/update.jsp").forward(request, response);
	      }  else if ("/product/list.do".equals(path)) {
	    	  int page = request.getParameter("page") != null ?
	    			  Integer.parseInt(request.getParameter("page"))
	    			  : DEFAULT_PAGE;
	    	  int size = request.getParameter("size") != null ?
	    			  Integer.parseInt(request.getParameter("size"))
	    			  : DEFAULT_SIZE;
	    	
	    	  
	    	  Product product = new Product();
	    	  NoticeBoard notice = new NoticeBoard();
	    	  
	    	  String endDate = request.getParameter("endDate");
	    	  String startDate = request.getParameter("startDate");
	    	  String searchText = request.getParameter("searchText");
	    	  String viewcategory = request.getParameter("viewcategory");
	    	  String minprice = request.getParameter("minprice");
	    	  String maxprice = request.getParameter("maxprice");
	    	  String likecount = request.getParameter("likecount");
	    	  String liked = request.getParameter("liked");
	    	  String category = request.getParameter("category");
	    	  
	    	  notice.setPage(1); // 필수
	    	  notice.setSize(5); // 원하는 공지 개수
	    	  
	    	  product.setSize(size);
	    	  product.setPage(page);
	    	  product.setSearchText(searchText);
	    	  product.setStartDate(startDate);
	    	  product.setEndDate(endDate);
	    	  product.setViewcategory(viewcategory);
	    	  product.setMinprice(minprice);
	    	  product.setMaxprice(maxprice);
	    	  product.setLikecount(likecount);
	    	  product.setLiked(liked);
	    	  product.setCategory(category);     
	    	  
	    	 
	    	  List productList = productService.getProductList(product);
	    	  List<NoticeBoard> noticeList = noticeService.getBoardList(notice);
	    	  
	    	  request.setAttribute("productList", productList);
	    	  request.setAttribute("currentPage", page);
	    	  request.setAttribute("totalPages", product.getTotalPages());
	    	  request.setAttribute("size", size);
	    	  request.setAttribute("product", product);
	    	  request.setAttribute("viewcategory", viewcategory);
	    	  request.setAttribute("minprice", minprice);
	    	  request.setAttribute("maxprice", maxprice);
	    	  request.setAttribute("likecount", likecount);
	    	  request.setAttribute("liked", liked);
	    	  request.setAttribute("noticeList", noticeList);
	    	  request.setAttribute("category", category);  
	    	  logger.info("공지 개수: " + noticeList.size());
	    	  request.getRequestDispatcher("/WEB-INF/jsp/product/list.jsp").forward(request, response);

	      }  else if ("/product/mylist.do".equals(path)) {
	    	  int page = request.getParameter("page") != null ?
	    			  Integer.parseInt(request.getParameter("page"))
	    			  : DEFAULT_PAGE;
	    	  int size = request.getParameter("size") != null ?
	    			  Integer.parseInt(request.getParameter("size"))
	    			  : DEFAULT_SIZE;
	    	  
	    	  
	    	  Product product = new Product(); 
	    	  
	    	  HttpSession session = request.getSession();
	    	  User user = (User) session.getAttribute("user");
	    	  
	    	  if (user == null) {
	    		    response.sendRedirect("/user/login.do"); // 또는 에러 페이지
	    		    return;
	    		}
	    	  
	    	  String endDate = request.getParameter("endDate");
	    	  String startDate = request.getParameter("startDate");
	    	  String searchText = request.getParameter("searchText");
	    	  String viewcategory = request.getParameter("viewcategory");
	    	  String minprice = request.getParameter("minprice");
	    	  String maxprice = request.getParameter("maxprice");
	    	  String likecount = request.getParameter("likecount");
	    	  String liked = request.getParameter("liked");
	    	  String sellstatus = request.getParameter("sellstatus");
	    	  String createId = ((User) session.getAttribute("user")).getUserId();
	    	  
	    	  product.setSize(size);
	    	  product.setPage(page);
	    	  product.setSearchText(searchText);
	    	  product.setStartDate(startDate);
	    	  product.setEndDate(endDate);
	    	  product.setViewcategory(viewcategory);
	    	  product.setMinprice(minprice);
	    	  product.setMaxprice(maxprice);
	    	  product.setLikecount(likecount);
	    	  product.setLiked(liked); 
	    	  product.setSellstatus(sellstatus);
	    	 
	    	  logger.info("여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기여기야여기"+sellstatus);
	    	  logger.info(product.toString());
	    	  List productList = productService.getMyProductList(product);
	    	  
	    	  request.setAttribute("productList", productList);
	    	  request.setAttribute("currentPage", page);
	    	  request.setAttribute("totalPages", product.getTotalPages());
	    	  request.setAttribute("size", size);
	    	  request.setAttribute("product", product);
	    	  request.setAttribute("viewcategory", viewcategory);
	    	  request.setAttribute("minprice", minprice);
	    	  request.setAttribute("maxprice", maxprice);
	    	  request.setAttribute("likecount", likecount);
	    	  request.setAttribute("liked", liked);
	    	  request.getRequestDispatcher("/WEB-INF/jsp/product/mylist.jsp").forward(request, response);

	      }
	}
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("ProductController doPost");
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
        PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
        JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
        try {
            logger.info("ProductController doPost path:" + path);
 
            if ("/product/create.do".equals(path)) { 
            	String title = request.getParameter("title"); 
            	String content= request.getParameter("content"); 
            	String viewCount = request.getParameter("viewCount"); 
            	String createId = request.getParameter("createId");
            	String price = request.getParameter("price");
            	String category = request.getParameter("category"); 
            	String sellstatus = request.getParameter("sellstatus");
            	Part photo= request.getPart("photo"); 
            	

            	// 파일명 추출
            	String photoFileName = null;
            	if (photo != null && photo.getSize() > 0) {
            	    photoFileName = Paths.get(photo.getSubmittedFileName()).getFileName().toString();

            	    String uploadPath = getServletContext().getRealPath("/upload");
            	    File uploadDir = new File(uploadPath);
            	    if (!uploadDir.exists()) uploadDir.mkdirs();

            	    photo.write(uploadPath + File.separator + photoFileName);
            	}
            	
            	Product product = new Product();
            	product.setTitle(title);
                product.setContent(content);
                product.setViewCount(viewCount); 
                product.setCreateId(createId);
                product.setPrice(price);
                product.setCategory(category);
                product.setPhoto(photoFileName);
                product.setSellstatus(sellstatus);

             
            	boolean isCreate = productService.createProduct(product, request);
                jsonResponse.put("success", isCreate);
            	jsonResponse.put("message", isCreate ? "게시글이 성공적으로 등록되었습니다." : "등록 실패");
            	
            }  else if ("/product/update.do".equals(path)) { 
            	String productId = request.getParameter("productId"); 
            	String title = request.getParameter("title"); 
            	String content= request.getParameter("content"); 
            	String viewCount = request.getParameter("viewCount"); 
            	String updateId = request.getParameter("updateId"); 
            	String price = request.getParameter("price");
            	String category = request.getParameter("category"); 
            	String sellstatus = request.getParameter("sellstatus");
            	Part photo= request.getPart("photo"); 
            	
            	// 파일명 추출
            	String photoFileName = null;
            	if (photo != null && photo.getSize() > 0) {
            	    photoFileName = Paths.get(photo.getSubmittedFileName()).getFileName().toString();

            	    String uploadPath = getServletContext().getRealPath("/upload");
            	    File uploadDir = new File(uploadPath);
            	    if (!uploadDir.exists()) uploadDir.mkdirs();

            	    photo.write(uploadPath + File.separator + photoFileName);
            	}
            	
            	Product product = new Product();
            	product.setProductId(productId);
            	product.setTitle(title);
            	product.setContent(content);
            	product.setViewCount(viewCount); 
            	product.setUpdateId(updateId);
            	product.setPrice(price);
                product.setCategory(category);
                product.setPhoto(photoFileName); 
                product.setSellstatus(sellstatus);
                logger.info(product.toString());
                
            	boolean isUpdate = productService.updateProduct(product, request);
                jsonResponse.put("success", isUpdate);
            	jsonResponse.put("message", isUpdate ? "게시글이 성공적으로 수정되었습니다." : "등록 실패");
            	
            }    else if ("/product/updatelike.do".equals(path)) { 
            	String productId = request.getParameter("productId");
                String updateId = request.getParameter("updateId");
                String likecount = request.getParameter("likecount");
                String liked = request.getParameter("liked");
            	
                Product product = new Product();
                product.setProductId(productId);
                product.setUpdateId(updateId);
                product.setLikecount(likecount);
                product.setLiked(liked);
                
                boolean isUpdate = productService.updateProductLike(product); // <- 이 메서드 필요
                jsonResponse.put("success", isUpdate);
                jsonResponse.put("message", isUpdate ? "좋아요 반영됨" : "DB 실패");
 
            	
            }   else if ("/product/delete.do".equals(path)) { 
            	String productId = request.getParameter("productId"); 
            	String updateId = request.getParameter("updateId"); 
            	
            	Product product = new Product();
            	product.setProductId(productId);
            	product.setUpdateId(updateId);
                 
            	boolean isDelete = productService.deleteProduct(product);
                jsonResponse.put("success", isDelete);
            	jsonResponse.put("message", isDelete ? "게시글이 성공적으로 삭제되었습니다." : "등록 실패");
            	
            }   else if ("/product/comment/create.do".equals(path)) { 
            	
            	int productId = Integer.parseInt(request.getParameter("productId")); 
            	String content = request.getParameter("content"); 
            	String createId = request.getParameter("createId"); 
            	int parentCommentId = Integer.parseInt(request.getParameter("parentCommentId")); 
            	
            	Comment comment = new Comment();
            	comment.setProductId(productId);
            	comment.setContent(content);
            	comment.setCreateId(createId);
            	comment.setParentCommentId(parentCommentId);
                 
            	boolean isSuccess = productService.createComment(comment);
                jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ? "댓글 생성 성공" : "댓글 생성 실패");
            	
            }   else if ("/product/comment/update.do".equals(path)) { 
            	
            	int commentId = Integer.parseInt(request.getParameter("commentId")); 
            	String content = request.getParameter("content"); 
            	String updateId = request.getParameter("updateId"); 
            	
            	Comment comment = new Comment();
            	comment.setCommentId(commentId);
            	comment.setContent(content);
            	comment.setUpdateId(updateId);
                 
            	boolean isSuccess = productService.updateComment(comment);
                jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ? "댓글 수정 성공" : "댓글 수정 실패");
            	
            }   else if ("/product/comment/delete.do".equals(path)) { 
            	
            	int commentId = Integer.parseInt(request.getParameter("commentId")); 
            	String updateId = request.getParameter("updateId"); 
            	
            	Comment comment = new Comment();
            	comment.setCommentId(commentId);
            	comment.setUpdateId(updateId);
                 
            	boolean isSuccess = productService.deleteComment(comment);
                jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ? "댓글 삭제 성공" : "댓글 삭제 실패");
            	
            }
        } catch (Exception e) {
            jsonResponse.put("success", false); // 오류 발생 시
            jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
            logger.error("Error in ProductController doPost", e); // 오류 로그 추가
        }
        
        logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
        // JSON 응답 출력
        out.print(jsonResponse.toString());
        out.flush();
	}

}
