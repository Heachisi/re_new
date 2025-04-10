package controller.share;

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
import jakarta.servlet.http.Part;
import model.share.Comment;
import model.share.Share;
import service.share.ShareService;
import service.share.ShareServiceImpl;


@WebServlet("/share/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class ShareController extends HttpServlet {
 
	private static final long serialVersionUID = 5726041575375068755L;
	private static final Logger logger = LogManager.getLogger(ShareController.class); 
	private ShareService shareService;
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 12;
	
	public ShareController() {
        super();
        shareService = new ShareServiceImpl(); 
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  logger.info("ShareController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("ShareController doGet path" + path); 
	      
	      //나중에 게시글 목록 나오면 사용될거임
	      if ("/share/view.do".equals(path)) {
	    	    String shareId = request.getParameter("id");
	    	    Share share = shareService.getShareById(shareId);
	    	    
	    	    request.setAttribute("share", share);
	            request.getRequestDispatcher("/WEB-INF/jsp/share/view.jsp").forward(request, response);
	      } else if ("/share/create.do".equals(path)) {
	    	  request.getRequestDispatcher("/WEB-INF/jsp/share/create.jsp").forward(request, response);
	      } else if ("/share/update.do".equals(path)) {
	    	   
	    	  String shareId = request.getParameter("id");
	    	    Share share = shareService.getShareById(shareId);
	    	    request.setAttribute("share", share);
	    	    
	            request.getRequestDispatcher("/WEB-INF/jsp/share/update.jsp").forward(request, response);
	      }  else if ("/share/list.do".equals(path)) {
	    	  int page = request.getParameter("page") != null ?
	    			  Integer.parseInt(request.getParameter("page"))
	    			  : DEFAULT_PAGE;
	    	  int size = request.getParameter("size") != null ?
	    			  Integer.parseInt(request.getParameter("size"))
	    			  : DEFAULT_SIZE;
	    	
	    	  
	    	  Share share = new Share();
	    	  
	    	  String endDate = request.getParameter("endDate");
	    	  String startDate = request.getParameter("startDate");
	    	  String searchText1 = request.getParameter("searchText");

	    	  
	    	  share.setSize(size);
	    	  share.setPage(page);
	    	  share.setSearchText(searchText1);
	    	  share.setStartDate(startDate);
	    	  share.setEndDate(endDate); 	 
	    	  List shareList = shareService.getShareList(share); 
	    	  
	    	  request.setAttribute("shareList", shareList);
	    	  request.setAttribute("searchText", searchText1);
	    	  request.setAttribute("currentPage", page);
	    	  request.setAttribute("totalPages", share.getTotalPages());
	    	  request.setAttribute("size", size);
	    	  request.setAttribute("share", share);
	    	  request.getRequestDispatcher("/WEB-INF/jsp/share/list.jsp").forward(request, response);
	    	  logger.info("searchText: " + share.getSearchText());
	    	  logger.info("공지 개수: " + shareList.size());

	      }  
	}
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("ShareController doPost");
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
        PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
        JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
        try {
            logger.info("ShareController doPost path:" + path);
 
            if ("/share/create.do".equals(path)) { 
            	String title = request.getParameter("title"); 
            	String content= request.getParameter("content"); 
            	String viewcount = request.getParameter("viewcount"); 
            	String createId = request.getParameter("createId");
            	
            	
            	Share share = new Share();
            	share.setTitle(title);
                share.setContent(content);
                share.setViewcount(viewcount); 
                share.setCreateId(createId);

            	boolean isCreate = shareService.createShare(share, request);
                jsonResponse.put("success", isCreate);
            	jsonResponse.put("message", isCreate ? "게시글이 성공적으로 등록되었습니다." : "등록 실패");
            	
            }  else if ("/share/update.do".equals(path)) { 
            	String shareId = request.getParameter("shareId"); 
            	String title = request.getParameter("title"); 
            	String content= request.getParameter("content"); 
            	String viewcount = request.getParameter("viewcount"); 
            	String updateId = request.getParameter("updateId");
            
            	Share share = new Share();
            	share.setShareId(shareId);
            	share.setTitle(title);
            	share.setContent(content);
            	share.setViewcount(viewcount); 
            	share.setUpdateId(updateId);
                logger.info(share.toString());
                
            	boolean isUpdate = shareService.updateShare(share, request);
                jsonResponse.put("success", isUpdate);
            	jsonResponse.put("message", isUpdate ? "게시글이 성공적으로 수정되었습니다." : "등록 실패");
            	
            }    else if ("/share/delete.do".equals(path)) { 
            	String shareId = request.getParameter("shareId"); 
            	String updateId = request.getParameter("updateId"); 
            	
            	Share share = new Share();
            	share.setShareId(shareId);
            	share.setUpdateId(updateId);
                 
            	boolean isDelete = shareService.deleteShare(share);
                jsonResponse.put("success", isDelete);
            	jsonResponse.put("message", isDelete ? "게시글이 성공적으로 삭제되었습니다." : "등록 실패");
            	
            }   else if ("/share/comment/create.do".equals(path)) { 
            	
            	int shareId = Integer.parseInt(request.getParameter("shareId")); 
            	String content = request.getParameter("content"); 
            	String createId = request.getParameter("createId"); 
            	int parentCommentId = Integer.parseInt(request.getParameter("parentCommentId")); 
            	
            	Comment comment = new Comment();
            	comment.setShareId(shareId);
            	comment.setContent(content);
            	comment.setCreateId(createId);
            	comment.setParentCommentId(parentCommentId);
                 
            	boolean isSuccess = shareService.createComment(comment);
                jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ? "댓글 생성 성공" : "댓글 생성 실패");
            	
            }   else if ("/share/comment/update.do".equals(path)) { 
            	
            	int commentId = Integer.parseInt(request.getParameter("commentId")); 
            	String content = request.getParameter("content"); 
            	String updateId = request.getParameter("updateId"); 
            	
            	Comment comment = new Comment();
            	comment.setCommentId(commentId);
            	comment.setContent(content);
            	comment.setUpdateId(updateId);
                 
            	boolean isSuccess = shareService.updateComment(comment);
                jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ? "댓글 수정 성공" : "댓글 수정 실패");
            	
            }   else if ("/share/comment/delete.do".equals(path)) { 
            	
            	int commentId = Integer.parseInt(request.getParameter("commentId")); 
            	String updateId = request.getParameter("updateId"); 
            	
            	Comment comment = new Comment();
            	comment.setCommentId(commentId);
            	comment.setUpdateId(updateId);
                 
            	boolean isSuccess = shareService.deleteComment(comment);
                jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ? "댓글 삭제 성공" : "댓글 삭제 실패");
            	
            }
        } catch (Exception e) {
            jsonResponse.put("success", false); // 오류 발생 시
            jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
            logger.error("Error in ShareController doPost", e); // 오류 로그 추가
        }
        
        logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
        // JSON 응답 출력
        out.print(jsonResponse.toString());
        out.flush();
	}

}
