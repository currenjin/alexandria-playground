# INTERVIEW-003. What are the main challenges of microservices?
- createdDate: 2025-11-06
- updatedDate: 2025-11-06

## Answer
From my study and experience, I'd highlight four main challenges - remember them as DMOT

### Distributed Transactions (D)
- Problem: No ACID guarantees across services
- Solution: Saga pattern with compensating transactions
- Example: Order -> Payment -> Inventory flow

### Monitoring & Debugging (M)
- Problem: Logs scattered across multiple services
- Solution: Distributed tracing with correlation IDs, centralized logging
- Example: One Excel upload request touches 3 services

### Operational Complexity (O)
- Problem: 10x deployment complexity vs monolith
- Solution: Strong CI/CD, container orchestration, automation
- Example: Need comprehensive automated testing

### Testing (T)
- Problem: Integration tests become expensive and slow
- Solution: Consumer-driven contract tests
- Example: Each service has contract tests with dependencies

## Important
- Don't just list problems
- Always mention solutions and show I've thought the challenges 

## Connection to experience
- Operational complexity was key factor in decision
- Planning to implement Saga pattern
