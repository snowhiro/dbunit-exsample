package jp.dip.snowsaber;

import java.sql.Timestamp;

/**
 * サンプル用 エンティティクラス
 *
 */
public class SampleTable {

	private Long id;

	private String docText;

	private Timestamp nowTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocText() {
		return docText;
	}

	public void setDocText(String docText) {
		this.docText = docText;
	}

	public Timestamp getNowTime() {
		return nowTime;
	}

	public void setNowTime(Timestamp nowTime) {
		this.nowTime = nowTime;
	}


}
