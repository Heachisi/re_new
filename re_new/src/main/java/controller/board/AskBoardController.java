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
import model.board.AskBoard;
import model.board.AskComment;
import service.board.AskBoardService;
import service.board.AskBoardServiceImpl;


@WebServlet("/askboard/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 10 * 1024 * 1024,  // 10MB
        maxRequestSize = 50 * 1024 * 1024 // 50MB
)
public class AskBoardController extends HttpServlet {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5726041575375068755L;
	private static final Logger logger = LogManager.getLogger(AskBoardController.class); 
	private AskBoardService boardService;
	
	
	public AskBoardController() {
        super();
        AskBoardServiceImpl boardService = new AskBoardServiceImpl(); 
    }
	
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 15;
	
	/**
	 * GET 화면 이동용 및 조회용
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  logger.info("AskBoardController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("AskBoardController doGet path" + path); 
	      
	      if ("/askboard/askView.do".equals(path)) {
	    	  String boardId =request.getParameter("id");
	    	  
	    	  HttpSession session = request.getSession();
	    	    String viewKey = "viewed_" + boardId;
	    	    if (session.getAttribute(viewKey) == null) {
	    	        boardService.increaseViewCount(boardId); // 조회수 증가
	    	        session.setAttribute(viewKey, true);     // 세션에 조회한 거 표시
	    	    }
	    	  AskBoard board = boardService.getBoardById(boardId);
	    	  List<AskComment> sortedComments = board.getComments().stream()
	    		        .sorted(Comparator.comparing(AskComment::getCreateDt))
	    		        .collect(Collectors.toList());

	    		    board.setComments(sortedComments); 
	    	  request.setAttribute("board", board);
	    	  
	    	  logger.info("불러온 게시글: "+board);
	    	  
	          request.getRequestDispatcher("/WEB-INF/jsp/askBoard/askView.jsp").forward(request, response);
	      } else if("/askboard/askCreate.do".equals(path)) {
	    	
	    	  request.getRequestDispatcher("/WEB-INF/jsp/askBoard/askCreate.jsp").forward(request, response);
	      } else if ("/askboard/askUpdate.do".equals(path)) {
	    	  
	    	  String boardId =request.getParameter("id");
	    	  AskBoard board = boardService.getBoardById(boardId);
	    	  request.setAttribute("board", board);
	    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/askBoard/askUpdate.jsp").forward(request, response);
	      }else if("/askboard/askDelete.do".equals(path)) {
		      String boardId =request.getParameter("id");
		      AskBoard board = boardService.getBoardById(boardId);
	    	  request.setAttribute("board", board);
    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/askBoard/askDelete.jsp").forward(request, response);
	      } else if ("/askboard/askList.do".equals(path)) {
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
	    	  
	    	  AskBoard board = new AskBoard();
	    	  board.setSize(size);
	    	  board.setPage(page);
	    	  board.setSearchText(searchText);
	    	  board.setStartDate(startDate);
	    	  board.setEndDate(endDate);
	    	  
	    	  List<AskBoard> boardList = boardService.getBoardList(board);
	    	  
	    	  request.setAttribute("boardList", boardList);
	    	  request.setAttribute("currentPage", page);
	    	  request.setAttribute("totalPages", board.getTotalPages());
	    	  request.setAttribute("size", size);
	    	  
	    	  request.setAttribute("board", board);
	    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/askBoard/askList.jsp").forward(request, response);
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
            
            if ("/askboard/askCreate.do".equals(path)) { 
            	String title =request.getParameter("title");
            	String content =request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String createId =request.getParameter("createId");
            	
            	AskBoard board = new AskBoard();
            	board.setTitle(title);
            	board.setContent(content);
            	board.setViewCount(viewCount);
            	board.setCreateId(createId);

            	System.out.println("📌 게시글 생성 요청");
                System.out.println("게시글 제목: " + board.getTitle());
                System.out.println("게시글 내용: " + board.getContent());
                System.out.println("조회수: " + board.getViewCount());
                System.out.println("작성자 ID: " + board.getCreateId());
            	
            	boolean isCreate= boardService.createBoard(board, request);
            	jsonResponse.put("success", isCreate);
            	jsonResponse.put("message", isCreate ?
            			"게시글이 성공적으로 등록되었습니다." : "게시글 등록이 실패하였습니다.");
            			
            } else if ("/askboard/askUpdate.do".equals(path)) { 
            	String boardId=request.getParameter("boardId");
            	String title =request.getParameter("title");
            	String content =request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String updateId =request.getParameter("updateId");
            	
            	AskBoard board = new AskBoard();
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
            else if ("/askboard/askDelete.do".equals(path)) { 
            	String boardId=request.getParameter("boardId");
            	String updateId=request.getParameter("updateId");

            	AskBoard board = new AskBoard();
            	board.setBoardId(boardId);
            	board.setUpdateId(updateId);

            	boolean isDelete= boardService.deleteBoard(board);
            	jsonResponse.put("success", isDelete);
            	jsonResponse.put("message", isDelete ?
            			"게시글이 성공적으로 삭제되었습니다." : "게시글 삭제에 실패하였습니다.");
          
            
            
            
            }else if("/askboard/comment/create.do".equals(path)) {
            	int boardId =Integer.parseInt(request.getParameter("boardId"));
            	String content =request.getParameter("content");
            	String createId =request.getParameter("createId");
            	int parentCommentId =Integer.parseInt(request.getParameter("parentCommentId"));
            	
            	AskComment comment = new AskComment();
            	comment.setBoardId(boardId);
            	comment.setContent(content);
            	comment.setCreateId(createId);
            	comment.setParentCommentId(parentCommentId);
            	
            	logger.info("boardId: "+boardId);
            	logger.info("content: "+content);
            	logger.info("createId: "+createId);
            	logger.info("parentCommentId: "+parentCommentId);
            	
            	
            	
            	boolean isSuccess= boardService.createComment(comment);//댓글 등록
            	jsonResponse.put("success", isSuccess);//성공여부
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 등록되었습니다." : "댓글 등록에 실패하였습니다.");//응답메세지
            }else if("/askboard/comment/update.do".equals(path)) {
            	int commentId =Integer.parseInt(request.getParameter("commentId"));
            	String content =request.getParameter("content");
            	String updateId =request.getParameter("updateId");

            	AskComment comment = new AskComment();
            	comment.setContent(content);
            	comment.setUpdateId(updateId);
            	comment.setCommentId(commentId);

            	boolean isSuccess= boardService.updateComment(comment);
            	jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 수정되었습니다." : "댓글 수정에 실패하였습니다.");
            }else if("/askboard/comment/delete.do".equals(path)) {
            	int commentId =Integer.parseInt(request.getParameter("commentId"));
            	String updateId =request.getParameter("updateId");

            	
            	AskComment comment = new AskComment();
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
        
        logger.info("jsonResponse.toString() : "+ jsonResponse.toString()); 
        // JSON 응답 출력
        out.print(jsonResponse.toString());
        out.flush();
	}
	
}
