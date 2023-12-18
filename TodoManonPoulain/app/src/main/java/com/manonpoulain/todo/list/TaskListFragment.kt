package com.manonpoulain.todo.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.manonpoulain.todo.R
import com.manonpoulain.todo.data.Api
import com.manonpoulain.todo.data.TaskListViewModel
import com.manonpoulain.todo.data.User
import com.manonpoulain.todo.databinding.FragmentTaskListBinding
import com.manonpoulain.todo.detail.DetailActivity
import kotlinx.coroutines.launch
import java.util.UUID

class TaskListFragment : Fragment(){
    val adapterListener : TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            //taskList = taskList - task
            //refreshAdapter()

            viewModel.remove(task)

        }// Supprimer la tâche
        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task",task)
            editTask.launch(intent)
        }// Editer la tâche
    }

    private val viewModel: TaskListViewModel by viewModels()

    override fun onResume() {
        lifecycleScope.launch {
            val user = Api.userWebService.fetchUser().body()!!
            binding.userTextView.text = user.name
        }

        viewModel.refresh() // on demande de rafraîchir les données sans attendre le retour directement
        super.onResume()
    }



    //private var taskList = listOf("Task 1", "Task 2", "Task 3")

    /*private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )*/

    private val adapter = TaskListAdapter(adapterListener)
    private lateinit var binding : FragmentTaskListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val rootView = binding.root

        //On récup les info sauvegardées
        var taskListArray = savedInstanceState?.getSerializable("tasklist") as? Array<Task>
        //taskList = taskListArray?.toList() ?: emptyList()
        viewModel.tasksStateFlow.value = taskListArray?.toList() ?: emptyList()
        //adapter.submitList(taskList)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.recyclerView
        val button = binding.floatingActionButton2
        button.setOnClickListener {
            // Instanciation d'un objet task avec des données préremplies:
            //val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            //refreshAdapter()

            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${ viewModel.tasksStateFlow.value.size + 1}")
            viewModel.add(newTask)

            val intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)
        }

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                adapter.submitList(newList)
            }
        }
        //super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
    }

    //En gros c'est le truc avant le Destroy, quand on change d'orientation ça destroy l'activité, et ça ça permet de pas tout perdre
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("tasklist",viewModel.tasksStateFlow.value.toTypedArray())
        super.onSaveInstanceState(outState)
    }

    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if(task != null) {
            viewModel.add(task)
        }
    }
    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if(task != null) {
            viewModel.edit(task)
        }
    }


}