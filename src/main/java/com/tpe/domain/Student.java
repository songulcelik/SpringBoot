package com.tpe.domain;
//1- student entity class olustur
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter//otomatik getter
@Setter//otomatik setter
/* field seviyede @Getter @Setter kullanbiliriz ama bu sefer her fieldda belirtmemiz gerekir */
@AllArgsConstructor//parametreli cons
@NoArgsConstructor//parametresiz cons
//@RequiredArgsConstructor//finalle setlenilen fieldlarla cons olusturmak icin
@Entity//dbde tablo oluusmasi icin
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)//id nin seti olusmasin
    private Long id; // int id : default 0 //non primitivede null alir

    @NotNull(message = "first name can not be null")
    @NotBlank(message = "first name can not be white space")
    @Size(min=2,max = 25,message = "First name '${validatedValue}' must be between {min} and {max} long")
    @Column(nullable = false, length = 25)//valide yaptigimiz halde column yaptik. valide kismi controllera gelmeden kontrol edilir
                                          // colunm kismi ise servicede requestte oynama yapilirsa kontrol edilsin diye. validationla isin basinda, column ile sonunda kontrol
    private /*final*/ String name; //

    @Column(nullable = false, length = 25)
    private /*final*/ String lastName;

    private /*final */Integer grade;

    @Column(nullable = false, length = 25, unique = true)
    @Email(message = "Provide valid email")//aaa@bbb.ccc formatinda olsun
    private /*final*/  String email;

    private/* final*/ String phoneNumber;

    @Setter(AccessLevel.NONE)//creatdate setlenmesin
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss", timezone = "Turkey")//clienta gidecek dosyayi formatliyorum
    private LocalDateTime createDate = LocalDateTime.now();


    @OneToMany(mappedBy = "student")

    private List<Book> books=new ArrayList<>();
    //2 olarak  StudentRepository

    //6
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
