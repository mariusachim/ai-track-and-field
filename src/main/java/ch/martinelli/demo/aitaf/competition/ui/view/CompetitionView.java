package ch.martinelli.demo.aitaf.competition.ui.view;

import ch.martinelli.demo.aitaf.competition.service.CompetitionService;
import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@PageTitle("Competitions")
@Route("competitions")
public class CompetitionView extends VerticalLayout {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionView.class);

    private final CompetitionService competitionService;
    private final Grid<CompetitionRecord> grid;
    private final Dialog formDialog;
    private final Binder<CompetitionRecord> binder;

    private CompetitionRecord currentCompetition;

    public CompetitionView(CompetitionService competitionService) {
        this.competitionService = competitionService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 title = new H2("Competitions");

        // Create dialog for form
        formDialog = new Dialog();
        formDialog.setWidth("500px");
        formDialog.setHeight("auto");

        // Create binder
        binder = new Binder<>(CompetitionRecord.class);

        // Create the form
        HorizontalLayout toolbar = createToolbar();
        grid = createGrid();

        add(title, toolbar, grid);
        refreshGrid();
    }

    private HorizontalLayout createToolbar() {
        Button addButton = new Button("New Competition", e -> openNewCompetitionDialog());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.END);

        return toolbar;
    }

    private Grid<CompetitionRecord> createGrid() {
        Grid<CompetitionRecord> grid = new Grid<>();
        grid.addColumn(CompetitionRecord::getName).setHeader("Name").setSortable(true);
        grid.addColumn(CompetitionRecord::getCompetitionDate).setHeader("Date").setSortable(true);
        grid.addColumn(CompetitionRecord::getLocation).setHeader("Location").setSortable(true);

        // Action buttons column
        grid.addComponentColumn(competition -> {
            HorizontalLayout actions = new HorizontalLayout();
            Button editButton = new Button("Edit", e -> openEditCompetitionDialog(competition));
            Button deleteButton = new Button("Delete", e -> confirmDelete(competition));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            actions.add(editButton, deleteButton);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Actions").setFlexGrow(0);

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();

        return grid;
    }

    private void refreshGrid() {
        List<CompetitionRecord> competitions = competitionService.findAll();
        grid.setItems(competitions);
    }

    private void openNewCompetitionDialog() {
        currentCompetition = new CompetitionRecord();
        binder.readBean(currentCompetition);
        openFormDialog("New Competition", true);
    }

    private void openEditCompetitionDialog(CompetitionRecord competition) {
        currentCompetition = competition;
        binder.readBean(competition);
        openFormDialog("Edit Competition", false);
    }

    private void openFormDialog(String title, boolean isNew) {
        formDialog.removeAll();
        formDialog.setHeaderTitle(title);

        FormLayout formLayout = createFormLayout();

        Button cancelButton = new Button("Cancel", e -> formDialog.close());
        Button saveButton = new Button("Save", e -> saveCompetition());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
        buttonLayout.setSpacing(true);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonLayout);
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);

        formDialog.add(dialogLayout);
        formDialog.open();
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Name");
        nameField.setWidthFull();

        DatePicker datePicker = new DatePicker("Date");
        datePicker.setWidthFull();

        TextField locationField = new TextField("Location");
        locationField.setWidthFull();

        formLayout.add(nameField, datePicker, locationField);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );

        // Bind fields
        binder.forField(nameField)
                .asRequired("Name is required")
                .bind(CompetitionRecord::getName, CompetitionRecord::setName);

        binder.forField(datePicker)
                .asRequired("Date is required")
                .bind(CompetitionRecord::getCompetitionDate, CompetitionRecord::setCompetitionDate);

        binder.forField(locationField)
                .bind(CompetitionRecord::getLocation, CompetitionRecord::setLocation);

        return formLayout;
    }

    private void saveCompetition() {
        try {
            binder.writeBean(currentCompetition);
            competitionService.save(currentCompetition);
            formDialog.close();
            refreshGrid();
            Notification.show("Competition saved successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (ValidationException e) {
            Notification.show("Please correct the errors in the form").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (Exception e) {
            LOGGER.error("Error saving competition", e);
            Notification.show("Error saving competition: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void confirmDelete(CompetitionRecord competition) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Delete Competition");
        confirmDialog.setText("Are you sure you want to delete \"" + competition.getName() + "\"?");

        if (competitionService.hasAthletes(competition.getId())) {
            confirmDialog.setText("This competition has registered athletes. You must remove all registrations before deleting.");
            confirmDialog.setConfirmText("OK");
            confirmDialog.setCancelable(false);
            confirmDialog.addConfirmListener(e -> confirmDialog.close());
        } else {
            confirmDialog.setConfirmText("Delete");
            confirmDialog.setConfirmButtonTheme("error primary");
            confirmDialog.setCancelable(true);
            confirmDialog.setCancelText("Cancel");
            confirmDialog.addConfirmListener(e -> deleteCompetition(competition));
            confirmDialog.addCancelListener(e -> confirmDialog.close());
        }

        confirmDialog.open();
    }

    private void deleteCompetition(CompetitionRecord competition) {
        try {
            competitionService.delete(competition.getId());
            refreshGrid();
            Notification.show("Competition deleted successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("Error deleting competition", e);
            Notification.show("Error deleting competition: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
