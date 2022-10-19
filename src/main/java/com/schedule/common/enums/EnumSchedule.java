package com.schedule.common.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Enum 클래스 잘 정리된 사이트
 * 출처 : https://velog.io/@kyle/%EC%9E%90%EB%B0%94-Enum-%EA%B8%B0%EB%B3%B8-%EB%B0%8F-%ED%99%9C%EC%9A%A9
 * 조금 더 공부해보니 Enum class는 다양한 상수 데이터가 존재할때 쓰는게 더 유용하다.
 * 그래서 Enum으로 하지 않고 delimiter 의 경우 static final arrayList에 담아서 처리해주는 정도로 작업하는게 더 깔끔하다.
 *  -> 좋은 코드가 아닌 것 같다는 피드백을 받음.
 */
@Deprecated
public enum EnumSchedule {
    DELIMITER("delimiter",
            Arrays.asList("^", "$", "(?!)", "|", "[]", "{}", "()", ".", "*", "+", "?",
                    "\\d", "\\D", "\\w", "\\W", "\\s", "\\S"));

    private final String delimiter;
    private final List<String> list;

    EnumSchedule(String delimiter, List<String> list) {
        this.delimiter = delimiter;
        this.list = list;
    }

    /**
     * split() 함수 사용 시 위 표에 있는 메타문자로 들어가는 특수문자를 구분자로 사용할 때는
     * 메타문자 앞에 \(역슬래쉬) 2번 혹은 [메타문자]를 붙여 이스케이프처리 해주면 된다.
     */
    public static String setSpecialDelimiter(String delimiter) {
        StringBuffer stringBuffer = new StringBuffer();
        if(DELIMITER.list.contains(delimiter)) {
            stringBuffer.append("["); //특수문자 구분자를 위한 대괄호
            stringBuffer.append(delimiter);
            stringBuffer.append("]");
            return stringBuffer.toString();
        } else {
            return delimiter;
        }
    }
}
