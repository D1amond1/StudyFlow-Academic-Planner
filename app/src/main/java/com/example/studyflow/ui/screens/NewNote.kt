package com.example.studyflow.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.studyflow.ui.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsState()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val defaultTitle = "Note ${notes.size + 1}"

    val onSave = {
        if (title.isBlank() && content.isBlank()) {
        } else {
            viewModel.saveNewNote(title.ifBlank { defaultTitle }, content)
        }
        navController.popBackStack()
    }

    BackHandler { onSave() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) Text("Note...", color = Color.Gray, fontSize = 20.sp)
                            innerTextField()
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onSave() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onSave() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            BasicTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier.fillMaxSize(),
                textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) Text("New note...", color = Color.Gray)
                    innerTextField()
                }
            )
        }
    }
}