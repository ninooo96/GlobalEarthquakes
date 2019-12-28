import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TextArea

object Main2 extends App {

  def init(): RDD[String] ={
    val master = SparkSession.builder
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
    val df2 = master.sparkContext.textFile("src/USGS_global_1900-2018_final.csv", 4).mapPartitions(_.drop(1))
    //  df2.para
//    var mapFile = searchByDate(df2, 1996, 4, 16)
    //  val map2 =  df2.flatMap(lines => lines.split("\n")).filter(value => value.contains("Calabria"))
    //  println(mapFile)

//    mapFile.saveAsTextFile("prova.txt")

    return df2
  }



    //  for (r <- mapFile) {
    //    toPrint(r).foreach(println)
    //    println()
    //  }


    //  def toPrint() : ObservableBuffer(Print){
    //    val data = ObservableBuffer(
    //      for (r <- mapFile) {
    //        val arr = r.split(",")
    //        Print(arr(9), arr(10), arr(11), "%1.2f".format(arr(7).toDouble), "%1.2f".format(arr(8).toDouble), arr(1), arr(2), arr(3), arr(4), arr(5), arr(12), arr(13))
    //      }
    ////  return data
    //    )
    //
    //    return data
    //
    //
    ////    val arrFinal = Array(arr(9),arr(10),arr(11),"%1.2f".format(arr(7).toDouble),"%1.2f".format(arr(8).toDouble),arr(1),arr(2),arr(3),arr(4),arr(5),arr(12),arr(13))
    //
    ////    case class Print( arr(9),arr(10),arr(11),"%1.2f".format(arr(7).toDouble),"%1.2f".format(arr(8).toDouble),arr(1),arr(2),arr(3),arr(4),arr(5),arr(12),arr(13))
    //
    //  }

    def searchByKey(df2: RDD[String], key: String): RDD[String] = {
      val query = df2.flatMap(lines => lines.split("\n")).filter(value => value.contains(key))
      return query
    }

    def searchByDepth(df2: RDD[String], minDepth: Double = 0, maxDepth: Double = 700): RDD[String] = { //min e max vengono passati come stringhe e poi modificati all'interno della funzione
      val query = df2
        .flatMap(lines => lines.split("\n")
          .filter(value => value.split(",")(12).toDouble >= minDepth)
          .filter(value => value.split(",")(12).toDouble <= maxDepth)
        )
      return query
    }

    def searchByMag(df2: RDD[String], minMag: Double = 0, maxMag: Double = 8): RDD[String] = {
      val query = df2
        .flatMap(lines => lines.split("\n")
          .filter(value => value.split(",")(13).toDouble >= minMag)
          .filter(value => value.split(",")(13).toDouble <= maxMag)
        )
      return query
    }

    def searchByYear(df2: RDD[String], year: Int): RDD[String] = {
      val query = df2
        .flatMap(lines => lines.split("\n")
          .filter(value => value.split(",")(1).toInt == year)
        )
      return query
    }

    def searchByMonth(df2: RDD[String], month: Int): RDD[String] = {
      val query = df2
        .flatMap(lines => lines.split("\n")
          .filter(value => value.split(",")(2).toInt == month)
        )
      return query
    }

    def searchByDay(df2: RDD[String], day: Int): RDD[String] = {
      val query = df2
        .flatMap(lines => lines.split("\n")
          .filter(value => value.split(",")(3).toInt == day)
        )
      return query
    }

    def searchByDate(df2: RDD[String], year: Int, month: Int, day: Int): RDD[String] = {
      val query = searchByDay(searchByMonth(searchByYear(df2, year), month), day)
      return query
    }

    def searchByDateRange(df2: RDD[String], minYear: Int = 1900, maxYear: Int = 2018, minMonth: Int = 1, maxMonth: Int = 12, minDay: Int = 1, maxDay: Int = 31): RDD[String] = {
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

  def searchByLatLon(df2 : RDD[String], lat : Double, lon : Double): RDD[String] ={
//    if ((lat - lon).abs < precision) true else false
    val precision = 0.25
    val query = df2
      .flatMap(lines => lines.split("\n")
        .filter(value => Math.abs(lat - value.split(",")(7).toDouble) <= precision)
        .filter(value =>  Math.abs(lon - value.split(",")(8).toDouble) <= precision)
      )
    return query
  }

  def output2(rdd : RDD[String], txtA : TextArea) ={
//    val rdd2 = rdd.cache()
    rdd.flatMap(lines => lines.split("\n")).map(x =>{
      val arr = x.split(",")
      txtA.appendText(arr(9) + "\t" + arr(10) + "\t" + arr(11) + "\t" + ("%1.2f".format(arr(7).toDouble)).toString + "\t" + ("%1.2f".format(arr(8).toDouble)).toString + "\t" + arr(1) + "\t" + arr(2) + "\t" + arr(3) + "\t" + arr(4) + "\t" + arr(5) + "\t" + arr(12) + "\t" + arr(13) + "\n")
    })
  }
  }
