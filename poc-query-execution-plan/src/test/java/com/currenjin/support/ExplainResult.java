package com.currenjin.support;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ExplainResult {
    private final Long id;
    private final String selectType;
    private final String table;
    private final String partitions;
    private final String type;
    private final String possibleKeys;
    private final String key;
    private final String keyLen;
    private final String ref;
    private final Long rows;
    private final Double filtered;
    private final String extra;

    public boolean isFullTableScan() {
        return "ALL".equals(type);
    }

    public boolean isIndexScan() {
        return "index".equals(type);
    }

    public boolean isRefLookup() {
        return "ref".equals(type);
    }

    public boolean isRangeScan() {
        return "range".equals(type);
    }

    public boolean usesFilesort() {
        return extra != null && extra.contains("Using filesort");
    }

    public boolean usesIndex() {
        return extra != null && extra.contains("Using index");
    }

    public boolean usesWhere() {
        return extra != null && extra.contains("Using where");
    }

    public boolean usesIndexCondition() {
        return extra != null && extra.contains("Using index condition");
    }
}
