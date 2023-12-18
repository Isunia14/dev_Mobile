package com.manonpoulain.todo.data

import com.manonpoulain.todo.list.Task
import retrofit2.Response
import retrofit2.http.GET

interface TaskWebService {
    @GET("/rest/v2/tasks/")
    suspend fun fetchTasks(): Response<List<Task>>
}