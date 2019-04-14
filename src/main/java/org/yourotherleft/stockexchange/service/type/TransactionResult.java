package org.yourotherleft.stockexchange.service.type;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResult {

    private final String identifier;
    private final String symbol;
    private final TransactionAmount amount;
    private final double shares;

    public static TransactionResult of(final String identifier, final String symbol, final String currencyUnit, final double amount, final double shares) {
        return new TransactionResult(identifier, symbol, new TransactionAmount(currencyUnit, amount), shares);
    }

}
