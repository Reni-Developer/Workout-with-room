package com.cursokotlin.entrenamientosconroom.ui.screenHome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursokotlin.entrenamientosconroom.data.bd.Exercise
import com.cursokotlin.entrenamientosconroom.data.bd.SetWithExercise
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutWithSetsAndExercises
import com.cursokotlin.entrenamientosconroom.ui.screenUser.TrainingViewModel

@Composable
fun HomeScreen(trainingViewModel: TrainingViewModel, homeViewModel: HomeViewModel) {

    val workoutWithSetsAndExercises by homeViewModel.workoutWithSets.observeAsState(emptyList())
    val sheetState by trainingViewModel.sheetState.observeAsState(false)

        Column(Modifier.fillMaxSize()) {
            TrainingSpacer(
                workoutWithSetsAndExercises,
                trainingViewModel
            )

            if (sheetState) {
                SheetWorkout(trainingViewModel, workoutWithSetsAndExercises)
            }
        }
}

@Composable
fun TrainingSpacer(
    workoutWithSetsAndExercises: List<WorkoutWithSetsAndExercises>,
    trainingViewModel: TrainingViewModel
) {
    Card(
        Modifier
            .fillMaxSize()
            .clickable { trainingViewModel.onChangeSheetState() },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
    ) {
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(workoutWithSetsAndExercises, key = { it.workout.workoutId }) { workoutWithSets ->
                HeaderWorkout(workoutWithSets)
                Spacer(modifier = Modifier.height(24.dp))
                workoutWithSets.sets.forEach { setWithExercise ->
                    HeaderSet(setWithExercise)
                    Spacer(modifier = Modifier.size(8.dp))
                    setWithExercise.exercises.forEach { exercise ->
                        Exercise(exercise)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun HeaderWorkout(workoutWithSets: WorkoutWithSetsAndExercises) {
    Text(
        color = Color(0xFF485C91),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        text = "Próximo entrenamiento"
    )
    Text(
        color = Color(0xFF485C91),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        text = workoutWithSets.workout.name
    )

    Text(
        color = Color(0xFF424657),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.End,
        text = workoutWithSets.workout.coach_explanation
    )
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        thickness = 1.dp,
        color = Color(0xFF485C91)
    )
}

@Composable
fun HeaderSet(setWithExercise: SetWithExercise) {
    Text(
        color = Color(0xFF282828),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        text = "Set No.${setWithExercise.set.order_set_id + 1}"
    )
    Text(
        color = Color.Gray,
        fontWeight = FontWeight.Bold,
        text = "${setWithExercise.set.rounds} Rounds"
    )
}

@Composable
fun Exercise(exercise: Exercise) {
    Text(
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        text = "Exercise: ${exercise.order_exercise_id + 1} "
    )
    Text(
        color = Color.Black,
        text = "Ejercicios para: ${getMuscleName(exercise.muscle_id)}"
    )
    Text(color = Color.Black, text = exercise.name)
    Text(color = Color.Black, text = "Reps ${exercise.reps} ")
    Text(
        color = Color.Black,
        text = "Movimiento de: ${getExerciseName(exercise.movement_id)}"
    )
    Spacer(modifier = Modifier.height(8.dp))
}

fun getMuscleName(id: Int): String {
    return when (id) {
        50 -> "Pecho"
        31 -> "Tríceps"
        90 -> "Hombros"
        10 -> "Espalda"
        30 -> "Bíceps"
        60 -> "Antebrazos"
        20 -> "Trapecios"
        11 -> "Cuádriceps"
        70 -> "Glúteos"
        40 -> "Isquiotibiales"
        21 -> "Pantorrillas"
        51 -> "Aductores"
        61 -> "Abductores"
        80 -> "Abdomen"
        41 -> "Espalda baja"
        else -> "Ejercicio desconocido"
    }
}

fun getExerciseName(id: Int): String {
    return when (id) {
        803 -> "Flexión del abdomen superior"
        116 -> "Prensa"
        902 -> "Elevación frontal de hombros"
        200 -> "Encogimientos de trapecio"
        115 -> "Extensión de rodilla"
        102 -> "Jalón en vertical"
        805 -> "Flexión lateral"
        501 -> "Empuje recto"
        110 -> "Subida al cajón"
        113 -> "Levantamiento de cadera"
        315 -> "Fondos"
        301 -> "Curls de martillo o invertido"
        504 -> "Aperturas"
        802 -> "Flexión abdominal completa"
        111 -> "Curl de isquiotibiales"
        103 -> "Extensión de codo en plano bajo"
        311 -> "Extensión de codo sobre la cabeza"
        502 -> "Empuje inclinado"
        505 -> "Pullover"
        314 -> "Empuje recto con agarre cerrado"
        210 -> "Elevación de talones"
        610 -> "Abducción de cadera"
        410 -> "Extensión de espalda"
        201 -> "Remo vertical"
        101 -> "Jalón en horizontal"
        117 -> "Zancada"
        801 -> "Estabilidad del core"
        313 -> "Extensión de codo hacia el frente"
        806 -> "Flexión de abdomen inferior"
        118 -> "Zancada estática"
        300 -> "Curl supinado"
        804 -> "Rotación de tronco"
        114 -> "Extensión de cadera"
        119 -> "Sentadillas"
        510 -> "Aducción de cadera"
        903 -> "Elevación lateral de hombros"
        901 -> "Elevación de deltoides posterior"
        904 -> "Empuje vertical"
        503 -> "Empuje declinado"
        112 -> "Bisagra de cadera"
        302 -> "Curls de aislamiento"
        600 -> "Curl de muñeca"
        312 -> "Extensión de codo en plano bajo"
        else -> "Ejercicio desconocido"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetWorkout(
    trainingViewModel: TrainingViewModel,
    workoutWithSetsAndExercises: List<WorkoutWithSetsAndExercises>
) {
    ModalBottomSheet(onDismissRequest = { trainingViewModel.onChangeSheetState() }) {
        TrainingSpacer(workoutWithSetsAndExercises, trainingViewModel)
    }
}