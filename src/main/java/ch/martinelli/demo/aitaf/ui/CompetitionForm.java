package ch.martinelli.demo.aitaf.ui;

import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class CompetitionForm extends FormLayout {

    private final TextField name = new TextField("Name");
    private final DatePicker competitionDate = new DatePicker("Date");
    private final TextField location = new TextField("Location");

    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");

    private final Binder<CompetitionRecord> binder = new Binder<>(CompetitionRecord.class);

    public CompetitionForm() {
        binder.forField(name)
                .asRequired("Name is required")
                .bind(CompetitionRecord::getName, CompetitionRecord::setName);

        binder.forField(competitionDate)
                .asRequired("Date is required")
                .bind(CompetitionRecord::getCompetitionDate, CompetitionRecord::setCompetitionDate);

        binder.forField(location)
                .bind(CompetitionRecord::getLocation, CompetitionRecord::setLocation);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> validateAndSave());

        cancelButton.addClickListener(e -> fireEvent(new CancelEvent(this)));

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        add(name, competitionDate, location, buttons);

        setColspan(buttons, 2);
    }

    public void setCompetition(CompetitionRecord competition) {
        binder.setBean(competition);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }

    public static abstract class CompetitionFormEvent extends ComponentEvent<CompetitionForm> {
        private final CompetitionRecord competition;

        protected CompetitionFormEvent(CompetitionForm source, CompetitionRecord competition) {
            super(source, false);
            this.competition = competition;
        }

        public CompetitionRecord getCompetition() {
            return competition;
        }
    }

    public static class SaveEvent extends CompetitionFormEvent {
        SaveEvent(CompetitionForm source, CompetitionRecord competition) {
            super(source, competition);
        }
    }

    public static class CancelEvent extends CompetitionFormEvent {
        CancelEvent(CompetitionForm source) {
            super(source, source.binder.getBean());
        }
    }
}
