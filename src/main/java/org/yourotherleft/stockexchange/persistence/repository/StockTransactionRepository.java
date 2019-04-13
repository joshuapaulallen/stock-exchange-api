package org.yourotherleft.stockexchange.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yourotherleft.stockexchange.persistence.entity.StockTransaction;

import java.util.List;

/**
 * A repository for basic CRUD operations on the {@link StockTransaction} entity.
 *
 * @author jallen
 */
@Repository
public interface StockTransactionRepository extends CrudRepository<StockTransaction, Integer> {

    /**
     * Find a {@link StockTransaction} with the given identifier.
     *
     * @param identifier The identifier.
     * @return The {@link StockTransaction} with the given identifier, or null if one doesn't exist.
     */
    @Query("select t from StockTransaction t where t.identifier = :identifier")
    StockTransaction findByIdentifier(@Param("identifier") String identifier);

    /**
     * Find all {@link StockTransaction}s with the given stock symbol.  Case-insensitive.
     *
     * @param stockSymbol The stock symbol.
     * @return A list of all {@link StockTransaction}s with the given stock symbol.
     */
    @Query("select t from StockTransaction t where t.stockSymbol = UPPER(:stockSymbol)")
    List<StockTransaction> findByStockSymbol(@Param("stockSymbol") String stockSymbol);

}
