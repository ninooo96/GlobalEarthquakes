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

  def calculateDistanceInKm(lat1 : Double, lon1 : Double, lat2 : Double, lon2 : Double) : Int ={//userLocation: Location, warehouseLocation: Location): Int = {
    val AVERAGE_RADIUS_OF_EARTH_KM = 6371

    print("CIAOOO")
    val latDistance = Math.toRadians(lat1 - lat2)
    val lngDistance = Math.toRadians(lon1 - lon2)
    val sinLat = Math.sin(latDistance / 2)
    val sinLng = Math.sin(lngDistance / 2)
    val a = sinLat * sinLat +
      (Math.cos(Math.toRadians(lat1)) *
        Math.cos(Math.toRadians(lat2)) *
        sinLng * sinLng)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return (AVERAGE_RADIUS_OF_EARTH_KM * c).toInt
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
      val key2 = key.toLowerCase
      val query = df2.flatMap(lines => lines.split("\n")).filter(value => value.toLowerCase().contains(key2))
      return query
    }

    def searchByDepth(df2: RDD[String], minDepth: Double = 0, maxDepth: Double = 750): RDD[String] = { //min e max vengono passati come stringhe e poi modificati all'interno della funzione
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

  def rectQuery(df2 : RDD[String], x_start : Double, y_start : Double, x_end : Double, y_end : Double) : RDD[String] = {
    if (x_end >= x_start){
      if(y_end >= y_start){
        println("coord: x_first= "+x_start+" - y_first= "+y_start+" - x_last= "+x_end + " - y_last= "+y_end)
        val query = df2
          .flatMap(lines => lines.split("\n")
            .filter(value => value.split(",")(7).toDouble >= x_start)
            .filter(value => value.split(",")(7).toDouble < x_end)
            .filter(value => value.split(",")(8).toDouble >= y_start)
            .filter(value => value.split(",")(8).toDouble < y_end)
          )
        return query
      }
      else{
        val query = df2
          .flatMap(lines => lines.split("\n")
            .filter(value => value.split(",")(7).toDouble >= x_start)
            .filter(value => value.split(",")(7).toDouble < x_end)
            .filter(value => value.split(",")(8).toDouble < y_start)
            .filter(value => value.split(",")(8).toDouble >= y_end)
          )
        return query
      }
    }
    else{
      if(y_end >= y_start){
        val query = df2
          .flatMap(lines => lines.split("\n")
            .filter(value => value.split(",")(7).toDouble >= x_end)
            .filter(value => value.split(",")(7).toDouble < x_start)
            .filter(value => value.split(",")(8).toDouble >= y_start)
            .filter(value => value.split(",")(8).toDouble < y_end)
          )
        return query
      }
      else{
        val query = df2
          .flatMap(lines => lines.split("\n")
            .filter(value => value.split(",")(7).toDouble >= x_end)
            .filter(value => value.split(",")(7).toDouble < x_start)
            .filter(value => value.split(",")(8).toDouble <= y_start)
            .filter(value => value.split(",")(8).toDouble > y_end)
          )
        return query
      }
    }
  }

  def radiusQuery(df2 : RDD[String], lat1 : Double, lon1 : Double, km : Double): RDD[String] ={
    val query = df2
      .flatMap(lines => lines.split("\n")
        .filter(value => {
//          calculateDistanceInKm(lat1,lon1,value.split(",")(7).toDouble, value.split(",")(8).toDouble) < km

          val lat2 = value.split(",")(7).toDouble
          val lon2 = value.split(",")(8).toDouble

          val AVERAGE_RADIUS_OF_EARTH_KM = 6371

          val latDistance = Math.toRadians(lat1 - lat2)
          val lngDistance = Math.toRadians(lon1 - lon2)
          val sinLat = Math.sin(latDistance / 2)
          val sinLng = Math.sin(lngDistance / 2)
          val a = sinLat * sinLat +
            (Math.cos(Math.toRadians(lat1)) *
              Math.cos(Math.toRadians(lat2)) *
              sinLng * sinLng)
          val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
          (AVERAGE_RADIUS_OF_EARTH_KM * c).toInt

        } <= km)
      )
    return query
  }


  }
