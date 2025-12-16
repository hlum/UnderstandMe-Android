package jp.ac.jec.cm0138.understandme.Entity

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
enum class HomeworkState {
    notAssigned,
    generatingQuestions,
    questionGenerated,
    completed,
    failed;

    val color: Color
        get() = when (this) {
            notAssigned -> Color.Gray
            generatingQuestions -> Color.Yellow
            questionGenerated -> Color.Blue
            completed -> Color.Green
            failed -> Color.Red
        }

    val stateDescription: String
        get() = when (this) {
            notAssigned -> "未提出"
            generatingQuestions -> "問題生成中"
            questionGenerated -> "問題生成完了"
            completed -> "提出完了"
            failed -> "生成失敗"
        }
}
