package we.employ.you.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import we.employ.you.util.LogUtil;
import we.employ.you.util.PasswordUtil;

@Entity
@Table(name = "user_password", schema = "we_employ_you")
public class UserPassword implements Serializable {
    
	private static final long serialVersionUID = -192663536685988492L;

	@Id
	@Column(name = "user_id")
    private String userId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", 
		insertable = false, updatable = false, nullable = false)
	@JsonIgnore
	private User user;
	
	@Id
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
	
	@Column(name = "password")
    private String password;
	
	@Column(name = "current_pwd_ind")
    @Type(type = "yes_no")	
    private boolean currentPasswordIndicator;
    
	@Column(name = "temp_ind", nullable = true)
    @Type(type = "yes_no")
    private boolean temporaryPasswordIndicator;
	
	@Column(name = "temp_pwd_expiry_date")
    private LocalDateTime temporaryPasswordExpirationDate;
	
	@Transient
	private boolean isExpiredPassword;
	
	@Transient
	private boolean deleteOldestPassword;
    
    public UserPassword() {
        super();
    }

    public UserPassword(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDecryptedPassword() {
    	try {
    		return new PasswordUtil().decrypt(this.password);
    	} catch (Exception exception) {
    		LogUtil.logStackTrace(exception);
    		return null;
    	}
    }
    
    public boolean getCurrentPasswordIndicator() {
        return currentPasswordIndicator;
    }
    public void setCurrentPasswordIndicator(boolean currentPasswordIndicator) {
        this.currentPasswordIndicator = currentPasswordIndicator;
    }
    
    public boolean getTemporaryPasswordIndicator() {
        return temporaryPasswordIndicator;
    }

    public void setTemporaryPasswordIndicator(boolean temporaryPasswordIndicator) {
        this.temporaryPasswordIndicator = temporaryPasswordIndicator;
    }
    
    public LocalDateTime getTemporaryPasswordExpirationDate() {
        return temporaryPasswordExpirationDate;
    }

    public void setTemporaryPasswordExpirationDate(LocalDateTime temporaryPasswordExpirationDate) {
        this.temporaryPasswordExpirationDate = temporaryPasswordExpirationDate;
    }

	public boolean isExpiredPassword() {
		return isExpiredPassword;
	}

	public void setExpiredPassword(boolean isExpiredPassword) {
		this.isExpiredPassword = isExpiredPassword;
	}
	
	public boolean isDeleteOldestPassword() {
		return deleteOldestPassword;
	}

	public void setDeleteOldestPassword(boolean deleteOldestPassword) {
		this.deleteOldestPassword = deleteOldestPassword;
	}

	@Override
	public boolean equals(Object otherPassword) {
		if (otherPassword == null) {
			return false;
		} else if (!(otherPassword instanceof UserPassword)) {
			return false;
		} else {
			return this.userId.equals(((UserPassword) otherPassword).userId) &&
				this.getDecryptedPassword().equals(((UserPassword) otherPassword).getDecryptedPassword());
		}
	}
	
	 @Override
	 public int hashCode() {
        int hash = 83 * 7 + Objects.hashCode(this.userId) + Objects.hashCode(this.getDecryptedPassword());
        
        return hash;
    }
}
