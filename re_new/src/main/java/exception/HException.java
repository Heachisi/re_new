package exception;



public class HException extends Exception {
    // 예외 코드 (예: 오류 종류를 구분할 수 있는 코드)
    private String errorCode;

    // 공통 메시지
    private String message;
    
    private Throwable cause;


    // 기본 생성자
    public HException(String message) {
        super(message); // 부모 클래스 Exception의 생성자 호출
        this.message = message;
    }

    // errorCode와 함께 메시지를 받는 생성자
    public HException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    // errorCode 가져오기
    public String getErrorCode() {
        return errorCode;
    }

    // 메시지 가져오기
    @Override
    public String getMessage() {
        return message;
    }
    
    // 원본 예외 객체 가져오기
    public Throwable getCause() {
        return cause;
    }
    
    // 원본 예외 객체를 받는 생성자 (예외 체인 구현)
    public HException(String message, String errorCode, Throwable cause) {
        super(message, cause);  // 부모 클래스 Exception의 생성자 호출
        this.message = message;
        this.errorCode = errorCode;
        this.cause = cause;
    }
    
    public HException(String message, Throwable cause) {
        super(message, cause);  // 부모 클래스 Exception의 생성자 호출
        this.message = message;
        this.cause = cause;
    }
    
}
