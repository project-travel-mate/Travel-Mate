package io.github.project_travel_mate.roompersistence;

import android.content.Context;
/**
 * Enables injection of data sources.
 */
public class Injection {

    public static ChecklistDataSource provideUserDataSource(Context context, int userId) {
        DbChecklist database = DbChecklist.getsInstance(context, userId);
        return new ChecklistDataSource(database.checklistItemDAO());
    }

    public static ViewModelFactory provideViewModelFactory(Context context, int userId) {
        ChecklistDataSource dataSource = provideUserDataSource(context, userId);
        return new ViewModelFactory(dataSource);
    }
}
