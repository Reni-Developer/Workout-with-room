@file:Suppress("DEPRECATION")

package com.cursokotlin.entrenamientosconroom.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursokotlin.entrenamientosconroom.R
import com.cursokotlin.entrenamientosconroom.data.bd.Exercise
import com.cursokotlin.entrenamientosconroom.data.bd.SetWithExercise
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutWithSetsAndExercises
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.LoginViewModel
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.TrainingViewModel
import org.checkerframework.checker.units.qual.Time

@Composable
fun TrainingScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    trainingViewModel: TrainingViewModel
) {

    val workoutWithSetsAndExercises by trainingViewModel.workoutWithSets.observeAsState(emptyList())

    val age by trainingViewModel.age.observeAsState(initial = 0)
    val currentAge by trainingViewModel.currentAge.observeAsState("")

    val time by trainingViewModel.time.observeAsState(initial = 0)
    val currentTime by trainingViewModel.currentTime.observeAsState("")

    val injuries by trainingViewModel.injure.observeAsState("ninguna")
    val currentInjuries by trainingViewModel.currentInjuries.observeAsState("")

    val sex by trainingViewModel.sex.observeAsState(0)
    val language by trainingViewModel.language.observeAsState(0)
    val target by trainingViewModel.target.observeAsState(0)
    val difficulty by trainingViewModel.difficulty.observeAsState(0)
    val muscles by trainingViewModel.muscles.observeAsState(listOf())

    val singOutDialogState by trainingViewModel.singOutDialogState.observeAsState(false)
    val changeColorMSelected by trainingViewModel.changeColorMSelected.observeAsState(true)
    val changeColorFSelected by trainingViewModel.changeColorFSelected.observeAsState(false)

    val isLoading by trainingViewModel.isLoading.observeAsState(false)
    val sheetState by trainingViewModel.sheetState.observeAsState(false)

    val lastUserData by trainingViewModel.lastUserData.observeAsState(
        UserDataModel(0, 0, 0, listOf(), 0, 0, "", 0))

    val currentUserData = UserDataModel(
        sex = sex,
        age = age,
        target = target,
        muscles = muscles,
        difficulty = difficulty,
        time = time,
        injuries = injuries,
        language = language
    )

    val languages = listOf("Spanish", "English")
    val languageIcon = R.drawable.ic_translate
    val targets = listOf("Gain muscle", "Lose weight", "Maintain or improve health")
    val targetsIcon = R.drawable.ic_assignment
    val difficulties = listOf("Easy", "Medium", "Hard")
    val difficultiesIcon = R.drawable.ic_difficulty

    val textSingOut = "''¿Está seguro que desea cerrar sesión en su cuenta de usuario?''"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3F2F7))
            .padding(horizontal = 8.dp)
    ) {

        Log.d("TrainingScreen","lastUserData : $lastUserData")
        Log.d("TrainingScreen","currentUserData: $currentUserData")

        SingOut(trainingViewModel)
        Spacer(Modifier.height(24.dp))

        Row(Modifier.weight(0.8f)) {
            Card(
                modifier = Modifier.weight(1f),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
            ) {
                AtiTextField(
                    modifier = Modifier.weight(1f),
                    current = currentAge,
                    label = "Your Age",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { trainingViewModel.onChangeCurrentAge(it.filter { it.isDigit() }) },
                    onClickIcon = {
                        val ageInt = currentAge.toIntOrNull()
                        if (ageInt != null && ageInt > 9 && ageInt < 100)
                            trainingViewModel.onChangeAge(ageInt)
                    },
                    tint = {
                        val intCurrent = currentAge.toIntOrNull()
                        if (intCurrent != null && intCurrent == age && intCurrent != 0)
                            Color.Blue
                        else Color.Black
                    }
                )
                AtiTextField(
                    modifier = Modifier.weight(1f),
                    current = currentTime,
                    label = "Workout Time",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { trainingViewModel.onChangeCurrentTime(it.filter { it.isDigit() }) },
                    onClickIcon = {
                        val timeInt = currentTime.toIntOrNull()
                        if (timeInt != null && timeInt > 14 && timeInt < 121)
                            trainingViewModel.onChangeTime(timeInt)
                    },
                    tint = {
                        val intCurrent = currentTime.toIntOrNull()
                        if (intCurrent != null && intCurrent == time && intCurrent != 0)
                            Color.Blue
                        else Color.Black
                    }
                )
                AtiTextField(
                    modifier = Modifier.weight(1f),
                    current = currentInjuries,
                    label = "Injuries",
                    keyboardType = KeyboardType.Text,
                    onValueChange = { trainingViewModel.onChangeCurrentInjure(it.filter { it.isLetter() }) },
                    onClickIcon = {
                        if (currentInjuries != "")
                            trainingViewModel.onChangeInjure(currentInjuries)
                    },
                    tint = {
                        if (injuries == currentInjuries) Color.Blue
                        else Color.Black
                    }
                )
            }
            Spacer(Modifier.width(8.dp))
            ConfigMuscles(Modifier.weight(1f), trainingViewModel)
        }
        Spacer(Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth()) {
            SelectSex(
                Modifier.weight(1f),
                changeColorMSelected,
                changeSex = 1,
                colorSelected = R.color.selectedMale,
                colorUnselected = R.color.unselected,
                icon = R.drawable.ic_male,
                description = "male",
                tint = {
                    if (changeColorMSelected == true) {
                        Color.Blue
                    } else
                        Color.Black
                },
                trainingViewModel
            )
            Spacer(Modifier.size(8.dp))
            SelectSex(
                Modifier.weight(1f),
                changeColorFSelected,
                changeSex = 0,
                colorSelected = R.color.selectedFemale,
                colorUnselected = R.color.unselected,
                icon = R.drawable.ic_female,
                description = "female",
                tint = {
                    if (changeColorFSelected == true) {
                        Color.Black
                    } else Color.Black
                },
                trainingViewModel
            )
        }
        Spacer(Modifier.height(24.dp))

        SelectDropdownMenu(Modifier, languages, languageIcon, trainingViewModel)
        Spacer(Modifier.height(8.dp))

        SelectDropdownMenu(Modifier, targets, targetsIcon, trainingViewModel)
        Spacer(Modifier.height(8.dp))

        SelectDropdownMenu(Modifier, difficulties, difficultiesIcon, trainingViewModel)
        Spacer(Modifier.height(24.dp))

        TrainingSpacer(Modifier.weight(0.9f), workoutWithSetsAndExercises, trainingViewModel)
        Spacer(modifier = Modifier.height(24.dp))

        UpdateTraining(
            Modifier.weight(0.18f),
            trainingViewModel,
            isLoading,
            currentUserData,
            lastUserData
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (sheetState) {
            SheetWorkout(trainingViewModel, workoutWithSetsAndExercises)
        }

        if (singOutDialogState) {
            SingOutDialog(textSingOut, trainingViewModel, loginViewModel)
        }
    }
}

@Composable
fun SingOut(trainingViewModel: TrainingViewModel) {
    Card(
        Modifier
            .clickable {
                trainingViewModel.openSingOutDialog()
            }
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF485C91)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                thickness = 0.5.dp,
                color = Color(0xFFA2A2A5)
            )
            Text(
                text = "Sing Out",
                Modifier.padding(vertical = 4.dp),
                color = Color.White,
                fontSize = 20.sp
            )
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                thickness = 0.5.dp,
                color = Color(0xFFA2A2A5)
            )
        }
    }
}

@Composable
fun ConfigMuscles(modifier: Modifier, trainingViewModel: TrainingViewModel) {

    val muscles by trainingViewModel.musclesById.observeAsState(listOf())

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
    ) {
        LazyColumn {
            itemsIndexed(muscles) { index, item ->
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = item.selected,
                        onCheckedChange = {
                            trainingViewModel.updateMuscleSelection(index, it)
                        },
                        Modifier
                            .clip(shape = RoundedCornerShape(80.dp))
                            .size(20.dp)
                            .background(Color(0xFF8A8A8A))
                            .border(BorderStroke(2.dp, Color.Black), shape = CircleShape),
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF3F3F3F),
                            uncheckedColor = Color(0xFF8A8A8A),
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(Modifier.size(16.dp))
                    Text(
                        text = item.muscle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = colorResource(R.color.letters_gray)
                    )
                }
            }
        }
    }
}

data class CheckInfo(
    val muscle: String,
    val selected: Boolean,
    val id: Int? = null
)

@Composable
fun AtiTextField(
    modifier: Modifier,
    current: String,
    label: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    onClickIcon: () -> Unit,
    tint: () -> Color
) {
    TextField(
        value = current,
        onValueChange = { onValueChange(it) },
        modifier = modifier,
        label = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(Modifier.weight(1f), thickness = 0.8.dp, color = Color(0xFF868686))
                Text(
                    text = label,
                    Modifier.padding(horizontal = 4.dp),
                    color = Color(0xF3757575),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Divider(Modifier.weight(1f), thickness = 0.8.dp, color = Color(0xFF868686))
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        maxLines = 1,
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Check",
                modifier = Modifier.clickable { onClickIcon() },
                tint = tint()
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.selected),
            unfocusedContainerColor = Color(0xFFFBFBFB)
        )
    )
}

@Composable
fun SelectSex(
    modifier: Modifier,
    changeColorSelected: Boolean,
    changeSex: Int,
    colorSelected: Int,
    colorUnselected: Int,
    icon: Int,
    description: String,
    tint: () -> Color,
    trainingViewModel: TrainingViewModel
) {
    Card(
        modifier
            .fillMaxWidth()
            .clickable {
                trainingViewModel.onChangeSex(changeSex)
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = if (changeColorSelected) {
            CardDefaults.cardColors(colorResource(id = colorSelected))
        } else {
            CardDefaults.cardColors(colorResource(id = colorUnselected))
        }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = description,
            Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            tint = tint()
        )
    }
}

@Composable
fun UpdateTraining(
    modifier: Modifier,
    trainingViewModel: TrainingViewModel,
    isLoading: Boolean,
    currentUserData: UserDataModel,
    lastUserData: UserDataModel
) {
    val enable = currentUserData != lastUserData
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min), // Asegura que solo crece lo necesario
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xFF485C91)),
        onClick = {
            if (currentUserData != lastUserData) {
                trainingViewModel.loadWorkout(currentUserData)
            }
        },
        enabled = enable
    ) {
        if (isLoading) {
            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            ButtonDesingUpdate(enable)
        }
    }
}

@Composable
fun ButtonDesingUpdate(enable: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(Modifier.width(8.dp))
        Text(
            text = "UpdateTraining",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterVertically),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_fitness),
            contentDescription = null,
            modifier = Modifier.size(35.dp),
            tint =
                if (enable)
                    Color(0xFFEEFF41)
                else Color(0xFFFFFFFF)
        )
    }
}

@Composable
fun SelectDropdownMenu(
    modifier: Modifier, options: List<String>, icon: Int, trainingViewModel: TrainingViewModel
) {
    var expanded by remember { mutableStateOf(false) }
//    val options = options
    var selected by remember { mutableStateOf(options[0]) }
    val selectedIndex = options.indexOf(selected)

    Column(modifier = modifier) {
        Card(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .clickable { expanded = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color(0xFFFDFDFD)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = modifier.padding(4.dp), horizontalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.size(20.dp))
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically),
                    tint = when (selected) {
                        "Spanish" -> Color(0xFF475B90)
                        "English" -> Color(0x9E475B90)
                        "Gain muscle" -> Color(0xFF475B90)
                        "Lose weight" -> Color(0xA4475B90)
                        "Maintain or improve health" -> Color(0x3B475B90)
                        "Easy" -> Color(0x3B475B90)
                        "Medium" -> Color(0xA4475B90)
                        "Hard" -> Color(0xFF475B90)
                        else -> Color.Black
                    }
                )
                Text(
                    text = selected,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically),
                    color = colorResource(id = R.color.letters_gray),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            options.forEach {
                Divider(
                    thickness = 0.5.dp,
                    color = Color(0xFF868686),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                DropdownMenuItem(
                    text = { Text(text = it, fontSize = 16.sp) },
                    onClick = {
                        selected = it
                        expanded = false
                        when (icon) {
                            R.drawable.ic_translate -> trainingViewModel.onChangeLanguage(
                                selectedIndex
                            )

                            R.drawable.ic_assignment -> trainingViewModel.onChangeTarget(
                                selectedIndex
                            )

                            R.drawable.ic_difficulty -> trainingViewModel.onChangeDifficulty(
                                selectedIndex
                            )
                        }
                        Log.d("TrainingScreen", "option selected: $options")
                        Log.d("TrainingScreen", "Index selected: $selected")
                        Log.d("TrainingScreen", "Index selected: $selectedIndex")
                    }, colors = MenuDefaults.itemColors(
                        textColor = Color(color = 0xFF545454),
                    ), contentPadding = PaddingValues(horizontal = 16.dp)
                )
                Divider(
                    thickness = 0.5.dp,
                    color = Color(0xFF868686),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TrainingSpacer(
    modifier: Modifier,
    workoutWithSetsAndExercises: List<WorkoutWithSetsAndExercises>,
    trainingViewModel: TrainingViewModel
) {
    Card(
        modifier
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
        text = "Entrenamiento: ${workoutWithSets.workout.name} "
    )
    Text(
        color = Color(0xFF424657),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.End,
        text = workoutWithSets.workout.coach_explanation
    )
    Spacer(modifier = Modifier.height(16.dp))
    Divider(
        thickness = 1.dp,
        color = Color(0xFF485C91),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
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

@Composable
fun SingOutDialog(
    textError: String,
    trainingViewModel: TrainingViewModel,
    loginViewModel: LoginViewModel
) {
    AlertDialog(
        onDismissRequest = { trainingViewModel.closeSingOutDialog() },
        confirmButton = {
            Button(
                onClick = {
                    trainingViewModel.closeSingOutDialog()
                    loginViewModel.signOut()
                },
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04527C)),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(text = "ACCEPT", color = Color.White)
            }
        },
        Modifier.size(320.dp),
        dismissButton = {
            Button(
                onClick = { trainingViewModel.closeSingOutDialog() },
                shape = RoundedCornerShape(25),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04527C)),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text(text = "CANCEL", color = Color.White)
            }
        },
        icon = {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Icon Error",
                modifier = Modifier.size(100.dp)
            )
        },
        title = {
            Text(
                text = "Sing Out",
                color = Color(0xFF04527C),
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Text(
                text = textError,
                color = Color.DarkGray,
                fontSize = 18.sp,
                textAlign = TextAlign.Justify
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetWorkout(trainingViewModel: TrainingViewModel, workoutWithSetsAndExercises: List<WorkoutWithSetsAndExercises>) {
    ModalBottomSheet(onDismissRequest = {trainingViewModel.onChangeSheetState()}) {
        TrainingSpacer(Modifier.weight(0.9f), workoutWithSetsAndExercises, trainingViewModel)
    }
}


