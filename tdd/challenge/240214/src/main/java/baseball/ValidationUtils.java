package baseball;

public class ValidationUtils {

    public static final int MIN_NO = 1;
    public static final int MAX_NO = 9;

    public static boolean validNo(int number) {
        return number >= MIN_NO && number <= MAX_NO;
    }
}
