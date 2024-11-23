# Spring Data JDBC

## JDBC Repositories
   This chapter points out the specialties for repository support for JDBC.This builds on the core repository support explained in [repositories]. You should have a sound understanding of the basic concepts explained there.

### Why Spring Data JDBC?
The main persistence API for relational databases in the Java world is certainly JPA, which has its own Spring Data module. Why is there another one?

JPA does a lot of things in order to help the developer. Among other things, it tracks changes to entities. It does lazy loading for you. It lets you map a wide array of object constructs to an equally wide array of database designs.

This is great and makes a lot of things really easy. Just take a look at a basic JPA tutorial. But it often gets really confusing as to why JPA does a certain thing. Also, things that are really simple conceptually get rather difficult with JPA.

Spring Data JDBC aims to be much simpler conceptually, by embracing the following design decisions:

If you load an entity, SQL statements get run. Once this is done, you have a completely loaded entity. No lazy loading or caching is done.

If you save an entity, it gets saved. If you do not, it does not. There is no dirty tracking and no session.

There is a simple model of how to map entities to tables. It probably only works for rather simple cases. If you do not like that, you should code your own strategy. Spring Data JDBC offers only very limited support for customizing the strategy with annotations.

### Domain Driven Design and Relational Databases.
All Spring Data modules are inspired by the concepts of “repository”, “aggregate”, and “aggregate root” from Domain Driven Design. These are possibly even more important for Spring Data JDBC, because they are, to some extent, contrary to normal practice when working with relational databases.

An aggregate is a group of entities that is guaranteed to be consistent between atomic changes to it. A classic example is an Order with OrderItems. A property on Order (for example, numberOfItems is consistent with the actual number of OrderItems) remains consistent as changes are made.

References across aggregates are not guaranteed to be consistent at all times. They are guaranteed to become consistent eventually.

Each aggregate has exactly one aggregate root, which is one of the entities of the aggregate. The aggregate gets manipulated only through methods on that aggregate root. These are the atomic changes mentioned earlier.

A repository is an abstraction over a persistent store that looks like a collection of all the aggregates of a certain type. For Spring Data in general, this means you want to have one Repository per aggregate root. In addition, for Spring Data JDBC this means that all entities reachable from an aggregate root are considered to be part of that aggregate root. Spring Data JDBC assumes that only the aggregate has a foreign key to a table storing non-root entities of the aggregate and no other entity points toward non-root entities.