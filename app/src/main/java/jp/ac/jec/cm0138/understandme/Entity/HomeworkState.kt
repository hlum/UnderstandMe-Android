package jp.ac.jec.cm0138.understandme.Entity

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
enum class HomeworkState {
    NOT_ASSIGNED,
    GENERATING_QUESTIONS,
    QUESTION_GENERATED,
    COMPLETED,
    FAILED;

    val color: Color
        get() = when (this) {
            NOT_ASSIGNED -> Color.Gray
            GENERATING_QUESTIONS -> Color.Yellow
            QUESTION_GENERATED -> Color.Blue
            COMPLETED -> Color.Green
            FAILED -> Color.Red
        }

    val stateDescription: String
        get() = when (this) {
            NOT_ASSIGNED -> "未提出"
            GENERATING_QUESTIONS -> "問題生成中"
            QUESTION_GENERATED -> "問題生成完了"
            COMPLETED -> "提出完了"
            FAILED -> "生成失敗"
        }
}
