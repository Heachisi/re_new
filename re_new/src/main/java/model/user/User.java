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
   
   private int rn;
   private int startRow;
   private int endRow;
	
	private int page;
	private int size;
	
	private int totalCount;
	private int totalPages;
   

   



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






public int getRn() {
	return rn;
}






public void setRn(int rn) {
	this.rn = rn;
}






public int getStartRow() {
	return startRow;
}






public void setStartRow(int startRow) {
	this.startRow = startRow;
}






public int getEndRow() {
	return endRow;
}






public void setEndRow(int endRow) {
	this.endRow = endRow;
}






public int getPage() {
	return page;
}






public void setPage(int page) {
	this.page = page;
}






public int getSize() {
	return size;
}






public void setSize(int size) {
	this.size = size;
}






public int getTotalCount() {
	return totalCount;
}






public void setTotalCount(int totalCount) {
	this.totalCount = totalCount;
}






public int getTotalPages() {
	return totalPages;
}






public void setTotalPages(int totalPages) {
	this.totalPages = totalPages;
}




}


