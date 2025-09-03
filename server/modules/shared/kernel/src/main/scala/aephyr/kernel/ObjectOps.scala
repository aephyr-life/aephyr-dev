package aephyr.kernel

object ObjectOps {
  
  extension[A] (o: A | Null)
    
    def withDefault(a: A): A =
      Option(o).map(_.nn).getOrElse(a)
      
    def option: Option[A] =
      Option(o).map(_.nn)
}
