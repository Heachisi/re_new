package controller.user;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.user.User;
import service.user.UserService;
import service.user.UserServiceImpl;


@WebServlet("/user/*")
public class UserController extends HttpServlet {

	private static final long serialVersionUID = 7949105235983859619L;
	private static final Logger logger = LogManager.getLogger(UserController.class); 
	private UserService userService;
	public UserController() {
        super();
        userService= new UserServiceImpl();
    }
	/**
	 * get 화면 이동용 및 조회용
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  logger.info("UserController doGet"); 
	      String path = request.getRequestURI();
	      logger.info("UserController doGet path" + path); 
	      
	      if ("/user/login.do".equals(path)) {
	            // 로그인 JSP로 포워딩
	            request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request, response);
	            
	      } else if ("/user/join.do".equals(path)) {
	            // 로그인 JSP로 포워딩
	            request.getRequestDispatcher("/WEB-INF/jsp/user/join.jsp").forward(request, response);
	            
	      } else if ("/user/main.do".equals(path)) {
	            // 로그인 JSP로 포워딩
	            request.getRequestDispatcher("/WEB-INF/jsp/user/main.jsp").forward(request, response);
	            
	      } else if ("/user/userInfo.do".equals(path)) {
	            // 로그인 JSP로 포워딩
	            request.getRequestDispatcher("/WEB-INF/jsp/user/userInfo.jsp").forward(request, response);
	            
	      }else if("/user/updateUser.do".equals(path)) {
	            request.getRequestDispatcher("/WEB-INF/jsp/user/updateUser.jsp").forward(request, response);
	           
	      }else if("/user/adminChart.do".equals(path)) {
	            request.getRequestDispatcher("/WEB-INF/jsp/user/adminChart.jsp").forward(request, response);
		           
	      }else if("/user/adminUserManage.do".equals(path)) {
	            request.getRequestDispatcher("/WEB-INF/jsp/user/adminUserManage.jsp").forward(request, response);
	      }
	      
	      
	      
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		logger.info("UserController doPost");
        String path = request.getRequestURI();
        response.setContentType("application/json; charset=UTF-8"); // 응답 타입 설정
        PrintWriter out = response.getWriter(); // PrintWriter 객체 생성
        JSONObject jsonResponse = new JSONObject(); // JSON 응답 객체 생성
        try {
            logger.info("UserController doPost path: " + path);
            
            
            
            if ("/user/checkUserId.do".equals(path)) {
                String userId = request.getParameter("userId");

                if (userId == null || userId.trim().isEmpty()) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "아이디를 입력해주세요.");
                } else {
                    boolean exists = userService.checkUserIdDuplicate(userId);
                    jsonResponse.put("exists", exists); 
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "중복체크 성공");
                }
            } else if ("/user/register.do".equals(path)) { 
            	// User 객체 생성
            	User user = new User();
            	user.setUserId(request.getParameter("userId"));
            	user.setPassword(request.getParameter("password"));
            	user.setEmail(request.getParameter("email"));
            	user.setBirthdate(request.getParameter("birthdate"));
            	user.setGender(request.getParameter("gender"));
        		user.setCreateId("SYSTEM");
        		jsonResponse.put("success", userService.registerUser(user)); // 오류 발생 시
            		

            } else if ("/user/loginCheck.do".equals(path)) { 
            	User user = new User();
            	user.setUserId(request.getParameter("userId"));
            	user.setPassword(request.getParameter("password"));
            	
            	boolean loginCheck = userService.validateUser(user);
            	
            	if (loginCheck) {
            		HttpSession session = request.getSession();
            		User selectUser = userService.getUserById(user.getUserId());
            		session.setAttribute("user", selectUser);
            		jsonResponse.put("success", true);
            	} else {
            		jsonResponse.put("success", false);
            		jsonResponse.put("message", "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
            	}
            	
            } else if ("/user/updateUser.do".equals(path)) {
                String userId = request.getParameter("userId");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                String updateId = request.getParameter("updateId");
                String birthdate = request.getParameter("birthdate");
                String gender = request.getParameter("gender");

                // User 객체 생성 및 등록
                User user = new User();
                user.setUserId(userId);
                user.setPassword(password);
                user.setEmail(email);
                user.setBirthdate(birthdate);
                user.setGender(gender);
                user.setUpdateId(updateId);

              
                boolean isUpdate = userService.updateUser(user);
                jsonResponse.put("success", isUpdate);
                jsonResponse.put("message", isUpdate ? "회원정보수정이 성공적으로 처리되었습니다." : "회원정보수정 처리 실패");
                
                if(isUpdate) {
                	HttpSession session = request.getSession();
                    User selectUser = userService.getUserById(user.getUserId());
                    session.setAttribute("user", selectUser);
                }
                
            //로그아웃 처리
            } else if ("/user/logout.do".equals(path)) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");

                if (user != null) {
                    session.invalidate();
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "로그아웃 되었습니다.");
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "로그아웃할 사용자 정보가 없습니다.");
                }
                
            } else if ("/user/delete.do".equals(path)) {
                String userId = request.getParameter("userId");
                String updateId = request.getParameter("updateId");
                
                // User 객체 생성 및 등록
                User user = new User();
                user.setUserId(userId);
                user.setUpdateId(updateId);

                // 사용자 탈퇴 처리
                boolean isDelete = userService.deleteUser(user);
                jsonResponse.put("success", isDelete);
                jsonResponse.put("message", isDelete ? "회원탈퇴가 성공적으로 처리되었습니다." : "회원탈퇴 처리 실패");
                if(isDelete) { 
                	HttpSession session = request.getSession();
                	session.invalidate();
                }
                
            } else if ("/user/toggleUserDeletion.do".equals(path)) {
                String userId = request.getParameter("userId");
                String delYn = request.getParameter("delYn"); // 'Y' 또는 'N'
                String updateId = request.getParameter("updateId"); // 변경을 수행한 사용자 ID

                if (userId == null || delYn == null || updateId == null) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "필수 데이터가 부족합니다.");
                } else {
                    User user = new User();
                    user.setUserId(userId);
                    user.setDelYn(delYn);
                    user.setUpdateId(updateId);

                    boolean result = userService.toggleUserDeletion(user);
                    jsonResponse.put("success", result);
                    jsonResponse.put("message", result ? "회원 상태 변경 성공" : "회원 상태 변경 실패");
                }response.getWriter().print(jsonResponse.toString());
                
            }else if ("/user/getUserRole.do".equals(path)) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    jsonResponse.put("adminYn", user.getAdminYn());  // 'Y' 또는 'N' 반환
                } else {
                    jsonResponse.put("adminYn", "N");  // 기본값 설정
                   
                }
                jsonResponse.put("success", true);
      
            } else if ("/user/age-group.do".equals(path)) {
                logger.info("Fetching age group data");
                jsonResponse.put("data", userService.getAgeGroupCounts());
                jsonResponse.put("success", true);

            } else if ("/user/gender-stats.do".equals(path)) {
                logger.info("Fetching gender stats");
                jsonResponse.put("data", userService.getGenderCounts());
                jsonResponse.put("success", true);

            }else if ("/user/getUserInfo.do".equals(path)) {
                String userId = request.getParameter("userId");
                if (userId == null || userId.trim().isEmpty()) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "검색할 사용자 ID를 입력하세요.");
                } else {
                    User user = userService.getUserById(userId);
                    if (user != null) {
                        jsonResponse.put("success", true);
                        jsonResponse.put("userId", user.getUserId());
                        jsonResponse.put("createDt", user.getCreateDt()); // 회원가입 날짜
                        jsonResponse.put("createId", user.getCreateId()); // 가입자 ID
                        jsonResponse.put("delYn", user.getDelYn()); // 삭제 여부
                    } else {
                        jsonResponse.put("success", false);
                        jsonResponse.put("message", "해당 ID를 가진 사용자가 없습니다.");
                    }
                }
                response.getWriter().print(jsonResponse.toString());
            }
            
            
            
        } catch (Exception e) {
            jsonResponse.put("success", false); // 오류 발생 시
            jsonResponse.put("message", "서버 오류 발생"); // 오류 메시지
            logger.error("Error in UserController doPost", e); // 오류 로그 추가
        }

        logger.info("jsonResponse.toString() : ", jsonResponse.toString()); 
        // JSON 응답 출력
        out.print(jsonResponse.toString());
        out.flush();
		
	}
	
	

}
