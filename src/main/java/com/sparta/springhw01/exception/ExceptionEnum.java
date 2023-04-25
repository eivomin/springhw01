package com.sparta.springhw01.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003"),
    SECURITY_01(HttpStatus.UNAUTHORIZED, "S0001", "권한이 없습니다."),

    /* 중복 username 예외 */
    DUPLICATE_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "중복된 username 입니다."),

    /* 유효하지 않은 토큰 예외 */
    INVALID_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "토큰이 유효하지 않습니다."),

    /* 권한이 없는 예외 */
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "400", "작성자만 삭제/수정할 수 있습니다."),

    /* 잘못된 예외 처리 */
    INVALID_USER_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "사용자가 존재하지 않습니다."),

    /* 잘못된 예외 처리 */
    INVALID_POST_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "게시글이 존재하지 않습니다."),

    /* 잘못된 예외 처리 */
    INVALID_COMMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "댓글이 존재하지 않습니다."),

    /* 잘못된 예외 처리 */
    ID_PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "아이디와 비밀번호가 일치하지 않습니다."),

    /* admin_token 예외 */
    ADMIN_TOKEN_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "관리자 암호가 틀려 등록이 불가능합니다."),

    /* 비밀번호 패턴 예외 */
    PASSWORD_EXCEPTION(HttpStatus.BAD_REQUEST, "400", "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다.");

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
    }