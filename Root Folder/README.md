#  Rescue and Disaster Management System

##  Description

The **Rescue and Disaster Management System** is a software system designed to help citizens, disaster management authorities, and rescue teams communicate and coordinate during emergencies.

The main goal of this system is to **report emergencies quickly, assign suitable rescue teams, and track rescue operations**. It helps reduce response time, improve communication, and provide better coordination during disasters.

###  Objectives

* Allow citizens to report emergencies easily.
* Record the emergency location and details.
* Help authorities manage and prioritize emergency requests.
* Assign rescue teams to emergencies.
* Allow rescue teams to update rescue status.
* Keep citizens informed about the progress of their emergency request.
* Improve coordination between citizens, authorities, and rescue teams.

---

## Team Members

| No. | Name                |
| --- | ------------------- |
| 1   | Parth Rai           |
| 2   | Battu Akshara Veena |
| 3   | Het Maheshwari      |
| 4   | Lakshya Goel        |
| 5   | Bhakti Paliwal      |

---

#  Use Cases

This document outlines three primary use cases of the **Rescue and Disaster Management System**.

---

## 1. Use Case: Report Emergency

**Actor:** Citizen

**Precondition:**
Citizen has access to the system through a web or mobile application.

### Main Flow

1. Citizen opens the emergency report form.
2. Citizen provides the emergency location and details.
3. Citizen submits the emergency request.
4. System records the request.
5. System generates a tracking ID.

### Alternate Flow

* If the location is missing, the system asks the citizen to provide it.
* If the network is unavailable, the system stores the request locally and sends it when connectivity is restored.

**Postcondition:**
The emergency request becomes visible to the disaster management authority.

---

## 2. Use Case: Assign Rescue Team

**Actor:** Disaster Management Authority

**Precondition:**
An emergency request exists in the system.

### Main Flow

1. Authority views pending emergency requests.
2. Authority checks the seriousness of the emergency.
3. Authority assigns a priority level.
4. Authority selects and assigns a suitable rescue team.
5. System notifies the rescue team.
6. System updates the citizen about the assigned rescue team.

### Alternate Flow

* If no rescue team is available, the system places the request in a queue.
* The system alerts the authority about the lack of available rescue teams.

**Postcondition:**
The rescue team receives the assignment and the citizen is notified.

---

## 3. Use Case: Update Rescue Status

**Actor:** Rescue Team

**Precondition:**
A rescue task has been assigned to the rescue team.

### Main Flow

1. Rescue team accepts the assigned task.
2. Team travels to the emergency location.
3. Team updates the rescue progress.
4. Team can set the status as:

   * En Route
   * In Action
   * Completed
5. Team marks the rescue operation as completed.

### Alternate Flow

* If the team is delayed, the status is updated to **Delayed**.
* If the team cannot reach the location, the status is updated to **Unable to Proceed**.

**Postcondition:**
The rescue status is visible to both the citizen and the disaster management authority.

---

##  System Flow

```text
Citizen
   ↓
Report Emergency
   ↓
System Generates Tracking ID
   ↓
Disaster Management Authority
   ↓
Check & Prioritize Emergency
   ↓
Assign Rescue Team
   ↓
Rescue Team
   ↓
Update Rescue Status
   ↓
Citizen + Authority
   ↓
Rescue Completed
```

---

##  Benefits

* Faster emergency reporting
* Better coordination
* Quick rescue team assignment
* Real-time rescue status updates
* Improved communication
* Better emergency tracking
* Reduced response time
* Increased transparency

---

## Conclusion

The **Rescue and Disaster Management System** provides a simple and organized way to manage emergencies. It connects citizens, disaster management authorities, and rescue teams in one system, helping them respond to emergencies more efficiently and effectively.