# playground-redis-cache

A small Redis-style cache playground written in Kotlin.

## What this project contains

- TTL-based cache entry expiration
- Key normalization (`trim + lowercase`)
- Simple cache API (`get`, `put`, `evict`)
- Unit tests for hit/miss/expiration behavior

## Run tests

```bash
./gradlew test
```
