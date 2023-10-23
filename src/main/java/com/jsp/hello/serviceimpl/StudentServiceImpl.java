package com.jsp.hello.serviceimpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.hello.dto.MessageData;
import com.jsp.hello.dto.StudentRequest;
import com.jsp.hello.dto.StudentResponse;
import com.jsp.hello.entity.Student;
import com.jsp.hello.exception.StudentNotFoundByIdException;
import com.jsp.hello.repository.StudentRepo;
import com.jsp.hello.service.StudentService;
import com.jsp.hello.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentRepo repo;
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest) {
		Student student = new Student();
		student.setStudentName(studentRequest.getStudentName());
		student.setStudentEmail(studentRequest.getStudentEmail());
		student.setStudentGrade(studentRequest.getStudentGrade());
		student.setStudentPhNo(studentRequest.getStudentPhNo());
		student.setStudentPassword(studentRequest.getStudentPassword());
		Student student2 = repo.save(student);
		StudentResponse response = new StudentResponse();
		response.setStudentId(student2.getStudentId());
		response.setStudentName(student2.getStudentName());
		response.setStudentGrade(student2.getStudentGrade());
		ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Student data saved successfully!!");
		structure.setData(response);
		return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Student> updateStudent(Student student, int studentId) {
		Optional<Student> optional = repo.findById(studentId);
		if (optional.isPresent()) {
			Student student2 = optional.get();
			student.setStudentId(student2.getStudentId());
			Student student3 = repo.save(student);
			return new ResponseEntity<Student>(student3, HttpStatus.OK);
		} else {
			return null;
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<Student>> deleteStudent(int studentId) {
		Optional<Student> optional = repo.findById(studentId);
		if (optional.isPresent()) {
			repo.deleteById(studentId);
			ResponseStructure<Student> structure = new ResponseStructure<Student>();
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("Student data deleted successfully!!");
			structure.setData(optional.get());
			return new ResponseEntity<ResponseStructure<Student>>(structure, HttpStatus.OK);
		} else {
			throw new StudentNotFoundByIdException("Failed to delete the Student");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<Student>> findStudentbyId(int studentId) {
		Optional<Student> optional = repo.findById(studentId);
		if (optional.isPresent()) {
			Student student = optional.get();
			ResponseStructure<Student> structure = new ResponseStructure<>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("data found successfully");
			structure.setData(student);
			return new ResponseEntity<ResponseStructure<Student>>(structure, HttpStatus.OK);
		} else
			return null;
	}

	@Override
	public ResponseEntity<List<Student>> findAllStudents() {
		return null;
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(String studentEmail) {
		Student student = repo.findByStudentEmail(studentEmail);
		if (student != null) {
			StudentResponse response = new StudentResponse();
			response.setStudentId(student.getStudentId());
			response.setStudentName(student.getStudentName());
			response.setStudentGrade(student.getStudentGrade());
			ResponseStructure<StudentResponse> structure = new ResponseStructure<StudentResponse>();
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("Student Found based on Email!!");
			return new ResponseEntity<ResponseStructure<StudentResponse>>(structure, HttpStatus.FOUND);

		} else {
			// throw new studentNoFoundByEmail("Failed to find Student");
			return null;
		}

	}

	@Override
	public List<String> getAllEmailsByGrade(String grade) {

		return null;

	}

	@Override
	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		for (Sheet sheet : workbook) {
			for (Row row : sheet) {
				if (row.getRowNum() > 0) {
					if (row != null) {
						String name = row.getCell(0).getStringCellValue();
						String email = row.getCell(1).getStringCellValue();
						long phoneNumber = (long) row.getCell(2).getNumericCellValue();
						String grade = row.getCell(3).getStringCellValue();
						String password = row.getCell(4).getStringCellValue();
						System.out.println(name + ", " + email + ", " + phoneNumber + ", " + grade + ", " + password);

						Student student = new Student();
						student.setStudentName(name);
						student.setStudentEmail(email);
						student.setStudentGrade(grade);
						student.setStudentPassword(password);
						student.setStudentPhNo(phoneNumber);
						repo.save(student);
					}

				}
			}
		}
		workbook.close();
		return null;
	}

	@Override
	public ResponseEntity<String> writeToExcel(String filePath) throws IOException {
		List<Student> students = repo.findAll();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("studentId");
		header.createCell(1).setCellValue("studentName");
		header.createCell(2).setCellValue("studentEmail");
		header.createCell(3).setCellValue("studentPhNo");
		header.createCell(4).setCellValue("studentGrade");
		header.createCell(5).setCellValue("studentPassword");
		int rowNum = 1;
		for (Student student : students) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(student.getStudentId());
			row.createCell(1).setCellValue(student.getStudentName());
			row.createCell(2).setCellValue(student.getStudentEmail());
			row.createCell(3).setCellValue(student.getStudentPhNo());
			row.createCell(4).setCellValue(student.getStudentGrade());
			row.createCell(5).setCellValue(student.getStudentPassword());

		}
		FileOutputStream outputStream = new FileOutputStream(filePath);
		workbook.write(outputStream);
		workbook.close();

		return new ResponseEntity<String>("Data transfered to Excel sheet!!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> writeToCsv(String filePath) throws IOException {
		List<Student> students = repo.findAll();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("studentId");
		header.createCell(1).setCellValue("studentName");
		header.createCell(2).setCellValue("studentEmail");
		header.createCell(3).setCellValue("studentPhNo");
		header.createCell(4).setCellValue("studentGrade");
		header.createCell(5).setCellValue("studentPassword");
		int rowNum = 1;
		for (Student student : students) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(student.getStudentId());
			row.createCell(1).setCellValue(student.getStudentName());
			row.createCell(2).setCellValue(student.getStudentEmail());
			row.createCell(3).setCellValue(student.getStudentPhNo());
			row.createCell(4).setCellValue(student.getStudentGrade());
			row.createCell(5).setCellValue(student.getStudentPassword());

		}
		FileOutputStream outputStream = new FileOutputStream(filePath);
		workbook.write(outputStream);
		workbook.close();

		return new ResponseEntity<String>("Data transfered to Excel sheet!!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMail(MessageData messageData) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		message.setText(messageData.getText() + "\n\nThanks & Regards" + "\n" + messageData.getSenderName() + "\n"
				+ messageData.getSenderAddress());
		message.setSentDate(new Date());
		javaMailSender.send(message);
		return new ResponseEntity<String>("Mail send successfully!!", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> sendMimeMessage(MessageData messageData) throws MessagingException {
		MimeMessage mime = javaMailSender.createMimeMessage();// internet pack
		MimeMessageHelper message = new MimeMessageHelper(mime, true);// multipart file->true
		message.setTo(messageData.getTo());
		message.setSubject(messageData.getSubject());
		String emailBody = messageData.getText() + "<br><hr><h4>Thanks & Regards</h4>" + "<h4>"
				+ messageData.getSenderName() + "</h4>" + "<h4>" + messageData.getSenderAddress() + "</h4>"
				+ "<img src=\"https://jspiders.com/_nuxt/img/logo_jspiders.3b552d0.png\" width=\"200\">";
		message.setText(emailBody, true);
		message.setSentDate(new Date());
		javaMailSender.send(mime);
		return new ResponseEntity<String>("Mime messages snt successfully!!", HttpStatus.OK);
	}

}
