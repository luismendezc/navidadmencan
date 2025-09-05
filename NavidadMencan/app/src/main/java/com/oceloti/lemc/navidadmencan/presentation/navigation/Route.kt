package com.oceloti.lemc.navidadmencan.presentation.navigation

sealed class Route(val path: String) {
    data object Welcome: Route("welcome")
    data object Lobby: Route("lobby") //TODO
}