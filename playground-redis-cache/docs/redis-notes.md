# Redis Notes

This playground focuses on core cache behavior that maps well to Redis:

- Key normalization before save/read
- TTL expiration
- Explicit eviction

## Mapping to real Redis commands

- `put(key, value, ttl)` -> `SET key value EX <seconds>`
- `get(key)` -> `GET key`
- `evict(key)` -> `DEL key`

## Next improvements

- Add JSON serialization strategy
- Add namespace support (`app:env:key`)
- Add cache hit/miss metrics
