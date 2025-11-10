package br.com.pix.simulator.psp.event;

import br.com.pix.simulator.psp.dto.event.TransactionEventRequest;
import br.com.pix.simulator.psp.dto.event.TransactionEventResponse;
import br.com.pix.simulator.psp.exception.InsufficientBalanceException;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.message.PixEventListener;
import br.com.pix.simulator.psp.message.PixEventPublisher;
import br.com.pix.simulator.psp.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PixEventListenerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private PixEventPublisher publisher;

    @InjectMocks
    private PixEventListener pixEventListener;

    private TransactionEventRequest request;
    private UUID transactionId;
    private UUID originAccount;
    private UUID destinationAccount;
    private BigDecimal value;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();
        originAccount = UUID.randomUUID();
        destinationAccount = UUID.randomUUID();
        value = BigDecimal.TEN;

        request = new TransactionEventRequest(
                transactionId,
                originAccount,
                destinationAccount,
                value
        );
    }

    // --- onRequestedDebit tests---

    @Test
    @DisplayName("[Debit] Must successfully process debit and publish event DEBIT_MADE")
    void onRequestedDebit_WhenSuccess_ShouldPublishDebitMade() {

        doNothing().when(accountService).processDebit(originAccount, value);

        pixEventListener.onRequestedDebit(request);

        verify(accountService, times(1)).processDebit(originAccount, value);

        verify(publisher, times(1)).publishDebitMade(any(TransactionEventResponse.class));

        verify(publisher, never()).publishDebitFailed(any());
    }

}
