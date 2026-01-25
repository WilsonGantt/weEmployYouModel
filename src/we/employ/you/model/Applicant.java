package we.employ.you.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "applicant", schema = "we_employ_you")
public class Applicant extends Response implements Serializable {

	private static final long serialVersionUID = -1017728626257154493L;

	@Id
    @Column(name = "applicant_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicantId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

	@Column(name = "job_title")
    private String jobTitle;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email")
	private String email;

	@JsonIgnore
	@Column(name = "begin_date", updatable = false)
    private LocalDateTime beginDate;

	@Column(name = "contract_to_hire_ind", nullable = true)
    @Type(type = "yes_no")
    private boolean contractToHireIndicator;

	@JsonIgnore
	@Transient
	private String contractToHire;

	@Column(name = "hire_date", nullable = true)
    private LocalDate hireDate;

	@Lob
	@Column(name = "applicant_photo", updatable = false)
	@Basic(optional = true, fetch = FetchType.LAZY)
	@JsonIgnore
	private byte[] applicantPhoto;

	@JsonIgnore
	@Transient
	private boolean hasPhoto;

	@ManyToOne
    @JoinColumn(name = "employer_id", referencedColumnName = "company_id")
    private Company employer;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "applicant",
            cascade = CascadeType.REMOVE)
    private List<Interview> interviews;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "applicant",
            cascade = CascadeType.REMOVE)
	private List<ApplicantFile> applicantFiles;

	@JsonIgnore
	@Transient
	private boolean pendingInterviewsOnly;

    public Applicant() {
        super();
    }

    public Applicant(int applicantId) {
        this.applicantId = applicantId;
    }

    public Applicant(String firstName, String lastName) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    }

    public Applicant(int applicantId, String firstName, String lastName) {
    	this.applicantId = applicantId;
    	this.firstName = firstName;
    	this.lastName = lastName;
    }
    
    public int getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(int applicantId) {
		this.applicantId = applicantId;
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

	public User getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(User recruiter) {
		this.recruiter = recruiter;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
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

	public LocalDateTime getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDateTime beginDate) {
		this.beginDate = beginDate;
	}

	public boolean isContractToHireIndicator() {
		return contractToHireIndicator;
	}

	public void setContractToHireIndicator(boolean contractToHireIndicator) {
		this.contractToHireIndicator = contractToHireIndicator;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public byte[] getApplicantPhoto() {
		return applicantPhoto;
	}

	public void setApplicantPhoto(byte[] applicantPhoto) {
		this.applicantPhoto = applicantPhoto;
	}

	public String getContractToHire() {
		return contractToHire;
	}

	public void setContractToHire(String contractToHire) {
		this.contractToHire = contractToHire;
	}

	public boolean isHasPhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(boolean hasPhoto) {
		this.hasPhoto = hasPhoto;
	}

	public Company getEmployer() {
		return employer;
	}

	public void setEmployer(Company employer) {
		this.employer = employer;
	}

	public List<Interview> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<Interview> interviews) {
		this.interviews = interviews;
	}

	public List<ApplicantFile> getApplicantFiles() {
		return applicantFiles;
	}

	public void setApplicantFiles(List<ApplicantFile> applicantFiles) {
		this.applicantFiles = applicantFiles;
	}

	public boolean isPendingInterviewsOnly() {
		return pendingInterviewsOnly;
	}

	public void setPendingInterviewsOnly(boolean pendingInterviewsOnly) {
		this.pendingInterviewsOnly = pendingInterviewsOnly;
	}

	public boolean hasResume() {
		if (this.applicantFiles == null) {
			return false;
		} else {
			return this.applicantFiles.stream().anyMatch(file -> file.isResumeIndicator());
		}
	}

	@Override
    public boolean equals(Object applicant) {
        boolean isEqual;

        if (applicant == null) {
            isEqual = false;
        } else if (!(applicant instanceof Applicant)) {
            isEqual = false;
        } else {
            Applicant otherApplicant = (Applicant) applicant;

            isEqual = this.applicantId == otherApplicant.applicantId;
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.applicantId;
        return hash;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " ID: " + this.applicantId;
    }
}
