package service.user;

import java.util.List;
import java.util.Map;

import model.user.User;

public interface UserService {
    /**
     * 사용자 회원가입
     */
    public boolean registerUser(User user);
    
    public boolean validateUser(User user);
    
    public User getUserById(String userId);
    
    public boolean checkUserIdDuplicate(String userId);
    
    public boolean updateUser(User user);
    
    public boolean deleteUser(User user);
    
    public boolean toggleUserDeletion(User user);
    
 // 연령대별 사용자 수 조회
    public List<Map<String, Object>> getAgeGroupCounts();

    // 성별별 사용자 수 조회
    public List<Map<String, Object>> getGenderCounts();
    

}