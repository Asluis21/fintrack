CREATE FUNCTION transaction_domain.check_transaction_balance()
RETURNS TRIGGER AS $$
DECLARE
    v_transaction_id BIGINT;
    v_diff NUMERIC;
BEGIN
    -- 1. figure out which transaction_id this fired for
    --    (INSERT/UPDATE -> NEW.transaction_id, DELETE -> OLD.transaction_id)
    v_transaction_id := COALESCE(NEW.transaction_id, OLD.transaction_id);

    -- 2. sum debit-equivalents minus credit-equivalents, converted to
    --    source_currency via the parent transaction's exchange_rate
    SELECT COALESCE(SUM(

        CASE 
            WHEN te.currency_id = t.source_currency_id
                THEN 
                    CASE 
                        WHEN te.transaction_entry_type = 'DEBIT'
                        THEN te.amount 
                    END
            WHEN te.currency_id != t.source_currency_id
                THEN 
                    CASE 
                        WHEN te.transaction_entry_type = 'DEBIT'
                        THEN te.amount / t.exchange_rate 
                    END
        END

    ),0) - COALESCE(SUM ( 
        
        CASE 
            WHEN te.currency_id = t.source_currency_id
                THEN 
                    CASE 
                        WHEN te.transaction_entry_type = 'CREDIT'
                        THEN te.amount 
                    END
            WHEN te.currency_id != t.source_currency_id
                THEN 
                    CASE 
                        WHEN te.transaction_entry_type = 'CREDIT'
                        THEN te.amount / t.exchange_rate 
                    END
        END
    ),0) INTO v_diff
    FROM transaction_domain.transaction_entries te
    JOIN transaction_domain.transactions t ON t.id = te.transaction_id
    WHERE te.transaction_id = v_transaction_id;

    -- 3. if it's not (approximately) zero, block the commit
    IF ABS(v_diff) > 0.00000001 THEN
        RAISE EXCEPTION 'Transaction % entries do not balance (diff = %)', v_transaction_id, v_diff;
    END IF;

    RETURN NULL; -- return value is ignored for AFTER triggers
END;
$$ LANGUAGE plpgsql;


CREATE CONSTRAINT TRIGGER trg_check_transaction_balance
    AFTER INSERT OR UPDATE OR DELETE ON transaction_domain.transaction_entries
    DEFERRABLE INITIALLY DEFERRED
    FOR EACH ROW
    EXECUTE FUNCTION transaction_domain.check_transaction_balance();
