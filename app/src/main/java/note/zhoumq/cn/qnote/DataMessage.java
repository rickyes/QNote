package note.zhoumq.cn.qnote;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "DataMessage")
public class DataMessage implements Serializable{

	@Column(name="id",isId=true,autoGen=true)
	private int id;
	@Column(name="title")
	private String title;
	@Column(name="content")
	private String content;
	@Column(name="date")
	private String date;
	@Column(name="overDate")
	private String overDate;
	
	public DataMessage() {
	}
	
	public DataMessage(String title,String content,String date,String overDate) {
		this.title = title;
		this.content = content;
		this.date = date;
		this.overDate = overDate;
	}

	public String getOverDate() {
		return overDate;
	}

	public void setOverDate(String overDate) {
		this.overDate = overDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
