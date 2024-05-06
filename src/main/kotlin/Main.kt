import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.io.File
import javax.swing.Icon
import javax.swing.Painter


/*
  DUDAS.
  En el object, en el innit hay dos dudas.
  showStudents en el viewModel
  No me deja mostrar el dialogo en el main
 */
fun main() = application {

    val gestionFicheros = GestionFicheros()



    val estado = rememberWindowState(size = DpSize(900.dp, 700.dp))
    val icon = BitmapPainter(useResource("IconoPredeterminado.png", ::loadImageBitmap))
    val viewModel = ViewModel()

    mainWindow(estado= estado, onClose = { exitApplication() }, icono = icon, gestionFicheros, viewModel)
}

@Composable
fun mainWindow(estado: WindowState,onClose: () -> Unit, icono:BitmapPainter, gestionFichero: GestionFicheros, viewModel: ViewModel) {


    val fichero = File("src\\main\\kotlin\\FicheroNombres")
    val focusRequester = remember { FocusRequester() }
    var botonDB = remember { mutableStateOf(false) }
    var botonFile = remember { mutableStateOf(false) }

    Window(
        icon = icono,
        onCloseRequest = onClose,
        title = "Ventana Principal",
        state = estado,
    ) {
        MaterialTheme {
            Box(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Que desea usar?", fontSize = 20.sp)
                    }

                    // Columna para los botones
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {

                        Button(
                            onClick = { botonDB.value = true },
                            modifier = Modifier
                                .width(200.dp)
                                .height(70.dp)
                        ) {
                            Text("Base de datos")
                        }


                        Button(
                            onClick = { botonFile.value = true },
                            modifier = Modifier
                                .width(200.dp)
                                .height(70.dp)
                                .padding(top = 16.dp)
                        ) {
                            Text("Ficheros")
                        }
                    }
                }

                if (botonDB.value) {
                    ventanaDB(focusRequester = focusRequester, viewModel = viewModel)
                }
                if (botonFile.value) {
                    ventanaFichero(fichero, focusRequester, gestionFichero)
                }
            }
        }
    }
}



@Composable
fun ventanaFichero(
    fichero : File,
    focusRequester: FocusRequester,
    gestionFichero: GestionFicheros
) {

    var nombre by remember { mutableStateOf("") }
    var listaNombres = remember { mutableStateOf(mutableListOf<String>()) }
    var show by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {  // key1 = true asegura que esto se ejecute solo una vez
        listaNombres.value = gestionFichero.readFile(fichero)

        //Esto lo que hace es si la lista tiene algo escrito dentro, carga la lazycolum con lo que tenga escrito dentro el fichero, si no, sale de la otra manera, vacia
    }

        Column (modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
        ) {
            Row(modifier = Modifier
                .width(800.dp)
                .height(500.dp))
            {//Parte de arriba
                Column( modifier = Modifier
                    .width(560.dp)
                    .height(450.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {//Columna de la izq

                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        placeholder = { Text("Nombre del estudiante") },
                        modifier = Modifier
                            .border(width = 1.dp, if (nombre.length > 10) Color.Red else Color.Transparent)
                            .align(Alignment.CenterHorizontally)
                            .focusRequester(focusRequester),
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onSurface),
                    )

                    Button(
                        onClick = {
                            listaNombres.value.add(nombre)
                            nombre = ""
                        },

                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        enabled = (nombre.isNotBlank() && nombre.length <= 10)
                    ) {
                        Text("Añadir estudiantes")
                    }
                }

                Column (modifier = Modifier
                    .padding(20.dp),
                ){

                    Text("Estudiantes ${listaNombres.value.count()}",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally))
                    LazyColumn(
                        modifier = Modifier
                            .background(Color.White)
                            .border(width = 2.dp, Color.Black)
                            .width(150.dp)
                            .height(220.dp)
                            .padding(10.dp)
                    ) {
                        items(listaNombres.value){
                            Text(
                                it.replaceFirstChar { it.uppercaseChar() }, //Para ponerlo en mayus
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(5.dp)
                            )
                        }

                    }

                    Button(
                        onClick = { listaNombres.value = mutableListOf() },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text("Borrar todo")
                    }

                }

            }

            Button(
                onClick = { gestionFichero.writeFile(listaNombres.value.joinToString ( "\n"), fichero) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    //.clickable {  dialogo(show = show, onDismiss = { show = false}, title = "Titulo de prueba", text = "Contenido del dialogo"  ) } //no me deja mostrarlo
            ){
                Text("Guardar cambios")

            }
        }
    }

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------


@Composable
fun ventanaDB(
    focusRequester: FocusRequester,
    viewModel: ViewModel
) {

    var nombre by remember { mutableStateOf("") }
    var listaNombres = remember { mutableStateOf(mutableListOf<DataBase.Estudiante>()) } //Esta mal? por eso de Database.Estudiante???


    LaunchedEffect(key1 = true) {  // key1 = true asegura que esto se ejecute solo una vez
        listaNombres.value = viewModel.showStudent()

    }

    Column (modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Gray)
    ) {
        Row(modifier = Modifier
            .width(800.dp)
            .height(500.dp))
        {//Parte de arriba
            Column( modifier = Modifier
                .width(560.dp)
                .height(450.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {//Columna de la izq

                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Nombre del estudiante") },
                    modifier = Modifier
                        .border(width = 1.dp, if (nombre.length > 10) Color.Red else Color.Transparent)
                        .align(Alignment.CenterHorizontally)
                        .focusRequester(focusRequester),
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onSurface),
                )

                Button(
                    onClick = {
                        viewModel.addStudent(DataBase.autoId(), nombre)
                        nombre = ""
                    },

                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    enabled = (nombre.isNotBlank() && nombre.length <= 10)
                ) {
                    Text("Añadir estudiantes")
                }
            }

            Column (modifier = Modifier
                .padding(20.dp),
            ){

                Text("Estudiantes ${listaNombres.value.count()}",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally))
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                        .border(width = 2.dp, Color.Black)
                        .width(150.dp)
                        .height(220.dp)
                        .padding(10.dp)
                ) {
                    items(listaNombres.value){
                        Text(
                            text = "$it", //Para ponerlo en mayus
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }

                }

                Button(
                    onClick = { listaNombres.value = mutableListOf() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Borrar todo")
                }

            }

        }

        Button(
            onClick = { viewModel.commit() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
            //.clickable {  dialogo(show = show, onDismiss = { show = false}, title = "Titulo de prueba", text = "Contenido del dialogo"  ) } //no me deja mostrarlo
        ){
            Text("Guardar cambios")

        }
    }
}

@Composable
fun dialogo(
    show:Boolean,
    onDismiss: () -> Unit,
    title: String,
    text: String,
    confirmButtonText: String = "Aceptar",
    onConfirmButtonClick: () -> Unit = {}
){
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = title)
            },
            text = {
                Text(text)
            },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                    onConfirmButtonClick()
                }) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancelar")
                }
            }
        )
    }

}




