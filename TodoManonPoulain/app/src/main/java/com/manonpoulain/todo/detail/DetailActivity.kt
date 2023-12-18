package com.manonpoulain.todo.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manonpoulain.todo.R
import com.manonpoulain.todo.detail.ui.theme.TodoManonPoulainTheme
import com.manonpoulain.todo.list.Task
import java.util.UUID

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var task=intent.getSerializableExtra("task") as Task?
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    val description = intent.getStringExtra(Intent.EXTRA_TEXT)
                    if(intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
                        task = Task(UUID.randomUUID().toString(),  "",description.toString())
                    } // Handle text being sent
                }
            }
        }
        setContent {
            TodoManonPoulainTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Detail("Android")
                    Detail(onValidate={newTask->
                        intent.putExtra("task", newTask)
                        setResult(RESULT_OK, intent);
                        finish();
                    }, initialTask = task)
                }
            }
        }

    }
}

@Composable
fun Detail(modifier: Modifier = Modifier,onValidate: (Task) -> Unit,initialTask: Task?) {
    var task by remember { mutableStateOf(initialTask ?: Task(UUID.randomUUID().toString(),"","")) } // faire les imports suggérés par l'IDE
    Column(
        modifier = Modifier.padding(16.dp),
        Arrangement.spacedBy(16.dp)
    ){
        Text(
            text = "Task Detail",
            style=MaterialTheme.typography.headlineLarge,
            modifier = modifier
        )
        OutlinedTextField(
            value=task.title,
            onValueChange = {task = task.copy(title = it)},
            label={Text("Title")}
            //text = "Title"
        )

        OutlinedTextField(
            value=task.description,
            onValueChange = {task = task.copy(description = it)},
            label={Text("Description")}
        )

        OutlinedButton(
            onClick = {
                //val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !");
                onValidate(task);
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Red),

        ) {
            Image(
                painter = painterResource(R.drawable.baseline_check_24),
                contentDescription = "",
            )

        }
        
    }

}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoManonPoulainTheme {
        //Detail("Android")
        //Detail()
    }
}