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


@WebServlet("/noticeboard/*")
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
	private NoticeBoardService boardService;
	
	
	public NoticeBoardController() {
        super();
        boardService = new NoticeBoardServiceImpl(); 
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
	      
	      if ("/noticeboard/noticeView.do".equals(path)) {
	    	  String boardId =request.getParameter("id");
	    	  
	    	  HttpSession session = request.getSession();
	    	    String viewKey = "viewed_" + boardId;
	    	    if (session.getAttribute(viewKey) == null) {
	    	        boardService.increaseViewCount(boardId); // 조회수 증가
	    	        session.setAttribute(viewKey, true);     // 세션에 조회한 거 표시
	    	    }
	    	  NoticeBoard board = boardService.getBoardById(boardId);
	    	  List<NoticeComment> sortedComments = board.getComments().stream()
	    		        .sorted(Comparator.comparing(NoticeComment::getCreateDt))
	    		        .collect(Collectors.toList());

	    		    board.setComments(sortedComments); 
	    	  request.setAttribute("board", board);
	    	  
	    	  logger.info("불러온 게시글: "+board);
	    	  
	          request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeView.jsp").forward(request, response);
	      } else if("/noticeboard/noticeCreate.do".equals(path)) {
	    	
	    	  request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeCreate.jsp").forward(request, response);
	      } else if ("/noticeboard/noticeUpdate.do".equals(path)) {
	    	  
	    	  String boardId =request.getParameter("id");
	    	  NoticeBoard board = boardService.getBoardById(boardId);
	    	  request.setAttribute("board", board);
	    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeUpdate.jsp").forward(request, response);
	      }else if("/noticeboard/noticeDelete.do".equals(path)) {
		      String boardId =request.getParameter("id");
		      NoticeBoard board = boardService.getBoardById(boardId);
	    	  request.setAttribute("board", board);
    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeDelete.jsp").forward(request, response);
	      } else if ("/noticeboard/noticeList.do".equals(path)) {
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
	    	  
	    	  NoticeBoard board = new NoticeBoard();
	    	  board.setSize(size);
	    	  board.setPage(page);
	    	  board.setSearchText(searchText);
	    	  board.setStartDate(startDate);
	    	  board.setEndDate(endDate);
	    	  
	    	  List<NoticeBoard> boardList = boardService.getBoardList(board);
	    	  
	    	  request.setAttribute("boardList", boardList);
	    	  request.setAttribute("currentPage", page);
	    	  request.setAttribute("totalPages", board.getTotalPages());
	    	  request.setAttribute("size", size);
	    	  
	    	  request.setAttribute("board", board);
	    	  
	    	  request.getRequestDispatcher("/WEB-INF/jsp/noticeBoard/noticeList.jsp").forward(request, response);
	      }
	}

	/**
	 * POST ajax 로직 처리용 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
        PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
        JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
        try {
            logger.info("NoticeBoardController doPost path: " + path);
            
            if ("/noticeboard/noticeCreate.do".equals(path)) { 
            	String title =request.getParameter("title");
            	String content =request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String createId =request.getParameter("createId");
            	
            	NoticeBoard board = new NoticeBoard();
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
            			
            } else if ("/noticeboard/noticeUpdate.do".equals(path)) { 
            	String boardId=request.getParameter("boardId");
            	String title =request.getParameter("title");
            	String content =request.getParameter("content");
            	String viewCount = request.getParameter("viewCount");
            	String updateId =request.getParameter("updateId");
            	
            	NoticeBoard board = new NoticeBoard();
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
            else if ("/bulletinboard/bulletinDelete.do".equals(path)) { 
            	String boardId=request.getParameter("boardId");
            	String updateId=request.getParameter("updateId");

            	NoticeBoard board = new NoticeBoard();
            	board.setBoardId(boardId);
            	board.setUpdateId(updateId);

            	boolean isDelete= boardService.deleteBoard(board);
            	jsonResponse.put("success", isDelete);
            	jsonResponse.put("message", isDelete ?
            			"게시글이 성공적으로 삭제되었습니다." : "게시글 삭제에 실패하였습니다.");
          
            
            
            
            }else if("/noticeboard/comment/create.do".equals(path)) {
            	int boardId =Integer.parseInt(request.getParameter("boardId"));
            	String content =request.getParameter("content");
            	String createId =request.getParameter("createId");
            	int parentCommentId =Integer.parseInt(request.getParameter("parentCommentId"));
            	
            	NoticeComment comment = new NoticeComment();
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
            }else if("/noticeboard/comment/update.do".equals(path)) {
            	int commentId =Integer.parseInt(request.getParameter("commentId"));
            	String content =request.getParameter("content");
            	String updateId =request.getParameter("updateId");

            	NoticeComment comment = new NoticeComment();
            	comment.setContent(content);
            	comment.setUpdateId(updateId);
            	comment.setCommentId(commentId);

            	boolean isSuccess= boardService.updateComment(comment);
            	jsonResponse.put("success", isSuccess);
            	jsonResponse.put("message", isSuccess ?
            			"댓글이 성공적으로 수정되었습니다." : "댓글 수정에 실패하였습니다.");
            }else if("/noticeboard/comment/delete.do".equals(path)) {
            	int commentId =Integer.parseInt(request.getParameter("commentId"));
            	String updateId =request.getParameter("updateId");

            	
            	NoticeComment comment = new NoticeComment();
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
