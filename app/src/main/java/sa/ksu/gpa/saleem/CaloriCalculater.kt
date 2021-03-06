package sa.ksu.gpa.saleem

import android.net.wifi.WifiConfiguration.GroupCipher.strings
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object CaloriCalculater {
    var totscalories=0

    //liquid
     var liquid=arrayOf<String>("عسل","فانيلا","زبدة","زيت زيتون","عصير ليمون","مويه","حليب","لبن")
     var liquidMeasureing=arrayOf<String>("كأس","مل")
     var liquidMlCaloris= arrayOf(500,12,102,119,0,0,56,85)

    //dry
    var Dry=arrayOf<String>("دقيق","بيكنج باودر","سكر","ملح","شوفان","رز أبيض","رز أسمر","مكرونة")
    var DryMeasureing=arrayOf<String>("كأس","جم")
    var DryGmCaloris= arrayOf(581,2,48,0,307,130,110,131)

    //fruit + figitables
    var fruit=arrayOf<String>("بيض","لوز","فواكه مجففة","موز","تفاح","بقدونس","بطاطس","بادنجان","زهرة")
    var fruitMeasureing=arrayOf<String>("كأس","جم","العدد")
    var fruitGmCaloris= arrayOf(72,7,332,88,52,3,76,24,209)

    fun calculateCalories(Ingredient:String,Measurment:String,Quantity:String): Int {
        //liquid
        if (liquid.indexOf(Ingredient)!=-1){
            var CalorisIndex=liquid.indexOf(Ingredient)
            var IngCals=liquidMlCaloris.get(CalorisIndex)
            var quanitity=Quantity.toInt()

            //liquid Ml
                if (Measurment.equals(liquidMeasureing.get(1))){
                    totscalories=IngCals*quanitity/100
                    return totscalories
            }
                //liquid Metric Cup
                else if (Measurment.equals(liquidMeasureing.get(0))){
                    totscalories=IngCals*quanitity/250
                }

        }
        //Dry
        if (Dry.indexOf(Ingredient)!=-1){
            var CalorisIndex=Dry.indexOf(Ingredient)
            var IngCals= DryGmCaloris.get(CalorisIndex)
            var quanitity=Quantity.toInt()
            //Dry gram
                if (Measurment.equals(DryMeasureing.get(1))){
                    totscalories=IngCals*quanitity/100
                    return totscalories
            }
                //Dry Metric Cup
                else if (Measurment.equals(DryMeasureing.get(0))){
                    totscalories=IngCals*quanitity/250
                }

        }
        //fruit
        if (fruit.indexOf(Ingredient)!=-1){
            var CalorisIndex=fruit.indexOf(Ingredient)
            var IngCals= fruitGmCaloris.get(CalorisIndex)
            var quanitity=Quantity.toInt()

            //fruit gram
                if (Measurment.equals(fruitMeasureing.get(1))){
                    totscalories=IngCals*quanitity/100
                    return totscalories
            }
                //fruit Metric Cup
                else if (Measurment.equals(fruitMeasureing.get(0))){
                    totscalories=IngCals*quanitity/250
                }
                //fruit by peice
                else if (Measurment.equals(fruitMeasureing.get(2))){
                    totscalories=IngCals*quanitity
                }

        }

        return 0
    }



}