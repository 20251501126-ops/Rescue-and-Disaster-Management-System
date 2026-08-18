# CRC Cards — Rescue and Disaster Management System

## 1. CRC Card: Citizen

**Class:** Citizen

### Responsibilities
- Report an emergency.
- Provide emergency location and details.
- Submit the emergency request.

### Collaborators
- Emergency


---

## 2. CRC Card: Emergency

**Class:** Emergency

### Responsibilities
- Store the reported emergency details.
- Maintain the emergency information and status.
- Provide emergency information to the Disaster Management Authority.
- Represent the rescue request that is assigned to a rescue team.

### Collaborators
- Citizen
- Disaster Management Authority
- Rescue Team


---

## 3. CRC Card: Disaster Management Authority

**Class:** Disaster Management Authority

### Responsibilities
- View pending emergency requests.
- Verify the seriousness of an emergency.
- Assign a priority level to an emergency.
- Select and assign a suitable rescue team.
- Handle situations where no rescue team is available.

### Collaborators
- Emergency
- Rescue Team


---

## 4. CRC Card: Rescue Team

**Class:** Rescue Team

### Responsibilities
- Accept the assigned rescue task.
- Travel to the emergency location.
- Update the progress of the rescue operation.
- Set the rescue status as En Route, In Action, Completed, Delayed, or Unable to Proceed.
- Complete the rescue operation.

### Collaborators
- Emergency
- Disaster Management Authority


---

## Summary

The four surviving classes identified through noun–verb analysis are:

1. Citizen
2. Emergency
3. Disaster Management Authority
4. Rescue Team

Each CRC card identifies the class, its responsibilities, and the other classes it collaborates with.
