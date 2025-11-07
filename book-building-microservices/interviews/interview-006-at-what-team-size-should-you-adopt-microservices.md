# INTERVIEW-005. What's the difference between microservices and SOA?
- createdDate: 2025-11-07
- updatedDate: 2025-11-07

## Answer
There's no magic number, but consider Conway's Law: system design mirrors organizational structure

### My rule of thumb
- < 10 people: Monolith (unless special circumstances)
- 10-30 people: Modular monolith or extract high-load services only
- 30+ people: Full microservices architecture makes sense

### But team size alone isn't enough. Also consider
1. Complexity: Can one person understand the entire codebase?
2. Scaling needs: Do different domains have different load patterns?
3. Team structure: Are teams already organized by business domain?
4. Deployment conflicts: Are teams blocking each other's releases?
5. Technology needs: Do some domains need different tech stacks?

### Real example
- My team: 7 people total, 2 backend developers
- Decision: NOT full microservices
- But: Extracted Excel service due to resource consumption
- Formula: Team size x System complexity = Total overhead

## Key message
It's about complexity and team structure, not just headcount. I've experienced this firsthand
