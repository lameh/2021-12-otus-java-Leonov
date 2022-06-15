package ru.otus.appcontainer.configs;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.EquationPreparer;
import ru.otus.services.EquationPreparerImpl;
import ru.otus.services.IOService;
import ru.otus.services.IOServiceStreams;

@AppComponentsContainerConfig(order = 0)
public class AppConfig1 {

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }

    @AppComponent(order = 0, name = "equatationPreparer")
    public EquationPreparer equationPreparer() {
        return new EquationPreparerImpl();
    }
}
