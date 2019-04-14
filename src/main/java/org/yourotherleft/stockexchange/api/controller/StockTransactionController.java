package org.yourotherleft.stockexchange.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.yourotherleft.stockexchange.service.StockTransactionService;
import org.yourotherleft.stockexchange.service.type.TransactionRequest;
import org.yourotherleft.stockexchange.service.type.TransactionResult;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/transaction")
public class StockTransactionController {

    private final StockTransactionService stockTransactionService;

    @Autowired
    public StockTransactionController(StockTransactionService stockTransactionService) {
        this.stockTransactionService = stockTransactionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TransactionResult> getAll() {
        return stockTransactionService.findAll();
    }

    @RequestMapping(value = "/symbol/{symbol}", method = RequestMethod.GET)
    public List<TransactionResult> getWithSymbol(@PathVariable final String symbol) {
        return stockTransactionService.findBySymbol(symbol);
    }

    @RequestMapping(value = "/id/{identifier}", method = RequestMethod.GET)
    public TransactionResult getWithIdentifier(@PathVariable final String identifier) {
        // look for a transaction with that identifier
        final Optional<TransactionResult> resultOptional = stockTransactionService.findByIdentifier(identifier);
        if (resultOptional.isPresent()) {
            // found! return it.
            return resultOptional.get();
        } else {
            // throw a not found exception, which will be handled as a 404 by our RestControllerAdvice
            throw new NotFoundException(String.format("no stock transaction with identifier '%s'", identifier));
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public TransactionResult buy(@RequestBody final TransactionRequest transactionRequest) {
        return stockTransactionService.buy(transactionRequest);
    }

}
