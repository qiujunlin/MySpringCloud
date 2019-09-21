package com.web.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonResult<T> {
	/** �ɹ� */
	public static final int SUCCESS = 200;

	/** û�е�¼ */
	public static final int NOT_LOGIN = 400;

	/** �����쳣 */
	public static final int EXCEPTION = 401;

	/** ϵͳ���� */
	public static final int SYS_ERROR = 402;

	/** �������� */
	public static final int PARAMS_ERROR = 403;

	/** ��֧�ֻ��Ѿ����� */
	public static final int NOT_SUPPORTED = 410;

	/** AuthCode���� */
	public static final int INVALID_AUTHCODE = 444;

	/** ̫Ƶ���ĵ��� */
	public static final int TOO_FREQUENT = 445;

	/** δ֪�Ĵ��� */
	public static final int UNKNOWN_ERROR = 499;
	
	private int code;
	private String msg;
	private T data;
	
	

	public static JsonResult build() {
		return new JsonResult();
	}
	public static JsonResult build(int code) {
		return new JsonResult().code(code);
	}
	public static JsonResult build(int code, String msg) {
		return new JsonResult<String>().code(code).msg(msg);
	}
	public static <T> JsonResult<T> build(int code, T data) {
		return new JsonResult<T>().code(code).data(data);
	}
	public static <T> JsonResult<T> build(int code, String msg, T data) {
		return new JsonResult<T>().code(code).msg(msg).data(data);
	}
	
	public JsonResult<T> code(int code) {
		this.code = code;
		return this;
	}
	public JsonResult<T> msg(String msg) {
		this.msg = msg;
		return this;
	}
	public JsonResult<T> data(T data) {
		this.data = data;
		return this;
	}
	
	
	public static JsonResult ok() {
		return build(SUCCESS);
	}
	public static JsonResult ok(String msg) {
		return build(SUCCESS, msg);
	}
	public static <T> JsonResult<T> ok(T data) {
		return build(SUCCESS, data);
	}
	public static JsonResult err() {
		return build(EXCEPTION);
	}
	public static JsonResult err(String msg) {
		return build(EXCEPTION, msg);
	}
	
	@Override
	public String toString() {
		return JsonUtil.to(this);
	}
}