package com.cursokotlin.entrenamientosconroom.ui.screenHome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cursokotlin.entrenamientosconroom.R
import com.cursokotlin.entrenamientosconroom.data.bd.Exercise
import com.cursokotlin.entrenamientosconroom.data.bd.SetWithExercise
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutWithSetsAndExercises
import com.cursokotlin.entrenamientosconroom.ui.screenUser.TrainingViewModel

@Composable
fun HomeScreen(trainingViewModel: TrainingViewModel, homeViewModel: HomeViewModel) {

    val workoutWithSetsAndExercises by homeViewModel.workoutWithSets.observeAsState(emptyList())

    val sheetState by trainingViewModel.sheetState.observeAsState(false)
    val time by trainingViewModel.time.observeAsState(initial = 0)
    val numberOfSets by homeViewModel.numberOfSets.observeAsState(initial = 0)
    val dialog by homeViewModel.stateDialog.observeAsState(false)
    val warmUp = "El calentamiento es importante porque prepara el cuerpo física y mentalmente " +
            "para el ejercicio. Aumenta la temperatura corporal, mejora la circulación sanguínea, activa los " +
            "músculos y articulaciones, y reduce el riesgo de lesiones. Además, mejora el rendimiento y " +
            "ayuda a tener una transición más segura hacia el esfuerzo físico intenso."

    Column(Modifier.fillMaxWidth()) {
        Card(
            Modifier
                .weight(0.95f)
                .fillMaxWidth()
                .clickable { },
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                HeaderWorkout(time, numberOfSets, homeViewModel, workoutWithSetsAndExercises)
                Spacer(modifier = Modifier.height(16.dp))
                ButtonsField(trainingViewModel)
            }
            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = colorResource(R.color.divider)
            )
            Spacer(modifier = Modifier.height(10.dp))

            TrainingSpacer(
                workoutWithSetsAndExercises,
                homeViewModel
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .weight(0.05f)
                .fillMaxWidth()
                .height(10.dp),
            thickness = 40.dp,
            color = colorResource(R.color.divider)
        )
        if (dialog) {
            InfoDialog(warmUp, homeViewModel)
        }

        if (sheetState) {
            SheetWorkout(trainingViewModel)
        }
    }
}

@Composable
fun TrainingSpacer(
    workoutWithSetsAndExercises: List<WorkoutWithSetsAndExercises>,
    homeViewModel: HomeViewModel
) {
    LazyColumn(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        items(
            workoutWithSetsAndExercises,
            key = { it.workout.workoutId }) { workoutWithSets ->
            workoutWithSets.sets.forEach { setWithExercise ->
                HeaderSet(setWithExercise, homeViewModel)
                Spacer(modifier = Modifier.size(8.dp))
                setWithExercise.exercises.forEach { exercise ->
                    Exercise(exercise)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}


@Composable
fun ButtonsField(trainingViewModel: TrainingViewModel) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonLong(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {},
            text = "Empezar"
        )
        Spacer(Modifier.size(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            ButtonLong(
                modifier = Modifier
                    .weight(0.76f)
                    .clickable { trainingViewModel.onChangeSheetState() },
                text = "Personalizar"
            )
            Spacer(Modifier.size(8.dp))

            ButtonShort(
                modifier = Modifier
                    .weight(0.12f)
                    .align(Alignment.CenterVertically),
                image = Icons.Filled.BookmarkBorder,
                modifierImage = Modifier
                    .padding(vertical = 12.dp)
                    .size(25.dp)
            )
            Spacer(Modifier.size(8.dp))

            ButtonShort(
                modifier = Modifier
                    .weight(0.12f)
                    .align(Alignment.CenterVertically),
                image = Icons.Filled.Add,
                modifierImage = Modifier
                    .padding(vertical = 12.dp)
                    .size(25.dp)
            )
        }
    }
}

@Composable
fun ButtonShort(
    modifier: Modifier,
    image: ImageVector,
    modifierImage: Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.blue_button),
            contentColor = Color.White
        )
    ) {
        Image(
            imageVector = image,
            contentDescription = "Bookmark",
            modifier = modifierImage.align(Alignment.CenterHorizontally),
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(
                Color.White
            )
        )
    }
}

@Composable
fun ButtonLong(modifier: Modifier, text: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.blue_button),
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(vertical = 14.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HeaderWorkout(
    time: Int,
    numberOfSets: Int,
    homeViewModel: HomeViewModel,
    workoutWithSets: List<WorkoutWithSetsAndExercises>
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                color = colorResource(R.color.title),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                text = "Próximo entrenamiento"
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "...",
                modifier = Modifier.align(Alignment.CenterVertically),
                color = colorResource(R.color.title),
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.size(4.dp))

        workoutWithSets.forEach { workoutWithSets ->
            val original = workoutWithSets.workout.name
            val words = original.split(Regex("[^\\p{L}]+"))
            Row(verticalAlignment = Alignment.CenterVertically) {
                words.forEach { workouts ->
                    Text(
                        text = ".",
                        Modifier.padding(bottom = 20.dp, end = 4.dp),
                        fontSize = 45.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                    Text(
                        color = colorResource(R.color.title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        text = workouts
                    )
                    Spacer(Modifier.size(8.dp))
                }
            }
        }
        Spacer(Modifier.size(4.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ItemHeader(
                Modifier
                    .weight(0.25f),
                icon = R.color.background_icon,
                description = "time",
                image = Icons.Filled.Watch,
                text = " $time min"
            )
            ItemHeader(
                Modifier
                    .weight(0.37f),
                icon = R.color.background_icon,
                description = "Sets",
                image = Icons.Filled.FitnessCenter,
                text = " $numberOfSets ejercicios"
            )
            Row(
                Modifier
                    .weight(0.38f)
                    .fillMaxWidth()
                    .clickable { homeViewModel.changeStateDialog() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Calentamiento",
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.5.sp,
                )
                Spacer(Modifier.size(2.dp))
                Card(
                    shape = RoundedCornerShape(100.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    border = BorderStroke(width = 2.dp, brush = SolidColor(Color.Blue))
                ) {
                    Icon(
                        imageVector = Icons.Filled.QuestionMark,
                        contentDescription = "Sets",
                        Modifier
                            .size(20.dp)
                            .padding(3.dp),
                        tint = Color.Blue
                    )
                }
            }
        }
    }
}

@Composable
fun ItemHeader(
    modifier: Modifier,
    icon: Int,
    description: String,
    image: ImageVector,
    text: String
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Card(
            colors = CardDefaults.cardColors(colorResource(icon)),
            shape = RoundedCornerShape(100.dp)
        ) {
            Icon(
                imageVector = image,
                contentDescription = description,
                Modifier
                    .size(25.dp)
                    .padding(2.dp),
                tint = Color.White
            )
        }
        Text(
            text = text,
            color = Color(0xFF282828),
            fontWeight = FontWeight.Bold,
            fontSize = 19.5.sp
        )
    }
}

@Composable
fun HeaderSet(setWithExercise: SetWithExercise, homeViewModel: HomeViewModel) {

    Text(
        color = Color(0xFF282828),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        text = "Set No.${setWithExercise.set.order_set_id + 1}"
    )
    homeViewModel.updateNumberOfSets(setWithExercise.set.order_set_id + 1)
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

@Composable
fun InfoDialog(text: String, homeViewModel: HomeViewModel) {

    Dialog(onDismissRequest = { homeViewModel.changeStateDialog() }) {
        Card(
            Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(text = text, Modifier.padding(24.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetWorkout(
    homeViewModel: TrainingViewModel,
) {
    ModalBottomSheet(onDismissRequest = { homeViewModel.onChangeSheetState() }) {

    }
}