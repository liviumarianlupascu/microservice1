package orange.transactions.controller;

import orange.transactions.dto.Transaction;
import orange.transactions.service.ExpeditionService;
import orange.transactions.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ExpeditionService expeditionService;


    @RequestMapping(value = "/enroll", method = RequestMethod.POST)
    public HttpEntity<String> enroll(@RequestBody Transaction transaction){
        if (!validationService.validateTransaction(transaction)) {
            return new ResponseEntity("Invalid request !", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(expeditionService.doWebserviceCallWithRetryCallback(transaction), HttpStatus.OK);
        }
    }


}
