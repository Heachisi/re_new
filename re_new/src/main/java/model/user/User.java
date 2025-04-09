package model.user;

import model.Model;

public class User extends Model {
   private String adminYn;
   private String userId;
   private String password;
   private String email;
   private String birthdate;
   private String gender;
   private String delYn;

   

   



public User() { 
	      
   }



public String getAdminYn() {
	return adminYn;
}



public void setAdminYn(String adminYn) {
	this.adminYn = adminYn;
}



public String getUserId() {
	return userId;
}



public void setUserId(String userId) {
	this.userId = userId;
}



public String getPassword() {
	return password;
}



public void setPassword(String password) {
	this.password = password;
}



public String getEmail() {
	return email;
}



public void setEmail(String email) {
	this.email = email;
}



public String getBirthdate() {
	return birthdate;
}



public void setBirthdate(String birthdate) {
	this.birthdate = birthdate;
}



public String getGender() {
	return gender;
}



public void setGender(String gender) {
	this.gender = gender;
}


public String getDelYn() {
	return delYn;
}



public void setDelYn(String delYn) {
	this.delYn = delYn;
}


}


