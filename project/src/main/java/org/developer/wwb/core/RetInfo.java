package org.developer.wwb.core;

public class RetInfo {
	private String status;  //操作成功与否 {ok, failed}
    private String info = "";   //关于结果的额外信息，如出错原因等
    private int code;
    private Object data;
	

	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}


	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

    
}
