package we.employ.you.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company_interviews", schema = "we_employ_you")
public class CompanyInterviews implements Serializable {

	private static final long serialVersionUID = -5013117996639133627L;

	@Id
    @Column(name = "interview_id")
	private int interviewId;
	
    @Column(name = "company_id")
	private int companyId;
    
    @Column(name = "contact_id")
	private int contactId;
    
    public CompanyInterviews() {
    	super();
    }
    
    public CompanyInterviews(int interviewId, int companyId, int contactId) {
    	this.interviewId = interviewId;
    	this.companyId = companyId;
    	this.contactId = contactId;
    }

	public int getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(int interviewId) {
		this.interviewId = interviewId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
}
