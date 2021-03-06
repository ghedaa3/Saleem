package sa.ksu.gpa.saleem

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_add_food.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList



class AddFoodActivity(context: Context, myFood: MyFood?, var type_of_food:String,key: String?, onSave: OnSave) : Dialog(context) {
    var onSave = onSave
    var myFood = myFood
    var key = key
    lateinit var adapter: ItemAdapter
    var listdata = ArrayList<Item>()
    var nutritionalValueList = ArrayList<NutritionalValue>()
    private lateinit var db: FirebaseFirestore
    lateinit var dialog:ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add_food)

        toolbar.title = "اضافة وجبة بالمكونات"
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        db= FirebaseFirestore.getInstance()
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowCustomEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        cancel.setOnClickListener {
            onBackPressed()
        }

        val recyclerView: RecyclerView = findViewById(R.id.rv_component)
        if (myFood != null){
            listdata.addAll(myFood!!.foods)
            nameOfFood.setText(myFood!!.food_name)
            save.text = "تعديل"
        }
        adapter = ItemAdapter(listdata, context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        initNutritionalValueList()
        save.setOnClickListener {
            var sum = 0.0
            if (listdata.size > 0){
                for (data in listdata){
                    sum += (data.amount.toInt() * data.nutritionalValue)
                    Log.e("hhh","sum ==> $sum")
                    Log.e("hhh","amount ==> ${data.amount.toInt()}")
                    Log.e("hhh","amount ==> ${data.nutritionalValue}")
                }
                addFoodToFirestore(sum,nameOfFood.text.toString())



            }else{
                Toast.makeText(context,"ادخل مكونات الوجبة",Toast.LENGTH_LONG).show()
            }
        }

        delete_button.setOnClickListener {

            addObject(it)
        }
        spFood.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateAdapter(position)
            }

        }

    }


    public fun updateAdapter( position: Int) {
        var list = ArrayList<String>()
        if (position == 3 || position == 0
            || position == 2 || position == 1) {
            list.add("كأس")
            list.add("كجم")
        } else if (position == 4 || position == 11|| position == 5
            || position == 6|| position == 7|| position == 8
            || position == 9|| position == 10) {
            list.add("العدد")
        } else if (position == 12 || position == 13
            || position == 14) {
            list.add("كأس")
            list.add("ملل")
        }else if (position == 15 || position == 16) {
            list.add("ملعقة كبيرة")
            list.add("ملعقة صغيرة")

        }

        val arrayAdapter = ArrayAdapter(this.context, android.R.layout.simple_spinner_item, list)
        spWeight.adapter = arrayAdapter
    }
    //اضافة مكونات الوجبات
    private fun initNutritionalValueList() {
        //رز أبيض
        var risw: NutritionalValue = NutritionalValue()
        risw.array = ArrayList()
        risw.array.add(2)//mm
        risw.array.add(2)//gram
        risw.array.add(100)//cup
        risw.array.add(25)//spoonC
        risw.array.add(15)//spoonS
        nutritionalValueList.add(risw)
        //رز أسمر
        var risb: NutritionalValue = NutritionalValue()
        risb.array = ArrayList()
        risb.array.add(2)//mm
        risb.array.add(2)//gram
        risb.array.add(100)//cup
        risb.array.add(25)//spoonC
        risb.array.add(15)//spoonS
        nutritionalValueList.add(risb)

        //دقيق
        var banana: NutritionalValue = NutritionalValue()
        banana.array = ArrayList()
        banana.array.add(2)//mm
        banana.array.add(60)//gram
        banana.array.add(100)//cup
        banana.array.add(25)//spoonC
        banana.array.add(15)//spoonS
        nutritionalValueList.add(banana)
        //خس
        var apple: NutritionalValue = NutritionalValue()
        apple.array = ArrayList()
        apple.array.add(2)//mm
        apple.array.add(2)//gram
        apple.array.add(100)//cup
        apple.array.add(25)//spoonC
        apple.array.add(15)//spoonS
        nutritionalValueList.add(apple)
        //بصل
        var Yogurt: NutritionalValue = NutritionalValue()
        Yogurt.array = ArrayList()
        Yogurt.array.add(2)//mm
        Yogurt.array.add(2)//gram
        Yogurt.array.add(100)//cup
        Yogurt.array.add(25)//spoonC
        Yogurt.array.add(15)//spoonS
        nutritionalValueList.add(Yogurt)
//طماطم
        var milk: NutritionalValue = NutritionalValue()
        milk.array = ArrayList()
        milk.array.add(2)//mm
        milk.array.add(2)//gram
        milk.array.add(100)//cup
        milk.array.add(25)//spoonC
        milk.array.add(15)//spoonS
        nutritionalValueList.add(milk)

        var r: NutritionalValue = NutritionalValue()
        r.array = ArrayList()
        r.array.add(2)//mm
        r.array.add(2)//gram
        r.array.add(100)//cup
        r.array.add(25)//spoonC
        r.array.add(15)//spoonS
        nutritionalValueList.add(r)
        //رز أسمر
        var i: NutritionalValue = NutritionalValue()
        i.array = ArrayList()
        i.array.add(2)//mm
        i.array.add(2)//gram
        i.array.add(100)//cup
        i.array.add(25)//spoonC
        i.array.add(15)//spoonS
        nutritionalValueList.add(i)

        //دقيق
        var b: NutritionalValue = NutritionalValue()
        b.array = ArrayList()
        b.array.add(2)//mm
        b.array.add(60)//gram
        b.array.add(100)//cup
        b.array.add(25)//spoonC
        b.array.add(15)//spoonS
        nutritionalValueList.add(b)
        //خس
        var a: NutritionalValue = NutritionalValue()
        a.array = ArrayList()
        a.array.add(2)//mm
        a.array.add(2)//gram
        a.array.add(100)//cup
        a.array.add(25)//spoonC
        a.array.add(15)//spoonS
        nutritionalValueList.add(a)
        //بصل
        var y: NutritionalValue = NutritionalValue()
        y.array = ArrayList()
        y.array.add(2)//mm
        y.array.add(2)//gram
        y.array.add(100)//cup
        y.array.add(25)//spoonC
        y.array.add(15)//spoonS
        nutritionalValueList.add(y)
//طماطم
        var m: NutritionalValue = NutritionalValue()
        m.array = ArrayList()
        m.array.add(2)//mm
        m.array.add(2)//gram
        m.array.add(100)//cup
        m.array.add(25)//spoonC
        m.array.add(15)//spoonS
        nutritionalValueList.add(m)

        var laban: NutritionalValue = NutritionalValue()
        laban.array = ArrayList()
        laban.array.add(2)//mm
        laban.array.add(2)//gram
        laban.array.add(100)//cup
        laban.array.add(25)//spoonC
        laban.array.add(15)//spoonS
        nutritionalValueList.add(laban)

        var haleeb: NutritionalValue = NutritionalValue()
        haleeb.array = ArrayList()
        haleeb.array.add(2)//mm
        haleeb.array.add(2)//gram
        haleeb.array.add(100)//cup
        haleeb.array.add(25)//spoonC
        haleeb.array.add(15)//spoonS
        nutritionalValueList.add(haleeb)


        var asser: NutritionalValue = NutritionalValue()
        asser.array = ArrayList()
        asser.array.add(2)//mm
        asser.array.add(2)//gram
        asser.array.add(100)//cup
        asser.array.add(25)//spoonC
        asser.array.add(15)//spoonS
        nutritionalValueList.add(asser)

        var c: NutritionalValue = NutritionalValue()
        c.array = ArrayList()
        c.array.add(2)//mm
        c.array.add(2)//gram
        c.array.add(100)//cup
        c.array.add(25)//spoonC
        c.array.add(15)//spoonS
        nutritionalValueList.add(c)

        var j: NutritionalValue = NutritionalValue()
        j.array = ArrayList()
        j.array.add(2)//mm
        j.array.add(2)//gram
        j.array.add(100)//cup
        j.array.add(25)//spoonC
        j.array.add(15)//spoonS
        nutritionalValueList.add(j)

    }

    inner class MyViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.item_component, parent, false)
        ) {
        var addIngredientcomp: LinearLayout? = null
        var spFood: TextView? = null
        var spWeight: TextView? = null
        var etNumber: EditText? = null
        var deleteButton: Button? = null

        init { // TODO: Customize the item layout

            addIngredientcomp = itemView.findViewById(R.id.addIngredientcomp)
            spFood = itemView.findViewById(R.id.spFood)
            spWeight = itemView.findViewById(R.id.spWeight)
            etNumber = itemView.findViewById(R.id.et_number)
            deleteButton = itemView.findViewById(R.id.delete_button)

        }


    }


    inner class ItemAdapter(var list: ArrayList<Item>, val context: Context) :
        RecyclerView.Adapter<MyViewHolder>() {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return MyViewHolder(inflater, parent)
        }

        fun addItem(item: Item) {
            list.add(item)
            Log.e("hhh", "hhh === === >> list size" + list.size)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.spFood?.text = list[position].name
            holder.spWeight?.text = list[position].weight
            holder.etNumber?.text =
                Editable.Factory.getInstance().newEditable(list[position].amount)
            holder.deleteButton?.setOnClickListener {
                list.removeAt(position)
                notifyDataSetChanged()
            }

        }


    }

    class Item {
        lateinit var name: String
        lateinit var amount: String
        lateinit var weight: String
        var nutritionalValue: Int = 0

        constructor()

    }

    inner class NutritionalValue {
        lateinit var array: ArrayList<Int>


        var mm: Int = 0
        var cup: Int = 0
        var gram: Int = 0
        var spoonC: Int = 0
        var spoonS: Int = 0

    }

    fun addObject(view: View) {
        if(et_number.text.toString() .equals("")){
            Toast.makeText(context,"ادخل الكمية المضافة ",Toast.LENGTH_SHORT).show()
            return
        }
        var item = Item()
        item.name = spFood.selectedItem.toString()
        item.weight = spWeight.selectedItem.toString()
        item.amount = et_number.text.toString()
        var nutritionalValue =   nutritionalValueList.get(spFood.selectedItemPosition)
        var ww = nutritionalValue.array[spWeight.selectedItemPosition]
        item.nutritionalValue = ww

        listdata.add(item)
        Log.e("hhh", "list size ==> " + listdata.size)
        rv_component.adapter?.notifyDataSetChanged()

    }

    fun addFoodToFirestore(sum: Double,name:String){

        val currentuser = FirebaseAuth.getInstance().currentUser?.uid

        val data = hashMapOf(
            "food_name" to name,
            "type" to "Detailed",
            "foods" to listdata,
            "date" to getCurrentDate(),
            "user_id" to currentuser,
            "cal_of_food" to sum,
            "type_of_food" to type_of_food
        )

        if(myFood != null){
            showLoadingDialog()
            key?.let {
                db.collection("Foods").document(it)
                    .set(data, SetOptions.merge()).addOnSuccessListener {
                        dialog.dismiss()
                        Toast.makeText(context,"تمت التعديل بنجاح",Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
            }
            return
        }

        showLoadingDialog()
        db.collection("Foods").document().set(data as Map<String, Any>).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(context,"تمت الاضافة بنجاح",Toast.LENGTH_SHORT).show()
            dismiss()


            var intent : Intent
            intent = Intent()
            intent.putExtra("data",sum)

            onSave.onSaveSuccess(sum)
            dismiss()

        }.addOnFailureListener {
            dialog.dismiss()
            Toast.makeText(context,"حصل خطأ في عملية الاضافة",Toast.LENGTH_SHORT).show();
        };
    }

    private fun showLoadingDialog() {
        dialog = ProgressDialog.show(
            context, "",
            "الرجاء الانتظار", true
        )
    }

    fun getCurrentDate():String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
            val formatted = current.format(formatter)
            return formatted
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        return "$currentDate"
    }
    public interface OnSave{
        fun onSaveSuccess(sum:Double )
    }
}
