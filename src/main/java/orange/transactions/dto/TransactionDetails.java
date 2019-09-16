package orange.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tr_details")
public class TransactionDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionDetailsIdgen")
    @SequenceGenerator(name = "transactionDetailsIdgen", sequenceName = "seqTransactionDetails")
    private Long trDetailsId;

    @NotEmpty(message = "Description may not be empty")
    @Column(name="description")
    private String description;

    @NotNull(message = "Amount may not be null")
    @Column(name="amount")
    private Integer amount;

    @Column(name="type")
    private String type;

}
