# Ejercicio Práctico: CRUD de Entrenamientos con Room

## Objetivo:
Desarrollar una pequeña aplicación Android en Kotlin que maneje entrenamientos usando Room.
Un Entrenamiento tendrá un nombre, una duración en minutos y una lista de Sets, cada uno con su lista de Ejercicios.

## Requerimientos:
### 1. Modelado de datos con Room

Crear las siguientes entidades con relaciones:

Entrenamiento (Workout)
id (Primary Key, auto-generado)
nombre (String)
duración (Int, en minutos)

Set (WorkoutSet)
id (Primary Key, auto-generado)
workoutId (Foreign Key → Entrenamiento)
orden (Int, representa el número del set en el entrenamiento)

Ejercicio (Exercise)
id (Primary Key, auto-generado)
setId (Foreign Key → Set)
nombre (String)
repeticiones (Int)
pesoKg (Float, opcional)

### 2. Implementar DAOs

Crear los DAO necesarios para:

Insertar, actualizar y eliminar Entrenamientos.
Insertar y eliminar Sets relacionados a un entrenamiento.
Insertar y eliminar Ejercicios dentro de un Set.
Obtener un Entrenamiento con todos sus Sets y Ejercicios (uso de @Transaction y @Relation).

La interfaz que tenga un selector ya sean botones o cualquier otro elemento para cambiar entre listas con los datos de cada entrenamiento
Los datos te los puedes inventar bastándote en los modelos del ejercicio
Inventate dos o tres entrenamientos y muestra sus datos en la vista**/


# Ejercicio #2: Implementando Retrofit 2 (consumo de apis)
    El siguiente ejercicio es para profundizar en el consumo de apis, uzando Retrofit

## Objetivos:
    
    1- Enviar el Modelo1 a la URL de la API con Header_Key y Valor_Key.
    2- Consumir con Retrofit y convertir con Gson el Json recibido que devuelve la API.
    3- El Json recibido se convierte en el Modelo2 guardándose en la base de datos.
    4- Mostrar los datos en la vista.

## Requerimientos:

### URL:
    https://coachai-server.onrender.com/coach/generate_workout

### Header_Key:
    X-API-Key

### Valor_Key:
    Wt5=+y%.XI!,gK.I?xgQ
    
### Modelo1: 
    data class UserDataModel(
    val sex: Int,                           (0 => fem, 1 => masc)
    val age: Int,
    val target: Int,                        (0 => ganar músculo,1 => bajar de peso y 2 => mantener o mejorar salud)
    val muscles: List<Int>,         (ID => Musculos a entrenar)
    val difficulty: Int,                    (0 => facil, 1 => medio, 2 => dificil)
    val time: Int,
    val injuries: String,
    val language: Int                   (0 => ingles, 1 => español)
)

### Modelo2: 

    data class ExerciseModel(
    val order_exercise_id: Int,
    val name: String,
    val language_name: String,
    val reps: String,
    val movement_id: Int,
    val muscle_id: Int,
    val url: String
    )

    data class SetModel(
    val exercises: List<ExerciseModel>,
    val rounds: Int,
    val order_set_id: Int
    )

    data class WorkoutModel(
    val name: String,
    val coach_explanation: String,
    val sets: List<SetModel>
    )

### "Músculos a entrenar (IDs)"

#### 💪Músculos del tren superior (upperBodyItems):
    Pecho	50
    Tríceps	31
    Hombros	90
    Espalda	10
    Bíceps	30
    Antebrazos	60
    Trapecios	20

#### 🦵 Músculos del tren inferior (lowerBodyItems):
    Cuádriceps	11
    Glúteos	70
    Isquiotibiales	40
    Pantorrillas	21
    Aductores	51
    Abductores	61

#### 🍫 Músculos del core (coreItems):
    Abdomen	80
    Espalda baja	41
    

