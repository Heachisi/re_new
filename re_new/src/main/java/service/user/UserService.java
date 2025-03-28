package service.user;

import model.user.User;

public interface UserService {
    /**
     * 사용자 회원가입
     */
    public boolean registerUser(User user);
    
    public boolean validateUser(User user);
    
    public User getUserById(String userId);

}