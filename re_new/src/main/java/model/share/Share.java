package model.share;

import java.util.List;

import jakarta.servlet.http.Part;
import model.Model;
import model.common.PostFile;

public class Share extends Model {

	private String shareId;   // 게시글 고유 식별자 (ID)
	private String title;     // 게시글 제목
	private String content;   // 게시글 내용
	private String viewcount; // 게시글 조회수
	
	private String searchText; // 게시글 조회수
	private String startDate; // 게시글 조회수
	private String endDate; // 게시글 조회수
	
	
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

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
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

	

	public String getViewcount() {
		return viewcount;
	}

	public void setViewcount(String viewcount) {
		this.viewcount = viewcount;
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

	@Override
	public String toString() {
		return "Share [shareId=" + shareId + ", title=" + title + ", content=" + content + ", viewCount=" + viewcount
				+ ", searchText=" + searchText + ", startDate=" + startDate + ", endDate=" + endDate + ", rn=" + rn
				+ ", startRow=" + startRow + ", endRow=" + endRow + ", page=" + page + ", size=" + size
				+ ", totalCount=" + totalCount + ", totalPages=" + totalPages + ", postFiles=" + postFiles
				+ ", comments=" + comments + "]";
	}
	
	
	
	
}