package com.currenjin.support;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExplainAnalyzer {

    private final DataSource dataSource;

    public ExplainAnalyzer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<ExplainResult> explain(String sql, Object... params) {
        List<ExplainResult> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("EXPLAIN " + sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(toExplainResult(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("EXPLAIN failed for: " + sql, e);
        }
        return results;
    }

    public ExplainResult explainFirst(String sql, Object... params) {
        List<ExplainResult> results = explain(sql, params);
        if (results.isEmpty()) {
            throw new IllegalStateException("EXPLAIN returned no rows for: " + sql);
        }
        return results.get(0);
    }

    public void createIndex(String indexName, String tableName, String... columns) {
        String columnList = String.join(", ", columns);
        executeStatement(String.format("CREATE INDEX %s ON %s (%s)", indexName, tableName, columnList));
    }

    public void dropIndex(String indexName, String tableName) {
        executeStatement(String.format("DROP INDEX %s ON %s", indexName, tableName));
    }

    public void dropIndexIfExists(String indexName, String tableName) {
        try {
            dropIndex(indexName, tableName);
        } catch (Exception ignored) {
        }
    }

    public void execute(String sql) {
        executeStatement(sql);
    }

    private void executeStatement(String sql) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Statement failed: " + sql, e);
        }
    }

    private ExplainResult toExplainResult(ResultSet rs) throws SQLException {
        return ExplainResult.builder()
                .id(getLong(rs, "id"))
                .selectType(rs.getString("select_type"))
                .table(rs.getString("table"))
                .partitions(rs.getString("partitions"))
                .type(rs.getString("type"))
                .possibleKeys(rs.getString("possible_keys"))
                .key(rs.getString("key"))
                .keyLen(rs.getString("key_len"))
                .ref(rs.getString("ref"))
                .rows(getLong(rs, "rows"))
                .filtered(getDouble(rs, "filtered"))
                .extra(rs.getString("Extra"))
                .build();
    }

    private Long getLong(ResultSet rs, String column) throws SQLException {
        long val = rs.getLong(column);
        return rs.wasNull() ? null : val;
    }

    private Double getDouble(ResultSet rs, String column) throws SQLException {
        double val = rs.getDouble(column);
        return rs.wasNull() ? null : val;
    }
}
