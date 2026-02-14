INSERT INTO account (id, balance) VALUES (1, 100000)
    ON DUPLICATE KEY UPDATE balance = VALUES(balance);
