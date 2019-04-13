package org.yourotherleft.stockexchange.service;

import org.yourotherleft.stockexchange.service.type.TransactionRequest;
import org.yourotherleft.stockexchange.service.type.TransactionResult;

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

}
