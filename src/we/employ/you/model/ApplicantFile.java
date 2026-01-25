package we.employ.you.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "applicant_file", schema = "we_employ_you")
public class ApplicantFile implements Serializable {

	private static final long serialVersionUID = 708177716521542409L;

	@Id
    @Column(name = "file_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fileId;

	@Column(name = "filename")
	private String fileName;

	@ManyToOne
    @JoinColumn(name = "applicant_id", referencedColumnName = "applicant_id")
    @JsonIgnore
	private Applicant applicant;

	@Lob
	@Column(name = "file")
	@JsonIgnore
	private byte[] file;

	@Column(name = "resume_indicator")
    @Type(type = "yes_no")
	private boolean resumeIndicator;

	public ApplicantFile() {
		super();
	}

	public ApplicantFile(int fileId, String fileName) {
		this.fileId = fileId;
		this.fileName = fileName;
	}

	public ApplicantFile(int fileId, String fileName, boolean resumeIndicator) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.resumeIndicator = resumeIndicator;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public boolean isResumeIndicator() {
		return resumeIndicator;
	}

	public void setResumeIndicator(boolean resumeIndicator) {
		this.resumeIndicator = resumeIndicator;
	}

	@Override
	public String toString() {
		return fileName;
	}
}
