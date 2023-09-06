package com.tpe.repository;
//2- repo olustur. poomda spring data jpa crud operasyonlarini yapiyor
import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Repository // optional. eklemesek de JpaRepositoryden extend edildigine gore bu repodur diyor. ama okunabilirleik icin @Repository
public interface StudentRepository extends JpaRepository<Student, Long> { // <hangi entity class, o classin primary keyi>

   //3 icin StudentService
    boolean existsByEmail(String email);

    //20
    @Query("SELECT s FROM Student s WHERE s.grade=:pGrade") // JPQL
    //@Query("SELECT s FROM Student s WHERE s.grade=:pGrade") // HQL
    //@Query(value = "SELECT * FROM Student s WHERE s.grade=:pGrade", nativeQuery = true) // native SQL
    List<Student> findAllEqualsGrade(@Param("pGrade") Integer grade);


    //23
    @Query("SELECT new com.tpe.dto.StudentDTO(s) FROM Student s WHERE s.id=:id") // StudentDTO st1 = new StudentDTO(Student);
    Optional<StudentDTO> findStudentDtoById(@Param("id") Long id);


}
