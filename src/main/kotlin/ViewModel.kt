/**
 * Clase que hace de intermediario para conectar con la base de datos y coger informacion de esta, o realizar cambios
 */
class ViewModel:IViewModel {
    /**
     * Añade un estudiante usando la funcion del object de la DB
     * @param id Identificador del estudiante a añadir
     * @param name Nombre del estudiante a añadir
     */
    override fun addStudent(id:Int, name:String) {
        DataBase.addStudent(id, name)
    }

    /**
     * Edita un estudiante usando la funcion de la DB
     * @param name Nombre del estudiante a editar
     */
    override fun editStudent(name: String) {
        DataBase.updateStudents(name)
    }

    /**
     * Elimina un estudiante basandose en el ID usando la funcion de la DB
     * @param id ID que va a tomar como referencia para eliminar el estudiante
     */
    override fun removeStudent(id: Int) {
        DataBase.deleteStudents(id)
    }

    /**
     * Muestra todos los estudiantes que hay usando la funcion del object de la DB
     */
    override fun showStudent(): MutableList<DataBase.Estudiante> { //Este deberia retornar una lista?
        return DataBase.selectAllStudents()
    }

    override fun commit() {
        DataBase.commit()
    }
}