package ch.martinelli.demo.aitaf.view;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import ch.martinelli.demo.aitaf.service.CompetitionService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route("")
public class CompetitionView extends VerticalLayout {

    private final CompetitionService service;
    private final Grid<CompetitionRecord> grid = new Grid<>(CompetitionRecord.class, false);
    private final Binder<CompetitionRecord> binder = new Binder<>(CompetitionRecord.class);

    private Dialog formDialog;
    private CompetitionRecord currentCompetition;

    public CompetitionView(CompetitionService service) {
        this.service = service;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeader(), createGrid());

        refreshGrid();
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H2 title = new H2("Competitions");

        Button addButton = new Button("Add Competition", e -> openFormDialog(null));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        header.add(title, addButton);
        return header;
    }

    private Component createGrid() {
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        grid.addColumn(CompetitionRecord::getName)
                .setHeader("Name")
                .setSortable(true)
                .setAutoWidth(true);

        grid.addColumn(CompetitionRecord::getCompetitionDate)
                .setHeader("Date")
                .setSortable(true)
                .setAutoWidth(true);

        grid.addColumn(CompetitionRecord::getLocation)
                .setHeader("Location")
                .setSortable(true)
                .setAutoWidth(true);

        grid.addColumn(CompetitionRecord::getDescription)
                .setHeader("Description")
                .setAutoWidth(true);

        grid.addComponentColumn(competition -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button editButton = new Button("Edit", e -> openFormDialog(competition));
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

            Button deleteButton = new Button("Delete", e -> confirmDelete(competition));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

            actions.add(editButton, deleteButton);
            return actions;
        }).setHeader("Actions").setAutoWidth(true);

        return grid;
    }

    private void openFormDialog(CompetitionRecord competition) {
        currentCompetition = competition != null ? copyRecord(competition) : new CompetitionRecord();

        formDialog = new Dialog();
        formDialog.setHeaderTitle(competition != null ? "Edit Competition" : "Add Competition");
        formDialog.setWidth("600px");

        FormLayout form = createForm();
        binder.readBean(currentCompetition);

        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        Button saveButton = new Button("Save", e -> saveCompetition());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> formDialog.close());

        actions.add(saveButton, cancelButton);

        formDialog.add(form, actions);
        formDialog.open();
    }

    private FormLayout createForm() {
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        TextField nameField = new TextField("Name");
        nameField.setRequired(true);
        nameField.setMaxLength(255);

        DatePicker dateField = new DatePicker("Competition Date");
        dateField.setRequired(true);

        TextField locationField = new TextField("Location");
        locationField.setMaxLength(255);

        TextArea descriptionField = new TextArea("Description");
        descriptionField.setHeight("100px");

        binder.forField(nameField)
                .asRequired("Name is required")
                .withValidator(name -> name.length() >= 3, "Name must be at least 3 characters")
                .bind(CompetitionRecord::getName, CompetitionRecord::setName);

        binder.forField(dateField)
                .asRequired("Date is required")
                .bind(CompetitionRecord::getCompetitionDate, CompetitionRecord::setCompetitionDate);

        binder.forField(locationField)
                .bind(CompetitionRecord::getLocation, CompetitionRecord::setLocation);

        binder.forField(descriptionField)
                .bind(CompetitionRecord::getDescription, CompetitionRecord::setDescription);

        form.add(nameField, dateField, locationField, descriptionField);
        return form;
    }

    private void saveCompetition() {
        try {
            if (!binder.isValid()) {
                binder.validate();
                return;
            }

            binder.writeBean(currentCompetition);

            if (currentCompetition.getId() == null) {
                service.create(currentCompetition);
                showNotification("Competition created successfully", NotificationVariant.LUMO_SUCCESS);
            } else {
                service.update(currentCompetition);
                showNotification("Competition updated successfully", NotificationVariant.LUMO_SUCCESS);
            }

            formDialog.close();
            refreshGrid();
        } catch (ValidationException e) {
            showNotification("Please correct the validation errors", NotificationVariant.LUMO_ERROR);
        }
    }

    private void confirmDelete(CompetitionRecord competition) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Confirm Delete");

        VerticalLayout content = new VerticalLayout();
        content.add("Are you sure you want to delete competition '" + competition.getName() + "'?");
        content.add("All associated categories, events, athletes, and results will also be deleted.");
        content.add("This action cannot be undone.");

        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        Button deleteButton = new Button("Delete", e -> {
            service.delete(competition.getId());
            confirmDialog.close();
            refreshGrid();
            showNotification("Competition deleted successfully", NotificationVariant.LUMO_SUCCESS);
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> confirmDialog.close());

        actions.add(deleteButton, cancelButton);
        content.add(actions);

        confirmDialog.add(content);
        confirmDialog.open();
    }

    private void refreshGrid() {
        grid.setItems(service.findAll());
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(variant);
    }

    private CompetitionRecord copyRecord(CompetitionRecord source) {
        CompetitionRecord copy = new CompetitionRecord();
        copy.setId(source.getId());
        copy.setName(source.getName());
        copy.setCompetitionDate(source.getCompetitionDate());
        copy.setLocation(source.getLocation());
        copy.setDescription(source.getDescription());
        copy.setCreatedAt(source.getCreatedAt());
        copy.setUpdatedAt(source.getUpdatedAt());
        return copy;
    }
}
