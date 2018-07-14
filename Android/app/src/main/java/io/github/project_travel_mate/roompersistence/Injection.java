package io.github.project_travel_mate.roompersistence;

import android.content.Context;
/**
 * Enables injection of data sources.
 */
public class Injection {

    public static ChecklistDataSource provideUserDataSource(Context context) {
        DbChecklist database = DbChecklist.getsInstance(context);
        return new ChecklistDataSource(database.checklistItemDAO());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        ChecklistDataSource dataSource = provideUserDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
