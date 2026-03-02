package we.employ.you.model;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.type.YesNoConverter;

@Entity
@Table(name = "applicant_file", schema = "we_employ_you")
public class ApplicantFile implements Serializable {

	@Serial
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
	@Convert(converter = YesNoConverter.class)
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
