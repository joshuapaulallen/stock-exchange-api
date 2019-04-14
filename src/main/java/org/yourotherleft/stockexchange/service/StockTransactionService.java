package org.yourotherleft.stockexchange.service;

import org.yourotherleft.stockexchange.service.type.TransactionRequest;
import org.yourotherleft.stockexchange.service.type.TransactionResult;

import java.util.List;
import java.util.Optional;

/**
 * A service that allows users to complete stock transactions.
 *
 * @author jallen
 */
public interface StockTransactionService {

    /**
     * Buy some stock.
     *
     * @param transactionRequest The {@link TransactionRequest}.
     * @return A {@link TransactionResult}.
     */
    TransactionResult buy(TransactionRequest transactionRequest);

    /**
     * Find all transaction results.
     *
     * @return A list of all transaction results.
     */
    List<TransactionResult> findAll();

    /**
     * Find a transaction result with the given identifier.
     *
     * @param identifier The transaction identifier.
     * @return The transaction result with the given identifier, if one exists.
     */
    Optional<TransactionResult> findByIdentifier(String identifier);

    /**
     * Find all transaction results for the given stock.
     *
     * @param stockSymbol The stock symbol.
     * @return A list of all transaction results for the given stock.
     */
    List<TransactionResult> findBySymbol(String stockSymbol);

}
