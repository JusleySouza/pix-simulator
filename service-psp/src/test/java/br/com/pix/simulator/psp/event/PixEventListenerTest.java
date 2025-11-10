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

    @Test
    @DisplayName("[Debit] You should catch the BalanceInsufficientException and post DEBIT_FAILED.")
    void onRequestedDebit_WhenBalanceInsufficient_ShouldPublishDebitFailed() {
        String errorMsg = "Insufficient balance";
        doThrow(new InsufficientBalanceException(errorMsg)).when(accountService).processDebit(originAccount, value);

        pixEventListener.onRequestedDebit(request);

        verify(accountService, times(1)).processDebit(originAccount, value);

        verify(publisher, never()).publishDebitMade(any());

        ArgumentCaptor<TransactionEventResponse> captor = ArgumentCaptor.forClass(TransactionEventResponse.class);
        verify(publisher, times(1)).publishDebitFailed(captor.capture());

        TransactionEventResponse response = captor.getValue();
        assertFalse(response.success());
        assertEquals(errorMsg, response.failureReason());
        assertEquals(transactionId, response.transactionID());
    }

    @Test
    @DisplayName("[Debit] You should catch the RuntimeException and post DEBIT_FAILED.")
    void onRequestedDebit_WhenRuntimeException_ShouldPublishDebitFailed() {
        String errorMsg = "Internal error in PSP";
        doThrow(new RuntimeException("DB offline")).when(accountService).processDebit(originAccount, value);

        pixEventListener.onRequestedDebit(request);

        verify(publisher, never()).publishDebitMade(any());

        ArgumentCaptor<TransactionEventResponse> captor = ArgumentCaptor.forClass(TransactionEventResponse.class);
        verify(publisher, times(1)).publishDebitFailed(captor.capture());
        assertEquals(errorMsg, captor.getValue().failureReason());
    }

    // --- onRequestedCredit tests ---

    @Test
    @DisplayName("[Credit] Must successfully process credit and publish event CREDIT_MADE")
    void onRequestedCredit_WhenSuccess_ShouldPublishCreditMade() {
        doNothing().when(accountService).processCredit(destinationAccount, value);

        pixEventListener.onRequestedCredit(request);

        verify(accountService, times(1)).processCredit(destinationAccount, value);
        verify(publisher, times(1)).publishCreditMade(any(TransactionEventResponse.class));
        verify(publisher, never()).publishCreditFailed(any());
    }

    @Test
    @DisplayName("[Credit] You should catch the ResourceNotFoundException and post CREDIT_FAILED.")
    void onRequestedCredit_WhenAccountNotFound_ShouldPublishCreditFailed() {
        String errorMsg = "Internal error in PSP";
        doThrow(new ResourceNotFoundException(errorMsg)).when(accountService).processCredit(destinationAccount, value);

        pixEventListener.onRequestedCredit(request);

        verify(accountService, times(1)).processCredit(destinationAccount, value);
        verify(publisher, never()).publishCreditMade(any());

        ArgumentCaptor<TransactionEventResponse> captor = ArgumentCaptor.forClass(TransactionEventResponse.class);
        verify(publisher, times(1)).publishCreditFailed(captor.capture());

        assertEquals(errorMsg, captor.getValue().failureReason());
    }

}
