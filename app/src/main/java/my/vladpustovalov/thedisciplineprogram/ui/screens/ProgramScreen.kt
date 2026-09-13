package my.vladpustovalov.thedisciplineprogram.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.data.model.Block
import my.vladpustovalov.thedisciplineprogram.domain.state.UiState
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.ProgramViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramScreen(
    navController: NavController,
    viewModel: ProgramViewModel = hiltViewModel()
) {
    val programState by viewModel.programState.collectAsState()
    val dateTitleFormatter = remember { DateTimeFormatter.ofPattern("EEEE dd.MM.yy") }

    if (viewModel.isShownPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = viewModel.programDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { viewModel.isShownPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedLocalDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.loadProgram(selectedLocalDate)
                        }
                        viewModel.isShownPicker = false
                    }
                ) {
                    Text("Select date")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.isShownPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewModel.programDate.format(dateTitleFormatter),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.loadPreviousDay() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Day"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.isShownPicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    }
                    IconButton(onClick = { viewModel.loadNextDay() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Day"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = programState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (state.message.contains("404")) {
                            Text(
                                text = "There is no program for today",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "Something went wrong with loading today's program",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { viewModel.loadProgram() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
                is UiState.Success -> {
                    val program = state.data
                    if (program.isRestDay) {
                        Text(
                            text = "Today is the rest day",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        val dayTrainings = program.dailyProgram?.dayTrainings ?: emptyList()
                        if (dayTrainings.isEmpty()) {
                            Text(
                                text = "There is no program for today",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(dayTrainings) { training ->
                                    Text(
                                        text = "Training number: ${training.trainingNumber}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    training.blocks.forEach { block ->
                                        BlockCard(block = block)
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BlockCard(block: Block) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = block.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            block.exercises.forEach { exercise ->
                Text(
                    text = exercise,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
