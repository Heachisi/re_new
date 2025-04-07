package service.file;

import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import model.common.AskPostFile;

public interface AskFileService {

	public AskPostFile getFileByFileId(AskPostFile file);

	public HashMap insertBoardFiles(HttpServletRequest request);

}