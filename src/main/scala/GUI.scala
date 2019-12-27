
//import Main2
import multirange.MultiRange
import org.apache.spark.rdd.RDD
import scalafx.application.JFXApp
import scalafx.scene.control.TableColumn._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.AccessibleRole.TextArea
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, ComboBox, Label, TableColumn, TableView, TextArea, TextField}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{AnchorPane, BorderPane, GridPane}
import scalafx.{application, stage}

import scala.collection.{TraversableOnce, mutable}
case class Print(city: String, region: String, state: String, latitude: String, longitude: String, year: String, month: String, day: String, hour: String, minute: String, depth: String, magnitude: String)

case class Studente(name :String, test1 : Int, test2 : Int)

object HelloSBT extends JFXApp {
//  Main2
  var df = Main2.init() //df2
  var query = Main2.searchByYear(df,2018)

  def op(seqTmp : List[Print]) : List[Print] = {
    var seq = List[Print]()
    val output = query.collect()
    for(x <- output){
      val arr = x.split(",")
      println(arr(1))
      //      val seqTmp1 = seqTmp
      val tmp = Print(arr(9), arr(10), arr(11), ("%1.2f".format(arr(7).toDouble)).toString, ("%1.2f".format(arr(8).toDouble)).toString, arr(1), arr(2), arr(3), arr(4), arr(5), arr(12), arr(13))
      //      println(List(tmp).size)

//      var ll2 = ll ++ (List("c"))
//      ll = ll2
//      println(ll.size)
      var seq2 = seq ++ List(tmp)
      seq = seq2
//      println(seq)
    }
    return seq
  }

  def output(rdd : RDD[String]): TableView[Print] = {
    var seq = List[Print]()
    val output = rdd.collect()
    for (x <- output) {
      val arr = x.split(",")
      val tmp = Print(arr(9), arr(10), arr(11), ("%1.2f".format(arr(7).toDouble)).toString, ("%1.2f".format(arr(8).toDouble)).toString, arr(1), arr(2), arr(3), arr(4), arr(5), arr(12), arr(13))
      var seq2 = seq ++ List(tmp)
      seq = seq2
    }

    val data = ObservableBuffer(seq)


    val table = new TableView(data)
    val col1 = new TableColumn[Print, String]("City")
    col1.cellValueFactory = cdf => ObjectProperty(cdf.value.city)
    table.columns.+("City")
    //    col1.cellValueFactory = cdf => StringProperty(st.name)

    val col2 = new TableColumn[Print, String]("Region")
    col2.cellValueFactory = cdf => ObjectProperty(cdf.value.region)
    table.columns.+("Region")

    val col3 = new TableColumn[Print, String]("State")
    col3.cellValueFactory = cdf => StringProperty(cdf.value.state)
    table.columns.+("State")

    val col4 = new TableColumn[Print, String]("Lat")
    col4.cellValueFactory = cdf => ObjectProperty(cdf.value.latitude)
    table.columns.+("Latitude")

    val col5 = new TableColumn[Print, String]("Lon")
    col5.cellValueFactory = cdf => ObjectProperty(cdf.value.longitude)
    table.columns.+("Longitude")

    val col6 = new TableColumn[Print, String]("YY")
    col6.cellValueFactory = cdf => ObjectProperty(cdf.value.year)
    table.columns.+("Year")

    val col7 = new TableColumn[Print, String]("MM")
    col7.cellValueFactory = cdf => ObjectProperty(cdf.value.month)
    table.columns.+("Month")

    val col8 = new TableColumn[Print, String]("DD")
    col8.cellValueFactory = cdf => ObjectProperty(cdf.value.day)
    table.columns.+("Day")

    val col9 = new TableColumn[Print, String]("h")
    col9.cellValueFactory = cdf => ObjectProperty(cdf.value.hour)
    table.columns.+("Hour")

    val col10 = new TableColumn[Print, String]("min")
    col10.cellValueFactory = cdf => ObjectProperty(cdf.value.minute)
    table.columns.+("Minute") //cancellare tutti

    val col11 = new TableColumn[Print, String]("Depth")
    col11.cellValueFactory = cdf => ObjectProperty(cdf.value.depth)
    //    table.columns.+("D

    val col12 = new TableColumn[Print, String]("Mag")
    col12.cellValueFactory = cdf => ObjectProperty(cdf.value.magnitude)


    //    table.columns ++= data
    table.columns ++= List(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12)

    return table
  }

    stage = new PrimaryStage {
      title = "Global Earthquakes"
      scene = new Scene(1200, 700) {
        val txa = new TextField
        val label = new Label("Ciao")
        val grid = new GridPane
        //    val gridYear = new Grid
        grid.setHgap(10)
        grid.setVgap(2)

        grid.add(new Label("Magnitude (Max, Min)"), 0, 0)
        grid.add(new GridPane {
          add(new TextField {
            promptText = "1"
            prefColumnCount = 6
          }, 0, 0)
          add(new TextField {
            promptText = "8"
            prefColumnCount = 6
          }, 1, 0)
        }, 0, 1)

        grid.add(new Label("Depth (Max, Min)"), 0, 2)
        grid.add(new GridPane {
          add(new TextField {
            promptText = "0"
            prefColumnCount = 6
          }, 0, 0)
          add(new TextField {
            promptText = "700"
            prefColumnCount = 6
          }, 1, 0)
        }, 0, 3)

        grid.add(new Label("Year "), 0, 4)
        grid.add(new GridPane {
          add(new TextField {
            promptText = "1900"
            prefColumnCount = 6
          }, 0, 0)
          add(new TextField {
            promptText = "2018"
            prefColumnCount = 6
          }, 1, 0)
        }, 0, 5)

        grid.add(new Label("Year"), 1, 0)
        grid.add(new ComboBox(List.range(1900, 2019)), 1, 1)

        grid.add(new Label("Month"), 1, 2)
        grid.add(new ComboBox(List.range(1, 13)), 1, 3)

        grid.add(new Label("Day"), 1, 4)
        grid.add(new ComboBox(List.range(1, 32)), 1, 5)

        grid.add(new Label("Keyword"), 2, 0)
        grid.add(new TextField {
          promptText = "Type keyword"
        }, 2, 1)
        grid.add(new Label("Latitude and Longitude"), 2, 2)
        grid.add(new GridPane {

          add(new TextField {
            prefColumnCount = 7
          }, 0, 0)
          add(new TextField {
            prefColumnCount = 7
          }, 1, 0)
        }, 2, 3)

        grid.add(new Button {
          text = "SUBMIT"
        }, 3, 4)
        grid.add(new Button {
          text = "RESET"
        }, 3, 5)
        grid.add(new Button {
          text = "SAVE"
        },3,3)


        val img = new Image("world.png", 3 * (width / 5).toInt, 5 * (height / 7).toInt /*3*(height/5).toInt*/ , false, false)
        val node = img.pixelReader match {
          case None => new Label("Image not found")
          case Some(pr) =>
            //        val wimg = new WritableImage(pr)//, 3*(width/5).toInt, 2*(height/3).toInt )
            val xScaleRatio = width / img.width.toInt
            val yScaleRatio = height / img.height.toInt
            resizable = true

            val wimg = new ImageView(img)
            wimg
        }
        val seqTmp = List[Print]()

        //    for (r <- mapFile) {
        //      val arr = r.split(",")
        //      seq.:+(Print(arr(9), arr(10), arr(11), ("%1.2f".format(arr(7).toDouble)).toString, ("%1.2f".format(arr(8).toDouble)).toString, arr(1), arr(2), arr(3), arr(4), arr(5), arr(12), arr(13)))


        val table = output(query) //l'Output della tableView - da ritornare dopo submit (query : RDD[String])
        //    println(seq.size)
        //    }
//        val data = ObservableBuffer(seq)
//
//
//        val table = new TableView(data)
//        //    table.rowFactory.+("ciao")
//        val col1 = new TableColumn[Print, String]("City")
//        col1.cellValueFactory = cdf => ObjectProperty(cdf.value.city)
//        table.columns.+("City")
//        //    col1.cellValueFactory = cdf => StringProperty(st.name)
//
//        val col2 = new TableColumn[Print, String]("Region")
//        col2.cellValueFactory = cdf => ObjectProperty(cdf.value.region)
//        table.columns.+("Region")
//
//        val col3 = new TableColumn[Print, String]("State")
//        col3.cellValueFactory = cdf => StringProperty(cdf.value.state)
//        table.columns.+("State")
//
//        val col4 = new TableColumn[Print, String]("Latitude")
//        col4.cellValueFactory = cdf => ObjectProperty(cdf.value.latitude)
//        table.columns.+("Latitude")
//
//        val col5 = new TableColumn[Print, String]("Longitude")
//        col5.cellValueFactory = cdf => ObjectProperty(cdf.value.longitude)
//        table.columns.+("Longitude")
//
//        val col6 = new TableColumn[Print, String]("Year")
//        col6.cellValueFactory = cdf => ObjectProperty(cdf.value.year)
//        table.columns.+("Year")
//
//        val col7 = new TableColumn[Print, String]("Month")
//        col7.cellValueFactory = cdf => ObjectProperty(cdf.value.month)
//        table.columns.+("Month")
//
//        val col8 = new TableColumn[Print, String]("Day")
//        col8.cellValueFactory = cdf => ObjectProperty(cdf.value.day)
//        table.columns.+("Day")
//
//        val col9 = new TableColumn[Print, String]("Hour")
//        col9.cellValueFactory = cdf => ObjectProperty(cdf.value.hour)
//        table.columns.+("Hour")
//
//        val col10 = new TableColumn[Print, String]("Minute")
//        col10.cellValueFactory = cdf => ObjectProperty(cdf.value.minute)
//        table.columns.+("Minute") //cancellare tutti
//
//        val col11 = new TableColumn[Print, String]("Depth")
//        col11.cellValueFactory = cdf => ObjectProperty(cdf.value.depth)
//        //    table.columns.+("D
//
//        val col12 = new TableColumn[Print, String]("Magnitude")
//        col12.cellValueFactory = cdf => ObjectProperty(cdf.value.magnitude)
//
//
//        //    table.columns ++= data
//        table.columns ++= List(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12)
//        val list = List(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12)
        val st = Studente("Chiara", 0, 0)

        val data2 = ObservableBuffer(

          Studente("Antonio", 33, 44),
          Studente("Davide", 22, 1),
          Studente("Gabriele", 44, 3)
        )
        //    val table = new TableView(data)
        val col1_2 = new TableColumn[Studente, String]("name")
        col1_2.cellValueFactory = cdf => StringProperty(cdf.value.name)
        //    val table.columns ++= List(col1,col2)
        //    table2.columns ++= List(col1_2)

        //   Seq(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12))
        //    table.columns
        //    table.getColumns.addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12))

        root = new AnchorPane {

          //      AnchorPane.setBottomAnchor(txa, 10)
          AnchorPane.setTopAnchor(node, 5)
          AnchorPane.setLeftAnchor(node, 5)
          //      AnchorPane.setBottomAnchor(grid,5)
          AnchorPane.setTopAnchor(grid, img.height.toDouble + 10)
          AnchorPane.setLeftAnchor(grid, 5)

          AnchorPane.setTopAnchor(table, 5)
          AnchorPane.setBottomAnchor(table, 5)
          AnchorPane.setLeftAnchor(table, img.width.toDouble)
          AnchorPane.setRightAnchor(table, 5)
          children = List(node, grid, table)


        }


      }
    }
  }

