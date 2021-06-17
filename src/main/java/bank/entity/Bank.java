package bank.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "BANKS")
public class Bank
{
    @Id
    @GeneratedValue (generator = "uuid2")
    @GenericGenerator (name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column
    @NotNull
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (name="bank_clients",
            joinColumns={@JoinColumn (name="bank_id")},
            inverseJoinColumns={@JoinColumn(name="client_id")})
    private Set<Client> clients ;

    @ManyToMany(mappedBy = "banks", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Credit> credits ;

    public String getClientsString(){
        StringBuilder stringForView = new StringBuilder();
        for (Client client:clients)
        {
            stringForView.append(client.getSurname())
                    .append(" ")
                    .append(client.getName())
                    .append(" ")
                    .append(client.getPatronymic())
                    .append("\n");
        }
        return stringForView.toString();
    }

    public String getCreditsString(){
        StringBuilder stringForView = new StringBuilder();
        for (Credit credit:credits)
        {
            stringForView.append("limit:")
                    .append(" ")
                    .append(credit.getLimit())
                    .append(", percent")
                    .append(credit.getPercent())
                    .append("\n");
        }
        return stringForView.toString();
    }

}
