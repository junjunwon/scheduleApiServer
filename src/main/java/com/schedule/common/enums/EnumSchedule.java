package com.schedule.common.enums;

import java.util.Arrays;
import java.util.List;

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
