package jp.ac.jec.cm0138.understandme.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


@Serializable
data object LOGIN_ROUTE
@Serializable
data object HOME_ROUTE

@Serializable
data object CLASSES_ROUTE

@Serializable
data object HOMEWORKS_ROUTE

@Serializable
data object PROFILE_ROUTE


data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)


val bottomNavItems = listOf(
    BottomNavItem(
        route = HOME_ROUTE::class.qualifiedName!!,
        icon = Icons.Filled.Home,
        label = "ホーム"
    ),
    BottomNavItem(
        route = CLASSES_ROUTE::class.qualifiedName!!,
        icon = Icons.Filled.Class,
        label = "科目一覧"
    ),
    BottomNavItem(
        route = HOMEWORKS_ROUTE::class.qualifiedName!!,
        icon = Icons.Filled.Book,
        label = "課題一覧"
    ),
    BottomNavItem(
        route = PROFILE_ROUTE::class.qualifiedName!!,
        icon = Icons.Filled.Person,
        label = "プロフィール"
    )
)
