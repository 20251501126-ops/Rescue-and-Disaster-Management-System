# Noun–Verb Analysis

## Rescue and Disaster Management System

## 1. Raw Candidate List

The nouns identified from the problem statement and the three use cases are listed below.

| Candidate | Type | Initial Interpretation |
|---|---|---|
| Citizen | Noun | Candidate class |
| Emergency | Noun | Candidate class |
| Disaster Management Authority | Noun | Candidate class |
| Rescue Team | Noun | Candidate class |
| Rescue Status | Noun | Possible class / attribute |
| Location | Noun | Possible class / attribute |
| Details | Noun | Possible class / attribute |
| System | Noun | System boundary, not a domain entity |
| Hospital | Noun | Candidate mentioned in the problem statement |
| Police | Noun | Candidate mentioned in the problem statement |
| Fire Department | Noun | Candidate mentioned in the problem statement |
| Ambulance Service | Noun | Candidate mentioned in the problem statement |

## 2. Raw Verb List

The following verbs were identified from the problem statement and use cases. They represent possible responsibilities or operations.

| Verb | Context | Interpretation |
|---|---|---|
| reports | Citizen reports an emergency | Candidate responsibility / operation |
| provides | Citizen provides emergency location and details | Candidate responsibility / operation |
| submits | Citizen submits the emergency request | Candidate responsibility / operation |
| verifies | Authority verifies the emergency | Candidate responsibility / operation |
| assigns | Authority assigns a rescue team | Candidate responsibility / operation |
| accepts | Rescue team accepts the assigned task | Candidate responsibility / operation |
| updates | Rescue team updates rescue progress/status | Candidate responsibility / operation |
| notifies | System notifies the rescue team/citizen | Candidate responsibility / operation |

## 3. Noun Filtering

Four filters are applied to the raw candidates: redundant, irrelevant, attribute, and outside scope.

| Candidate | Decision | Filter Applied | Reason |
|---|---|---|---|
| Citizen | Survives | None | Independent actor/entity that reports an emergency. |
| Emergency | Survives | None | Central domain entity representing the reported incident. |
| Disaster Management Authority | Survives | None | Independent actor/entity that verifies and assigns rescue teams. |
| Rescue Team | Survives | None | Independent entity that performs rescue work and updates status. |
| Rescue Status | Discarded | Attribute | Represented as a status value such as En Route, In Action, or Completed. |
| Location | Discarded | Attribute | Can be stored as an attribute of an Emergency in the current scope. |
| Details | Discarded | Irrelevant / Attribute | General information about an emergency rather than a separate domain class. |
| System | Discarded | Outside domain model | Represents the software/system boundary rather than a domain entity. |
| Hospital | Discarded | Outside current scope | Mentioned in the broader problem statement, but not used by the three current specifications. |
| Police | Discarded | Outside current scope | Mentioned in the broader problem statement, but not used by the three current specifications. |
| Fire Department | Discarded | Outside current scope | Mentioned in the broader problem statement, but not used by the three current specifications. |
| Ambulance Service | Discarded | Outside current scope | Mentioned in the broader problem statement, but not used by the three current specifications. |

## 4. Surviving Classes

After applying the filters, the following classes survive:

### Citizen
Reports emergencies and provides emergency information.

### Emergency
Represents the reported emergency and stores its relevant information and status.

### Disaster Management Authority
Verifies and prioritizes emergencies and assigns suitable rescue teams.

### Rescue Team
Accepts rescue tasks, performs rescue operations, and updates rescue status.

## 5. Verb → Responsibility / Operation Mapping

| Class | Responsibility | Possible Operation |
|---|---|---|
| Citizen | Report emergency | `reportEmergency()` |
| Citizen | Provide emergency information | `provideEmergencyDetails()` |
| Disaster Management Authority | Verify emergency | `verifyEmergency()` |
| Disaster Management Authority | Assign rescue team | `assignRescueTeam()` |
| Rescue Team | Accept assigned task | `acceptTask()` |
| Rescue Team | Update rescue status | `updateStatus()` |

## 6. Final Result

The four surviving domain classes are:

1. Citizen
2. Emergency
3. Disaster Management Authority
4. Rescue Team

The identified verbs are used as candidate responsibilities or operations for these surviving classes.