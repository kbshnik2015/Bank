package bank.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table (name = "CLIENTS")
public class Client
{

    @Id
    @GeneratedValue (generator = "uuid2")
    @GenericGenerator (name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String surname;

    @Column
    private String patronymic;

    @NotNull
    @Column
    private String phone;

    @NotNull
    @Column
    private String email;

    @NotNull
    @Column
    private String passportNumber;
}