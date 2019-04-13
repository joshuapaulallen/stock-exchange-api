package org.yourotherleft.stockexchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yourotherleft.stockexchange.persistence.entity.StockTransaction;
import org.yourotherleft.stockexchange.persistence.repository.StockTransactionRepository;
import org.yourotherleft.stockexchange.persistence.type.TransactionType;
import org.yourotherleft.stockexchange.service.type.TransactionRequest;
import org.yourotherleft.stockexchange.service.type.TransactionResult;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
public class StockTransactionServiceImpl implements StockTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(StockInfoServiceIEXTradingImpl.class);

    private final StockInfoService stockInfoService;
    private final StockTransactionRepository stockTransactionRepository;

    @Autowired
    public StockTransactionServiceImpl(final StockInfoService stockInfoService, final StockTransactionRepository stockTransactionRepository) {
        this.stockInfoService = stockInfoService;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    @Override
    public TransactionResult buy(final TransactionRequest transactionRequest) {
        requireNonNull(transactionRequest, "transaction request is null");

        // fetch the price of the stock in the request
        final double stockPrice = stockInfoService.getMarketPrice(transactionRequest.getSymbol());

        // calculate the number of shares purchased
        final double shares = transactionRequest.getAmount().getValue() / stockPrice;

        // calculate a transaction identifier
        final String identifier = generateUniqueTransactionIdentifier();

        // persist the transaction
        final StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setIdentifier(identifier);
        stockTransaction.setTransactionType(TransactionType.BUY);
        stockTransaction.setStockSymbol(transactionRequest.getSymbol());
        stockTransaction.setCurrency(transactionRequest.getAmount().getCurrencyUnit());
        stockTransaction.setAmount(transactionRequest.getAmount().getValue());
        stockTransaction.setShares(shares);
        stockTransactionRepository.save(stockTransaction);

        // prepare a transaction result
        final TransactionResult transactionResult = TransactionResult.of(stockTransaction.getIdentifier(), stockTransaction.getStockSymbol(), stockTransaction.getCurrency(), stockTransaction.getAmount(), stockTransaction.getShares());

        // log it
        LOG.info("completed buy transaction request '{}' with result '{}'", transactionRequest, transactionResult);

        return transactionResult;
    }

    private String generateUniqueTransactionIdentifier() {
        return UUID.randomUUID().toString();
    }
}
