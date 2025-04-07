package util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.Part;
import model.common.NoticePostFile;


public class NoticeFileUploadUtil {
    private static final String UPLOAD_DIR = "uploads"; // 업로드 파일이 저장될 폴더 이름

    /**
     * 다중 파일을 업로드하고 저장된 파일명을 리스트로 반환
     * @param parts 업로드된 파일 목록 (Servlet의 Part 객체)
     * @param uploadPath 파일이 저장될 경로
     * @return 저장된 파일명 리스트
     * @throws IOException 파일 저장 중 오류 발생 시 예외 처리
     */
    public static List<NoticePostFile>  uploadFiles(List<Part> parts, String basePath, int boardId, String userId) throws IOException {
        List<NoticePostFile> files = new ArrayList<>(); // 업로드된 파일명을 저장할 리스트
        String uploadPath = getUploadPath(basePath);
        File uploadDir = new File(uploadPath); // 업로드 경로 폴더 객체 생성

        // 업로드 폴더가 존재하지 않으면 생성
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS"); // 날짜 포맷 설정 (밀리초 포함)
        NoticePostFile postFile = null;
        // 업로드된 각 파일에 대해 처리
        for (Part part : parts) {
            String originalFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString(); // 원본 파일명 가져오기

            // 파일명이 비어있지 않은 경우에만 처리
            if (!originalFileName.isEmpty()) {
                // 파일명에서 특수문자 제거 (보안상 안전한 파일명 유지)
                String safeFileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");

                // 파일 확장자 분리
                String fileExtension = "";
                int dotIndex = safeFileName.lastIndexOf(".");
                if (dotIndex > 0) { // 확장자가 있는 경우
                    fileExtension = safeFileName.substring(dotIndex); // 확장자 저장 (.jpg, .png 등)
                    safeFileName = safeFileName.substring(0, dotIndex); // 확장자를 제외한 파일명
                }

                // 파일명 중복 방지를 위해 현재 날짜 + nanoTime을 조합하여 유니크한 파일명 생성
                String timestamp = sdf.format(new Date()) + "_" + System.nanoTime();
                String newFileName = timestamp + "_" + safeFileName + fileExtension;

                // 최종 저장 경로 설정
                String filePath = uploadPath + File.separator + newFileName;
                
                // 파일 저장
                part.write(filePath);
                postFile = new NoticePostFile();
                postFile.setBoardId(boardId);
                postFile.setCreateId(userId);
                postFile.setFileName(originalFileName);
                postFile.setFilePath(filePath);
                postFile.setUpdateId(userId);
                files.add(postFile);
            }
        }
        
        return files;
    }

    /**
     * 업로드 경로 반환
     * @param basePath 프로젝트의 기본 경로 (ServletContext.getRealPath("")로 전달 가능)
     * @return 실제 업로드 디렉토리 경로
     */
    public static String getUploadPath(String basePath) {
        return "C:"+File.separator+UPLOAD_DIR + File.separator + basePath;
    }
}
