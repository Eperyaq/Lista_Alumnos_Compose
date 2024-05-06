interface IViewModel {

    fun addStudent(id:Int, name:String)
    fun editStudent(name:String)
    fun removeStudent(id:Int)
    fun showStudent(): MutableList<DataBase.Estudiante>
    fun commit()
}