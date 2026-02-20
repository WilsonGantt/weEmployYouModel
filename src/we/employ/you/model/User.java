package we.employ.you.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * This class represents the USERS table in the database.
 */
@Entity
@Table(name = "user", schema = "we_employ_you")
public class User extends Response implements Serializable {

	@Serial
	private static final long serialVersionUID = 7149901395862783397L;

	@Id
    @Column(name = "user_id")
    private String userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "hire_date")
	private LocalDate hireDate;

	@Column(name = "terminated_date")
    private LocalDate terminatedDate;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@Transient
	private String password;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@Transient
	private Boolean isTemporaryPassword;

	@JsonIgnore
	@Transient
    private UserPassword currentPassword;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userId", cascade = CascadeType.REMOVE)
    private List<UserPassword> passwords;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private UserRole userRole;

	@Lob
	@Column(name = "user_photo", updatable = false)
	@Basic(optional = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] userPhoto;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "recruiter")
    private List<Applicant> applicants;


    /**
     * Default constructor.
     */
    public User() {
        super();
    }

    public User(String userId) {
    	this.userId = userId;
    }

    public User(String firstName, String lastName) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    }

    public User(String userId, String firstName, String lastName) {
    	this.userId = userId;
    	this.firstName = firstName;
    	this.lastName = lastName;
    }

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public LocalDate getTerminatedDate() {
		return terminatedDate;
	}

	public void setTerminatedDate(LocalDate terminatedDate) {
		this.terminatedDate = terminatedDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsTemporaryPassword() {
		return isTemporaryPassword;
	}

	public void setIsTemporaryPassword(Boolean isTemporaryPassword) {
		this.isTemporaryPassword = isTemporaryPassword;
	}

	public void setCurrentPassword(UserPassword currentPassword) {
		this.currentPassword = currentPassword;
	}

	public UserPassword getCurrentPassword() {
		return currentPassword;
	}

	public List<UserPassword> getPasswords() {
		return passwords;
	}

	public void setPasswords(List<UserPassword> passwords) {
		this.passwords = passwords;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public byte[] getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(byte[] userPhoto) {
		this.userPhoto = userPhoto;
	}

	public List<Applicant> getApplicants() {
		return applicants;
	}

	public void setApplicants(List<Applicant> applicants) {
		this.applicants = applicants;
	}

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + "ID: " + this.userId;
    }

    @Override
    public boolean equals(Object user) {
        boolean isEqual;

        if (user == null) {
            isEqual = false;
        } else if (!(user instanceof User)) {
            isEqual = false;
        } else {
            User otherUser = (User) user;

            isEqual = this.userId.equals(otherUser.userId);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.userId.hashCode();
        return hash;
    }
}
