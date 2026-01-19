DOCUMENT SEARCH SERVICE

Core Capabilities-

1)Security & Access Control
JWT-based authentication
Role-based authorization (USER, ADMIN)
Method-level access enforcement
Tenant-aware authorization checks

2)Multi-Tenancy
Tenant ID embedded in JWT claims
Enforced at controller, service, and query layers
Prevents cross-tenant data leakage

3)Search & Indexing
Full-text search using Elasticsearch
Tenant-scoped indexing
Support for metadata and tag-based queries

4)Performance
Redis-backed caching layer
Optimized read paths
Reduced database load

Install Docker, JDK 17
Start dependencies
docker-compose up -d


Services:
PostgreSQL
Elasticsearch
Redis

Running the Application

mvn clean install -DskipTests
mvn spring-boot:run 

App runs on :
http://localhost:8080

Health check-
GET /actuator/health

Authentication
POST /auth/token

Document Management
POST   /documents
GET    /documents/{id}
DELETE /documents/{id} (ADMIN only)

Search
GET /search?q={query}&tenant={tenantId}

http://localhost:8080

