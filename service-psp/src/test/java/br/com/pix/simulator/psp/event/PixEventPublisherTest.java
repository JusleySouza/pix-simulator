package br.com.pix.simulator.psp.event;

import br.com.pix.simulator.psp.config.RabbitMQConfig;
import br.com.pix.simulator.psp.dto.event.TransactionEventResponse;
import br.com.pix.simulator.psp.message.PixEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PixEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PixEventPublisher pixEventPublisher;

    private final UUID transactionId = UUID.randomUUID();

    @Test
    @DisplayName("You must publish the debit event that was correctly performed.")
    void publishDebitMade_ShouldCallRabbitTemplate() {
        TransactionEventResponse response = new TransactionEventResponse(transactionId, true, null);

        pixEventPublisher.publishDebitMade(response);

        verify(rabbitTemplate, times(1)).convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_DEBIT_MADE,
                response
        );
    }

    @Test
    @DisplayName("You must publish the failed debit event.")
    void publishDebitFailed_ShouldCallRabbitTemplate() {
        TransactionEventResponse response = new TransactionEventResponse(transactionId, false, "Insufficient Balance");

        pixEventPublisher.publishDebitFailed(response);

        verify(rabbitTemplate, times(1)).convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.RK_DEBIT_FAILED,
                response
        );
    }

}
