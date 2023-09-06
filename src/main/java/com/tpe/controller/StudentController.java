package com.tpe.controller;
//4- studentcontroller sinifini olustur
import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController//restfulapi yaptigim icin normal bir controllar degil
@RequestMapping("/students")  // URL,endpoint-->  http://localhost:8080/students     base path+end point
//Restfull bir mimari olması için yapıldı.. Requestler Http protokolleri ve url olacak sekilde http methodları ile gelecek;
// dönen değerde de http status code olacak; duruma göre exceptionlar handle edilecek
@RequiredArgsConstructor   //final olan fieldları constructor injection yapılacak
public class StudentController {

    //23
    /*
    loglama islemi icin logger classi/ slf4jden import
     */
    Logger logger= LoggerFactory.getLogger(StudentController.class);


    private final StudentService studentService;//servisi injeksin yaptik. controller servise gonderdigi icin

    // Not: getAll() *********************************
    @PreAuthorize("hasRole('ADMIN')")//bu methoddan once yetkisi var mi kontrol et.//enumda ROLE_ADMIN.
    @GetMapping  // http://localhost:8080/students  + Get
    public ResponseEntity<List<Student>> getAll(){//ResponseEntity: restful apide obje disinda ne doncek. status kod.ResponseEntity bu classla status kodda gelir

        List<Student> students = studentService.getAll();
        return ResponseEntity.ok(students); // 200 + students
        //return new ResponseEntity<>(students,HttpStatus.OK); ayni islem

    }

    //5
    //not:createStudent(). yeni student objesini olusturacak requesti karsilayavak kod
    @PostMapping //http://localhost:8080/students + post + json //jackson kutuphanesi bize gelen jsonlari java classina ceviriyor
    public ResponseEntity<Map<String,String >> createStudent(@RequestBody @Valid Student student){
        studentService.createStudent(student);
        Map<String ,String > map= new HashMap<>();
        map.put("message","Student is created successfully");
        map.put("status","true");
        return new ResponseEntity<>(map, HttpStatus.CREATED);//map+ 201
    }

    //7-- not: getByIdWithRequestParam
    @GetMapping("/query")//http://localhost:8080/students/query?id=1 + get
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id){ //@RequestParam URL'de yer alan veya form verilerindeki parametreleri almak için kullanılır
        Student student= studentService.findStudent(id);     //birden fazla variable endpointten alınacaksa request param ile almak daha kolay ve readable
        return ResponseEntity.ok(student);
    }
    //birden fazla data alcaksak requestparam kullanmak daha mantikli neyin ne old belli
    //9-- not: getByIdWithPath
    @GetMapping("{id}")//http://localhost:8080/students/1 + get
    public ResponseEntity<Student> getStudentWithPath(@PathVariable("id") Long id){
        Student student= studentService.findStudent(id);
        return ResponseEntity.ok(student);
    }

    //10 not:delete()
    @DeleteMapping("/{id}")//http://localhost:8080/students/1+delete
    public ResponseEntity<String> deleteStudent(@PathVariable("id") Long id){
        studentService.deleteStudent(id);
        String message="Student is deleted successfully";
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //12 not:update()//put ile petch arasindaki fark putmapping butun fieldlari setlemek lazim yoksa girilmeyeni null yapar
    //http://localhost:8080/students/1 + PUT + JSON

    @PutMapping("/{id}")     //PUTMAPPING hepsini set eder, doldurmadıklarınızı null yapar.. //PATCHMAPPING parçalı updatedir, eski verilere dokunmaz, verdikleriniz günceller
    public ResponseEntity<String> updateStudent(@PathVariable("id") Long id,@RequestBody StudentDTO studentDTO ){
        studentService.updateStudent(id, studentDTO);   //burada bir exception olursa kod buradan devam etmez, response exceptionu tasır

        String message="Student is updated succesfully";
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //16 not: pageable
 /*
    PAGINATION:
     */

    //http://localhost:8080/students/page?page=0&size=10sort=name&direction=ASC + GET
    @GetMapping("/page")
    public ResponseEntity<Page<Student>> getAllWithPage(       //Page, list gibi collection mantıgıyla çalışır   ????
                                                               @RequestParam("page") int page,   //kacıncı sayfa gelecek
                                                               @RequestParam("size") int size,   //her page de kac tane ürün olacak    bu ilk iki zorunlu
                                                               @RequestParam("sort") String prop,  //hangi fielda göre sıralama yapılacak
                                                               @RequestParam("direction")Sort.Direction direction    //naturel order mı reserve order mı
    ){
        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));  //Pagenation teknolojisini kullanabilmemiz için SpringDAtaJpa Pageable object istiyor.. repo da hazır method kullanacagız
        //controller olabildince logic olmamalı, service te bunu yapmalıydık.. objeler service tarafında oluşturulur
        Page<Student> studentPage= studentService.getAllWithPage(pageable);
        return ResponseEntity.ok(studentPage);
    }


    //18-- not:jpql
     /*
    JPQL:
    Her zaman SpringDataJPA nın hazır methodlarını kullanarak requestleri responselara ceviremem.. SQL, HQL oluşturabiliriz veya
    SpringDataJPA nın JPQL yazarak da yapabiliriz.
     */
    //75 puan alan ogrencileri getirelim
    @GetMapping("/grade/{grade}")//http://localhost:8080/students/grade/75 + get
    public ResponseEntity<List<Student>> getStudentsEqualsGrade(@PathVariable("grade") Integer grade){
        List<Student> students=studentService.findAllEqualsGrade(grade);
        return ResponseEntity.ok(students);
    }

    //21 dbdeb direk dto olarak verileri alabilir miyim
    @GetMapping("/query/dto")  //http://localhost:8080/student/query/dto?id=1   + GET
    public ResponseEntity<StudentDTO> getStudentDTO(@RequestParam("id") Long id){
        StudentDTO studentDTO= studentService.findStudentDtoById(id);
        return ResponseEntity.ok(studentDTO);
    }

    //24 // logger icin yazildi
    @GetMapping("/welcome")//http://localhost:8080/students/welcome + get
    public String welcome(HttpServletRequest request){
        logger.warn("-----------------------------Welcome {}", request.getServletPath());
        return "Welcome to Student Controller";

    }






}
