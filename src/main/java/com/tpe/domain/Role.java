package com.tpe.domain;

import com.tpe.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
//---3-----
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity//rollerimin entityde kayitli olmasi lazim
@Table(name = "tbl_role")
public class Role {//useri userrole (enum) ile baglicam.enum degisirse user etkilenir o nedenle araya kopru koyduk kopru=role.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)//enum type ile bir classi iliskilendirmek istiyorsak @Enumerated
    @Column(length = 30, nullable = false)
    private UserRole name;// namenindata turu enum oldu. hataya kapali. ikisinden birini alcak

    public String toString(){
        return "Role [name = " +name+ "]";

    }

}
