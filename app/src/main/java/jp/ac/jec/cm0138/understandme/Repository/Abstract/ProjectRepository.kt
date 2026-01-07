package jp.ac.jec.cm0138.understandme.Repository.Abstract

interface ProjectRepository {
    suspend fun uploadProject(
        userID: String,
        homeworkID: String,
        projectLink: String
    )
}