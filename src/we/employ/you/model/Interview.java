package we.employ.you.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.type.YesNoConverter;

/**
 * This class represents the INTERVIEW table in the database.
 */
@Entity
@Table(name = "interview", schema = "we_employ_you")
public class Interview implements Serializable {

	private static final long serialVersionUID = -8258328200629041522L;

	@Id
    @Column(name = "interview_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int interviewId;

	@ManyToOne
	@JoinColumn(name = "applicant_id", referencedColumnName = "applicant_id", updatable = false)
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Applicant applicant;

	@ManyToOne
	@JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private Company company;

	@Column(name = "job_position")
    private String jobPosition;

	@ManyToOne
	@JoinColumn(name = "contact_id", referencedColumnName = "contact_id")
    private Contact contact;

	@Column(name = "interview_date")
    private LocalDateTime interviewDate;

	@JsonIgnore
	@Transient
    private String formattedInterviewDate = "N/A";

	@Column(name = "applicant_attend_ind", nullable = true)
	@Convert(converter = YesNoConverter.class)
    private boolean attendInterviewIndicator;

	@Column(name = "successful_hire_ind", nullable = true)
	@Convert(converter = YesNoConverter.class)
    private boolean successfulHireIndicator;

    /**
     * Default constructor.
     */
    public Interview() {
        super();
    }

    /**
     * Overloaded customer, which constructs an Interview object with the
     * appointment ID and title initialized.
     * @param interviewId the interview ID to set
     */
    public Interview(int interviewId) {
        this.interviewId = interviewId;
    }

    public int getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(int interviewId) {
		this.interviewId = interviewId;
	}

	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public LocalDateTime getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(LocalDateTime interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getFormattedInterviewDate() {
		return formattedInterviewDate;
	}

	public void setFormattedInterviewDate(String formattedInterviewDate) {
		this.formattedInterviewDate = formattedInterviewDate;
	}

	public boolean isAttendInterviewIndicator() {
		return attendInterviewIndicator;
	}

	public void setAttendInterviewIndicator(boolean attendInterviewIndicator) {
		this.attendInterviewIndicator = attendInterviewIndicator;
	}

	public boolean isSuccessfulHireIndicator() {
		return successfulHireIndicator;
	}

	public void setSuccessfulHireIndicator(boolean successfulHireIndicator) {
		this.successfulHireIndicator = successfulHireIndicator;
	}

	@Override
    public String toString() {
        return this.company.getCompanyName();
    }

    @Override
    public boolean equals(Object interview) {
        boolean isEqual;

        if (interview == null) {
            isEqual = false;
        } else if (!(interview instanceof Interview)) {
            isEqual = false;
        } else {
            Interview otherInterview = (Interview) interview;

            isEqual = this.interviewId == otherInterview.interviewId;
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.interviewId);
        return hash;
    }
}
