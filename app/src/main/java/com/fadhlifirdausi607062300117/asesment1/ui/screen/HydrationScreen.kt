package com.fadhlifirdausi607062300117.asesment1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fadhlifirdausi607062300117.asesment1.R
import com.fadhlifirdausi607062300117.asesment1.ui.theme.Asesment1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydrationScreen(navController: NavHostController) {
    var weightInput by rememberSaveable { mutableStateOf("") }
    var activityLevel by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf<String?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    var weightInt by rememberSaveable { mutableIntStateOf(0) }
    var waterNeed by rememberSaveable { mutableIntStateOf(0) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.hydration),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    SettingsDropdownMenu(navController)
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF2E7D32))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InputSection(
                weightInput = weightInput,
                onWeightChange = { weightInput = it },
                selectedActivity = activityLevel,
                onActivitySelected = { activityLevel = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Baris tombol Hitung dan Reset
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val weight = weightInput.toIntOrNull()
                        if (weight != null && activityLevel.isNotEmpty()) {
                            val water = calculateWaterNeed(weight, activityLevel.lowercase())
                            result = context.getString(R.string.result_message, weight, activityLevel, water)
                            weightInt = weight
                            waterNeed = water
                            errorMessage = ""
                        } else {
                            result = null
                            errorMessage = if (weight == null || weight <= 0) {
                                context.getString(R.string.error_invalid_weight)
                            } else {
                                context.getString(R.string.error_empty_activity)
                            }
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.calculate))
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        weightInput = ""
                        activityLevel = ""
                        result = null
                        errorMessage = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB72A2A))
                ) {
                    Text(text = stringResource(R.string.reset))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            result?.let {
                Text(
                    text = it,
                    color = Color(0xFF1565C0),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = {
                    shareResult(context, weightInt, activityLevel, waterNeed)
                }) {
                    Text(text = stringResource(R.string.share))
                }
            }
        }
    }
}


@Composable
fun InputSection(
    weightInput: String,
    onWeightChange: (String) -> Unit,
    selectedActivity: String,
    onActivitySelected: (String) -> Unit
) {
    Text(text = stringResource(R.string.satuan_berat))
    OutlinedTextField(
        value = weightInput,
        onValueChange = { onWeightChange(it.filter { c -> c.isDigit() }) },
        label = { Text(text = stringResource(R.string.weight)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(text = stringResource(R.string.activity_level))
    ActivityRadioGroup(
        selected = selectedActivity,
        onSelected = onActivitySelected
    )
}

@Composable
fun ActivityRadioGroup(selected: String, onSelected: (String) -> Unit) {
    val options = listOf(
        "light" to stringResource(R.string.activity_light),
        "moderate" to stringResource(R.string.activity_moderate),
        "intense" to stringResource(R.string.activity_intense)
    )
    Column {
        options.forEach { (key, label) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selected == key,
                    onClick = { onSelected(key) }
                )
                Text(text = label, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

fun calculateWaterNeed(weight: Int, activity: String): Int {
    val base = weight * 35
    val extra = when (activity.lowercase()) {
        "light" -> 0
        "moderate" -> 500
        "intense" -> 1000
        else -> 0
    }
    return base + extra
}

fun getActivityLabel(context: Context, key: String): String {
    return when (key.lowercase()) {
        "light" -> context.getString(R.string.activity_light)
        "moderate" -> context.getString(R.string.activity_moderate)
        "intense" -> context.getString(R.string.activity_intense)
        else -> key
    }
}

fun shareResult(context: Context, weight: Int, activity: String, water: Int) {
    val label = getActivityLabel(context, activity)
    val message = context.getString(R.string.share_message, weight, label, water)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    val chooser = Intent.createChooser(intent, context.getString(R.string.share_title))
    context.startActivity(chooser)
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HydrationScreenPreview() {
    Asesment1Theme {
        MainScreen(rememberNavController())
    }
}