package cn.believeus.entity;

public class Tmenu {
	private int id;
	private String title;
	/* Œª÷√ */
	private int locid;
	/* ∏∏id */
	private int pid;

	public Tmenu() {
	}

	public Tmenu(int id, String title, int locid,int pid) {
		super();
		this.id = id;
		this.title = title;
		this.locid = locid;
		this.pid=pid;
	}

	public Tmenu(String title, int locid,int pid) {
		super();
		this.title = title;
		this.locid = locid;
		this.pid=pid;
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

	public int getLocid() {
		return locid;
	}

	public void setLocid(int locid) {
		this.locid = locid;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		return "Tmenu [id=" + id + ", title=" + title + ", locid=" + locid
				+ "]";
	}

}
