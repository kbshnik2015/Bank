package bank.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;

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
    private Set<Bank> clientBanks = new HashSet<>();

    public String getStringBanks() {
        Set<Bank> banks = getClientBanks();
        StringBuilder stringForView = new StringBuilder();
        for (Bank bank:banks)
        {
            stringForView.append(bank.getName())
                    .append(",")
                    .append("\n");
        }
        return String.valueOf(stringForView);
    }

}