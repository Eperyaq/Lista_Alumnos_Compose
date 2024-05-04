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

    data class Estudiante(val name: String, val id:Int) //Crear la "tabla"

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


    fun addStudent(id: Int, name:String) {
        val conexion = connectDB()
        val query = ("INSERT INTO STUDENTS(name, id) VALUES (?, ?)")

        try {
            val statement = conexion?.prepareStatement(query)

            statement?.setString(1,name)
            statement?.setInt(2,id)

            statement?.executeUpdate()
            statement?.close()

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeDB(conexion)
        }
    }

    /**
     * Realiza una sentencia SELECT para sacar todos los estudiantes de la DB
     */
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

    /**
     * Realiza una sentencia UPDATE para cambiar a los estudiantes de la DB
     * @param id ID nuevo que va a ser introducido en la tabla
     * @param name Nombre nuevo que va a ser introducido en la tabla
     */
    fun updateStudents(id:Int, name:String){

        val conexion = connectDB()
        val query = ("UPDATE STUDENTS SET ID = ?, NAME = ?")

        try {
            val statement = conexion?.prepareStatement(query)//realizamos la conexion

            statement?.setString(1, name)//le introducimos los valores
            statement?.setInt(2, id)

            statement?.executeUpdate()//le decimos que se ejecute
            statement?.close() // y que cierre
        } catch (e: SQLException) {
             e.printStackTrace()
        } finally {
            closeDB(conexion)
        }
    }

    /**
     * Funcion que realiza una sentencia DELETE para eliminar a los estudiantes de la DB
     * @param id ID por el que se va a buscar al estudiante para poder borrarlo de la DB
     */
    fun deleteStudents(id: Int){

        val conexion = connectDB()
        val query = ("DELETE FROM STUDENTS WHERE ID = ?")

        try {
            val statement = conexion?.prepareStatement(query)

            statement?.setInt(1,id)
            statement?.executeUpdate()
            statement?.close()

        } catch (e: SQLException){
            e.printStackTrace()
        } finally {
            closeDB(conexion)
        }

    }

}