package bank.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Table (name = "credit_offer")
@Entity
@Getter
@Setter
public class CreditOffer
{
    @Id
    @GeneratedValue (generator = "uuid2")
    @GenericGenerator (name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne( cascade = CascadeType.MERGE )
    @NotNull
    private Client client;

    @ManyToOne( cascade = CascadeType.MERGE )
    @NotNull
    private Credit credit;

    @Column
    @NotNull
    private double creditAmount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "creditOffer", cascade = CascadeType.REMOVE)
    private List<OneTimePayment> paymentSchedule;

}