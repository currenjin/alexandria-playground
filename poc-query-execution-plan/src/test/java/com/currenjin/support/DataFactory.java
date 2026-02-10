package com.currenjin.support;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

public class DataFactory {

    private static final Random RANDOM = new Random(42);

    public static void insertMembers(DataSource dataSource, int count) {
        String sql = "INSERT INTO member (name, email, age, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        String[] statuses = {"ACTIVE", "INACTIVE", "SUSPENDED"};
        String[] firstNames = {"Kim", "Lee", "Park", "Choi", "Jung", "Kang", "Cho", "Yoon", "Jang", "Lim"};
        LocalDateTime now = LocalDateTime.now();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < count; i++) {
                ps.setString(1, firstNames[i % firstNames.length] + i);
                ps.setString(2, "user" + i + "@example.com");
                ps.setInt(3, 20 + (i % 50));
                ps.setString(4, statuses[i % statuses.length]);
                ps.setObject(5, now.minusDays(i % 365));
                ps.setObject(6, now);
                ps.addBatch();

                if (i % 1000 == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert members", e);
        }
    }

    public static void insertOrders(DataSource dataSource, int count, int memberCount) {
        String sql = "INSERT INTO orders (member_id, status, order_date, created_at) VALUES (?, ?, ?, ?)";
        String[] statuses = {"PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"};
        LocalDateTime now = LocalDateTime.now();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < count; i++) {
                ps.setLong(1, (i % memberCount) + 1);
                ps.setString(2, statuses[i % statuses.length]);
                ps.setObject(3, now.minusDays(RANDOM.nextInt(365)));
                ps.setObject(4, now);
                ps.addBatch();

                if (i % 1000 == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert orders", e);
        }
    }

    public static void insertProducts(DataSource dataSource, int count) {
        String sql = "INSERT INTO product (name, price, stock_quantity, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        String[] statuses = {"AVAILABLE", "OUT_OF_STOCK", "DISCONTINUED"};
        String[] prefixes = {"Laptop", "Phone", "Tablet", "Monitor", "Keyboard", "Mouse", "Camera", "Speaker", "Headset", "Charger"};
        LocalDateTime now = LocalDateTime.now();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < count; i++) {
                ps.setString(1, prefixes[i % prefixes.length] + " Model-" + i);
                ps.setInt(2, 10000 + RANDOM.nextInt(990000));
                ps.setInt(3, RANDOM.nextInt(1000));
                ps.setString(4, statuses[i % statuses.length]);
                ps.setObject(5, now.minusHours(i));
                ps.setObject(6, now);
                ps.addBatch();

                if (i % 1000 == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert products", e);
        }
    }
}
