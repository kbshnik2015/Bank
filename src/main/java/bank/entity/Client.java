package bank.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name="bank_clients",
            joinColumns={@JoinColumn(name="client_id")},
            inverseJoinColumns={@JoinColumn(name="bank_id")})
    private Set<Bank> banks = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", cascade ={ CascadeType.MERGE, CascadeType.REMOVE })
    private Set<CreditOffer> creditOffers;

    public String getStringBanks() {
        Set<Bank> banks = getBanks();
        StringBuilder stringForView = new StringBuilder();
        for (Bank bank:banks)
        {
            stringForView.append(bank.getName())
                    .append(",")
                    .append("\n");
        }
        return String.valueOf(stringForView);
    }

    @Override
    public String toString() {
        return
                this.name +" "+ this.surname +" "+ this.patronymic  +"; "+
                        " Phone: "  + this.phone  +"; "+
                        " E-mail: " + this.email  +"; "+
                        " Passport number: " + this.passportNumber +";";
    }

}