package we.employ.you.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "company", schema = "we_employ_you")
public class Company implements Serializable {

	private static final long serialVersionUID = -8503688705288509056L;

	@Id
    @Column(name = "company_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int companyId;

	@Column(name = "company_name")
    private String companyName;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employer")
	@JsonIgnore
	private List<Applicant> applicants;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	@JsonIgnore
	private List<Interview> interviews;

    public Company() {
        super();
        this.companyName = "";
    }

    public Company(int companyId) {
    	this.companyId = companyId;
    }

    public Company(int companyId, String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public Company(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Applicant> getApplicants() {
		return applicants;
	}

	public void setApplicants(List<Applicant> applicants) {
		this.applicants = applicants;
	}

	public List<Interview> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<Interview> interviews) {
		this.interviews = interviews;
	}

	@JsonIgnore
	public boolean isNewCompany() {
        return this.companyId == 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.companyId;
        return hash;
    }

    @Override
    public boolean equals(Object company) {
        boolean isEqual;

        if (company == null) {
            isEqual = false;
        } else if (!(company instanceof Company)) {
            isEqual = false;
        } else {
            Company other = (Company) company;

            isEqual = this.companyName.toUpperCase().equals(other.companyName.toUpperCase());
        }

        return isEqual;
    }

    @Override
    public String toString() {
        return this.companyName + " ID: " + this.companyId;
    }
}
