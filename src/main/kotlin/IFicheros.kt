import java.io.File

interface IFicheros {
    fun writeFile(text:String, fichero:File)

    fun readFile(fichero: File): MutableList<String>
}