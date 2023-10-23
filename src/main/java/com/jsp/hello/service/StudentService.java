package com.jsp.hello.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.hello.dto.MessageData;
import com.jsp.hello.dto.StudentRequest;
import com.jsp.hello.dto.StudentResponse;
import com.jsp.hello.entity.Student;
import com.jsp.hello.util.ResponseStructure;

import jakarta.mail.MessagingException;

public interface StudentService {
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(StudentRequest studentRequest);

	public ResponseEntity<Student> updateStudent(Student student, int studentId);

	public ResponseEntity<ResponseStructure<Student>> deleteStudent(int studentId);

	public ResponseEntity<ResponseStructure<Student>> findStudentbyId(int studentId);

	public ResponseEntity<List<Student>> findAllStudents();

	// find by email
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(String studentEmail);

	public List<String> getAllEmailsByGrade(String grade);

	public ResponseEntity<String> extractDataFromExcel(MultipartFile file) throws IOException;

	public ResponseEntity<String> writeToExcel(String filePath) throws IOException;
	
	public ResponseEntity<String> writeToCsv(String filePath) throws IOException;

	public ResponseEntity<String> sendMail(MessageData messageData);

	public ResponseEntity<String> sendMimeMessage(MessageData messageData) throws MessagingException;

}
