package ch.martinelli.demo.aitaf.ui;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import ch.martinelli.demo.aitaf.service.CompetitionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;

@Route("")
public class CompetitionView extends VerticalLayout {

    private final CompetitionService competitionService;
    private final Grid<CompetitionRecord> grid;
    private final CompetitionForm form;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public CompetitionView(CompetitionService competitionService) {
        this.competitionService = competitionService;

        setSizeFull();
        setPadding(true);

        Button addButton = new Button("Add Competition", VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openForm(new CompetitionRecord()));

        HorizontalLayout toolbar = new HorizontalLayout(addButton);

        grid = new Grid<>(CompetitionRecord.class, false);
        grid.addColumn(CompetitionRecord::getName)
                .setHeader("Name")
                .setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(record -> record.getCompetitionDate() != null
                ? record.getCompetitionDate().format(DATE_FORMATTER)
                : "")
                .setHeader("Date")
                .setSortable(true)
                .setAutoWidth(true);
        grid.addColumn(CompetitionRecord::getLocation)
                .setHeader("Location")
                .setSortable(true)
                .setAutoWidth(true);

        grid.addComponentColumn(record -> {
            Button editButton = new Button(VaadinIcon.EDIT.create());
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> openForm(record));

            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteCompetition(record));

            return new HorizontalLayout(editButton, deleteButton);
        }).setHeader("Actions").setAutoWidth(true);

        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                openForm(e.getValue());
            }
        });

        form = new CompetitionForm();
        form.setVisible(false);
        form.addSaveListener(this::saveCompetition);
        form.addCancelListener(e -> closeForm());

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        mainContent.setFlexGrow(2, grid);
        mainContent.setFlexGrow(1, form);

        add(toolbar, mainContent);

        refreshGrid();
    }

    private void openForm(CompetitionRecord record) {
        form.setCompetition(record);
        form.setVisible(true);
    }

    private void closeForm() {
        form.setVisible(false);
        grid.asSingleSelect().clear();
    }

    private void saveCompetition(CompetitionForm.SaveEvent event) {
        competitionService.save(event.getCompetition());
        refreshGrid();
        closeForm();
    }

    private void deleteCompetition(CompetitionRecord record) {
        competitionService.delete(record.getId());
        refreshGrid();
    }

    private void refreshGrid() {
        grid.setItems(competitionService.findAll());
    }
}
