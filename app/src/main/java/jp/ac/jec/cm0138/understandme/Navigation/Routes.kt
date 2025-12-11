package jp.ac.jec.cm0138.understandme.Navigation
import jp.ac.jec.cm0138.understandme.R
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
    val icon: Int,   // Drawable resource ID
    val label: String
)


val bottomNavItems = listOf(
    BottomNavItem(
        route = HOME_ROUTE::class.qualifiedName!!,
        icon = R.drawable.house_fill,  // built-in vector
        label = "ホーム"
    ),
    BottomNavItem(
        route = CLASSES_ROUTE::class.qualifiedName!!,
        icon = R.drawable.graduationcap_fill,  // your SVG
        label = "科目一覧"
    ),
    BottomNavItem(
        route = HOMEWORKS_ROUTE::class.qualifiedName!!,
        icon = R.drawable.list_bullet_clipboard_fill,
        label = "課題一覧"
    ),
    BottomNavItem(
        route = PROFILE_ROUTE::class.qualifiedName!!,
        icon = R.drawable.person_fill,
        label = "プロフィール"
    )
)
