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

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.stream.StreamSupport.stream;

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

        // validate prior to doing anything else
        validateTransactionRequest(transactionRequest);

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
        final TransactionResult transactionResult = toTransactionResult(stockTransaction);

        // log it
        LOG.info("completed buy transaction request '{}' with result '{}'", transactionRequest, transactionResult);

        return transactionResult;
    }

    @Override
    public List<TransactionResult> findAll() {
        // fetch all known transaction entities and transform to transaction results
        return stream(stockTransactionRepository.findAll().spliterator(), false)
                .map(this::toTransactionResult)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TransactionResult> findByIdentifier(final String identifier) {
        requireNonNull(identifier, "identifier is null");

        // fetch the transaction entity with the given identifier and transform to a transaction result, if one exists
        final StockTransaction stockTransaction = stockTransactionRepository.findByIdentifier(identifier);
        if (stockTransaction == null) {
            return Optional.empty();
        } else {
            return Optional.of(toTransactionResult(stockTransaction));
        }
    }

    @Override
    public List<TransactionResult> findBySymbol(final String stockSymbol) {
        requireNonNull(stockSymbol, "stock symbol is null");

        // fetch all transaction entities with the given stock symbol and transform to transaction results
        return stockTransactionRepository.findByStockSymbol(stockSymbol).stream()
                .map(this::toTransactionResult)
                .collect(Collectors.toList());
    }

    private void validateTransactionRequest(final TransactionRequest transactionRequest) {
        // only USD is supported, so make sure that's the currency unit
        if (!transactionRequest.getAmount().getCurrencyUnit().equals("USD")) {
            throw new BadRequestException(String.format("invalid currency unit '%s', only USD is supported", transactionRequest.getAmount().getCurrencyUnit()));
        }
    }

    private TransactionResult toTransactionResult(final StockTransaction stockTransaction) {
        return TransactionResult.of(stockTransaction.getIdentifier(), stockTransaction.getStockSymbol(), stockTransaction.getCurrency(), stockTransaction.getAmount(), stockTransaction.getShares());
    }

    private String generateUniqueTransactionIdentifier() {
        return UUID.randomUUID().toString();
    }
}
