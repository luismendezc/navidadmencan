package com.oceloti.lemc.navidadmencan.presentation.navigation

sealed class Route(val path: String) {
    data object GameHub: Route("game_hub")
    data object Lobby: Route("lobby")
    data object ImpostorGame: Route("impostor_game")
}