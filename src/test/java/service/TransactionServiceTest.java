package service;

import model.Transaction;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.TransactionRepository;
import repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sender = new User();
        sender.setId(1);
        sender.setBalance(100.0); // Solde suffisant pour la transaction

        receiver = new User();
        receiver.setId(2);
        receiver.setBalance(50.0); // Solde initial du récepteur
    }

    @Test
    public void testGetTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(20.0);

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        Optional<Transaction> result = transactionService.getTransactionById(1);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(transaction);
    }

    @Test
    public void testDeleteTransactionById() {
        doNothing().when(transactionRepository).deleteById(1);

        transactionService.deleteTransactionById(1);

        verify(transactionRepository, times(1)).deleteById(1);
    }

    @Test
    public void testSaveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(20.0);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(userRepository.save(any(User.class))).thenReturn(sender).thenReturn(receiver);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        assertThat(savedTransaction).isEqualTo(transaction);
        assertThat(sender.getBalance()).isEqualTo(80.0); // Vérifie que le solde de l'expéditeur a été mis à jour
        assertThat(receiver.getBalance()).isEqualTo(70.0 - (20.0*0.005)); // Vérifie que le solde du récepteur a été mis à jour
        verify(transactionRepository, times(1)).save(transaction);
        verify(userRepository, times(1)).save(sender);
        verify(userRepository, times(1)).save(receiver);
    }

    @Test
    public void testSaveTransactionInsufficientFunds() {
        sender.setBalance(10.0); // Solde insuffisant

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(20.0);

        try {
            transactionService.saveTransaction(transaction);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Fonds insuffisants !");
        }

        // Vérifie que les soldes n'ont pas changé
        assertThat(sender.getBalance()).isEqualTo(10.0);
        assertThat(receiver.getBalance()).isEqualTo(50.0);
    }
}

