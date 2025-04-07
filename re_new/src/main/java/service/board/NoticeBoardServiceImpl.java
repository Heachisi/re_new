package service.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.board.NoticeBoardDAO;
import dao.file.NoticeFileDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import model.board.NoticeBoard;
import model.board.NoticeComment;
import model.common.NoticePostFile;
import util.NoticeFileUploadUtil;
import util.MybatisUtil;

public class NoticeBoardServiceImpl implements NoticeBoardService {
	private static final Logger logger = LogManager.getLogger(NoticeBoardServiceImpl.class);
	private NoticeBoardDAO boardDAO;// DB접속용
	private NoticeFileDAO fileDAO;

	private SqlSessionFactory sqlSessionFactory; // MyBatis SQL 세션 팩토리
	private Object NoticeBoardMapper;

	/**
	 * BoardServiceImpl 생성자 //서비스 생성 이유: 비즈니스 로직을 담기 위해(하나의 비즈니스 로직은 하나의 함수만 담음)
	 */
	public NoticeBoardServiceImpl() {
		this.boardDAO = new NoticeBoardDAO();			
		this.fileDAO = new NoticeFileDAO();
		try {
			sqlSessionFactory = MybatisUtil.getSqlSessionFactory(); // SQL 세션 팩토리 초기화
		} catch (Exception e) {
			logger.error("Mybatis 오류", e); // 오류 발생 시 로그 출력
		}
	}

	// 유저 정보를 조회
	public NoticeBoard getBoardById(String boardId) {
		SqlSession session = sqlSessionFactory.openSession();
		NoticeBoard selectBoard = boardDAO.getBoardById(session, boardId);
		//파일 목록 조회
		selectBoard.setPostFiles(fileDAO.getFilesByBoardId(session,boardId));
		
		//댓글 목록 조회
		selectBoard.setComments(boardDAO.getCommentList(session, boardId));
		return selectBoard;
	}

	public boolean createBoard(NoticeBoard board, HttpServletRequest request) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		
		try {
			result = boardDAO.createBoard(session, board);

			// 파일 업로드 파트 필터링
			List<Part> fileParts = new ArrayList<>();
			for (Part part : request.getParts()) {
				if ("files".equals(part.getName()) && part.getSize() > 0) {
					fileParts.add(part);
					
				}
			}
			// 업로드 된 파일들을 처리하여 PostFile 객체 리스트 반환
			List<NoticePostFile> fileList = NoticeFileUploadUtil.uploadFiles(fileParts, "board",
					Integer.parseInt(board.getBoardId()), board.getCreateId());

			for (NoticePostFile postFile : fileList) {
				fileDAO.insertBoardFile(session, postFile);
			}

			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	public boolean updateBoard(NoticeBoard board, HttpServletRequest request) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = boardDAO.updateBoard(session, board);
			if (result) {
				String postFilesParam = request.getParameter("remainingFileIds");

				List<String> postFiles = new ArrayList<String>();
				if (postFilesParam != null && !postFilesParam.trim().isEmpty()) {
					postFiles = Arrays.asList(postFilesParam.split(","));
				}
				// 기존 파일 조회
				List<NoticePostFile> existingFiles = fileDAO.getFilesByBoardId(session, (board.getBoardId()));

				// 기존 파일 리스트가 있을 때
				if (existingFiles != null && existingFiles.size() > 0) {
					boolean fileExists = false;
					for (NoticePostFile existingFile : existingFiles) {
						fileExists = false;
						// 새로 넘어온 파일 목록에 기존 파일이 포함되어 있는지 체크
						for (String fileId : postFiles) {
							if (existingFile.getFileId() == Integer.parseInt(fileId)) {
								fileExists = true;
								break;
							}
						}

						// 넘어온 파일 목록에 없으면 삭제
						if (!fileExists) {
							existingFile.setUpdateId(board.getUpdateId());
							boolean deleteSuccess = fileDAO.deleteFile(session, existingFile);
							if (!deleteSuccess) {
								session.rollback();
								return false;
							}
						}
					}
				}

				// 새로운 파일 업로드
				List<Part> fileParts = new ArrayList<>();
				for (Part part : request.getParts()) {
					if ("files".equals(part.getName()) && part.getSize() > 0) {
						fileParts.add(part);
					}
				}
				// 업로드 된 파일들을 처리하여 PostFile 객체 리스트 반환
				List<NoticePostFile> fileList = NoticeFileUploadUtil.uploadFiles(fileParts, "board",
						Integer.parseInt(board.getBoardId()), board.getUpdateId());

				for (NoticePostFile postFile : fileList) {
					fileDAO.insertBoardFile(session, postFile);
				}
			}

			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	@Override
	public boolean deleteBoard(NoticeBoard board) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = boardDAO.deleteBoard(session, board);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	@Override
	public List<NoticeBoard> getBoardList(NoticeBoard board) {
		SqlSession session = sqlSessionFactory.openSession();

		int page = board.getPage();
		int size = board.getSize();

		
		int totalCount = boardDAO.getTotalBoardCount(session, board);
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		int startRow = (page - 1) * size + 1;
		int endRow = page * size;

		board.setTotalCount(totalCount);
		board.setTotalPages(totalPages);
		board.setStartRow(startRow);
		board.setEndRow(endRow);

		List<NoticeBoard> list = boardDAO.getBoardList(session, board);
		
		return list;
	}

	@Override
	public boolean createComment(NoticeComment comment) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = boardDAO.insertComment(session, comment);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	@Override
	public boolean updateComment(NoticeComment comment) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = boardDAO.updateComment(session, comment);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	@Override
	public boolean deleteComment(NoticeComment comment) {
		SqlSession session = sqlSessionFactory.openSession();
		boolean result = false;
		try {
			result = boardDAO.deleteComment(session, comment);
			session.commit(); // 트랜잭션 커밋
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	//조회수
	@Override
	public void increaseViewCount(String boardId) {
		 try (SqlSession session = sqlSessionFactory.openSession()) {
		        boardDAO.increaseViewCount(session, boardId);
		        session.commit();
		    }
		}
}