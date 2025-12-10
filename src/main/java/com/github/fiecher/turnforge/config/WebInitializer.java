package com.github.fiecher.turnforge.config;

import com.github.fiecher.turnforge.config.containers.RepositoryContainer;
import com.github.fiecher.turnforge.config.containers.ServiceContainer;
import com.github.fiecher.turnforge.config.containers.UseCaseContainer;
import com.github.fiecher.turnforge.config.factories.PostgresRepositoryFactory;
import com.github.fiecher.turnforge.config.factories.ServiceFactory;
import com.github.fiecher.turnforge.config.factories.UseCaseFactory;
import com.github.fiecher.turnforge.domain.services.PasswordGenerator;
import com.github.fiecher.turnforge.infrastructure.db.postgres.PostgresConnectionManager;
import com.github.fiecher.turnforge.infrastructure.security.TokenService;
import com.github.fiecher.turnforge.presentation.servlet.api.*;
import com.github.fiecher.turnforge.presentation.servlet.filter.AuthFilter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.annotation.WebListener;

import java.util.EnumSet;

@WebListener
public class WebInitializer implements ServletContextListener {

    private PostgresConnectionManager connectionManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        System.out.println("=== TurnForge Web Application Initializing ===");

        try {
            String salt = getRequiredEnv("SALT", context);
            String url = getRequiredEnv("DB_URL", context);
            String user = getRequiredEnv("DB_USER", context);
            String password = System.getenv("DB_PASSWORD");
            String driver = context.getInitParameter("DB_DRIVER");
            if (driver == null || driver.isEmpty()) driver = "org.postgresql.Driver";

            PasswordGenerator.init(salt);
            TokenService tokenService = new TokenService(salt);
            System.out.println("✓ Security services initialized");

            connectionManager = PostgresConnectionManager.init(url, user, password, driver);
            System.out.println("✓ Database connection established");

            PostgresRepositoryFactory repoFactory = new PostgresRepositoryFactory(connectionManager);
            RepositoryContainer repositories = repoFactory.createAllRepositories();

            ServiceFactory serviceFactory = new ServiceFactory(repositories);
            ServiceContainer services = serviceFactory.createAllServices();

            UseCaseFactory useCaseFactory = new UseCaseFactory(repositories, services);
            UseCaseContainer useCases = useCaseFactory.createAllUseCases();
            System.out.println("✓ Application layers wired");

            AuthFilter authFilter = new AuthFilter(tokenService);
            FilterRegistration.Dynamic filterReg = context.addFilter("AuthFilter", authFilter);
            filterReg.addMappingForUrlPatterns(EnumSet.of(jakarta.servlet.DispatcherType.REQUEST), true, "/api/v1/*");
            System.out.println("✓ AuthFilter registered");

            initializeServlets(context, useCases, tokenService);

            registerDefaultServlet(context);

            System.out.println("=== TurnForge Web Application Started Successfully ===");

        } catch (Exception e) {
            e.printStackTrace(System.err);
            context.log("FATAL ERROR during application initialization:", e);
            throw new RuntimeException("Application configuration failed.", e);
        }
    }

    private void initializeServlets(ServletContext context, UseCaseContainer useCases,
                                    TokenService tokenService) {

        UserServlet.setDependencies(
                useCases.getCreateUserUseCase(),
                useCases.getLoginUserUseCase(),
                tokenService
        );
        registerServlet(context, new UserServlet(), "/api/v1/users/*");

        CharacterServlet characterServlet = new CharacterServlet(
                useCases.getCreateCharacterUseCase(),
                useCases.getGetCharactersUseCase(),
                useCases.getUpdateCharacterStatsUseCase(),
                useCases.getAddAbilityToCharacterUseCase()
        );
        registerServlet(context, characterServlet, "/api/v1/characters/*");

        AbilityServlet.setDependencies(
                useCases.getCreateAbilityUseCase(),
                useCases.getGetAbilityUseCase(),
                useCases.getUpdateAbilityUseCase(),
                useCases.getDeleteAbilityUseCase()
        );
        registerServlet(context, new AbilityServlet(), "/api/v1/abilities/*");

        WeaponServlet.setDependencies(
                useCases.getCreateWeaponUseCase(),
                useCases.getGetWeaponUseCase(),
                useCases.getUpdateWeaponUseCase(),
                useCases.getDeleteWeaponUseCase()
        );
        registerServlet(context, new WeaponServlet(), "/api/v1/weapons/*");

        ArmorServlet.setDependencies(
                useCases.getCreateArmorUseCase(),
                useCases.getGetArmorUseCase(),
                useCases.getUpdateArmorUseCase(),
                useCases.getDeleteArmorUseCase()
        );
        registerServlet(context, new ArmorServlet(), "/api/v1/armor/*");

        ItemServlet.setDependencies(
                useCases.getCreateItemUseCase(),
                useCases.getGetItemUseCase(),
                useCases.getUpdateItemUseCase(),
                useCases.getDeleteItemUseCase()
        );
        registerServlet(context, new ItemServlet(), "/api/v1/items/*");

        SkillServlet.setDependencies(
                useCases.getCreateSkillUseCase(),
                useCases.getGetSkillUseCase(),
                useCases.getUpdateSkillUseCase(),
                useCases.getDeleteSkillUseCase()
        );
        registerServlet(context, new SkillServlet(), "/api/v1/skills/*");

        TraitServlet.setDependencies(
                useCases.getCreateTraitUseCase(),
                useCases.getGetTraitUseCase(),
                useCases.getUpdateTraitUseCase(),
                useCases.getDeleteTraitUseCase()
        );
        registerServlet(context, new TraitServlet(), "/api/v1/traits/*");

        registerServlet(context, new HealthServlet(), "/api/v1/health");

        System.out.println("✓ All API servlets registered");
    }

    private void registerServlet(ServletContext context, jakarta.servlet.http.HttpServlet servlet, String path) {
        context.addServlet(servlet.getClass().getSimpleName(), servlet)
                .addMapping(path);
    }


    private void registerDefaultServlet(ServletContext context) {
        ServletRegistration.Dynamic defaultServlet =
                (ServletRegistration.Dynamic) context.getServletRegistration("default");

        if (defaultServlet != null) {
            defaultServlet.addMapping("/");
            System.out.println("✓ DefaultServlet (static files handler) registered to '/'");
        } else {
            System.err.println("Default servlet not found. Cannot serve static files.");
        }
    }


    private String getRequiredEnv(String key, ServletContext context) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            context.log("FATAL ERROR: Environment variable " + key + " is required and not set.");
            throw new IllegalStateException("Environment variable " + key + " is required.");
        }
        return value;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        System.out.println("=== TurnForge Web Application Shutting Down ===");
    }
}