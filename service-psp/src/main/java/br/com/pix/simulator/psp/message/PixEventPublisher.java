package br.com.pix.simulator.psp.message;

import br.com.pix.simulator.psp.config.RabbitMQConfig;
import br.com.pix.simulator.psp.dto.event.TransactionEventResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PixEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PixEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public PixEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishDebitMade(TransactionEventResponse response) {
        log.info("Publishing DEBIT_COMPLETED event for transaction: {}", response.transactionID());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RK_DEBIT_MADE, response);
    }

    public void publishDebitFailed(TransactionEventResponse response) {
        log.warn("Publishing DEBIT_FAILED event for transaction: {}. Reason: {}", response.transactionID(), response.failureReason());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RK_DEBIT_FAILED, response);
    }

    public void publishCreditMade(TransactionEventResponse response) {
        log.info("Publishing CREDIT_COMPLETED event for transaction: {}", response.transactionID());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RK_CREDIT_MADE, response);
    }

    public void publishCreditFailed(TransactionEventResponse response) {
        log.warn("Publishing CREDIT_FAILED event for transaction: {}. Reason: {}", response.transactionID(), response.failureReason());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RK_CREDIT_FAILED, response);
    }
}
