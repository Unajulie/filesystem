package cn.believeus.entity;

public class Tuser {
private int id;
private String username;
private String passwd;
public Tuser() {
}
public Tuser(String username, String passwd) {
	super();
	this.username = username;
	this.passwd = passwd;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPasswd() {
	return passwd;
}
public void setPasswd(String passwd) {
	this.passwd = passwd;
}

}
