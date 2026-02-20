package we.employ.you.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_role", schema = "we_employ_you")
public class UserRole implements Serializable {
    
	private static final long serialVersionUID = 4137213562880030249L;
	
	@Id
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	@JsonIgnore
    private User user;
	
	@Column(name = "user_role")
    private String userRole;
    
    public static final String RECRUITER_USER_ROLE = "REC";
    public static final String MANAGER_USER_ROLE = "MAN";
    
    public UserRole() {
        super();
        userRole = "";
    }

    public UserRole(User user, String userRole) {
    	this.user = user;
    	this.userRole = userRole;
    }
    
	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
