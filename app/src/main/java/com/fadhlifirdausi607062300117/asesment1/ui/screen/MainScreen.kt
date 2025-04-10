package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fadhlifirdausi607062300117.asesment1.R
import com.fadhlifirdausi607062300117.asesment1.ui.theme.Asesment1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            DrawerMenu(drawerState, scope, navController)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = stringResource(id = R.string.app_name),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = stringResource(id = R.string.title_description),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(0xFF2E7D32), // Warna hijau BPJS awokwkw
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.hamburger_menu),
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Tambahkan aksi notifikasi */ }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = stringResource(id = R.string.notifications),
                                tint = Color.White
                            )
                        }
                        SettingsDropdownMenu(navController)
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth() // ← penting agar konten tidak menyempit saat scroll
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                HeroImageBanner()
                ScreenContent(
                    modifier = Modifier.fillMaxWidth(),
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun DrawerMenu(drawerState: DrawerState, scope: CoroutineScope, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(230.dp)
            .background(Color.White)
    ) {

        IconButton(
            onClick = { scope.launch { drawerState.close() } },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { /* Action for About App */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(id = R.string.about_app),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.about_app),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        // Help
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    scope.launch { drawerState.close() }
                    navController.navigate("helpScreen")
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.help),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp)) // Jarak antara ikon dan teks
            Text(
                text = stringResource(id = R.string.help),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Composable
fun SettingsDropdownMenu(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(id = R.string.setting),
                tint = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.profile)) },
                onClick = {
                    expanded = false
                    // TODO: Navigasi ke halaman Profile jika ada
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.setting)) },
                onClick = {
                    expanded = false
                    navController.navigate("settingScreen")
                }
            )
        }
    }
}

@Composable
fun HeroImageBanner() {
    BoxWithConstraints {
        val screenWidth = maxWidth
        val aspectRatio = 578f / 1262f // Rasio asli gambar kamu (tinggi / lebar) di ingat bro pls

        Image(
            painter = painterResource(id = R.drawable.hero_image),
            contentDescription = stringResource(id = R.string.hero_banner),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(screenWidth)
                .height(screenWidth * aspectRatio)
        )
    }
}

@Composable
fun FeatureBox(
    title: String,
    imageRes: Int,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier
                    .size(60.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            FeatureBox(
                title = stringResource(id = R.string.hydration),
                imageRes = R.drawable.green_kiwi, // Ganti dengan resource icon kamu
                backgroundColor = Color(0xFF2196F3), // Biru
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = {
                    navController.navigate("hydrationScreen")
                }
            )
            FeatureBox(
                title = "Aktivity",
                imageRes = R.drawable.orange, // Ganti icon sesuai
                backgroundColor = Color(0xFF4CAF50), // Hijau
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = {
                    navController.navigate("activityScreen")
                }
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            FeatureBox(
                title = "Mood",
                imageRes = R.drawable.peach_grape, // Ganti icon sesuai
                backgroundColor = Color(0xFFFFA726), // Oranye
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = {
                    navController.navigate("moodScreen")
                }
            )
            FeatureBox(
                title = "Health Tips",
                imageRes = R.drawable.peach_grape, // Ganti icon sesuai
                backgroundColor = Color(0xFFAB47BC), // Ungu
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onClick = {
                    navController.navigate("tipsScreen")
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asesment1Theme {
        MainScreen(rememberNavController())
    }
}
