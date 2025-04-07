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
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5726041575375068755L;
	private static final Logger logger = LogManager.getLogger(BoardController.class); 
	private BoardService boardService;
	
	
	public BoardController() {
        super();
        boardService = new BoardServiceImpl(); 
    }
	
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 15;
	
	/**
	 * GET 화면 이동용 및 조회용
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  logger.info("BoardController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("BoardController doGet path" + path); 
	      
	      if ("/board/view.do".equals(path)) {
	    	  
	    	  String boardId =request.getParameter("id");
	    	  Board board = boardService.getBoardById(boardId);
	    	  request.setAttribute("board", board);
	    	  
	          request.getRequestDispatcher("/WEB-INF/jsp/board/view.jsp").forward(request, response);
	      } else if("/board/create.do".equals(path)) {
	    	  request.getRequestDispatcher("/WEB-INF/jsp/board/create.jsp").forward(request, response);
	      } else if ("/board/update.do".equals(path)) {
	    	  
	    	  String boardId =request.getParameter("id");
	    	  Board board = boardService.getBoardById(boardId);
	    	  request.setAttribute("board", board);
	    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/board/update.jsp").forward(request, response);
	      }else if("/board/delete.do".equals(path)) {
		      String boardId =request.getParameter("id");
	    	  Board board = boardService.getBoardById(boardId);
	    	  request.setAttribute("board", board);
    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/board/delete.jsp").forward(request, response);
	      } else if ("/board/list.do".equals(path)) {
	    	  int page = request.getParameter("page") != null?
	    			  Integer.parseInt(request.getParameter("page"))
	    			  :DEFAULT_PAGE;
	    	  int size = request.getParameter("size") != null?
	    			  Integer.parseInt(request.getParameter("size"))
	    			  :DEFAULT_SIZE;
	    	  
	    	  String searchKey = request.getParameter("searchKey");
	    	  String searchQuery = request.getParameter("searchQuery");
	    	  if(searchQuery !=null) {
	    		  searchQuery = searchQuery.replace("\"","");
	    	  }
	    	  
	    	  Board board = new Board();
	    	  board.setSize(size);
	    	  board.setPage(page);
	    	  
	    	  if(searchKey == null ||searchQuery == null) {
	    		  searchKey = "";
	    		  searchQuery = "";
	    		  }
	    	  
	    	  board.setSearchKey(searchKey);
	    	  board.setSearchQuery(searchQuery);
	    	  
	    	  List boardList = boardService.getBoardList(board);
	    	  
	    	  int totalPosts = board.getTotalCount();  // 전체 게시글 수 가져오기
//	    	    int totalPages = (totalPosts + size - 1) / size;  // 총 페이지 수 계산
//	    	    if (totalPosts < size) {
//	    	        totalPages = 1;  // 15개 미만이면 페이지네이션을 1로 설정
//	    	    }
	    	  int totalPages = 0;
	    	  if(totalPosts>0) {
	    		  totalPages= (totalPosts+size-1)/size;
	    	  }
	    	  
	    	  request.setAttribute("boardList", boardList);
	    	  request.setAttribute("currentPage", page);
	    	  request.setAttribute("totalPages", board.getTotalPages());
	    	  request.setAttribute("size", size);
	    	  request.setAttribute("searchKey", searchKey);
	    	  request.setAttribute("searchQuery", searchQuery);
	    	  
	    	  logger.info("searchKey: " + searchKey);
	    	  logger.info("searchQuery: " + searchQuery);
	    	  
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
            
            if ("/board/create.do".equals(path)) { 
            	String title =request.getParameter("title");
            	String content =request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String createId =request.getParameter("createId");
            	
            	Board board = new Board();
            	board.setTitle(title);
            	board.setContent(content);
            	board.setViewCount(viewCount);
            	board.setCreateId(createId);

            	boolean isCreate= boardService.createBoard(board, request);
            	jsonResponse.put("success", isCreate);
            	jsonResponse.put("message", isCreate ?
            			"게시글이 성공적으로 등록되었습니다." : "게시글 등록이 실패하였습니다.");
            			
            } else if ("/board/update.do".equals(path)) { 
            	String boardId=request.getParameter("boardId");
            	String title =request.getParameter("title");
            	String content =request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String updateId =request.getParameter("updateId");
            	
            	Board board = new Board();
            	board.setBoardId(boardId);
            	board.setTitle(title);
            	board.setContent(content);
            	board.setViewCount(viewCount);
            	board.setUpdateId(updateId);

            	boolean isUpdate= boardService.updateBoard(board,request);
            	jsonResponse.put("success", isUpdate);
            	jsonResponse.put("message", isUpdate ?
            			"게시글이 성공적으로 수정되었습니다." : "게시글 수정에 실패하였습니다.");
            			
            } 
            else if ("/board/delete.do".equals(path)) { 
            	String boardId=request.getParameter("boardId");
            	String updateId=request.getParameter("updateId");

            	Board board = new Board();
            	board.setBoardId(boardId);
            	board.setUpdateId(updateId);

            	boolean isDelete= boardService.deleteBoard(board);
            	jsonResponse.put("success", isDelete);
            	jsonResponse.put("message", isDelete ?
            			"게시글이 성공적으로 삭제되었습니다." : "게시글 삭제에 실패하였습니다.");
            }else if("/board/comment/create.do".equals(path)) {
            	int boardId =Integer.parseInt(request.getParameter("boardId"));
            	String content =request.getParameter("content");
            	String createId =request.getParameter("createId");
            	int parentCommentId =Integer.parseInt(request.getParameter("parentCommentId"));
            	
            	Comment comment = new Comment();
            	comment.setBoardId(boardId);
            	comment.setContent(content);
            	comment.setCreateId(createId);
            	comment.setParentCommentId(parentCommentId);

            	boolean isSuccess= boardService.createComment(comment);//댓글 등록
            	jsonResponse.put("success", isSuccess);//성공여부
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 등록되었습니다." : "댓글 등록에 실패하였습니다.");//응답메세지
            }else if("/board/comment/update.do".equals(path)) {
            	int commentId =Integer.parseInt(request.getParameter("commentId"));
            	String content =request.getParameter("content");
            	String updateId =request.getParameter("updateId");

            	Comment comment = new Comment();
            	comment.setContent(content);
            	comment.setUpdateId(updateId);
            	comment.setCommentId(commentId);

            	boolean isSuccess= boardService.updateComment(comment);
            	jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 수정되었습니다." : "댓글 수정에 실패하였습니다.");
            }else if("/board/comment/delete.do".equals(path)) {
            	int commentId =Integer.parseInt(request.getParameter("commentId"));
            	String updateId =request.getParameter("updateId");

            	
            	Comment comment = new Comment();
            	comment.setUpdateId(updateId);
            	comment.setCommentId(commentId);

            	boolean isSuccess= boardService.deleteComment(comment);
            	jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 삭제되었습니다." : "댓글 삭제에 실패하였습니다.");
            			
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
