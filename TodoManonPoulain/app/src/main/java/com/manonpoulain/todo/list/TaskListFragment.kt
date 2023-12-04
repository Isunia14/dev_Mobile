package com.manonpoulain.todo.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.manonpoulain.todo.R
import java.util.UUID

class TaskListFragment : Fragment() {

    //private var taskList = listOf("Task 1", "Task 2", "Task 3")

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    private val adapter = TaskListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
       adapter.currentList() = taskList
        return rootView
    }

    fun refreshAdapter(recyclerView : RecyclerView){
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.manonpoulain)
        val button = view.findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        button.setOnClickListener {
            // Instanciation d'un objet task avec des données préremplies:
            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList = taskList + newTask
            refreshAdapter(recyclerView)
        }
        //super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
    }
}