import androidx.compose.ui.unit.Constraints
import java.sql.DriverManager
import java.sql.SQLException

object DataBase {

    const val URL = "jdbc:mysql://localhost:3306/studentdb"
    const val USUARIO = "studentuser"
    const val CONTRASENIA = "password"

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
    }

    data class Estudiante(val nombre: String, val id:Int) //Crear la "tabla"
    val conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENIA) //Dar conexion con la base de datos

    fun addStudent() {
        try {

            println("Conexión exitosa")
            conexion.close()
        } catch (e: SQLException) {
            println("Error en la conexión: ${e.message}")
        } catch (e: ClassNotFoundException) {
            println("No se encontró el driver JDBC: ${e.message}")
        }
    }

    fun selectAllStudents(){
        try {
            val estatement = conexion.prepareStatement("SELECT * FROM STUDENTS")

        } catch (e: SQLException){
            println("Error")
        }

    }


}