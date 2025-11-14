package com.github.fiecher.turnforge.config.containers;

import com.github.fiecher.turnforge.app.usecase.CreateCharacterUseCase;
import com.github.fiecher.turnforge.app.usecase.CreateUserUseCase;
import com.github.fiecher.turnforge.app.usecase.GetCharactersUseCase;
import com.github.fiecher.turnforge.app.usecase.LoginUserUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.CreateAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.DeleteAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.GetAbilityUseCase;
import com.github.fiecher.turnforge.app.usecase.ability.UpdateAbilityUseCase;

public record UseCaseContainer(
        CreateUserUseCase createUserUseCase,
        LoginUserUseCase loginUserUseCase,
        CreateCharacterUseCase createCharacterUseCase,
        GetCharactersUseCase getCharactersUseCase,
        CreateAbilityUseCase createAbilityUseCase,
        GetAbilityUseCase getAbilityUseCase,
        UpdateAbilityUseCase updateAbilityUseCase,
        DeleteAbilityUseCase deleteAbilityUseCase) {
}
