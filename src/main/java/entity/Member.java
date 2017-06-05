package entity;
import java.util.*;
import javax.persistence.*;

@Entity @Table(name="member")
public class Member {
	@Id @Column(name="member_id") @GeneratedValue(strategy=GenerationType.IDENTITY)      
	public Long id;
        @Column(name="member_name")
	public String name;
	@Column(name="member_username")
	public String username;
	@Column(name="member_password")
	public String password;
        @Column(name="member_type")
        public String type;
        @Column(name="time_login")
        public String timelogin;
        @Column(name="ip_login")
        public String ip_client;
        @Column(name="member_last_activity")
        public Long last_activity;
        @Column(name="member_session_id")
        public String session_id;
        
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getUserName() { return username; }
	public void setUserName(String username) { this.username = username; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
        public String getType() { return type; }
	public void setType(String type) { this.type = type; }
        public String getTimeLogin() { return timelogin; }
	public void setTimeLogin(String timelogin) { this.timelogin = timelogin; }
        public String getIpClient() { return ip_client; }
	public void setIpClient(String ip_client) { this.ip_client = ip_client; }
        public Long getLastActivity() { return last_activity; }
	public void setLastActivity(Long last_activity) { this.last_activity = last_activity; }
        public String getSessionId() { return session_id; }
	public void setSessionId(String session_id) { this.session_id = session_id; }
	
	public Member() {}
        public Member(Long id, String username) {
		this.id       = id;
		this.username = username;
	}
	public Member(Long id, String name, String email) {//use insert
	}
}

