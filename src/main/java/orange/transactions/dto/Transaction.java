package orange.transactions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name ="transaction")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionsIdgen")
    @SequenceGenerator(name = "transactionsIdgen", sequenceName = "seqTransactions")
    @Column(name ="transactionId")
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="cnpPayee", referencedColumnName="cnp"),
            @JoinColumn(name="ibanPayee", referencedColumnName="iban")
    })
    private Account payee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="cnpPayer", referencedColumnName="cnp"),
            @JoinColumn(name="ibanPayer", referencedColumnName="iban")
    })
    private Account payer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionDetailsId")
    private TransactionDetails transactionDetails;
}


