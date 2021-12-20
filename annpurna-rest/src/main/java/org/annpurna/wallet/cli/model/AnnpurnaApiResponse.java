package org.annpurna.wallet.cli.model;

public class AnnpurnaApiResponse {

	public static final String SUCCESS  = "SUCCESS" ;
	public static final String FAILED  = "FAILED" ;
	
	private String msg ;
	private String status;
	private Object data ;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "DoorApiResponse [msg=" + msg + ", status=" + status + ", data=" + data + "]";
	}
}
