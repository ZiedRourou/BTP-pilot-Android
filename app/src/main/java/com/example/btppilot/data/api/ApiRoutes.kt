package com.example.btppilot.data.api

object ApiRoutes {
//    const val BASE_URL = "http://10.0.2.2:3000/"
    const val BASE_URL = "http://192.168.1.35:3000/"

    const val API_AUTH_LOGIN_ROUTE = "auth/login/"
    const val API_AUTH_REGISTER_ROUTE = "auth/register/"

    const val API_INVITE_USER_TO_COMPANY_ROUTE =  "/companies/inviteUser"
    const val API_USER_COMPANY_ROUTE = "/companies/{id}/users"

    const val API_COMPANY_ROUTE = "/companies"

    const val API_PROJECT_COMPANY_ROUTE = "/company/{id}/project"
    const val API_PROJECT_ROUTE = "/project/{projectId}"

    const val API_TASK_COMPANY_ROUTE = "/company/{id}/tasks"
    const val API_TASK_PROJECT_ROUTE = "/projects/{projectId}/tasks"
    const val API_TASK_ROUTE = "/tasks/{taskId}"
}