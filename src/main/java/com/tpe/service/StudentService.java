package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

//3- studentservice olustur
@Service//bu zorunlu
@RequiredArgsConstructor//final ile isaretledigimiz fieldlardan cons uretiyor
public class StudentService {

    //@Autowired kullanmadik field injection yapmak icin cons kullanalim dedik
    private final StudentRepository studentRepository;


    // Not: getAll() *********************************
    public List<Student> getAll() {

        return studentRepository.findAll(); // SELECT * FROM student ;
    }

    //6--not:createStudent()
    public void createStudent(Student student) {
        //ilk olarak unique olan emaili kontrol ederiz
        if(studentRepository.existsByEmail(student.getEmail())){
            throw new ConflictException("Email is already exist!");
        }
        studentRepository.save(student);

    }

    //8 id varsa gondercek yoksa exception firlatcak
    //not: getByIdWithRequestParam

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Student not found with id: "+ id));
    }

    //11 not:delete()
    public void deleteStudent(Long id) {
    Student student=findStudent(id);
    studentRepository.delete(student);
    //studentRepository.deleteById(id); bu method olmayan id de patlar
    }

    //15 not:update
    public void updateStudent(Long id, StudentDTO studentDTO) {
        //1 idli ogrenci var mi
        Student student=findStudent(id);
        //e mail unique mi
        /*
        1)mevcut email : mrc, yeni mrc --> true
        2)mevcut email: mrc, yeni ahmt(dbde zaten var) --> false
        3)mevcut e mail : mrc, yeni mhmt(dbde yok) --> true
         */
        boolean emailExist=studentRepository.existsByEmail(studentDTO.getEmail());//dbde s.dtodan gelen email var mi yok mu
        if (emailExist && !studentDTO.getEmail().equals(student.getEmail())){
            throw new ConflictException("Email is already exist!");
        }
        student.setName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setGrade(studentDTO.getGrade());
        student.setEmail(studentDTO.getEmail());
        student.setPhoneNumber(studentDTO.getPhoneNumber());

        studentRepository.save(student);
    }

    //17
    public Page<Student> getAllWithPage(Pageable pageable) {

        return studentRepository.findAll(pageable);
    }

    //19
    public List<Student> findAllEqualsGrade(Integer grade) {

        return studentRepository.findAllEqualsGrade(grade);//findByGrade ile de olur
    }

    //22
    public StudentDTO findStudentDtoById(Long id) {
        return studentRepository.findStudentDtoById(id).orElseThrow(()->
                new ResourceNotFoundException("Student not found with id: "+ id));
    }
}
