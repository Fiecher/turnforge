module com.github.fiecher.turnforge {
    requires java.sql;
    exports com.github.fiecher.turnforge;
    exports com.github.fiecher.turnforge.config;
    exports com.github.fiecher.turnforge.presentation.cli.input;
    exports com.github.fiecher.turnforge.presentation.cli.output;
    exports com.github.fiecher.turnforge.presentation.cli.commands;
    exports com.github.fiecher.turnforge.domain.services;
}