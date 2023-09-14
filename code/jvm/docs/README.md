# Report phase 1

## Introduction

We design and implement an information system and server that serves requests from players that use our game

## Operations implemented in this phase

## Schema with the new paths of the SPA

![]()

## Modeling the database

### **Conceptual model (Entity-Relationship) **

The following diagram holds the Entity-Relationship model for the information managed by the system.
![]()

We highlight the following aspects:

The conceptual model has the following restrictions:

### **Physical Model (database-schema)**

The following diagram holds the Database schema / relation model for the information managed by the system.

![]()

We highlight the following aspects of this model:

# Software organization

### **Open-API Specification**

In our Open-API specification, we highlight the following aspects:

## SPA demonstrations of new functionalities with pictures

### **How and where command parameters are validated**

### **Connection Management**

All connections to the DB are AutoCloseable because of the use of the ".use" block. Connections are open while querrying
and altering the database

### **Data Access**

The classes that aid on the data access are:

Some non-trivial (relevant) used SQL statements are:

- CREATE TABLE, in order to create the tables in the DB when starting the application
- SELECT, for getting properties from the tables
- INSERT, for adding more tuples of an Entity
- UPDATE, for updating route, sport or activity
- DELETE, in order to delete 1 activity (and we also have a POST request to delete several activities)

### **Paging**

To facilitate and properly make use of paging functionalities we created a Paging class data class. The Paging object is
described by the parameters `limit` and `skip`, which are initialized and checked for their proper ranges.

Now this object is passed to the data-classes (in *src/main/kotlin/pt/isel/ls/api/data/*) and these classes are the ones
that compute the results. (Before we were getting everything from the database and then paging it
with `computeRangeForPaging`, but now it's fixed)

### **Error Handling/Processing**

Per example, when a querry doesn't find a tuple with a certain ID, we return empty lists. When requests are incomplete
or malformed, the server returns an appropriate status code.

