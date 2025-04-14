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
