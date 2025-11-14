package com.github.fiecher.turnforge.config.containers;

import com.github.fiecher.turnforge.domain.services.AbilityService;
import com.github.fiecher.turnforge.domain.services.CharacterService;
import com.github.fiecher.turnforge.domain.services.UserService;

public record ServiceContainer(CharacterService characterService, UserService userService,
                               AbilityService abilityService) {

}