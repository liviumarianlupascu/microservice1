package orange.transactions.dto;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
class AccountId implements Serializable {

    private String cnp;
    private String iban;
}
