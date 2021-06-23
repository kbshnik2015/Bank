package bank.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.lang.annotation.Around;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@Table (name = "ONE_TIME_PAYMENTS")
@Entity
@NoArgsConstructor
public class OneTimePayment
{
    public OneTimePayment(Date payDay, double amountOfPayment, double loanBodyAmount, double interestRepaymentAmount)
    {
        this.payDay = payDay;
        this.amountOfPayment = amountOfPayment;
        this.loanBodyAmount = loanBodyAmount;
        this.interestRepaymentAmount = interestRepaymentAmount;
    }

    @Id
    @GeneratedValue (generator = "uuid2")
    @GenericGenerator (name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column
    @NotNull
    private Date payDay;

    @Column
    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double amountOfPayment; // общая сумма платежа

    @Column
    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double loanBodyAmount; // сумма гашения тела кредита

    @Column
    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private double interestRepaymentAmount; // сумма погошения процентов

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE )
    @JoinColumn(name = "credit_offer_id", nullable = false)
    private CreditOffer creditOffer;

}