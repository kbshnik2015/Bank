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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "credit", cascade ={ CascadeType.MERGE, CascadeType.REMOVE })
    private Set<CreditOffer> creditOffers;

    @Override
    public String toString()
    {
        return "Bank: "+bank.getName()+", limit: "+ limit+", percent: "+percent;
    }
}