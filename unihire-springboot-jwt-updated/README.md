# UniHire - Campus Job Portal (Updated)

This project implements the data model and relationships extracted from the provided PDF.

## Data model (as corrected)
- **User (UserInfo)**: userId (Integer), username (String), password (String), roles (enum: STUDENT or COMPANY)
  - One-to-Many with Job (Company -> Jobs)
- **Skill**: skillId (Integer), skillName (String)
  - Many-to-Many with Job via JobSkill
- **Job**: jobId (Integer), title, description, salary, postedBy (User)
  - Many-to-Many with Skill via JobSkill (link table JobSkill)
- **JobSkill**: jsId (Integer), job, skill
- **JobApplication**: applicationId (Integer), job, student (User), status (enum: PENDING, ACCEPTED, REJECTED)
  - Unique constraint on (job, student)

## What's included
- Spring Boot 3.2.2, Java 17, Maven
- JWT authentication, role-based authorization
- Correct JPA relationships and enums
- Global exception handler mapping to proper HTTP status codes
- H2 in-memory DB for dev; PostgreSQL dependency available
- Original PDF included (if file was present)

---
## Extracted PDF content (if available):
Original PDF included.
