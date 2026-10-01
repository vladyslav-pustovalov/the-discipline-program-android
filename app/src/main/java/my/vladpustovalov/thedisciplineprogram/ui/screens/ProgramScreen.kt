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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import my.vladpustovalov.thedisciplineprogram.R
import my.vladpustovalov.thedisciplineprogram.data.model.Block
import my.vladpustovalov.thedisciplineprogram.data.model.Program
import my.vladpustovalov.thedisciplineprogram.domain.state.UiState
import my.vladpustovalov.thedisciplineprogram.presentation.viewmodel.ProgramViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun ProgramRoute(
    navController: NavController,
    viewModel: ProgramViewModel = hiltViewModel()
) {
    val programState by viewModel.programState.collectAsState()

    ProgramScreenContent(
        programState = programState,
        programDate = viewModel.programDate,
        isShownPicker = viewModel.isShownPicker,
        onPreviousDayClick = { viewModel.loadPreviousDay() },
        onNextDayClick = { viewModel.loadNextDay() },
        onSelectDateClick = { viewModel.isShownPicker = true },
        onDismissPicker = { viewModel.isShownPicker = false },
        onDateSelected = { selectedDate ->
            viewModel.loadProgram(selectedDate)
            viewModel.isShownPicker = false
        },
        onRetry = { viewModel.loadProgram() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramScreenContent(
    programState: UiState<Program>,
    programDate: LocalDate,
    isShownPicker: Boolean,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onSelectDateClick: () -> Unit,
    onDismissPicker: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onRetry: () -> Unit
) {
    val dateTitleFormatter = remember { DateTimeFormatter.ofPattern("EEEE dd.MM.yy") }

    if (isShownPicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = programDate
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = onDismissPicker,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedLocalDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            onDateSelected(selectedLocalDate)
                        } ?: onDismissPicker()
                    }
                ) {
                    Text(stringResource(R.string.program_select_date_title))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissPicker) {
                    Text(stringResource(R.string.common_cancel))
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
                        text = programDate.format(dateTitleFormatter),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onPreviousDayClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.program_prev_day_cd)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSelectDateClick) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = stringResource(R.string.program_select_date_cd)
                        )
                    }
                    IconButton(onClick = onNextDayClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.program_next_day_cd)
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
                                text = stringResource(R.string.program_no_program_today),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.program_load_error),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = onRetry) {
                                Text(stringResource(R.string.common_retry))
                            }
                        }
                    }
                }
                is UiState.Success -> {
                    val program = state.data
                    if (program.isRestDay) {
                        Text(
                            text = stringResource(R.string.program_rest_day),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        val dayTrainings = program.dailyProgram?.dayTrainings ?: emptyList()
                        if (dayTrainings.isEmpty()) {
                            Text(
                                text = stringResource(R.string.program_no_program_today),
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
                                        text = stringResource(R.string.program_training_number, training.trainingNumber),
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
