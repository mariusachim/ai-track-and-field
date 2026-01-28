# Requirements

This document contains the functional requirements for the Track and Field Competition Management System.

## Use Cases

### UC-1: Manage Competitions
**Actor:** Administrator

**Description:** Administrators can create, update, and delete track and field competitions.

**Requirements:**
- REQ-1.1: System shall allow administrators to create a new competition with basic details (name, date, location).
- REQ-1.2: System shall allow administrators to update existing competition information.
- REQ-1.3: System shall allow administrators to delete competitions.
- REQ-1.4: System shall allow administrators to view a list of all competitions.

---

### UC-2: Manage Categories
**Actor:** Administrator

**Description:** Administrators can define age and gender categories for athletes.

**Requirements:**
- REQ-2.1: System shall allow administrators to create categories with gender (male/female).
- REQ-2.2: System shall allow administrators to define birth year range (year from, year to) for each category.
- REQ-2.3: System shall allow administrators to assign events to categories.
- REQ-2.4: System shall allow administrators to update and delete categories.

---

### UC-3: Manage Clubs
**Actor:** Administrator

**Description:** Administrators can manage athlete clubs/teams.

**Requirements:**
- REQ-3.1: System shall allow administrators to create clubs with name and other details.
- REQ-3.2: System shall allow administrators to update and delete club information.

---

### UC-4: Manage Athletes
**Actor:** Administrator

**Description:** Administrators can register and manage athletes.

**Requirements:**
- REQ-4.1: System shall allow administrators to register athletes with personal details (name, birth year, gender).
- REQ-4.2: System shall allow administrators to assign athletes to clubs.
- REQ-4.3: System shall automatically assign athletes to categories based on birth year and gender.
- REQ-4.4: System shall allow administrators to update and delete athlete information.

---

### UC-5: Record Event Results
**Actor:** Administrator

**Description:** Administrators can record athlete performance in events.

**Requirements:**
- REQ-5.1: System shall allow administrators to enter results for each athlete in their assigned events.
- REQ-5.2: System shall support different result types (time for track events, distance for field events).

---

### UC-6: Calculate Rankings
**Actor:** System (Automated) / Administrator

**Description:** The system calculates athlete rankings based on event results using IAAF scoring formulas.

**Requirements:**
- REQ-6.1: System shall calculate points for each athlete based on their event results using IAAF formulas.
- REQ-6.2: System shall calculate total points for each athlete across all their events.
- REQ-6.3: System shall rank athletes within their categories based on total points.
- REQ-6.4: System shall generate a ranking list grouped by category.

---

### UC-7: View Rankings
**Actor:** Administrator

**Description:** Administrators can view competition rankings.

**Requirements:**
- REQ-7.1: System shall display rankings grouped by category.
- REQ-7.2: Each ranking line shall show athlete details, individual event results, and total points.
- REQ-7.3: System shall display rankings in descending order by total points.

## Business Rules

| ID | Rule | Description |
|----|------|-------------|
| BR-1 | Automatic Category Assignment | Athlete category is determined automatically based on birth year and gender. |
| BR-2 | IAAF Scoring | Points are calculated according to official IAAF (World Athletics) scoring formulas. |
| BR-3 | Category-Based Events | Athletes can only compete in events assigned to their category. |
| BR-4 | Ranking Completion | Rankings are finalized when all athletes in a category have completed their events. |

## Non-Functional Requirements

| ID | Requirement | Description |
|----|-------------|-------------|
| NFR-1 | Web-Based | The application shall be accessible via web browser. |
| NFR-2 | Responsive | The user interface shall be responsive and work on desktop devices. |
| NFR-3 | Performance | Page load times shall be under 2 seconds for standard operations. |
