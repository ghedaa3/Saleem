package sa.ksu.gpa.saleem.exercise


data class ExerciseModel(
    var exerciseId: String,
    var exerciseTitle: String,
    var exercisePicture: Int,
    var exerciseCalories:String,
    var exerciseDuration:String,
    var exerciseDescription:String) {
    constructor() : this("", "", 0,"","","")

}