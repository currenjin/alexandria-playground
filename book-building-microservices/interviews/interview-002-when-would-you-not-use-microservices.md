# INTERVIEW-002. When would you not use microservices?
- createdDate: 2025-11-05
- updatedDate: 2025-11-05

## Situation
At my current company, the product team wanted to migrate our service to microservices architecture for "scalability". Our team has 7 people total - 2 backend developers, 2 frontend developers, and others 

## Tasks
I needed to evaluate whether microservices were appropriate given our team capacity and system complexity, and provide a recommendation with clear reasoning.

## Action
- Analyzed the operational overhead: each microservice needs monitoring, deployment pipelines, on-call rotation
- Calculated team capacity: with 2 backend developers, managing multiple service would spread us too thin
- Researched industry examples: found that team under 10-15 people typically struggle with microservices overhead
- Proposed an alternative: extract only the Excel service which had the highest resource consumption and different load patterns

## Result
- Avoided introduction 10x operational complexity with a small team
- Still achieved key benefits by extracting the Excel service for independent scaling
- Team remained productive and focused on delivering features
- Kept the migration path open for future growth when team size justifies it
- This decision was documented and accepted by leadership

## Key points to emphasize
- Show maturity: knowing when NOT to use a pattern
- Quantify: 7 people, 2 backend developers, 10x complexity
- Offered alternative solution, not just "no"
- Data-driven decision, not opinion
