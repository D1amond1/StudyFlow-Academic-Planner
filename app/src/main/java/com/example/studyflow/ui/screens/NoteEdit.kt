package com.example.studyflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import com.example.studyflow.ui.theme.*

@Composable
fun NoteEdit(
    initialTitle: String,
    initialDescription: String,
    onBack: () -> Unit,
    onSave: (title: String, description: String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDescription) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onSave(title, description)
                    onBack()
                },
                containerColor = TealPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Check, contentDescription = "Сохранить изменения")
            }
        },
        containerColor = LightAppBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD9D9D9))
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Отменить изменения", tint = TextUserData)
                }

                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextUserData
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }

            BasicTextField(
                value = description,
                onValueChange = { description = it },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = TextUserData,
                    lineHeight = 24.sp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhiteSurface)
                    .padding(16.dp)
            )
        }
    }
}