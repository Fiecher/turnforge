package com.github.fiecher.turnforge.config.containers;

import com.github.fiecher.turnforge.domain.services.CharacterService;
import com.github.fiecher.turnforge.domain.services.UserService;

public class ServiceContainer {
    private final CharacterService characterService;
    private final UserService userService;

    public ServiceContainer(CharacterService characterService, UserService userService) {
        this.characterService = characterService;
        this.userService = userService;
    }

    public CharacterService getCharacterService() {
        return characterService;
    }

    public UserService getUserService() {
        return userService;
    }
}