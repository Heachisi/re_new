package util;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("암호화할 문자열 입력: ");
        String input = scanner.nextLine(); // 사용자 입력 받기

        String encrypted = SHA256Util.encrypt(input); // 암호화 수행

        System.out.println("SHA-256 해시값: " + encrypted); // 결과 출력

        scanner.close();
    }
}