package service.file;

import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import model.common.BulletinPostFile;
import model.common.PostFile;

public interface BulletinFileService {

	public BulletinPostFile getFileByFileId(BulletinPostFile file);

	public HashMap insertBoardFiles(HttpServletRequest request);

}