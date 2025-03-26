package service.user;

import org.apache.ibatis.session.SqlSession;

import model.user.User;

public interface UserService {

    public boolean registerUser(User user);

    public boolean validateUser(User user);

    public User getUserById(String userId);

}
