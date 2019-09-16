package orange.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "account")
@IdClass(AccountId.class)
public class Account implements Serializable {

    @Id
    @NotEmpty(message = "CNP may not be empty")
    @Column(name="cnp",length = 13)
    private String cnp;

    @Id
    @NotEmpty(message = "IBAN may not be empty")
    @Column(name="iban",length = 50)
    private String iban;

    @NotEmpty(message = "Name may not be empty")
    @Column(name="name")
    private String name;

}
