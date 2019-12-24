import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

class Main extends App {
  val master =SparkSession.builder
    .master("local[*]")
    .appName("Global Earthquakes")
    .getOrCreate

  //  val df = master.read
  //    .format("csv")
  //    .option("header","true")
  //    .option("delimiter",",")
  //    .load("src/USGS_global_1900-2018_final.csv")
  //    .load("/home/antoniog/Documenti/anaconda"+" "+"project/USGS_global_1900-2018.csv")

  //  println(df)
  val df2 = master.sparkContext.textFile("src/USGS_global_1900-2018_final.csv",4).mapPartitions(_.drop(1))
  //  df2.para
  val mapFile = searchByDate(df2,1996,4,16)
  //  val map2 =  df2.flatMap(lines => lines.split("\n")).filter(value => value.contains("Calabria"))
  //  println(mapFile)
  mapFile.saveAsTextFile("prova.txt")
  //  for (r <- mapFile) {
  //    toPrint(r).foreach(println)
  //    println()
  //  }
  def toPrint(row : String): Array[String] = {
    val arr = row.split(",")
    val arrFinal = Array(arr(9),arr(10),arr(11),"%1.2f".format(arr(7).toDouble),"%1.2f".format(arr(8).toDouble),arr(1),arr(2),arr(3),arr(4),arr(5),arr(12),arr(13))
    return arrFinal
  }

  def searchByKey(df2 : RDD[String], key : String): RDD[String] ={
    val query = df2.flatMap(lines => lines.split("\n")).filter(value => value.contains(key))
    return query
  }

  def searchByDepth(df2 : RDD[String], minDepth : Double, maxDepth :Double): RDD[String] ={ //min e max vengono passati come stringhe e poi modificati all'interno della funzione
    val query = df2
      .flatMap(lines => lines.split("\n")
        .filter(value => value.split(",")(12).toDouble >= minDepth)
        .filter(value => value.split(",")(12).toDouble <= maxDepth)
      )
    return query
  }

  def searchByMag(df2 : RDD[String], minMag : Double, maxMag : Double) : RDD[String] = {
    val query = df2
      .flatMap(lines => lines.split("\n")
        .filter(value => value.split(",")(13).toDouble >= minMag)
        .filter(value => value.split(",")(13).toDouble <= maxMag)
      )
    return query
  }

  def searchByYear (df2 : RDD[String], year : Int):RDD[String] = {
    val query = df2
      .flatMap(lines => lines.split("\n")
        .filter(value => value.split(",")(1).toInt == year)
      )
    return query
  }

  def searchByMonth(df2 : RDD[String], month : Int):RDD[String] = {
    val query = df2
      .flatMap(lines => lines.split("\n")
        .filter(value => value.split(",")(2).toInt == month)
      )
    return query
  }

  def searchByDay (df2 : RDD[String], day : Int):RDD[String] = {
    val query = df2
      .flatMap(lines => lines.split("\n")
        .filter(value => value.split(",")(3).toInt == day)
      )
    return query
  }

  def searchByDate(df2 : RDD[String], year : Int, month : Int, day : Int) : RDD[String] = {
    val query = searchByDay(searchByMonth(searchByYear(df2, year), month), day)
    return query
  }

  def searchByDateRange(df2 : RDD[String], minYear : Int = 1900, maxYear : Int = 2018, minMonth : Int = 1, maxMonth : Int = 12, minDay : Int = 1, maxDay : Int = 31) : RDD[String] = {
    val query = df2
      .flatMap(lines => lines.split("\n")
        .filter(value => value.split(",")(1).toDouble >= minYear)
        .filter(value => value.split(",")(1).toDouble <= maxYear)
        .filter(value => value.split(",")(2).toDouble >= minMonth)
        .filter(value => value.split(",")(2).toDouble <= maxMonth)
        .filter(value => value.split(",")(3).toDouble >= minDay)
        .filter(value => value.split(",")(3).toDouble <= maxDay)
      )
    return query
  }
}
