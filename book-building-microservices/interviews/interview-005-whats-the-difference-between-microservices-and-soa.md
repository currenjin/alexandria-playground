# INTERVIEW-005. What's the difference between microservices and SOA?
- createdDate: 2025-11-07
- updatedDate: 2025-11-07

## Answer
Both are about services, but different philosophy

| Aspect        | SOA                               | Microservices                             |
|---------------|-----------------------------------|-------------------------------------------|
| Goal          | Enterprise-wide integration       | Team independence                         |
| Communication | ESB (smart pipes, dumb endpoints) | HTTP/events (dumb pipes, smart endpoints) |
| Data          | Often shared databases            | Database per service                      |
| Governance    | Centralized standards             | Decentralized, team autonomy              |
| Service size  | Larger, more coarse-grained       | Smaller, focused                          |

### Key phrase
Microservices are about maximizing team independence, while SOA focused on enterprise-wide reuse

### What Not to do
- Don't over-explain unless interviewer asks
- Don't say "SOA is old, microservices are new"
- Focus on different goals and design principles
