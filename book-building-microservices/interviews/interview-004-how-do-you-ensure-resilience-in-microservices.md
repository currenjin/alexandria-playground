# INTERVIEW-004. How do you ensure resilience in microservices?
- createdDate: 2025-11-06
- updatedDate: 2025-11-06

## Answer
The key concept is the Bulkhead pattern

isolate failures so they don't cascade

Like compartments in a ship, one breach doesn't snk the whole vessel

### Four key techniques
1. Timeouts: Don't wait forever for downstream services(e.g. 3-seconds timeout)
2. Circuit Breakers: Stop calling a failing service temporarily, fail fast
   - Open circuit after N failures
   - Try again after cooldown period
3. Fallbacks: Degrade gracefully instead of complete failure
   - Return cached data
   - Return default/static response
   - Show partial results
4. Resource Isolation: Separate thread pools per dependency
   - payment service gets its own thread pool
   - If it's slow, doesn't block other operations

### Example from my work
I extracted Excel service because
- Excel upload failures were affecting order processing
- By separating services: physical isolation
- Using async messaging: temporal isolation
- Independent databases: data isolation

This is bulkhead pattern in practice
