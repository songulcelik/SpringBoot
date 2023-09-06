package com.tpe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//--1----
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity//dbde karsiligi olsun
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 25,nullable = false)
    private String firstName;

    @Column(length = 25,nullable = false)
    private String lastName;

    @Column(length = 25,nullable = false, unique = true)
    private String userName;

    //@Column(length = 25,nullable = false). ihtiyacimizi karsilamamz. passwoed daha fazla olmasi lazim max deger
    @Column(length = 255,nullable = false)
    private String password;

    //roller her zaman enum type olarak tanimlanir

    //4 --user classini role clasiyla iliskilendirmem lazim

    @JoinTable(name = "tbl_user_role", //3.tabloya ihtiyacimiz var
            joinColumns = @JoinColumn(name = "user_id"), //iliski sahibinden gelen sutun
            inverseJoinColumns = @JoinColumn(name = "role_id"))//digerinden
    @ManyToMany(fetch = FetchType.EAGER)//manytoone da olur. ikinci tarfa many ise lazy oluyordu defaultta.eager olsun
    private Set<Role> roles= new HashSet<>();//neden set roller unique olsun diye

    //5 studentle iliskilendirelim
    @JsonIgnore//7
    @OneToOne(mappedBy = "user")//bir userin bir studenti olur.student bir user olur. iliski sahibi student classi olsun
    private Student student;




}
