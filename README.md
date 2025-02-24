A microservices system consisting of two services:

**Resource Service** - for MP3 file processing
**Song Service** - for song metadata management

The services are designed to work together as follows:

Resource Service handles the storage and processing of MP3 files.
Song Service manages metadata for each song, ensuring that each metadata entry corresponds to a unique MP3 file in the Resource Service.

The song metadata and resource entities maintain a one-to-one relationship:
Each song metadata entry is uniquely associated with a resource, linked via the resource ID.
Deleting a resource triggers a cascading deletion of its associated metadata.


**Resource Service**
The Resource Service implements CRUD operations for processing MP3 files. 
When uploading an MP3 file, the service:

1. Stores the MP3 file in the Minio object storage.
2. Extracts the MP3 file tags (metadata) using external libraries of the Apache Tika.
3. Invokes the Song Service to save the MP3 file tags (metadata).

**Song Service**
The Song Service implements CRUD operations for managing song metadata records. 
The service uses the Resource ID to uniquely identify each metadata record, establishing a direct one-to-one relationship between resources and their metadata.

**How to run:**
**Locally** (services in IDE, databases in Docker):
1. Start only the databases and storage in Docker:
docker compose up -d resource-db song-db minio
2. Run Eureka Server, Cloud Gateway and microservices in IDE
3. Verify Eureka registration http://localhost:8761
4. Check application working http://localhost:8080/resources and http://localhost:8080/songs

**Run the application in Docker Compose**:
1. Run docker compose: docker compose up -d --build
2. Check application working http://localhost:8080/resources and http://localhost:8080/songs

**Tools and libs:**
Spring Boot 3.4.0 or higher
Java 17 or later (LTS versions)
Build Tool: Maven
Database: PostgreSQL
Application Startup: Docker
Service Registration: Eureka
API Gateway: Spring Cloud Gateway
Load Balancer: Spring Cloud LoadBalancer
Object storage: Minio
Content analysis toolkit: Apache Tika





