package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.content.ContentResolver
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.fadhlifirdausi607062300117.asesment1.R
import com.fadhlifirdausi607062300117.asesment1.model.Recipes
import com.fadhlifirdausi607062300117.asesment1.model.User
import com.fadhlifirdausi607062300117.asesment1.network.ApiStatus
import com.fadhlifirdausi607062300117.asesment1.network.RecipesApi
import com.fadhlifirdausi607062300117.asesment1.network.UserDataStore
import com.fadhlifirdausi607062300117.asesment1.ui.theme.Asesment1Theme


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RecipesScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStoreUser = UserDataStore(context)
    val viewModel: MainViewModelRecipes = viewModel()

    val user by dataStoreUser.userFlow.collectAsState(initial = User("", "", ""))
    val errorMessage by viewModel.errorMessage

    var showRecipeDialog by remember { mutableStateOf(false) }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedRecipeToDeleteId by remember { mutableStateOf<String?>(null) }

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedRecipeToEdit by remember { mutableStateOf<Recipes?>(null) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showRecipeDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.healthy_recipes),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { /* Tambahkan aksi notifikasi jika perlu */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val options = CropImageContractOptions(
                    null, CropImageOptions(
                        imageSourceIncludeGallery = false,
                        imageSourceIncludeCamera = true,
                        fixAspectRatio = true
                    )
                )
                launcher.launch(options)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_data)
                )
            }
        }
    ) { innerPadding ->

        ScreenContent(
            viewModel = viewModel,
            userId = user.email,
            modifier = Modifier.padding(innerPadding),
            onDeleteClick = { id ->
                selectedRecipeToDeleteId = id
                showDeleteDialog = true
            },
            onEditClick = { recipe ->
                selectedRecipeToEdit = recipe
                showEditDialog = true
            }
        )

        if (showRecipeDialog) {
            RecipesDialog(
                bitmap = bitmap,
                onDismissRequest = { showRecipeDialog = false }
            ) { nama, namaLatin ->
                viewModel.saveData(user.email, nama, namaLatin, bitmap!!)
                showRecipeDialog = false
            }
        }

        if (showEditDialog && selectedRecipeToEdit != null) {
            RecipesDialog(
                bitmap = null,
                initialNama = selectedRecipeToEdit!!.nama,
                initialDeskripsi = selectedRecipeToEdit!!.namaLatin,
                onDismissRequest = {
                    showEditDialog = false
                    selectedRecipeToEdit = null
                }
            ) { namaBaru, latinBaru ->
                viewModel.editData(
                    userId = user.email,
                    id = selectedRecipeToEdit!!.id,
                    nama = namaBaru,
                    namaLatin = latinBaru,
                    bitmap = null
                )
                showEditDialog = false
                selectedRecipeToEdit = null
            }
        }

        if (showDeleteDialog) {
            DeleteConfirmationDialog(
                onConfirmDelete = {
                    selectedRecipeToDeleteId?.let { id ->
                        viewModel.deleteData(user.email, id)
                    }
                    showDeleteDialog = false
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }
}

@Composable
fun ScreenContent(
    viewModel: MainViewModelRecipes,
    userId: String,
    modifier: Modifier = Modifier,
    onDeleteClick: (String) -> Unit,
    onEditClick: (Recipes) -> Unit
) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    LaunchedEffect(userId) {
        viewModel.retrievedata(userId)
    }

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCES -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize().padding(4.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data) { recipes ->
                    ListItem(
                        recipes = recipes,
                        onDeleteClick = onDeleteClick,
                        onEditClick = onEditClick
                    )
                }
            }
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrievedata(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}


@Composable
fun ListItem(
    recipes: Recipes,
    onDeleteClick: (String) -> Unit,
    onEditClick: (Recipes) -> Unit // bisa kamu sambungkan nanti
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Sekat 1: Gambar
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(RecipesApi.getRecipesUrl(recipes.imageId))
                    .crossfade(true)
                    .build(),
                contentDescription = recipes.nama,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.broken_img),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f) // Proporsi modern
            )

            // Sekat 2: Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F7F7))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = recipes.nama,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = recipes.namaLatin,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )
                )
            }

            // Sekat 3: Aksi
            if (recipes.mine == "1" || recipes.id.trim() == "0") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFAFAFA))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onEditClick(recipes) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = "Edit",
                            tint = Color(0xFF1976D2)
                        )
                    }
                    IconButton(onClick = { onDeleteClick(recipes.id) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = "Hapus",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}



private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap?{

    if(!result.isSuccessful){
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri=result.uriContent?:return null

    return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
        MediaStore.Images.Media.getBitmap(resolver,uri)
    }else{
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RecipesScreenPreview() {
    Asesment1Theme{
        MainScreen(rememberNavController())
    }
}