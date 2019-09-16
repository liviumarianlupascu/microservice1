package orange.transactions.service;

import orange.transactions.dto.Transaction;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;


@Service
public class ExpeditionService {

    private final static Logger LOGGER = Logger.getLogger(ExpeditionService.class.getName());

    @Autowired
    private RetryTemplate retryTemplate;

    @Async
    public String doWebserviceCallWithRetryCallback(Transaction transaction) {
        try {
            retryTemplate.execute(new RetryCallback<String, RuntimeException>() {
                                      @Override
                                      public String doWithRetry(RetryContext retryContext) throws RuntimeException {
                                          URI uri = null;
                                          try {
                                              uri = new URIBuilder()
                                                      .setScheme("http")
                                                      .setHost("localhost:8082")
                                                      .setPath("/orange/enroll/transaction").build();
                                          } catch (URISyntaxException e) {
                                              e.printStackTrace();
                                          }
                                          HttpHeaders headers = new HttpHeaders();
                                          headers.setContentType(MediaType.APPLICATION_JSON);
                                          HttpEntity<Transaction> requestEntity = new HttpEntity<>(transaction, headers);
                                          ResponseEntity<Transaction> responseEntity = new RestTemplate().exchange(uri, HttpMethod.POST, requestEntity, Transaction.class);
                                          LOGGER.warning(String.valueOf(responseEntity.getStatusCode()));
                                          return String.valueOf(responseEntity.getStatusCode());
                                      }
                                  },
                    new RecoveryCallback<String>() {
                        @Override
                        public String recover(RetryContext retryContext) {
                            LOGGER.severe("Unsuccess. The retry reached maximum retry count :" + retryContext.getRetryCount());
                            return "Unsuccess. The retry reached maximum retry count :" + retryContext.getRetryCount();
                        }
                    });
        } catch (final RuntimeException e) {
            e.printStackTrace();
            LOGGER.severe("Exception was been occurred");
            return "Exception was been occurred";
        }
        LOGGER.info("Request sent to microservice 2 !");
        return "Request sent to microservice 2 !";
    }
}

