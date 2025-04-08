package model.product;

import java.util.List;

import jakarta.servlet.http.Part;
import model.Model;
import model.common.PostFile;

public class Product extends Model {

	private String productId;   // 게시글 고유 식별자 (ID)
	private String title;     // 게시글 제목
	private String content;   // 게시글 내용
	private String viewCount; // 게시글 조회수
	
	private String searchText; // 게시글 조회수
	private String startDate; // 게시글 조회수
	private String endDate; // 게시글 조회수
	
	private String category; // 게시글 조회수
	private String price; // 게시글 조회수
	private String photo; // 게시글 조회수
	private String viewcategory;
	private String minprice; // 게시글 조회수
	private String maxprice; // 게시글 조회수
	private String likecount;
	private String liked;
	private String sellstatus;
	
	private int rn;           // 게시글 순번(Row Number, DB 조회 시 사용)
	private int startRow;     // 페이지 내의 시작 행 번호 (페이징 처리용)
	private int endRow;       // 페이지 내의 끝 행 번호 (페이징 처리용) 


	private int page;         // 현재 페이지 번호
	private int size;         // 한 페이지에 보여줄 게시글 개수

	private int totalCount;   // 전체 게시글 개수
	private int totalPages;   // 전체 페이지 수
	
	//<>는 제네릭(Generic)을 나타내는 기호로, 리스트(List)가 어떤 타입의 객체를 저장할지 지정하는 역할
	private List<PostFile> postFiles; // 게시글에 첨부된 파일들의 목록을 담는 리스트
	
	private List<Comment> comments; // 게시글에 작성된 댓글 목록을 담는 리스트
	


	@Override
	public String toString() {
		return "Product [productId=" + productId + ", title=" + title + ", content=" + content + ", viewCount=" + viewCount
				+ ", searchText=" + searchText + ", startDate=" + startDate + ", endDate=" + endDate + ", category="
				+ category + ", price=" + price + ", photo=" + photo + ", viewcategory=" + viewcategory + ", minprice="
				+ minprice + ", maxprice=" + maxprice + ", likecount=" + likecount + ", liked=" + liked
				+ ", sellstatus=" + sellstatus + ", rn=" + rn + ", startRow=" + startRow + ", endRow=" + endRow
				+ ", page=" + page + ", size=" + size + ", totalCount=" + totalCount + ", totalPages=" + totalPages
				+ ", postFiles=" + postFiles + ", comments=" + comments + ", createId=" + createId + ", updateId="
				+ updateId + ", createDt=" + createDt + ", updateDt=" + updateDt + "]";
	}

	public String getSellstatus() {
		return sellstatus;
	}

	public void setSellstatus(String sellstatus) {
		this.sellstatus = sellstatus;
	}

	public String getLikecount() {
		return likecount;
	}

	public void setLikecount(String likecount) {
		this.likecount = likecount;
	}


	public String getLiked() {
		return liked;
	}

	public void setLiked(String liked) {
		this.liked = liked;
	}

	public String getMinprice() {
		return minprice;
	}

	public void setMinprice(String minprice) {
		this.minprice = minprice;
	}

	public String getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(String maxprice) {
		this.maxprice = maxprice;
	}

	public String getViewcategory() {
		return viewcategory;
	}

	public void setViewcategory(String viewcategory) {
		this.viewcategory = viewcategory;
	}


	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
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

	public List<PostFile> getPostFiles() {
		return postFiles;
	}

	public void setPostFiles(List<PostFile> postFiles) {
		this.postFiles = postFiles;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	
	
}