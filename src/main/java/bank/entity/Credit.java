package bank.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Table (name = "CREDITS")
@Entity

public class Credit
{
    @Id
    @GeneratedValue (generator = "uuid2")
    @GenericGenerator (name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Column
    private double limit;

    @NotNull
    @Column
    private double percent;

   @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Bank> banks = new HashSet<>();


}