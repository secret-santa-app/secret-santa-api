package org.builtonaws.secretsanta.dagger;

import dagger.Component;
import org.builtonaws.secretsanta.controller.CreateAssignmentController;
import org.builtonaws.secretsanta.controller.CreateDraftController;
import org.builtonaws.secretsanta.controller.GetDraftController;

@Component(modules = {PersistenceModule.class, UtilsModule.class, DatabaseModule.class})
public interface ControllersComponent {
    CreateAssignmentController createAssignmentController();

    CreateDraftController createDraftController();

    GetDraftController getDraftController();
}
