package we.employ.you.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * This class represents the CONTACTS table in the database.
 */
@Entity
@Table(name = "contact", schema = "we_employ_you")
public class Contact implements Serializable {

	private static final long serialVersionUID = -6895103709456574655L;

	@Id
    @Column(name = "contact_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int contactId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	@OneToOne
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    @JsonIgnore
    private Company company;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contact")
	@JsonIgnore
	private List<Interview> interviews;

    /**
     * Default constructor.
     */
    public Contact() {
        super();
    }

    public Contact(int contactId) {
    	this.contactId = contactId;
    }

    public Contact(int contactId, String firstName, String lastName) {
    	this.contactId = contactId;
    	this.firstName = firstName;
    	this.lastName = lastName;
    }

    public Contact(String firstName, String lastName) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    }
    
    public int getContactId() {
		return contactId;
	}


	public void setContactId(int contactId) {
		this.contactId = contactId;
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Interview> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<Interview> interviews) {
		this.interviews = interviews;
	}

	public boolean isNewContact() {
		return this.contactId == 0;
	}
	
	@Override
    public boolean equals(Object contact) {
        boolean isEqual;

        if (contact == null) {
            isEqual = false;
        } else if (!(contact instanceof Contact)) {
            isEqual = false;
        } else {
            Contact otherContact = (Contact) contact;

            isEqual = this.firstName.toUpperCase().equals(otherContact.firstName.toUpperCase()) &&
            		this.lastName.toUpperCase().equals(otherContact.lastName.toUpperCase());
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.contactId;
        return hash;
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
