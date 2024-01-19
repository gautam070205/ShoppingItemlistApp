package eu.tutorials.myshoppinglistapp

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp

data class ShoppingItem (val id:Int,
                         var name: String,
                         var Quantity:Int,
                         val isEditing:Boolean=false
)
@OptIn(ExperimentalMaterial3Api::class)   //for alert dialogs
@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) } //store some variable
//shopping item objects here are of data class item which we have classified down in data class
// InShort it is to take shopping items as input
    var ShowDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity  by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,

        ) {
        Button(
            onClick = {
                      ShowDialog=true
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(sItems) {
                item ->
                if(item.isEditing) {
                    ShoppingItemEditor(item =item , onEditComplete = {
                        editedName,editedQuantity ->                             //70--80 item
                        sItems=sItems.map{it.copy(isEditing = false)}
                        val editedItem=sItems.find{it.id==item.id}
                        editedItem?.let{
                            it.name=editedName
                            it.Quantity=editedQuantity
                        }

                    }
                    )
                }
                else {
                    ShoppingListItem(item = item, onEditClick = {
                        //finding out which item we are editing
                        sItems=sItems.map{it.copy(isEditing=it.id==item.id)}}
                        , onDeleteClick = {
                            sItems=sItems-item


                    })

                }


            }

        }
       if(ShowDialog) {
        AlertDialog(onDismissRequest = { ShowDialog=false }, confirmButton = {
                         Row(modifier= Modifier
                             .fillMaxWidth()
                             .padding(8.dp),
                         horizontalArrangement = Arrangement.SpaceBetween
                             ){
                            Button(onClick = {
                                if(itemName.isNotBlank()) {
                                    val newItem=ShoppingItem(
                                        id=sItems.size+1,
                                      name=itemName,
                                        Quantity=itemQuantity.toInt(),

                                    )
                                    sItems=sItems+newItem
                                    ShowDialog=false
                                    itemName=""
                                }
                            }) {
                                Text("Add")
                            }
                             Button(onClick = { ShowDialog=false }) {
                                 Text(text = "Cancel")
                             }
                         }
        },
               title= {Text( "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(value = itemName, onValueChange ={itemName=it} ,
                        singleLine = true,
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) //provide us with string
                    OutlinedTextField(value = itemQuantity, onValueChange ={itemQuantity=it} ,
                        singleLine = true,
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )

       }

    }
}
@Composable
fun ShoppingItemEditor(item:ShoppingItem,onEditComplete:(String,Int)->Unit) {
    var editedName by remember { mutableStateOf (item.name)}
    var editedQuantity by remember { mutableStateOf(item.Quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row (modifier= Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement=Arrangement.SpaceEvenly
    )
    {
        Column {
            BasicTextField(     //it is for whne we will cahnge all of it.......
                value = editedName,
            onValueChange = { editedName=it},
            singleLine = true,
                modifier= Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity=it},
                singleLine = true,
                modifier= Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

        }

        Button(onClick = {
            isEditing=false
            onEditComplete(editedName,editedQuantity.toIntOrNull()?:1)

        }) {
            Text(text = "Save")
        }
    }

}

@Composable
fun ShoppingListItem(item:ShoppingItem,
                     onEditClick:()->Unit,   //these are the function which are predefined in this software.
                     onDeleteClick:() ->Unit ,  //lamda functions....//?: is elvis operator
                     ) {
    Row (
        modifier= Modifier
            .padding(8.dp)
                .fillMaxWidth()
                .border(
                    border  = BorderStroke(2.dp, Color(0XFF018786)),
                    shape = RoundedCornerShape(28)

                    //BORDER OF OUR COLUMN HERE WILL TELL HERE
                ),  horizontalArrangement=Arrangement.SpaceBetween

    ){
    Text(text = item.name, modifier=Modifier.padding(8.dp))
        Text(text = "Qty:${item.Quantity}".toString(),modifier=Modifier.padding(8.dp))
        Row (modifier=Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick) {


             Icon(imageVector = Icons.Default.Edit,contentDescription=null)
            }
IconButton(onClick = onDeleteClick) {
    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
}
        }


    }
}
//MAP,COPY
//IT IS FOR LOOP TYPE KAAM IT ITERATE THE NUMBERS....LIKE NUMBERS.MAP(IT*2) IT WILL TRANSFORM MULTIPLY
//ALL NUMBERS BY 2..............
//let keyword
//fun main () {
//    val name =String?
//}