package com.example.btppilot.data.api

object ApiRoutes {
//    const val BASE_URL = "http://10.0.2.2:3000/"
    const val BASE_URL = "http://192.168.1.35:3000/"

    const val BTP_API_AUTH_LOGIN = "auth/login/"
    const val AUTH_REGISTER = "auth/register/"
    const val INVITE_USER_TO_COMPANY =  "/companies/inviteUser"
    const val CREATE_COMPANY = "/companies"
    const val GET_POST_PROJECTS_COMPANY = "/company/{id}/project"
    const val GET_TASK_COMPANY = "/company/{id}/tasks"
    const val GET_USER_COMPANY = "/companies/{id}/users"

    const val GET_PROJECT_BY_ID = "/project/{projectId}"
    const val GET_TASK_BY_PROJECT_ID = "/projects/{projectId}/tasks"

    const val GET_USERS_PROJECT = "/project/{projectId}/member"
}