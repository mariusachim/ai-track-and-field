# Use Case: Manage Competitions

## Overview

**Use Case ID:** UC-1
**Use Case Name:** Manage Competitions
**Primary Actor:** Administrator
**Goal:** Allow administrators to create, update, and delete track and field competitions
**Status:** Draft

## Preconditions

- Administrator is authenticated and has proper permissions
- Database connection is available

## Main Success Scenario

1. Administrator navigates to the Competition Management view
2. System displays a list of all existing competitions (REQ-1.4)
3. Administrator chooses an action:
   - **Create new competition:**
     3a. Administrator clicks "Create Competition" button
     3b. System displays a form with fields for name, date, and location
     3c. Administrator enters competition details
     3d. System validates the input data
     3e. System saves the new competition (REQ-1.1)
     3f. System displays confirmation message and updates the competition list

   - **Update existing competition:**
     3a. Administrator selects a competition from the list
     3b. Administrator clicks "Edit" button
     3c. System displays a form pre-filled with competition details
     3d. Administrator modifies the competition details
     3e. System validates the input data
     3f. System saves the updated competition (REQ-1.2)
     3g. System displays confirmation message and updates the competition list

   - **Delete competition:**
     3a. Administrator selects a competition from the list
     3b. Administrator clicks "Delete" button
     3c. System displays a confirmation dialog
     3d. Administrator confirms the deletion
     3e. System deletes the competition if no athletes are registered (REQ-1.3)
     3f. System displays confirmation message and updates the competition list

## Alternative Flows

### A1: Validation Failure

**Trigger:** Administrator submits a form with invalid or missing data

**Flow:**

1. System validates the input data
2. System detects missing or invalid fields
3. System displays validation error messages next to the affected fields
4. Administrator corrects the errors and resubmits
5. System returns to step 3d of Main Success Scenario

### A2: Cancel Deletion

**Trigger:** Administrator clicks "Delete" button but changes mind

**Flow:**

1. System displays confirmation dialog
2. Administrator clicks "Cancel" button
3. System closes the dialog without deleting
4. System returns to step 2 of Main Success Scenario

### A3: Competition Has Registered Athletes

**Trigger:** Administrator attempts to delete a competition with registered athletes

**Flow:**

1. Administrator clicks "Delete" button for a competition with registrations
2. System displays confirmation dialog with warning about existing registrations
3. Administrator confirms the deletion
4. System prevents deletion and displays error message
5. Administrator must first remove all athlete registrations before deletion is allowed

## Postconditions

### Success Postconditions

- Competition data is created, updated, or deleted in the database
- Competition list is refreshed to reflect changes
- Administrator receives confirmation of the action

### Failure Postconditions

- No changes are made to the database
- System displays appropriate error message
- Administrator can retry or cancel the operation

## Business Rules

### BR-5: Competition Name Uniqueness
Competition names should be unique within the system to avoid confusion.

### BR-6: Competition Date Validity
Competition dates must be valid dates and should not be in the past when creating new competitions.

### BR-7: Deletion Restriction
Competitions cannot be deleted if they have registered athletes. All athlete registrations must be removed first.

## Related Requirements

- REQ-1.1: System shall allow administrators to create a new competition with basic details (name, date, location).
- REQ-1.2: System shall allow administrators to update existing competition information.
- REQ-1.3: System shall allow administrators to delete competitions.
- REQ-1.4: System shall allow administrators to view a list of all competitions.
