@file:Suppress("DEPRECATION")

package com.cursokotlin.entrenamientosconroom.ui.screenUser

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursokotlin.entrenamientosconroom.R
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.ui.screenHome.HomeViewModel
import com.cursokotlin.entrenamientosconroom.ui.screenLogin.LoginViewModel

@Composable
fun TrainingScreen(
    loginViewModel: LoginViewModel,
    trainingViewModel: TrainingViewModel,
    homeViewModel: HomeViewModel
) {
    val age by trainingViewModel.age.observeAsState(initial = 0)
    val currentAge by trainingViewModel.currentAge.observeAsState("30")

    val time by trainingViewModel.time.observeAsState(initial = 0)
    val currentTime by trainingViewModel.currentTime.observeAsState("30")

    val injuries by trainingViewModel.injure.observeAsState("ninguna")
    val currentInjuries by trainingViewModel.currentInjuries.observeAsState("ninguna")

    val sex by trainingViewModel.sex.observeAsState(0)
    val language by trainingViewModel.language.observeAsState(0)
    val target by trainingViewModel.target.observeAsState(0)
    val difficulty by trainingViewModel.difficulty.observeAsState(0)
    val muscles by trainingViewModel.muscles.observeAsState(listOf())

    val singOutDialogState by trainingViewModel.singOutDialogState.observeAsState(false)
    val changeColorMSelected by trainingViewModel.changeColorMSelected.observeAsState(true)
    val changeColorFSelected by trainingViewModel.changeColorFSelected.observeAsState(false)

    val isLoading by homeViewModel.isLoading.observeAsState(false)

    val lastUserData by homeViewModel.lastUserData.observeAsState(
        UserDataModel(1, 30, 0, listOf(50,11,80), 1, 60, "ninguna", 1)
    )

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


    Column(Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(color = Color(0xFFF3F2F7))
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn {
                item {
                    Spacer(Modifier.height(8.dp))
                    SingOut(trainingViewModel)
                    Spacer(Modifier.height(24.dp))

                    TargetsCurrent(targets, target, targetsIcon, trainingViewModel)
                    Spacer(Modifier.height(24.dp))

                    Title(text = "Datos personales")
                    Spacer(Modifier.height(2.dp))
                    Card(
                        modifier = Modifier,
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
                    ) {
                        SelectYourSex(
                            changeColorMSelected,
                            changeColorFSelected,
                            trainingViewModel
                        )
                        DividerObject()
                        AgeFiled(currentAge, trainingViewModel, age)
                    }
                    Spacer(Modifier.height(24.dp))

                    Title(text = "Datos de entrenamiento")
                    Spacer(Modifier.height(2.dp))
                    Card(
                        Modifier,
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
                    ) {
                        ConfigMuscles(Modifier.height(60.dp), trainingViewModel)
                        DividerObject()
                        DifficultTraining(difficulties, difficultiesIcon, difficulty, trainingViewModel)
                        DividerObject()
                        TimeFiled(currentTime, trainingViewModel, time)
                        DividerObject()
                        InjuriesFiled(currentInjuries, trainingViewModel, injuries)
                    }
                    Spacer(Modifier.height(24.dp))

                    SelectLanguage(languages, language, languageIcon, trainingViewModel)
                    Spacer(Modifier.height(8.dp))

                    Spacer(Modifier.height(24.dp))

                    UpdateTraining(
                        Modifier,
                        homeViewModel,
                        loginViewModel,
                        isLoading,
                        currentUserData,
                        lastUserData
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }

    if (singOutDialogState) {
        SingOutDialog(textSingOut, trainingViewModel, loginViewModel)
    }
}

@Composable
fun TargetsCurrent(
    targets: List<String>,
    target: Int,
    targetsIcon: Int,
    trainingViewModel: TrainingViewModel
) {
    Card(
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(
                Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AssignmentTurnedIn,
                    contentDescription = "description",
                    Modifier
                        .size(35.dp),
                    tint = colorResource(id = R.color.primary_icon)
                )
                Text(
                    text = "Mis objetivos actuales",
                    Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            SelectDropdownMenu(
                Modifier.padding(4.dp), targets, targetsIcon,
                shape = { RoundedCornerShape(15.dp) },
                tint = {
                    when (target) {
                        0 -> Color(0xFF475B90)
                        1 -> Color(0xA4475B90)
                        2 -> Color(0x3B475B90)
                        else -> Color.Black
                    }
                }, onClickItem = { selectedIndex ->
                    trainingViewModel.onChangeTarget(selectedIndex)
                })
        }
    }
}

@Composable
fun SelectLanguage(
    languages: List<String>,
    language: Int,
    languageIcon: Int,
    trainingViewModel: TrainingViewModel
) {
    SelectDropdownMenu(
        Modifier,
        languages,
        languageIcon,
        shape = { RoundedCornerShape(15.dp) },
        tint = {
            when (language) {
                0 -> Color(0xFF475B90)
                1 -> Color(0x9E475B90)
                else -> Color.Black
            }
        },
        onClickItem = { selectedIndex ->
            trainingViewModel.onChangeLanguage(selectedIndex)
        })
}

@Composable
fun DifficultTraining(
    difficulties: List<String>,
    difficultiesIcon: Int,
    difficulty: Int,
    trainingViewModel: TrainingViewModel
) {
    SelectDropdownMenu(
        Modifier, difficulties, difficultiesIcon,
        shape = { RoundedCornerShape(0.dp) },
        tint = {
            when (difficulty) {
                0 -> Color(0x3B475B90)
                1 -> Color(0xA4475B90)
                2 -> Color(0xFF475B90)
                else -> Color.Black
            }
        }, onClickItem = { selectedIndex ->
            trainingViewModel.onChangeDifficulty(selectedIndex)
        })
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        Modifier.padding(horizontal = 16.dp),
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun SelectYourSex(
    changeColorMSelected: Boolean,
    changeColorFSelected: Boolean,
    trainingViewModel: TrainingViewModel
) {
    Row(Modifier.fillMaxWidth()) {
        SelectSex(
            Modifier.weight(1f),
            changeColorMSelected,
            changeSex = 1,
            colorSelected = R.color.selectedMale,
            colorUnselected = R.color.unselected,
            icon = R.drawable.ic_male,
            description = "male",
            tint = { Color.Black },
            trainingViewModel
        )
        SelectSex(
            Modifier.weight(1f),
            changeColorFSelected,
            changeSex = 0,
            colorSelected = R.color.selectedFemale,
            colorUnselected = R.color.unselected,
            icon = R.drawable.ic_female,
            description = "female",
            tint = { Color.Black },
            trainingViewModel
        )
    }
}

@Composable
fun InjuriesFiled(
    currentInjuries: String,
    trainingViewModel: TrainingViewModel,
    injuries: String
) {
    SameTextField(
        modifier = Modifier,
        current = currentInjuries,
        textLabel = "Injuries",
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

@Composable
fun TimeFiled(currentTime: String, trainingViewModel: TrainingViewModel, time: Int) {
    SameTextField(
        modifier = Modifier,
        current = currentTime,
        textLabel = "Workout Time",
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
}

@Composable
fun AgeFiled(currentAge: String, trainingViewModel: TrainingViewModel, age: Int) {
    SameTextField(
        modifier = Modifier,
        current = currentAge,
        textLabel = "Your Age",
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
}

@Composable
fun SingOut(trainingViewModel: TrainingViewModel) {
    Card(
        Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.user), contentDescription = "User",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                )
                Column {
                    Text(
                        text = "Escribe tu nombre", Modifier
                            .padding(vertical = 4.dp), color = Color.Black, fontSize = 20.sp
                    )
                    Text(
                        text = "Reni", Modifier
                            .padding(vertical = 4.dp), color = Color.Black, fontSize = 20.sp
                    )
                }
                Text(
                    text = "Sing Out", Modifier
                        .padding(start = 40.dp)
                        .clickable {
                            trainingViewModel.openSingOutDialog()
                        }, color = Color.Red, fontSize = 20.sp
                )
            }
            HorizontalDivider(
                Modifier.fillMaxWidth(), thickness = 4.dp, color = Color(0xFFEFEEF3)
            )
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CurrencyExchange, contentDescription = "User",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp)
                )
                Text(
                    text = "Suscripción",
                    Modifier.padding(vertical = 4.dp),
                    color = Color.Black,
                    fontSize = 20.sp
                )
                Text(
                    text = "none",
                    Modifier.padding(start = 190.dp, end = 2.dp),
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun ConfigMuscles(modifier: Modifier, trainingViewModel: TrainingViewModel) {

    val muscles by trainingViewModel.musclesById.observeAsState(listOf())

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
    ) {
        LazyRow(Modifier.fillMaxWidth()) {
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
fun SameTextField(
    modifier: Modifier,
    current: String,
    textLabel: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
    onClickIcon: () -> Unit,
    tint: () -> Color
) {
    TextField(
        value = current,
        onValueChange = { onValueChange(it) },
        modifier = modifier.fillMaxWidth(),
        label = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = textLabel,
                    Modifier.padding(horizontal = 4.dp),
                    color = Color(0xF3757575),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
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
        shape = RoundedCornerShape(0.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.selected),
            unfocusedContainerColor = Color(0xFFFBFBFB),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
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
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = if (changeColorSelected) {
            CardDefaults.cardColors(colorResource(id = colorSelected))
        } else {
            CardDefaults.cardColors(colorResource(id = colorUnselected))
        },
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = description,
            Modifier
                .size(50.dp)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 4.dp),
            tint = tint()
        )
    }
}

@Composable
fun UpdateTraining(
    modifier: Modifier,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
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
                Log.d("TrainingScreen", "lastUserData : $lastUserData")
                Log.d("TrainingScreen", "currentUserData: $currentUserData")
                homeViewModel.loadWorkout(currentUserData)
                if (isLoading == false) loginViewModel.goScreenHome()
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
    modifier: Modifier,
    options: List<String>,
    icon: Int,
    shape: () -> Shape,
    tint: () -> Color,
    onClickItem: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(options[0]) }

    Column(modifier = modifier) {
        Card(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .clickable { expanded = true },
            shape = shape(),
            colors = CardDefaults.cardColors(Color(0xFFFDFDFD)),
            elevation = CardDefaults.cardElevation(2.dp)
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
                    tint = tint()
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
                .padding(vertical = 8.dp),
            containerColor = colorResource(R.color.unselected)
        ) {
            DividerObject()
            options.forEach {
                DropdownMenuItem(
                    text = { Text(text = it, fontSize = 16.sp) },
                    onClick = {
                        selected = it
                        expanded = false
                        val newSelectedIndex = options.indexOf(it)
                        onClickItem(newSelectedIndex)
                        Log.d("TrainingScreen", "option selected: $options")
                        Log.d("TrainingScreen", "Index selected: $selected")
                        Log.d("TrainingScreen", "Index selected: $newSelectedIndex")
                    }, colors = MenuDefaults.itemColors(
                        textColor = Color(color = 0xFF545454),
                    ), contentPadding = PaddingValues(horizontal = 16.dp)
                )
                DividerObject()
            }
        }
    }
}

@Composable
fun DividerObject() {
    Divider(
        thickness = 0.7.dp,
        color = colorResource(R.color.divider),
        modifier = Modifier.padding(horizontal = 8.dp)
    )}

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
                    loginViewModel.goScreenLogin()
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




