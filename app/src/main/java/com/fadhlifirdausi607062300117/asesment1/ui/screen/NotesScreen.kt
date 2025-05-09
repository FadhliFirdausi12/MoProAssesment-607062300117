package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fadhlifirdausi607062300117.asesment1.R
import com.fadhlifirdausi607062300117.asesment1.database.NotesDb
import com.fadhlifirdausi607062300117.asesment1.model.Notes
import com.fadhlifirdausi607062300117.asesment1.navigation.Screen
import com.fadhlifirdausi607062300117.asesment1.ui.theme.Asesment1Theme
import com.fadhlifirdausi607062300117.asesment1.util.SettingsDataStore
import com.fadhlifirdausi607062300117.asesment1.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NotesScreen(navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(initial = true)


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.notes),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )

                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch{
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (showList) R.drawable.baseline_grid_view_24 else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                id = if (showList) R.string.grid else R.string.list
                            ),
                            tint = Color.White
                        )
                    }
                    SettingsDropdownMenu(navController)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White,
                onClick = {
                    navController.navigate(Screen.NotesBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_notes),
                    tint = MaterialTheme.colorScheme.primary
                )

            }
        }
    ) { innerPadding ->
        NotesScreenContent(showList, modifier = Modifier.padding(innerPadding), navController)
    }
}

@Composable
fun NotesScreenContent(showList: Boolean,modifier: Modifier = Modifier, navController: NavHostController){
    val context = LocalContext.current
    val db = NotesDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModelDb = viewModel(factory = factory)
    val data = viewModel.data.collectAsState(initial = emptyList()).value

    if(data.isEmpty()){
        Column(
            modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.list_kosong))
        }

    }else{
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp),
            ) {
                items(data) {
                    ListItem(notes = it) {
                        navController.navigate(Screen.NotesDetail.withId(it.id))
                    }
                    HorizontalDivider()
                }

            }
        }else{
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp),
            ) {
                items(data) {
                    GridItem(notes = it) {
                        navController.navigate(Screen.NotesDetail.withId(it.id))
                    }
                }
            }

        }

    }


}

@Composable
fun ListItem(notes: Notes, onClick: () -> Unit) {
    val normalizedMood = notes.mood.lowercase().replace("\\s".toRegex(), "").trim()

    val moodEmoji = when {
        normalizedMood.contains("senang") || normalizedMood.contains("happy") -> "😊"
        normalizedMood.contains("biasa") || normalizedMood.contains("neutral") -> "😐"
        normalizedMood.contains("sedih") || normalizedMood.contains("sad") -> "😢"
        else -> "📝"

    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = notes.judul,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = moodEmoji, fontSize = 20.sp)
            }

            Text(
                text = notes.catatan,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            Divider(color = Color.Gray.copy(alpha = 0.3f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "📅 ${notes.tanggal.take(10)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "🕒 ${notes.waktu.takeLast(8)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun GridItem(notes: Notes, onClick: () -> Unit) {
    val moodEmoji = when (notes.mood.lowercase().trim()) {
        "senang", "happy" -> "😊"
        "biasa saja", "neutral" -> "😐"
        "sedih", "sad" -> "😢"
        else -> "📝"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notes.judul,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = moodEmoji, fontSize = 20.sp)
            }

            Text(
                text = notes.catatan,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Divider(color = Color.Gray.copy(alpha = 0.3f))

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "📅 ${notes.tanggal.take(10)}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "🕒 ${notes.waktu.takeLast(8)}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun  NotesScreenPreview() {
    Asesment1Theme {
        MainScreen(rememberNavController())
    }
}

