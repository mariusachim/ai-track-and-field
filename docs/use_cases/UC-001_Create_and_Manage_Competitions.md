# Use Case: Create and Manage Competitions

## Overview

**Use Case ID:** UC-001
**Use Case Name:** Create and Manage Competitions
**Primary Actor:** Administrator
**Goal:** Allow administrators to create new track and field competitions and manage their basic information
**Status:** Draft

## Preconditions

- Administrator is authenticated and has permission to manage competitions
- The system is operational and database is accessible

## Main Success Scenario

1. Administrator navigates to the competition management interface
2. System displays a list of existing competitions with options to create, view, edit, or delete
3. Administrator selects "Create New Competition"
4. System displays a competition creation form with the following fields:
   - Name (required)
   - Competition Date (required)
   - Location (optional)
   - Description (optional)
5. Administrator enters competition details:
   - Name: "Spring Athletics Championship 2025"
   - Competition Date: 2025-05-15
   - Location: "City Stadium"
   - Description: "Annual spring track and field competition"
6. Administrator submits the form
7. System validates the input data (see Business Rules)
8. System creates the competition record in the database
9. System displays a success message: "Competition created successfully"
10. System navigates to the competition detail view showing the newly created competition

## Alternative Flows

### A1: Validation Error - Missing Required Fields

**Trigger:** Administrator submits form with missing required fields (Step 7)
**Flow:**

1. System detects missing required field(s)
2. System displays error message indicating which required fields are missing
3. System keeps the form open with previously entered data intact
4. Use case continues at Step 5 of Main Success Scenario

### A2: Validation Error - Invalid Date

**Trigger:** Administrator enters an invalid or past date (Step 7)
**Flow:**

1. System detects invalid date format or past date
2. System displays error message: "Competition date must be a valid future date"
3. System keeps the form open with previously entered data intact
4. Use case continues at Step 5 of Main Success Scenario

### A3: View Existing Competition

**Trigger:** Administrator selects an existing competition from the list (Step 3)
**Flow:**

1. System displays competition details in read-only view
2. System shows associated categories, events, and athletes (if any)
3. Administrator can choose to edit or delete the competition
4. Use case ends or continues with A4 or A5

### A4: Edit Existing Competition

**Trigger:** Administrator selects "Edit" for an existing competition (Step 3 or A3.3)
**Flow:**

1. System displays competition edit form pre-populated with existing data
2. Administrator modifies competition details (name, date, location, or description)
3. Administrator submits the changes
4. System validates the updated data (see Business Rules)
5. System updates the competition record in the database
6. System displays success message: "Competition updated successfully"
7. System returns to competition detail view with updated information
8. Use case ends

### A5: Delete Competition

**Trigger:** Administrator selects "Delete" for an existing competition (Step 3 or A3.3)
**Flow:**

1. System displays confirmation dialog: "Are you sure you want to delete this competition? All associated categories, events, athletes, and results will also be deleted. This action cannot be undone."
2. Administrator confirms deletion
3. System deletes the competition and all cascading related records (categories, category_events, results)
4. System displays success message: "Competition deleted successfully"
5. System refreshes the competition list
6. Use case ends

### A6: Cancel Delete Competition

**Trigger:** Administrator cancels deletion in confirmation dialog (A5.2)
**Flow:**

1. System closes the confirmation dialog
2. System returns to the previous view (competition list or detail view)
3. Use case ends

### A7: Cancel Competition Creation/Edit

**Trigger:** Administrator selects "Cancel" during creation or edit (Steps 5 or A4.2)
**Flow:**

1. System discards any unsaved changes
2. System returns to the competition list view
3. Use case ends

## Postconditions

### Success Postconditions

- New competition record is created in the database with a unique ID (for creation)
- Competition record is updated in the database (for edit)
- Competition and all associated records are removed from the database (for deletion)
- Administrator can proceed to define categories for the competition (UC-002)
- System timestamps (created_at, updated_at) are properly maintained

### Failure Postconditions

- No changes are made to the database
- Administrator remains on the current form/view
- Previously entered data is retained (for validation errors)
- Error messages guide the administrator to correct the issues

## Business Rules

### BR-001: Competition Name Uniqueness

While the system does not enforce uniqueness of competition names at the database level, administrators should use descriptive and unique names to avoid confusion.

### BR-002: Competition Date Format

Competition date must be a valid date in ISO format (YYYY-MM-DD). The system accepts both past and future dates to support historical record keeping, but a warning may be displayed for past dates.

### BR-003: Required Fields

The following fields are mandatory for competition creation:
- Name (max 255 characters)
- Competition Date

### BR-004: Optional Fields

The following fields are optional:
- Location (max 255 characters)
- Description (unlimited text)

### BR-005: Cascade Deletion

When a competition is deleted, all associated records are automatically deleted:
- All categories belonging to the competition
- All category_event associations for those categories
- All results linked to those category_event associations
- Athletes and their category assignments are affected (category_id set to NULL)

This ensures referential integrity but requires careful confirmation before deletion.

### BR-006: Competition Edit Restrictions

Competitions can be edited at any time. However, administrators should be cautious when modifying competition dates if results have already been recorded, as this may affect reporting and historical accuracy.

## UI/UX Considerations

- The competition list should display competitions in reverse chronological order (most recent first)
- Each competition should show: name, date, location, and number of categories/athletes
- Form validation should occur both client-side (immediate feedback) and server-side (security)
- Delete operations should require explicit confirmation due to their destructive nature
- Success and error messages should be clear and actionable

## Related Use Cases

- **UC-002**: Define Categories - Follows competition creation to set up athlete groupings
- **UC-011**: Edit Competition Details - Specialized edit functionality (if needed as separate use case)
