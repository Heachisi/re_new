package util;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String encrypted = SHA256Util.encrypt("admin00000!"); // 암호화 수행

        System.out.println("SHA-256 해시값: " + encrypted); // 결과 출력

    }
}