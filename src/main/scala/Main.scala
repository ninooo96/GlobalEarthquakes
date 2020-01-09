import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Main extends App {

  def init(): RDD[String] ={
    val master = SparkSession.builder
      .master("local[*]")
      .appName("Global Earthquakes")
      .getOrCreate()

    val df2 = master.sparkContext.textFile("src/USGS_global_1900-2018_final.csv", 4).mapPartitions(_.drop(1)) //remove header
    return df2
  }

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

    def searchByYearInterval(df2: RDD[String], minYear: Int = 1900, maxYear: Int = 2018): RDD[String] = {
      val query = df2
        .flatMap(lines => lines.split("\n")
          .filter(value => value.split(",")(1).toDouble >= minYear)
          .filter(value => value.split(",")(1).toDouble <= maxYear)
        )
      return query
    }

  def searchByLatLon(df2 : RDD[String], lat : Double, lon : Double): RDD[String] ={
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
          // calculate Distance in KM
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
