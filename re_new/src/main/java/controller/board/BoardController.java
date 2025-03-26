package controller.board;

import java.io.IOException;
import java.io.PrintWriter;
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
import model.board.Board;
import model.board.Comment;
import service.board.BoardService;
import service.board.BoardServiceImpl;


@WebServlet("/board/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class BoardController extends HttpServlet {

	private static final long serialVersionUID = 2531063948418577462L;
	private static final Logger logger = LogManager.getLogger(BoardController.class); 
	private BoardService boardService;
	
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 10;
	
	
	public BoardController() {
        super();
        boardService = new BoardServiceImpl(); 
    }	
	/**
	 * GET 화면 이동용 및 조회용
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  // 로그 기록: doGet 메서드 호출 시 정보 기록 
		  logger.info("BoardController doGet"); 
		  // 요청된 URI를 가져와 path 변수에 저장
	      String path = request.getRequestURI();
	      logger.info("BoardController doGet path" + path); 
	   // 요청된 path에 따라 다른 처리를 수행

	      if ("/board/view.do".equals(path)) {
	    	  
	  
	    	    String boardId = request.getParameter("id");
	    	    Board board = boardService.getBoardById(boardId);
	    	    request.setAttribute("board", board);
	    	    
	            request.getRequestDispatcher("/WEB-INF/jsp/board/view.jsp").forward(request, response);
	           
	      } else if ("/board/create.do".equals(path)) {
	    	
	            request.getRequestDispatcher("/WEB-INF/jsp/board/create.jsp").forward(request, response);
	           
	      } else if ("/board/update.do".equals(path)) {
	    	  
	    	  
	    	    String boardId = request.getParameter("id");
	    	    Board board = boardService.getBoardById(boardId);
	    	    request.setAttribute("board", board);
	    	    
	            request.getRequestDispatcher("/WEB-INF/jsp/board/update.jsp").forward(request, response);
	           
	      } else if("/board/list.do".equals(path)) {
	    	  int page = request.getParameter("page") != null ?
	    			  Integer.parseInt(request.getParameter("page"))
	    			  :DEFAULT_PAGE;
	    	  int size = request.getParameter("size") != null ?
	    			  Integer.parseInt(request.getParameter("size"))
	    			  :DEFAULT_SIZE;
	    	  Board board = new Board();
	    	  board.setSize(size);
	    	  board.setPage(page);
	    	  
	    	  List boardList = boardService.getBoardList(board);
	    	  
	    	  request.setAttribute("boardList", boardList);
	    	  request.setAttribute("currentPage", page);
	    	  request.setAttribute("totalPages", board.getTotalPages());
	    	  request.setAttribute("size", size);
	    	    
	          request.getRequestDispatcher("/WEB-INF/jsp/board/list.jsp").forward(request, response);
	      }
	}

	/**
	 * POST ajax 로직 처리용 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("BoardController doPost");
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
        PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
        JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
        
        try {
            logger.info("BoardController doPost path: " + path);
            //유저 가입시 처리
 
            if ("/board/create.do".equals(path)) { 
            	String title = request.getParameter("title");
            	String content = request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String createId = request.getParameter("createId");
            	
              
            	
                Board board = new Board();
                board.setTitle(title);
                board.setContent(content);
                board.setViewCount(viewCount); 
                board.setCreateId(createId);
    
         
                logger.info("board.toString() "+board.toString());
                boolean isCreate = boardService.createBoard(board, request);
                jsonResponse.put("success", isCreate);
                jsonResponse.put("message", isCreate ?
                		"게시글이 성공적으로 등록되었습니다." : "게시글 등록 실패");
                
               
            } else if ("/board/update.do".equals(path)) {
            	String boardId = request.getParameter("boardId");
            	String title = request.getParameter("title");
            	String content = request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String updateId = request.getParameter("updateId");
            	
              
            	
                Board board = new Board();
                board.setBoardId(boardId);
                board.setTitle(title);
                board.setContent(content);
                board.setViewCount(viewCount); 
                board.setUpdateId(updateId);
    
                logger.info("board.toString() "+board.toString());
                boolean isUpdate = boardService.updateBoard(board, request);
                jsonResponse.put("success", isUpdate);
                jsonResponse.put("message", isUpdate ?
                		"게시글이 성공적으로 등록되었습니다." : "게시글 등록 실패");
                
               
            } else if ("/board/delete.do".equals(path)) {
            	String boardId = request.getParameter("boardId");
            	String updateId = request.getParameter("updateId");
            	
                Board board = new Board();
                board.setBoardId(boardId);
                board.setUpdateId(updateId);
    
                boolean isDelete = boardService.deleteBoard(board);
                jsonResponse.put("success", isDelete);
                jsonResponse.put("message", isDelete ?
                		"게시글이 성공적으로 삭제되었습니다." : "게시글 삭제 실패");
                
               
            } else if ("/board/comment/create.do".equals(path)) {
                int boardId = Integer.parseInt(request.getParameter("boardId"));
                String content = request.getParameter("content");
                String createId = request.getParameter("createId");
                int parentCommentId = Integer.parseInt(request.getParameter("parentCommentId"));

                Comment comment = new Comment();
                comment.setBoardId(boardId);
                comment.setContent(content);
                comment.setCreateId(createId);
                comment.setParentCommentId(parentCommentId);

                boolean isSuccess = boardService.createComment(comment); // 댓글 등록
                jsonResponse.put("success", isSuccess); // 성공 여부
                jsonResponse.put("message", isSuccess ? "댓글 생성 성공" : "댓글 생성 실패"); // 응답 메시지
            } else if ("/board/comment/update.do".equals(path)) {
                int commentId = Integer.parseInt(request.getParameter("commentId"));
                String content = request.getParameter("content");
                String updateId = request.getParameter("updateId");

                Comment comment = new Comment();
                comment.setContent(content);
                comment.setUpdateId(updateId);
                comment.setCommentId(commentId);

                boolean isSuccess = boardService.updateComment(comment); // 댓글 등록
                jsonResponse.put("success", isSuccess); // 성공 여부
                jsonResponse.put("message", isSuccess ? "댓글 수정 성공" : "댓글 수정 실패"); // 응답 메시지
            } else if ("/board/comment/delete.do".equals(path)) {
                int commentId = Integer.parseInt(request.getParameter("commentId"));
                String updateId = request.getParameter("updateId");

                Comment comment = new Comment();
                comment.setUpdateId(updateId);
                comment.setCommentId(commentId);

                boolean isSuccess = boardService.deleteComment(comment); // 댓글 등록
                jsonResponse.put("success", isSuccess); // 성공 여부
                jsonResponse.put("message", isSuccess ? "댓글 삭제 성공" : "댓글 삭제 실패"); // 응답 메시지
            }

            
        } catch (Exception e) {
                jsonResponse.put("success", false); // 오류 발생 시
                jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
                logger.error("Error in BoardController doPost", e); // 오류 로그 추가
            }
            
            logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
            // JSON 응답 출력
            out.print(jsonResponse.toString());
            out.flush();
	}
}

    