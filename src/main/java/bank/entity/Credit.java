package bank.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;

@Data
@Table (name = "CREDIT")
@Entity
public class Credit
{
    @Id
    @GeneratedValue (generator = "uuid2")
    @GenericGenerator (name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column (name = "LIMIT")
    private float limit;


    @NotNull
    @Column (name = "INSERT_RATE")
    private float interestRate;


}