package com.jsp.hello.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.hello.dto.MessageData;
import com.jsp.hello.dto.StudentRequest;
import com.jsp.hello.dto.StudentResponse;
import com.jsp.hello.entity.Student;
import com.jsp.hello.service.StudentService;
import com.jsp.hello.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {
	@Autowired
	private StudentService service;

	// @RequestMapping(method=RequestMethod.POST,value="student")
	// @PostMapping("/student")
	@PostMapping
	public ResponseEntity<ResponseStructure<StudentResponse>> saveStudent(
			@RequestBody @Valid StudentRequest studentRequest) {
		return service.saveStudent(studentRequest);
	}

	// @PutMapping("/student")
	// @PutMapping("/student/{studentId}")
	@PutMapping("/{studentId}")
	// public ResponseEntity<Student> updateStudent(@RequestBody Student
	// student,@RequestParam int studentId){
	public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable int studentId) {
		return service.updateStudent(student, studentId);
	}

	@DeleteMapping("/{studentId}")
	public ResponseEntity<ResponseStructure<Student>> deleteStudent(@PathVariable int studentId) {
		return service.deleteStudent(studentId);
	}
	@GetMapping("/id/{studentId}")
	public ResponseEntity<ResponseStructure<Student>> findStudentbyId(@PathVariable int studentId) {
		return service.findStudentbyId(studentId);
	}

	@GetMapping
	@CrossOrigin//to connect frontend
	public ResponseEntity<List<Student>> GetAllSTudents() {
		return service.findAllStudents();
	}

	@GetMapping(params = "email")
	public ResponseEntity<ResponseStructure<StudentResponse>> findByEmail(@RequestParam String email) {
		return service.findByEmail(email);
	}

	// @PostMapping("/extract/file/{file}")
	@PostMapping("/extract")
	public ResponseEntity<String> ExtractDataFromExcel(@RequestParam MultipartFile file) throws IOException {
		return service.extractDataFromExcel(file);
	}
	/*
	 * Read the data from excel  file and save it to the database dependency :-
	 * org.apache.commos->1.8
	 */
	@PostMapping("/write/excel") // path in postman :->%3A and \->%2F
	public ResponseEntity<String> writeToExcel(@RequestParam String filePath) throws IOException {
		return service.writeToExcel(filePath);
	}
	/*
	 * Read the data from  csv file and save it to the database dependency :-
	 * org.apache.commos->1.8
	 */
	@PostMapping("/write/csv") // path in postman :->%3A and \->%2F
	public ResponseEntity<String> writeToCsv(String filePath) throws IOException {
		return service.writeToExcel(filePath);
	}
	@PostMapping("/mail")
	public ResponseEntity<String> sendMail(@RequestBody MessageData messageData){
		return service.sendMail(messageData);
	}
	@PostMapping("/mime-message")
	public ResponseEntity<String> sendMimeMessage(@RequestBody MessageData messageData)throws MessagingException {
		return service.sendMimeMessage(messageData);
	}
}
