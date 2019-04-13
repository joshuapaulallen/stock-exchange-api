package org.yourotherleft.stockexchange.service.type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionAmount {

    private final String currencyUnit;
    private final double value;

}
