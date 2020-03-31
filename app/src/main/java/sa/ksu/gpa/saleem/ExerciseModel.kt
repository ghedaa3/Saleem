package sa.ksu.gpa.saleem


data class ExerciseModel(
    var exerciseId: String,
    var exerciseTitle: String,
    var exercisePicture: Int,
    var exerciseCalories:String) {
    constructor() : this("", "", 0,"")

}