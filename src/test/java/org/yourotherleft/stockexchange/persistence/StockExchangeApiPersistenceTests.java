package org.yourotherleft.stockexchange.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yourotherleft.stockexchange.persistence.entity.StockTransaction;
import org.yourotherleft.stockexchange.persistence.repository.StockTransactionRepository;
import org.yourotherleft.stockexchange.persistence.type.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Integration tests to prove functionality in the persistence tier.
 *
 * @author jallen
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockExchangeApiPersistenceTests {

    @Autowired
    private StockTransactionRepository stockTransactionRepository;

    @Test
    public void testCrud() {
        // create a transaction and save it
        final StockTransaction newStockTransaction = new StockTransaction();
        newStockTransaction.setIdentifier(UUID.randomUUID().toString());
        newStockTransaction.setTransactionType(TransactionType.SELL);
        newStockTransaction.setStockSymbol("AAPL");
        newStockTransaction.setCurrency("USD");
        newStockTransaction.setAmount(5.0d);
        newStockTransaction.setShares(1.0d);
        final StockTransaction savedStockTransaction = stockTransactionRepository.save(newStockTransaction);

        // look up by id and validate
        final Optional<StockTransaction> transactionByIdOptional = stockTransactionRepository.findById(savedStockTransaction.getId());
        assertTrue(transactionByIdOptional.isPresent());
        final StockTransaction stockTransactionById = transactionByIdOptional.get();
        assertEquals(stockTransactionById.getIdentifier(), newStockTransaction.getIdentifier());

        // update the transaction type
        stockTransactionById.setTransactionType(TransactionType.BUY);
        stockTransactionRepository.save(stockTransactionById);

        // look up by id and validate the transaction type was updated
        final Optional<StockTransaction> transactionByIdOptionalAfterUpdate = stockTransactionRepository.findById(savedStockTransaction.getId());
        assertTrue(transactionByIdOptionalAfterUpdate.isPresent());
        final StockTransaction stockTransactionByIdAfterUpdate = transactionByIdOptionalAfterUpdate.get();
        assertEquals(stockTransactionByIdAfterUpdate.getTransactionType(), TransactionType.BUY);

        // delete it
        stockTransactionRepository.delete(stockTransactionById);

        // look up by id again and validate that it's gone
        final Optional<StockTransaction> transactionByIdOptionalAfterDelete = stockTransactionRepository.findById(savedStockTransaction.getId());
        assertFalse(transactionByIdOptionalAfterDelete.isPresent());
    }

    @Test
    public void testFinds() {
        // create some transactions
        final List<StockTransaction> transactions = new ArrayList<>();
        final StockTransaction newStockTransactionNikeOne = new StockTransaction();
        newStockTransactionNikeOne.setIdentifier(UUID.randomUUID().toString());
        newStockTransactionNikeOne.setTransactionType(TransactionType.SELL);
        newStockTransactionNikeOne.setStockSymbol("NIKE");
        newStockTransactionNikeOne.setCurrency("USD");
        newStockTransactionNikeOne.setAmount(5.0d);
        newStockTransactionNikeOne.setShares(1.0d);
        transactions.add(newStockTransactionNikeOne);
        final StockTransaction newStockTransactionNikeTwo = new StockTransaction();
        newStockTransactionNikeTwo.setIdentifier(UUID.randomUUID().toString());
        newStockTransactionNikeTwo.setTransactionType(TransactionType.SELL);
        newStockTransactionNikeTwo.setStockSymbol("NIKE");
        newStockTransactionNikeTwo.setCurrency("USD");
        newStockTransactionNikeTwo.setAmount(10.0d);
        newStockTransactionNikeTwo.setShares(2.0d);
        transactions.add(newStockTransactionNikeTwo);
        final StockTransaction newStockTransactionReebok = new StockTransaction();
        newStockTransactionReebok.setIdentifier(UUID.randomUUID().toString());
        newStockTransactionReebok.setTransactionType(TransactionType.SELL);
        newStockTransactionReebok.setStockSymbol("REEBOK");
        newStockTransactionReebok.setCurrency("USD");
        newStockTransactionReebok.setAmount(10.0d);
        newStockTransactionReebok.setShares(2.0d);
        transactions.add(newStockTransactionReebok);

        // save them all
        stockTransactionRepository.saveAll(transactions);

        // find by identifier
        final StockTransaction findByIdentifier = stockTransactionRepository.findByIdentifier(newStockTransactionNikeOne.getIdentifier());
        assertEquals(newStockTransactionNikeOne.getId(), findByIdentifier.getId());
        final StockTransaction findByIdentifierNotFound = stockTransactionRepository.findByIdentifier(UUID.randomUUID().toString());
        assertNull(findByIdentifierNotFound);

        // find by stock symbol
        final List<StockTransaction> findBySymbolUppercase = stockTransactionRepository.findByStockSymbol("NIKE");
        assertEquals(findBySymbolUppercase.size(), 2);
        final List<StockTransaction> findBySymbolLowercase = stockTransactionRepository.findByStockSymbol("nike");
        assertEquals(findBySymbolLowercase.size(), 2);
        final List<StockTransaction> findBySymbolBogus = stockTransactionRepository.findByStockSymbol("BOGUS");
        assertEquals(findBySymbolBogus.size(), 0);
    }
}
