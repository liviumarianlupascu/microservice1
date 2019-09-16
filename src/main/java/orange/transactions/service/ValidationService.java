package orange.transactions.service;

import orange.transactions.dto.Account;
import orange.transactions.dto.Transaction;
import orange.transactions.dto.TransactionDetails;
import orange.transactions.repository.AccountRepository;
import orange.transactions.repository.TransactionRepository;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ValidationService {

    private final static Logger LOGGER = Logger.getLogger(ValidationService.class.getName());
    private static final String PAYEE = "payee";
    private static final String PAYER = "payer";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public boolean validateTransaction(Transaction transaction) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);

        for (ConstraintViolation<Transaction> violation : violations) {
            LOGGER.severe(violation.getMessage());
        }
        if (violations.size() > 0) {
            return false;
        }
        return verifyTransaction(transaction.getTransactionDetails()) &&
               verifyAccount(transaction.getPayee(), PAYEE) &
               verifyAccount(transaction.getPayer(), PAYER);
    }

    private boolean verifyTransaction(TransactionDetails transactionDetails) {
        if (Optional.ofNullable(transactionDetails).isPresent()) {
            if (Optional.ofNullable(transactionDetails.getTrDetailsId()).isPresent()) {
                if (transactionRepository.findById(transactionDetails.getTrDetailsId()).isPresent()) {
                    LOGGER.severe("This transaction was already registered !");
                    return false;
                }
                return true;
            }
            else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean verifyAccount(Account account, String role) {
        if (Optional.ofNullable(account).isPresent()) {


            return checkIbanValidity(account.getIban(), role) && (checkCnpValidity(account.getCnp()));
        } else {
            LOGGER.severe("The transaction must have a " + role + " !");
            return false;
        }
    }

    private boolean checkIbanValidity(String ibanParameter, String ibanProvenance) {
        IBANCheckDigit ibanCheckDigit = new IBANCheckDigit();
        boolean result = ibanCheckDigit.isValid(ibanParameter);
        String option = ibanProvenance.equals(PAYEE) ? "payee !" : "payer !";
        String message = "Invalid IBAN for the " + option;
        if (!result) {
            LOGGER.severe(message);
        }
        return result;
    }

    private boolean checkCnpValidity(String cnp) {
        if (cnp.length()!=13) {
            LOGGER.severe("Invalid CNP - size !");
            return false;}
        for (int i=0; i<=12; i++){
            if (!Character.isDigit(cnp.charAt(i))){
                LOGGER.severe("Invalid CNP - digits !");
                return false;}
        }
        int tip = Integer.parseInt(cnp.substring(0,1));
        if ((tip<1)||(tip>5)){
            LOGGER.severe("Invalid CNP - sex !");
            return false;}

        int year = Integer.parseInt(cnp.substring(1,3));
        if (year<1){
            LOGGER.severe("Invalid CNP - year !");
            return false;}

        int month = Integer.parseInt(cnp.substring(3,5));
        if ((month<1)||(month>12)){
            LOGGER.severe("Invalid CNP - month !");
            return false;}

        int day = Integer.parseInt(cnp.substring(5,7));
        if ((day<1)||(day>31)){
            LOGGER.severe("Invalid CNP - day !");
            return false;}

        int judet = Integer.parseInt(cnp.substring(7,9));
        if ((judet<1)||(judet>50)){
            LOGGER.severe("Invalid CNP - judet !");
            return false;
        }
        return true;
    }
}
