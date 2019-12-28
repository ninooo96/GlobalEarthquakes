import scalafx.application.{JFXApp, Platform}
import scalafx.scene.control.TableColumn._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.event.{ActionEvent, Event}
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Alert, Button, ComboBox, Label, ProgressBar, TableColumn, TableView, TextArea, TextField}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{AnchorPane, BorderPane, GridPane, Pane, StackPane}
import scalafx.Includes._
import scalafx.geometry.Point2D
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
//import scalafx.print.PrintColor.Color
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.shape.Rectangle
import scalafx.stage.FileChooser

case class Print(city: String, region: String, state: String, latitude: String, longitude: String, year: String, month: String, day: String, hour: String, minute: String, depth: String, magnitude: String)

object HelloSBT extends JFXApp {
  val df = Main2.init() //df2
  var query = df//Main2.searchByYear(df,2018)



  stage = new PrimaryStage {
    title = "Global Earthquakes"
    scene = new Scene(1200, 700) {
      val txa = new TextField
      val label = new Label("Ciao")
      val grid = new GridPane
      grid.setHgap(10)
      grid.setVgap(2)

      val magMax = new TextField {
        promptText = "8"
        prefColumnCount = 6
      }
      val magMin = new TextField {
        promptText = "1"
        prefColumnCount = 6
      }

      val depthMax = new TextField {
        promptText = "700"
        prefColumnCount = 6
      }
      val depthMin = new TextField {
        promptText = "0"
        prefColumnCount = 6
      }

      val yearMax = new TextField {
        promptText = "2018"
        prefColumnCount = 6
      }
      val yearMin = new TextField {
        promptText = "1900"
        prefColumnCount = 6
      }

      val yearCombo = new ComboBox(List.range(1900, 2019))
      val monthCombo = new ComboBox(List.range(1, 13))
      val dayCombo = new ComboBox(List.range(1, 32))
      yearCombo.prefWidth = 100
      monthCombo.prefWidth = 100
      dayCombo.prefWidth = 100

      def getCombo(combo : ComboBox[Int]): Int ={
        combo.onAction = (e : ActionEvent) => {
          return combo.value.apply
        }
        return combo.value.apply
      }

      val keyword = new TextField {
        promptText = "Type keyword"
      }

      val latitude = new TextField {
        prefColumnCount = 7
      }

      val longitude = new TextField {
        prefColumnCount = 7
      }

      val submit = new Button {
        text = "SUBMIT"
        prefWidth = 100
      }

      val reset = new Button {
        text = "RESET"
        prefWidth = 100
      }

      val resetImg = new Button {
        text = "RESET IMG"
        prefWidth = 100
      }
      val saveImg = new Button {
        text = "SAVE IMG"
        prefWidth = 100
      }

      val save = new Button {
        text = "SAVE"
        prefWidth = 100
      }

      grid.add(new Label("Magnitude (Min, Max)"),0,0)
      grid.add(new GridPane {
        add(magMin,  0, 0)
        add(magMax, 1, 0)
      }, 0, 1)

      grid.add(new Label("Depth (Min, Max)"), 0, 2)
      grid.add(new GridPane {
        add(depthMin, 0, 0)
        add(depthMax, 1, 0)
      }, 0, 3)

      grid.add(new Label("Year (Min, Max) "), 0, 4)
      grid.add(new GridPane {
        add(yearMin, 0, 0)
        add(yearMax, 1, 0)
      }, 0, 5)

      grid.add(new Label("Year"), 1, 0)
      grid.add(yearCombo, 1, 1)

      val monthComboLabel = new Label ("Month")
      grid.add(monthComboLabel, 1, 2)
      grid.add(monthCombo, 1, 3)

      grid.add(new Label("Day"), 1, 4)
      grid.add(dayCombo, 1, 5)

      grid.add(new Label("Keyword"), 2, 0)
      grid.add(keyword, 2, 1)
      grid.add(new Label("Latitude and Longitude"), 2, 2)
      grid.add(new GridPane {

        add(latitude, 0, 0)
        add(longitude, 1, 0)
      }, 2, 3)

      grid.add(submit
        , 3, 4)
      grid.add(reset, 3, 5)
      grid.add(save,3,3)
      grid.add(resetImg, 4,0)
      grid.add(saveImg, 4,1)
      val progress = new ProgressBar
      progress.layoutX = 80
      progress.layoutY = 10

      val size = new Label()
      val info = new Label()
      grid.add(size,3,2)
      grid.add(info,3,0)

      reset.onAction = (e : ActionEvent) => {
        magMin.text=""
        magMax.text=""
        depthMin.text=""
        depthMax.text=""
        yearMin.text=""
        yearMax.text=""
        keyword.text=""
        latitude.text=""
        longitude.text=""

        yearCombo.value = 0
        monthCombo.value = 0
        dayCombo.value = 0
      }

      resetImg.onAction = (e : ActionEvent) =>{
        pane.children = Updater.r
      }

      saveImg.onAction = (e : ActionEvent) =>{
        val fileChooser = new FileChooser
        val selectedFile = fileChooser.showSaveDialog(stage)
        //IMPLEMENT SAVE IMAGE
      }

      save.onAction = (e : ActionEvent) => {
        val fileChooser = new FileChooser
        val selectedFile = fileChooser.showSaveDialog(stage)
        query.saveAsTextFile(selectedFile.getAbsolutePath)
      }

      submit.onAction = (e:ActionEvent) => {
        var ok = true
        info.text = ""
        var subQuery = df
        pane.children = Updater.r


        if (!magMin.getText.equals("") && !magMax.getText.equals("")) {
          subQuery = Main2.searchByMag(subQuery, magMin.getText.toDouble, magMax.getText.toDouble)
        }
        else if (!magMin.getText.equals("")) {
          subQuery = Main2.searchByMag(subQuery, minMag = magMin.getText.toDouble)
        }
        else if (!magMax.getText.equals("")) {
          subQuery = Main2.searchByMag(subQuery, maxMag = magMax.getText.toDouble)
        }

        if (!depthMin.getText.equals("") && !depthMax.getText.equals("")) {
          subQuery = Main2.searchByDepth(subQuery, depthMin.getText.toDouble, depthMax.getText.toDouble)
        }
        else if (!depthMin.getText.equals("")) {
          subQuery = Main2.searchByDepth(subQuery, minDepth = depthMin.getText.toDouble)
        }
        else if (!depthMax.getText.equals("")) {
          subQuery = Main2.searchByDepth(subQuery, maxDepth = depthMax.getText.toDouble)
        }

        if (!yearMin.getText.equals("") && !yearMax.getText.equals("")) {
          subQuery = Main2.searchByDateRange(subQuery, minYear = yearMin.getText.toInt, maxYear = yearMax.getText.toInt)
        }
        else if (!yearMin.getText.equals("")) {
          subQuery = Main2.searchByDateRange(subQuery, minYear = yearMin.getText.toInt)
        }
        else if (!yearMax.getText.equals("")) {
          subQuery = Main2.searchByDateRange(subQuery, maxYear = yearMax.getText.toInt)
        }

        if (!getCombo(yearCombo).equals(0) && !getCombo(monthCombo).equals(0) && !getCombo(dayCombo).equals(0)) {
          subQuery = Main2.searchByDate(subQuery, getCombo(yearCombo), getCombo(monthCombo), getCombo(dayCombo))
        }
        else if (!getCombo(yearCombo).equals(0) && !getCombo(monthCombo).equals(0)) {
          subQuery = Main2.searchByMonth(Main2.searchByYear(subQuery, getCombo(yearCombo)), getCombo(monthCombo))
        }
        else if (!getCombo(yearCombo).equals(0) && !getCombo(dayCombo).equals(0)) {
          ok = false
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Error Dialog"
            headerText = "Error"
            contentText = "You must indicate the month for the query!"
          }.showAndWait()
        }
        else if (!getCombo(monthCombo).equals(0) && !getCombo(dayCombo).equals(0)) {
          subQuery = Main2.searchByDay(Main2.searchByMonth(subQuery, getCombo(monthCombo)), getCombo(dayCombo))
        }
        else if (!getCombo(yearCombo).equals(0)){
          subQuery = Main2.searchByYear(subQuery,getCombo(yearCombo))
        }
        else if (!getCombo(monthCombo).equals(0)){
          subQuery = Main2.searchByYear(subQuery,getCombo(monthCombo))
        }
        else if (!getCombo(yearCombo).equals(0)){
          ok = false
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Error Dialog"
            headerText = "Error"
            contentText = "You can't indicate only a day for the query!"
          }.showAndWait()
        }

        if (!keyword.getText.equals("")) {
          subQuery = Main2.searchByKey(subQuery, keyword.getText)
        }

        if (!latitude.getText.equals("") && !longitude.getText.equals("")) {
          subQuery = Main2.searchByLatLon(subQuery, latitude.getText.toDouble, longitude.getText.toDouble)
        }
        else if (latitude.getText.equals("") ^ longitude.getText.equals("")) {
          ok = false
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Error Dialog"
            headerText = "Error"
            contentText = "You must indicate both latitude \nand longitude for the query!"
          }.showAndWait()
        }

        if (ok) {
          anchor.children.-=(table)
          table = output(subQuery.collect())
          AnchorPane.setTopAnchor(table, 5)
          AnchorPane.setBottomAnchor(table, 5)
          AnchorPane.setLeftAnchor(table, img.width.toDouble)
          AnchorPane.setRightAnchor(table, 5)
          anchor.children ++= List(table)
      }
        grid.children.-=(progress)
        query = subQuery
      }

      val root2 = new StackPane()

      var wimg = new ImageView()
      val img = new Image("world.png", 3 * (width / 5).toInt, 5 * (height / 7).toInt /*3*(height/5).toInt*/ , false, false)
      val node = img.pixelReader match {
        case None => new Label("Image not found")
        case Some(pr) =>
          resizable = true

          wimg = new ImageView(img)
          wimg
      }


      val canvas = new Canvas(3 * (width / 5).toInt, 5 * (height / 7).toInt)
      val gc = canvas.getGraphicsContext2D
      root2.getChildren.addAll(canvas,wimg)


      val seqTmp = List[Print]()

      val dataTmp = ObservableBuffer(seqTmp)
      var table = new TableView(dataTmp)
      val col1 = new TableColumn[Print, String]("City")

      val col2 = new TableColumn[Print, String]("Region")

      val col3 = new TableColumn[Print, String]("State")

      val col4 = new TableColumn[Print, String]("Lat")

      val col5 = new TableColumn[Print, String]("Lon")

      val col6 = new TableColumn[Print, String]("YY")

      val col7 = new TableColumn[Print, String]("MM")

      val col8 = new TableColumn[Print, String]("DD")

      val col9 = new TableColumn[Print, String]("h")

      val col10 = new TableColumn[Print, String]("min")

      val col11 = new TableColumn[Print, String]("Depth")

      val col12 = new TableColumn[Print, String]("Mag")


      private def populateSeq(output : Array[String]) : List[Print]= {
        var seq = List[Print]()
        var paneTmp = pane
        //        val output = rdd.collect()
        //    for (x <- output) {
        var i = 0
//        root2.getChildren.remove(pane)
        output.foreach( x => {
          val arr = x.split(",")
          var y_p = 0.0
          var x_p = 0.0
          val tmp = Print(arr(9), arr(10), arr(11), ("%1.2f".format(arr(7).toDouble)).toString, ("%1.2f".format(arr(8).toDouble)).toString, arr(1), arr(2), arr(3), arr(4), arr(5), arr(12), arr(13))
          if(arr(8).toDouble > 0) { //long
            y_p = (((arr(8).toDouble) * (canvas.width.toDouble / 2)) / 180) + (canvas.width.toDouble / 2)
          }
          else{
            y_p = ((canvas.width.toDouble/2 - (Math.abs(arr(8).toDouble))*(canvas.width.toDouble/2))/180)+(canvas.width.toDouble/2)
//            y_p = (canvas.width.toDouble/2) - yTmp
//            println(yTmp + "" + y_p)
            println(Math.abs(arr(8).toDouble))
          }

          if(arr(7).toDouble > 0) { //lat
           x_p = ((arr(7).toDouble)*(canvas.height.toDouble/2))/90 + canvas.height.toDouble/2 - 15
//            println(x_p)
            //((arr(7).toDouble)*canvas.height.toDouble)/180
          }
          else{
            x_p = ((canvas.height.toDouble/2 - (Math.abs(arr(7).toDouble))*(canvas.height.toDouble/2))/90) + (canvas.height.toDouble/2) + 15
            //((arr(7).toDouble)*canvas.height.toDouble)/180

          }


//          println(x_p + " "+ y_p +" --- "+ arr(8) +" "+ arr(7))

          val point = Rectangle( y_p, x_p + (canvas.height.toDouble - (x_p*2)) , 2,2)
          point.fill = Color.Red
//          val node = new Node()
//
//          pane.handleEvent(Event.ANY) {
//          pane.children -= Updater.r
//          Updater.update(point,point)

          pane.children += point
//          val pane2 = new Pane{
//            children =
//          }
//          Updater.plot(p)
//          }

//          pane.children.add(p2)

//          pane.children.addAll(Updater.update(p2, p2))


          var seq2 = seq ++ List(tmp)
          seq = seq2
          i= i+1
          if (i==80000){
            info.text = "Output exceed 80.000 entries.\nTo see all entries save the output"
            return seq
          }
        })
//        pane.children = Updater.r
//        root2.getChildren.add(pane)

        return seq
      }
      def output(output :Array[String]/**rdd : RDD[String]**/, col1 : TableColumn[Print,String] = col1, col2 : TableColumn[Print,String] = col2,col3 : TableColumn[Print,String] = col3, col4 : TableColumn[Print,String] = col4,col5  : TableColumn[Print,String] = col5, col6 : TableColumn[Print,String] = col6, col7 : TableColumn[Print,String] = col7, col8 : TableColumn[Print,String] = col8, col9 : TableColumn[Print,String] = col9, col10 : TableColumn[Print,String] = col10, col11 : TableColumn[Print,String] = col11, col12 : TableColumn[Print,String] = col12): TableView[Print] = {
        var seq = populateSeq(output)
        size.text = "Size: "+seq.size
        val data = ObservableBuffer(seq)


        val table= new TableView(data)
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

//        //    val col12 = new TableColumn[Print, String]("Mag")
        col12.cellValueFactory = cdf => ObjectProperty(cdf.value.magnitude)
//        root = new AnchorPane {
//          AnchorPane.setTopAnchor(table, 5)
//          AnchorPane.setBottomAnchor(table, 5)
//          AnchorPane.setLeftAnchor(table, img.width.toDouble)
//          AnchorPane.setRightAnchor(table, 5)
//          children ++= List(table)
//        }
        table.columns ++= List(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12)
//        anchor.children.-=(table)
//        anchor.children ++= List(table)
//        anchor = {
//            AnchorPane.setTopAnchor(table, 5)
//            AnchorPane.setBottomAnchor(table, 5)
//            AnchorPane.setLeftAnchor(table, img.width.toDouble)
//            AnchorPane.setRightAnchor(table, 5)
//          }

//        root = anchor

        //    table.columns ++= data
//        rdd.saveAsTextFile("prova")
        return table
      }

      table.columns ++= List(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12)

//      ImageView imageView = new ImageView( ... );
      // Optional: locating the image at iX-iY
      // imageView.setX( iX );
      // imageView.setY( iY );

      object Updater {

        private var _start = new Point2D(-1, -1)
        private var _end = new Point2D(-1, -1)

//        val canvas = new Canvas(3 * (width / 5).toInt, 5 * (height / 7).toInt)
//        root2.getChildren.addAll(canvas)

        var p = new Point2D(0,0)

        val r = new Rectangle {
          fill = Color.Transparent
          stroke = Color.Red
          strokeWidth = 2
        }

        /** Update location of the rectangle proving two defining point (along the diameter) */
        def update(start: Point2D =_start , end: Point2D =_end): Unit = {
          _start = start
          _end = end
          r.x = math.min(_start.x, _end.x)
          r.y = math.min(_start.y, _end.y)
          r.width = math.abs(_start.x - _end.x)
          r.height = math.abs(_start.y - _end.y)
        }

        def plot (p : Point2D) : Unit = {
//          root2.getChildren().remove(canvas)


        }
      }


    val pane = new Pane {
      // Add rectangle that will be updated with user interactions
      children += Updater.r
    }

    // Define handling of mouse events
    var x_first = -1.0
    var y_first = -1.0

    var x_last = -1.0
    var y_last = -1.0

    var rangeQuery = false
    var radiusQuery = false
    pane.handleEvent(MouseEvent.Any) {
      me: MouseEvent => {

        me.eventType match {
          case MouseEvent.MousePressed => {
            if (!radiusQuery && !rangeQuery) {
              radiusQuery = true
//              rangeQuery = false
              // Reset the shape
              val p = new Point2D(me.x, me.y)
              Updater.update(p, p)

              //            if(me.x >= canvas.width.toDouble/2){
              var tmp = (360 * me.x) / canvas.width.toDouble
              x_first = tmp - 180

              var tmp2 = (180 * me.y) / canvas.height.toDouble
              y_first = 90 - tmp2
              //            }
              //            var x_first
              println("coord iniziali: " + x_first + " " + y_first+"\n"+"Range = "+rangeQuery+" Radius = "+radiusQuery)
            }
            else if (rangeQuery || radiusQuery) {
              radiusQuery = false
              rangeQuery = false
              val p = new Point2D(-1, -1)
              Updater.update(p, p)
            }
          }
          case MouseEvent.MouseDragged => {
//            radiusQuery = false
            rangeQuery = true
            // Adjust the shape
            Updater.update(end = new Point2D(me.x, me.y))

            var tmp = (360*me.x)/canvas.width.toDouble
            x_last = tmp-180

            var tmp2 = (180*me.y)/canvas.height.toDouble
            y_last = 90- tmp2

            println("coord finali: "+x_last + " "+y_last)

          }
          case _                       => {}
        }
      }

    }

      root2.getChildren.add(pane)


      // Add rectangle at the last, so it shows up on the top of other children
//      var pane = new Pane( wimg, r )
//
//      final Scene scene = new Scene( pane, 400, 300 );
//      primaryStage.setScene( scene );
//      primaryStage.show();

      var anchor = new AnchorPane {
        AnchorPane.setTopAnchor(root2, 5)
        AnchorPane.setLeftAnchor(root2, 5)
        AnchorPane.setTopAnchor(grid, img.height.toDouble + 10)
        AnchorPane.setLeftAnchor(grid, 5)

//        AnchorPane.setLeftAnchor(txtA,img.width.toDouble)
//        AnchorPane.setTopAnchor(txtA, 5)
//        AnchorPane.setBottomAnchor(txtA, 5)
//        AnchorPane.setRightAnchor(txtA, 5)
        AnchorPane.setTopAnchor(table, 5)
        AnchorPane.setBottomAnchor(table, 5)
        AnchorPane.setLeftAnchor(table, img.width.toDouble+5)
        AnchorPane.setRightAnchor(table, 5)

        AnchorPane.setLeftAnchor(pane,5)
        AnchorPane.setTopAnchor(pane,5)
        children = List( grid, table,root2)
      }
      root = anchor
    }
  }
}

