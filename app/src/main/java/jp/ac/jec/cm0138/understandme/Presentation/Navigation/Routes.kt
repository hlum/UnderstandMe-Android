package jp.ac.jec.cm0138.understandme.Presentation.Navigation
import androidx.annotation.Keep
import jp.ac.jec.cm0138.understandme.R
import kotlinx.serialization.Serializable


@Serializable
data object LOGIN_ROUTE
@Serializable
data object HOME_ROUTE

@Serializable
data object CLASSES_ROUTE

@Serializable
data class HOMEWORKS_FOR_CLASS_ROUTE (
    val className: String?,
    val classID: String?
)

@Serializable
data object HOMEWORK_ENTRY_ROUTE

@Serializable
data object PROFILE_ROUTE


@Serializable
data class HOMEWORK_DETAIL_ROUTE(
    val homeworkID: String
)


@Serializable
data class TEST_EXPLANATION_ROUTE(
    val homeworkID: String,
)

@Serializable
data class ANSWER_QUESTIONS_ROUTE(
    val homeworkID: String,
    val mode: AnswerMode
)

@Serializable
data class RESULT_CONFIRMATION_ROUTE(
    val homeworkID: String
)

@Serializable
data object DETAIL_STATS_ROUTE

@Keep
enum class AnswerMode {
    ANSWER,
    REVIEW
}



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
        route = HOMEWORK_ENTRY_ROUTE::class.qualifiedName!!,
        icon = R.drawable.list_bullet_clipboard_fill,
        label = "課題一覧"
    ),
    BottomNavItem(
        route = PROFILE_ROUTE::class.qualifiedName!!,
        icon = R.drawable.person_fill,
        label = "プロフィール"
    )
)
