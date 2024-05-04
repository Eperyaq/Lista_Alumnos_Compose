import java.io.File

class GestionFicheros:IFicheros {

    override fun writeFile(text: String, fichero:File) {
        fichero.writeText(text)
    }
    override fun readFile(fichero:File): MutableList<String> {
        return fichero.readLines().toMutableList()
    }


}