package com.example.dictionaryapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dictionaryapp.ui.theme.DictionaryAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.dictionaryapp.data.database.Word
import com.example.dictionaryapp.data.database.WordDefinition
import com.example.dictionaryapp.data.database.WordQuery
import com.example.dictionaryapp.model.ApiState


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DictionaryAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DictionaryScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun DictionaryScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    var text by remember { mutableStateOf("") }
    val state = viewModel.dictionaryLookupState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp), // spacing between TextField and Button
                    label = { Text("Enter a word") }
                )
                Button(
                    onClick = {
                        viewModel.lookupWord(text)
                    }
                ) {
                    Text(text = "Submit")
                }
            }

            if (state.value is ApiState.Success) {
                val wordQueries = (state.value as ApiState.Success<List<WordQuery>>).data
                val listItem = wordQueries.map { wordQuery ->
                    wordQuery.definitions.map {
                        wordQuery.word to it
                    }
                }.flatten()
                DictionaryList(listItem)
            } else if (state.value is ApiState.Error) {
                ErrorMessage(state.value as ApiState.Error)
            }
        }
        if (state.value is ApiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun DictionaryList(items: List<Pair<Word, WordDefinition>>) {

    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn {
        items(items) { item ->
            val word = item.first
            val definition = item.second
            val text = "${word.wordText} (${definition.partOfSpeech}): ${definition.definition}"
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
fun ErrorMessage(state: ApiState.Error) {
    val context = LocalContext.current
    Toast.makeText(context, "issue happened while trying to fetch data", Toast.LENGTH_SHORT).show()
}
