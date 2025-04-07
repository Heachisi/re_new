package service.file;

import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import model.common.NoticePostFile;

public interface NoticeFileService {

	public NoticePostFile getFileByFileId(NoticePostFile file);

	public HashMap insertBoardFiles(HttpServletRequest request);

}