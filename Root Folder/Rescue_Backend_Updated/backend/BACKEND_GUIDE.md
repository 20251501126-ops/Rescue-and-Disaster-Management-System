# Rescue & Disaster Management Backend

## Setup

1. Install Java 17+ and use the included Maven wrapper.
2. Create the MySQL database once:
   ```sql
   CREATE DATABASE rescue_db;
   ```
3. Set the database password in PowerShell (do not commit it to Git):
   ```powershell
   $env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
   ```
4. From the `backend` folder run:
   ```powershell
   .\mvnw.cmd clean compile
   .\mvnw.cmd spring-boot:run
   ```
5. Server base URL: `http://localhost:8081`.

The `application.properties` reads the password from `DB_PASSWORD`. The previous uploaded ZIP contained a plaintext database password, so change that MySQL password before using this updated backend and do not commit credentials.

## API endpoints

### Health
- `GET /api/hello`

### Emergencies
- `POST /api/emergencies` — report emergency
- `GET /api/emergencies` — list emergencies
- `PUT /api/emergencies/{id}/verify` — verify
- `PUT /api/emergencies/{id}/assign` — assign first available team to a verified emergency
- `PUT /api/emergencies/{id}/complete` — complete and release assigned team

Example emergency JSON:
```json
{
  "type": "Flood",
  "location": "Kota",
  "severity": "High",
  "peopleAffected": 25,
  "reportedBy": "CIT-REPLACE-WITH-CITIZEN-ID"
}
```
Emergency IDs are now UUID-based (`E-XXXXXXXX`) so a server restart will not reset the counter and collide with existing records. `reportedBy` and `location` remain text fields to preserve compatibility with the original API; they are not database foreign-key relationships yet.

### Rescue teams
- `POST /api/teams` — create team (provide `teamId`, `teamName`, `currentLocation`)
- `GET /api/teams` — list teams
- `PUT /api/teams/{teamId}/status?status=Available` — update status

### Citizens
- `POST /api/citizens` — register citizen
- `GET /api/citizens` — list citizens
- `GET /api/citizens/{id}` — get citizen
- `PUT /api/citizens/{id}` — update citizen
- `DELETE /api/citizens/{id}` — delete citizen

Example:
```json
{
  "name": "Aarav Sharma",
  "contactNumber": "9876543210",
  "email": "aarav@example.com",
  "address": "Kota, Rajasthan"
}
```

### Rescue team members
- `POST /api/team-members` — add member to an existing team
- `GET /api/team-members` — list all members
- `GET /api/team-members/team/{teamId}` — list members of a team
- `PUT /api/team-members/{id}/status?status=Inactive` — set `Active` or `Inactive`

Example:
```json
{
  "name": "Ravi Kumar",
  "contactNumber": "9876543210",
  "specialization": "First Aid",
  "teamId": "RT01"
}
```

### Locations
- `POST /api/locations` — add location
- `GET /api/locations` — list locations
- `GET /api/locations/{id}` — get location
- `PUT /api/locations/{id}` — update location
- `DELETE /api/locations/{id}` — delete location

Example:
```json
{
  "address": "Near Main Market",
  "city": "Kota",
  "state": "Rajasthan",
  "latitude": 25.2138,
  "longitude": 75.8648,
  "description": "Flood-prone area"
}
```

### Dashboard
- `GET /api/dashboard/summary` — total emergencies, status counts, total/available teams, citizens and team members.

## Notes
- Hibernate `ddl-auto=update` creates/updates entity tables when the application starts. Back up important data before schema changes.
- The new Citizen, RescueTeamMember and Location modules currently use string IDs for references. Emergency `reportedBy` and `location` are retained as strings for backward compatibility. A future migration can convert these to proper JPA relationships after the existing frontend/API contract is agreed.
- Run `./mvnw.cmd test` on Windows to execute tests. Add integration tests for each endpoint before production use.
