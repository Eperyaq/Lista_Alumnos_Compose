import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.io.File


fun main() = application {
    val estado = rememberWindowState(size = DpSize(900.dp, 900.dp))


    ventanaPrincipal(estado= estado, onClose = ::exitApplication)
}

@Composable
fun ventanaPrincipal(estado: WindowState, onClose: () -> Unit) {

    var nombre by remember { mutableStateOf("") } //Problema porque esto no puedo elevar el estado porque como lo cambio en el onValueChange...
    var listaNombres = remember { mutableStateOf(mutableListOf<String>()) }
    var fichero = File("src\\main\\kotlin\\FicheroNombres")
    var contadorAlumnos = remember { mutableStateOf(0) }


    Window(
        onCloseRequest = onClose,
        title = "Ventana Principal",
        state = estado,
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
        ) {
            Row{//Parte de arriba
                Column {//Columna de la izq
                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        placeholder = { Text("Nombre del estudiante") },
                        modifier = Modifier
                            .padding(16.dp)
                            .border(width = 1.dp, if (nombre.length > 10) Color.Red else Color.Transparent),
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onSurface)
                    )

                    Button(
                        onClick = {
                            listaNombres.value.add(nombre)
                            nombre = ""
                            contadorAlumnos.value += 1
                        },

                        modifier = Modifier,
                        enabled = (nombre.isNotBlank() && nombre.length <= 10)
                    ) {
                        Text("Añadir estudiantes")
                    }
                }

                Column {

                    Text("Estudiantes ${contadorAlumnos.value}")
                    LazyColumn(
                        modifier = Modifier
                            .background(Color.White)
                            .border(width = 1.dp, Color.Black)

                    ) {
                        items(listaNombres.value){
                            Text(it)
                        }
                    }

                    Button(
                        onClick = { listaNombres.value = mutableListOf() }
                    ) {
                        Text("Borrar todo")
                    }

                }

            }

            Button(onClick = { fichero.writeText(listaNombres.value.joinToString ( "\n")) } ){
                Text("Guardar cambios")
            }
        }
    }
}

