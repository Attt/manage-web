package com.dszj.manage.enums;

import lombok.Getter;

@Getter
public enum ActivityTypeEnum {
    SOURCE(0, "课程签到"),
    ACTIVITY(1, "活动签到");

    private Integer code;

    private String message;

    ActivityTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
