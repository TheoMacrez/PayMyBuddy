package repository;

import model.*;
import org.springframework.data.repository.*;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
    List<Transaction> findBySender(User sender);
    List<Transaction> findByReceiver(User receiver);
}
