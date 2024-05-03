import androidx.compose.ui.unit.Constraints
import java.sql.Connection
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

    /**
     * Funcion que establece la conexion con la base de datos
     * @return La conexion con la base de datos
     */
    fun connectDB(): Connection?{
        var conexion : Connection? = null

        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENIA) //Dar conexion con la base de datos
        } catch ( e: SQLException){
            e.printStackTrace()
        }

        return conexion
    }

    /**
     * Funcion que cierra la database
     * @param conexion Conexion que hay que cerrar
     */
    fun closeDB(conexion:Connection?) {
        try {
            conexion?.close()
        } catch (e:SQLException){
            e.printStackTrace()
        }
    }


    fun addStudent() {
        val conexion = connectDB()

        try {

            println("Conexión exitosa")
            conexion?.close()
        } catch (e: SQLException) {
            println("Error en la conexión: ${e.message}")
        } catch (e: ClassNotFoundException) {
            println("No se encontró el driver JDBC: ${e.message}")
        }
    }

    fun selectAllStudents(){

        val conexion = connectDB()
        val query = "SELECT * FROM STUDENTS"
        val listaEstudiante = mutableListOf<Estudiante>()

        try {
            val statement = conexion?.createStatement() //Sirve para crear una query
            val resultado = statement?.executeQuery(query) //Esto te va a ejecutar la query y va a preparar el resultado

            while (resultado?.next() == true){

                val idStudent = resultado.getInt("id")//Saca el id y lo mete en la tabla
                val nameStudent = resultado.getString("nombre") //Este saca el nombre y lo mete en la tabla
                listaEstudiante.add(Estudiante(nameStudent, idStudent))
            }
            resultado?.close()
            statement?.close()

        } catch (e: SQLException){
            e.printStackTrace()
        }  finally {
            closeDB(conexion)
        }

    }

}