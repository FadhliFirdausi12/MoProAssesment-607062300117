package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fadhlifirdausi607062300117.asesment1.R
import com.fadhlifirdausi607062300117.asesment1.database.NotesDb
import com.fadhlifirdausi607062300117.asesment1.ui.theme.Asesment1Theme
import com.fadhlifirdausi607062300117.asesment1.util.ViewModelFactory


const val KEY_NOTES_ID = "notesId"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesDetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = NotesDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)


    var judul by rememberSaveable { mutableStateOf("") }
    var catatan by rememberSaveable { mutableStateOf("") }
    var mood by rememberSaveable { mutableStateOf("") }
    var tanggal by rememberSaveable { mutableStateOf("") }
    var isDataLoaded by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val data by viewModel.data.collectAsState()

    // ADDED: Load data when screen first appears or id changes
    LaunchedEffect(id) {
        if (id != null && !isDataLoaded) {
            viewModel.getNoteById(id)?.let { notes ->
                judul = notes.judul
                catatan = notes.catatan
                mood = notes.mood
                tanggal = notes.tanggal
                isDataLoaded = true
            }
        } else if (id == null) {
            // Reset form untuk catatan baru
            judul = ""
            catatan = ""
            mood = ""
            tanggal = ""
            isDataLoaded = true
        }
    }
//    LaunchedEffect(id) {
//        if (id != null) {
//            viewModel.getNoteById(id) ?: return@LaunchedEffect
//
//        }
//    }
//    LaunchedEffect(data) {
//        data?.let { notes ->
//            judul = notes.judul
//            catatan = notes.catatan
//            mood = notes.mood
//            tanggal = notes.tanggal
//        }
//    }


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White

                        )
                    }
                },
                title = {
                    Text(
                        text = if (id == null) stringResource(R.string.add_notes) else stringResource(R.string.edit_notes),
                        color = Color.White

                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (judul.isBlank() || catatan.isBlank()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_SHORT).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insertNote(judul, catatan, mood, tanggal)
                        } else {
                            viewModel.updateNote(id, judul, catatan, mood, tanggal)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.save),
                            tint = Color.White
                        )
                    }
                    if (id != null) {
                        DeleteAction {
                            showDialog = true
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormCatatan(
            title = judul,
            onTitleChange = { judul = it },
            desc = catatan,
            onDescChange = { catatan = it },
            mood = mood,
            onMoodChange = { mood = it },
            tanggal = tanggal,
            onTanggalChange = { tanggal = it },
            modifier = Modifier.padding(padding)
        )

        if (id != null && showDialog) {
            DisplayAlertDialog(
                onDismissRequest = { showDialog = false }) {
                showDialog = false
                viewModel.deleteNote(id)
                navController.popBackStack()

            }
        }

    }
}
@Composable
fun FormCatatan(
    title: String,
    onTitleChange: (String) -> Unit,
    desc: String,
    onDescChange: (String) -> Unit,
    mood: String,
    onMoodChange: (String) -> Unit,
    tanggal: String,
    onTanggalChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val moodOptions = listOf(
            stringResource(R.string.mood_happy),
            stringResource(R.string.mood_neutral),
            stringResource(R.string.mood_sad),

        )
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text(text = stringResource(R.string.title)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = desc,
            onValueChange = onDescChange,
            label = { Text(text = stringResource(R.string.notes_content)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray).padding()
        ) {
            moodOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (mood == text),
                            onClick = { onMoodChange(text)}
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (mood == text),
                        onClick = { onMoodChange(text) }
                    )
                    Text(text = text, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
        TanggalPicker(
            tanggal = tanggal,
            onTanggalChange = onTanggalChange
        )

    }
}

@Composable
fun TanggalPicker(
    tanggal: String,
    onTanggalChange: (String) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val calendar = Calendar.getInstance()

                if (tanggal.isNotBlank()) {
                    try {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        calendar.time = sdf.parse(tanggal) ?: Date()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val picked = Calendar.getInstance()
                        picked.set(year, month, dayOfMonth)
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        onTanggalChange(sdf.format(picked.time))
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
    ) {
        OutlinedTextField(
            value = tanggal,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = stringResource(R.string.date)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun DeleteAction(onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.other),
            tint = Color.White
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.delete)) },
                onClick = {
                    expanded = false
                    onDelete()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun NotesDetailScreenPreview() {
    Asesment1Theme {
        NotesDetailScreen(rememberNavController())
    }
}
