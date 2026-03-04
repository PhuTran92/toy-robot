package com.example.toyrobot.di

import com.example.toyrobot.domain.usecase.MoveRobotUseCase
import com.example.toyrobot.domain.usecase.PlaceRobotUseCase
import com.example.toyrobot.domain.usecase.ReportRobotUseCase
import com.example.toyrobot.domain.usecase.TurnLeftUseCase
import com.example.toyrobot.domain.usecase.TurnRightUseCase
import com.example.toyrobot.domain.usecase.ValidatePositionUseCase
import com.example.toyrobot.presentation.viewmodel.RobotViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // Use cases
    factoryOf(::ValidatePositionUseCase)
    factoryOf(::PlaceRobotUseCase)
    factoryOf(::MoveRobotUseCase)
    factoryOf(::TurnLeftUseCase)
    factoryOf(::TurnRightUseCase)
    factoryOf(::ReportRobotUseCase)

    // ViewModel
    viewModelOf(::RobotViewModel)
}

