package jp.ac.jec.cm0138.understandme.Nav

import kotlinx.serialization.Serializable

@Serializable
object Route {
    @Serializable
    object LoginScreen

    @Serializable
    object HomeRoute

    @Serializable
    object MajorListRoute

    @Serializable
    object HomeworkListRoute

    @Serializable
    object ProfileRoute
}