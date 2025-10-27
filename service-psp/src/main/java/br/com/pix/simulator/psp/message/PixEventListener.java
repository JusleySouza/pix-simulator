package br.com.pix.simulator.psp.message;

import br.com.pix.simulator.psp.config.RabbitMQConfig;
import br.com.pix.simulator.psp.dto.event.TransactionEventRequest;
import br.com.pix.simulator.psp.dto.event.TransactionEventResponse;
import br.com.pix.simulator.psp.exception.InsufficientBalanceException;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PixEventListener {

    private static final Logger log = LoggerFactory.getLogger(PixEventListener.class);

    private final AccountService accountService;
    private final PixEventPublisher publisher;

    public PixEventListener(AccountService accountService, PixEventPublisher publisher) {
        this.accountService = accountService;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REQUESTED_DEBIT)
    public void onRequestedDebit(TransactionEventRequest request) {
        log.info("Received REQUIRED_DEBIT event for transaction: {}", request.transactionId());

        try {
            accountService.processDebit(request.originAccount(), request.value());

            TransactionEventResponse response = new TransactionEventResponse(request.transactionId(), true, null);
            publisher.publishDebitMade(response);

        } catch (InsufficientBalanceException | ResourceNotFoundException e) {
            log.warn("Failed to process debit: {}", e.getMessage());
            TransactionEventResponse response = new TransactionEventResponse(request.transactionId(), false, e.getMessage());
            publisher.publishDebitFailed(response);

        } catch (Exception e) {
            log.error("Unexpected error processing debit: {}", request.transactionId(), e);
            TransactionEventResponse response = new TransactionEventResponse(request.transactionId(), false, "Internal error in PSP");
            publisher.publishDebitFailed(response);

        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_REQUESTED_CREDIT)
    public void onRequestedCredit(TransactionEventRequest request) {
        log.info("Received REQUIRED_CREDIT event for transaction: {}", request.transactionId());

        try {
            accountService.processCredit(request.destinationAccount(), request.value());

            TransactionEventResponse response = new TransactionEventResponse(request.transactionId(), true, null);
            publisher.publishCreditMade(response);

        } catch (EntityNotFoundException e) {
            log.warn("Failed to process credit: {}", e.getMessage());
            TransactionEventResponse response = new TransactionEventResponse(request.transactionId(), false, e.getMessage());
            publisher.publishCreditFailed(response);

        } catch (Exception e) {
            log.error("Unexpected error processing credit: {}", request.transactionId(), e);
            TransactionEventResponse response = new TransactionEventResponse(request.transactionId(), false, "Internal error in PSP");
            publisher.publishCreditFailed(response);
        }
    }
}
