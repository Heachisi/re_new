package controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
import model.board.NoticeBoard;
import model.board.NoticeComment;
import service.board.NoticeBoardService;
import service.board.NoticeBoardServiceImpl;


@WebServlet("/noticeBoard/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class NoticeBoardController extends HttpServlet {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5726041575375068755L;
	private static final Logger logger = LogManager.getLogger(NoticeBoardController.class); 
	private NoticeBoardService noticeBoardService;
	
	
	public NoticeBoardController() {
        super();
        noticeBoardService = new NoticeBoardServiceImpl(); 
    }
	
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 15;
	
	/**
	 * GET 화면 이동용 및 조회용
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  logger.info("NoticeBoardController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("NoticeBoardController doGet path" + path); 
	      
	      if ("/noticeBoard/noticeView.do".equals(path)) {
	    	  String boardId =request.getParameter("id");
	    	  
	    	  HttpSession session = request.getSession();
	    	    String viewKey = "viewed_" + boardId;
	    	    if (session.getAttribute(viewKey) == null) {
	    	        noticeBoardService.increaseViewCount(boardId); // 조회수 증가
	    	        session.setAttribute(viewKey, true);     // 세션에 조회한 거 표시
	    	    }
	    	  NoticeBoard noticeBoard = noticeBoardService.getBoardById(boardId);
	    	  List<NoticeComment> sortedComments = noticeBoard.getComments().stream()
	    		        .sorted(Comparator.comparing(NoticeComment::getCreateDt))
	    		        .collect(Collectors.toList());

	    		    noticeBoard.setComments(sortedComments); 
	    	  request.setAttribute("noticeBoard", noticeBoard);
	    	  
	    	  logger.info("불러온 게시글: "+noticeBoard);
	    	  
	          request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeView.jsp").forward(request, response);
	      } else if("/noticeBoard/noticeCreate.do".equals(path)) {
	    	  request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeCreate.jsp").forward(request, response);
	    	  
	      } else if ("/noticeBoard/noticeUpdate.do".equals(path)) {
	    	  
	    	  String boardId =request.getParameter("id");
	    	  NoticeBoard noticeBoard = noticeBoardService.getBoardById(boardId);
	    	  request.setAttribute("noticeBoard", noticeBoard);
	    	  request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeUpdate.jsp").forward(request, response);
	    	  
	      }else if("/noticeBoard/noticeDelete.do".equals(path)) {
		      String boardId =request.getParameter("id");
		      NoticeBoard noticeBoard = noticeBoardService.getBoardById(boardId);
	    	  request.setAttribute("noticeBoard", noticeBoard);
	    	  request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeDelete.jsp").forward(request, response);
	    	  
	      } else if ("/noticeBoard/noticeList.do".equals(path)) {
	    	  int page = request.getParameter("page") != null?
	    			  Integer.parseInt(request.getParameter("page"))
	    			  :DEFAULT_PAGE;
	    	  int size = request.getParameter("size") != null?
	    			  Integer.parseInt(request.getParameter("size"))
	    			  :DEFAULT_SIZE;
	    	  
	    	  String searchText = request.getParameter("searchText");
	    	  String startDate = request.getParameter("startDate");
	    	  String endDate = request.getParameter("endDate");
	    	  
	    	  logger.info("검색어: " + searchText);
	    	  logger.info("시작 날짜: " + startDate);
	    	  logger.info("종료 날짜: " + endDate);
	    	  
	    	  NoticeBoard noticeBoard = new NoticeBoard();
	    	  noticeBoard.setSize(size);
	    	  noticeBoard.setPage(page);
	    	  noticeBoard.setSearchText(searchText);
	    	  noticeBoard.setStartDate(startDate);
	    	  noticeBoard.setEndDate(endDate);
	    	  
	    	  List<NoticeBoard> noticeBoardList = noticeBoardService.getBoardList(noticeBoard);
	    	  
	    	  request.setAttribute("noticeBoardList", noticeBoardList);
	    	  request.setAttribute("currentPage", page);
	    	  request.setAttribute("totalPages", noticeBoard.getTotalPages());
	    	  request.setAttribute("size", size);
	    	  
	    	  request.setAttribute("noticeBoard", noticeBoard);
	    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeList.jsp").forward(request, response);
	      }
	}

	/**
	 * POST ajax 로직 처리용 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("NoticeBoardController doPost");
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
        PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
        JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
        try {
            logger.info("NoticeBoardController doPost path: " + path);
            
            if ("/noticeBoard/noticeCreate.do".equals(path)) { 
            	String title =request.getParameter("title");
            	String content =request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String createId =request.getParameter("createId");
            	
            	NoticeBoard noticeBoard = new NoticeBoard();
            	noticeBoard.setTitle(title);
            	noticeBoard.setContent(content);
            	noticeBoard.setViewCount(viewCount);
            	noticeBoard.setCreateId(createId);

            	System.out.println("📌 게시글 생성 요청");
                System.out.println("게시글 제목: " + noticeBoard.getTitle());
                System.out.println("게시글 내용: " + noticeBoard.getContent());
                System.out.println("조회수: " + noticeBoard.getViewCount());
                System.out.println("작성자 ID: " + noticeBoard.getCreateId());
            	
            	boolean isCreate= noticeBoardService.createBoard(noticeBoard, request);
            	jsonResponse.put("success", isCreate);
            	jsonResponse.put("message", isCreate ?
            			"게시글이 성공적으로 등록되었습니다." : "게시글 등록이 실패하였습니다.");
            			
            } else if ("/noticeBoard/noticeUpdate.do".equals(path)) { 
            	String boardId=request.getParameter("boardId");
            	String title =request.getParameter("title");
            	String content =request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String updateId =request.getParameter("updateId");
            	
            	NoticeBoard noticeBoard = new NoticeBoard();
            	noticeBoard.setBoardId(boardId);
            	noticeBoard.setTitle(title);
            	noticeBoard.setContent(content);
            	noticeBoard.setViewCount(viewCount);
            	noticeBoard.setUpdateId(updateId);

            	boolean isUpdate= noticeBoardService.updateBoard(noticeBoard,request);
            	jsonResponse.put("success", isUpdate);
            	jsonResponse.put("message", isUpdate ?
            			"게시글이 성공적으로 수정되었습니다." : "게시글 수정에 실패하였습니다.");
            			
            } 
            else if ("/noticeBoard/noticeDelete.do".equals(path)) { 
            	String boardId=request.getParameter("boardId");
            	String updateId=request.getParameter("updateId");

            	NoticeBoard noticeBoard = new NoticeBoard();
            	noticeBoard.setBoardId(boardId);
            	noticeBoard.setUpdateId(updateId);

            	boolean isDelete= noticeBoardService.deleteBoard(noticeBoard);
            	jsonResponse.put("success", isDelete);
            	jsonResponse.put("message", isDelete ?
            			"게시글이 성공적으로 삭제되었습니다." : "게시글 삭제에 실패하였습니다.");
          
            
            
            
            }else if("/noticeBoard/noticeComment/create.do".equals(path)) {
            	int boardId =Integer.parseInt(request.getParameter("boardId"));
            	String content =request.getParameter("content");
            	String createId =request.getParameter("createId");
            	int parentCommentId =Integer.parseInt(request.getParameter("parentCommentId"));
            	
            	NoticeComment noticeComment = new NoticeComment();
            	noticeComment.setBoardId(boardId);
            	noticeComment.setContent(content);
            	noticeComment.setCreateId(createId);
            	noticeComment.setParentCommentId(parentCommentId);
            	
            	logger.info("boardId: "+boardId);
            	logger.info("content: "+content);
            	logger.info("createId: "+createId);
            	logger.info("parentCommentId: "+parentCommentId);
            	
            	
            	boolean isSuccess= noticeBoardService.createComment(noticeComment);//댓글 등록
            	jsonResponse.put("success", isSuccess);//성공여부
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 등록되었습니다." : "댓글 등록에 실패하였습니다.");//응답메세지
            	
            }else if("/noticeBoard/noticeComment/update.do".equals(path)) {
            	int commentId =Integer.parseInt(request.getParameter("commentId"));
            	String content =request.getParameter("content");
            	String updateId =request.getParameter("updateId");

            	NoticeComment noticeComment = new NoticeComment();
            	noticeComment.setContent(content);
            	noticeComment.setUpdateId(updateId);
            	noticeComment.setCommentId(commentId);

            	boolean isSuccess= noticeBoardService.updateComment(noticeComment);
            	jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 수정되었습니다." : "댓글 수정에 실패하였습니다.");
            }else if("/noticeBoard/comment/delete.do".equals(path)) {
            	int commentId =Integer.parseInt(request.getParameter("commentId"));
            	String updateId =request.getParameter("updateId");

            	
            	NoticeComment noticeComment = new NoticeComment();
            	noticeComment.setUpdateId(updateId);
            	noticeComment.setCommentId(commentId);

            	boolean isSuccess= noticeBoardService.deleteComment(noticeComment);
            	jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 삭제되었습니다." : "댓글 삭제에 실패하였습니다.");
            			
            }
        } catch (Exception e) {
            jsonResponse.put("success", false); // 오류 발생 시
            jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
            logger.error("Error in NoticeBoardController doPost", e); // 오류 로그 추가
        }
        
        logger.info("jsonResponse.toString() : "+ jsonResponse.toString()); 
        // JSON 응답 출력
        out.print(jsonResponse.toString());
        out.flush();
	}
	
}
