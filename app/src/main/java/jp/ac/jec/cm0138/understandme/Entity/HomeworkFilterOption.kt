package jp.ac.jec.cm0138.understandme.Entity

sealed class HomeworkFilterOption {

    object All : HomeworkFilterOption()

    data class State(val homeworkState: HomeworkState) : HomeworkFilterOption()

    val displayName: String
        get() = when (this) {
            is All -> "すべて"
            is State -> homeworkState.stateDescription
        }

    companion object {
        val allCases: List<HomeworkFilterOption>
            get() = listOf(All) + HomeworkState.values().map { State(it) }
    }
}
