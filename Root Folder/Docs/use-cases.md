# Use Cases for Rescue and Disaster Management System

# Use Case 1: Report Emergency

- **Actor:** Citizen
- **Precondition:** User has access to the system.
- **Main Flow:**
  1. Citizen opens the emergency report form.
  2. Citizen provides the location and details of the emergency.
  3. Citizen submits the emergency request.
- **Alternate Flow:** If the location is missing, the system prompts the citizen to provide or correct the location.
- **Postcondition:** The emergency request is logged and becomes visible to the appropriate authorities.

# Use Case 2: Assign Rescue Team

- **Actor:** Disaster Management Authority
- **Precondition:** An emergency request exists in the system.
- **Main Flow:**
  1. Authority views the pending emergency requests.
  2. Authority verifies the seriousness and requirements of the emergency.
  3. Authority assigns a suitable rescue team.
- **Postcondition:** The assigned rescue team receives the rescue assignment.

# Use Case 3: Update Rescue Status

- **Actor:** Rescue Team
- **Precondition:** A rescue task has been assigned to the team.
- **Main Flow:**
  1. Rescue team accepts the assigned task.
  2. Rescue team updates the progress of the operation, such as **En Route**, **In Action**, or **Completed**.
- **Postcondition:** The updated rescue status becomes visible to the citizen and the disaster management authority.