import java.text.NumberFormat
import java.util.Locale
import javax.imageio.ImageIO
import scalafx.application.{JFXApp, Platform}
import scalafx.scene.control.TableColumn._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.event.{ActionEvent, Event}
import scalafx.scene.{Node, Scene, SnapshotParameters}
import scalafx.scene.control.{Alert, Button, ComboBox, Label, ProgressBar, TableColumn, TablePosition, TableView, TextArea, TextField}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{AnchorPane, BorderPane, GridPane, Pane, StackPane}
import scalafx.Includes._
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Point2D
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.shape.Rectangle
import scalafx.stage.FileChooser

case class Print(city: String, region: String, state: String, latitude: String, longitude: String, year: String, month: String, day: String, hour: String, minute: String, depth: String, magnitude: String){
  def getLat() : Double = {
    val format = NumberFormat.getInstance(Locale.FRANCE)
    val number = format.parse(latitude)
    val d = number.doubleValue
    return d
  }

  def getLon() : Double = {
    val format = NumberFormat.getInstance(Locale.FRANCE)
    val number = format.parse(longitude)
    val d = number.doubleValue
    return d
  }
}

object Updater {

  private var _start = new Point2D(0,0)
  private var _end = new Point2D(0,0)

  val r = new Rectangle {
    fill = Color.Transparent
    visible = false
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
    r.visible = true
  }
}

object GUI extends JFXApp {
  val df = Main.init()
  var query = df
  var point2 = Rectangle(0,0,1,1)

  stage = new PrimaryStage {

    maximized = true
    title = "Global Earthquakes"
    scene = new Scene(1200, 700) {

      def doubleControl ( s : String, info : Label) : Boolean = {
        try {
          val s2 = s.toDouble
          return true
        }
        catch {
          case x: Exception =>
            info.text = "Ooh, you can't type string in these cells"
            AnchorPane.setLeftAnchor(info,5)
            AnchorPane.setTopAnchor(info, img.getHeight+10)
            anchor.children.+=(info)
            return false
        }
      }

      def getCombo(combo : ComboBox[Int]): Int ={
        combo.onAction = (e : ActionEvent) => {
          return combo.value.apply
        }
        return combo.value.apply
      }

      private def populateSeq(output : Array[String]) : List[Print]= {
        var seq = List[Print]()
        var i = 0
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
          }

          if(arr(7).toDouble > 0) { //lat
            x_p = ((arr(7).toDouble)*(canvas.height.toDouble/2))/90 + canvas.height.toDouble/2 - 15
          }
          else{
            x_p = ((canvas.height.toDouble/2 - (Math.abs(arr(7).toDouble))*(canvas.height.toDouble/2))/90) + (canvas.height.toDouble/2) + 15
          }

          val point = Rectangle( y_p, x_p + (canvas.height.toDouble - (x_p*2)) , 2,2)
          point.fill = Color.Red

          pane.children += point

          var seq2 = seq ++ List(tmp)
          seq = seq2
          i= i+1
          if (i==80000){
            info.text = "Output exceed 80.000 entries. To see all entries save the output"
            AnchorPane.setLeftAnchor(info,5)
            AnchorPane.setTopAnchor(info, img.getHeight+10)
            anchor.children.+=(info)

            return seq
          }
        })
        return seq
      }

      def output(output :Array[String], col1 : TableColumn[Print,String] = col1, col2 : TableColumn[Print,String] = col2,col3 : TableColumn[Print,String] = col3, col4 : TableColumn[Print,String] = col4,col5  : TableColumn[Print,String] = col5, col6 : TableColumn[Print,String] = col6, col7 : TableColumn[Print,String] = col7, col8 : TableColumn[Print,String] = col8, col9 : TableColumn[Print,String] = col9, col10 : TableColumn[Print,String] = col10, col11 : TableColumn[Print,String] = col11, col12 : TableColumn[Print,String] = col12): TableView[Print] = {
        var seq = populateSeq(output)
        size.text = "Size: "+seq.size
        val data = ObservableBuffer(seq)

        val table= new TableView(data)
        val col1 = new TableColumn[Print, String]("City")
        col1.cellValueFactory = cdf => ObjectProperty(cdf.value.city)

        val col2 = new TableColumn[Print, String]("Region")
        col2.cellValueFactory = cdf => ObjectProperty(cdf.value.region)

        val col3 = new TableColumn[Print, String]("State")
        col3.cellValueFactory = cdf => StringProperty(cdf.value.state)

        val col4 = new TableColumn[Print, String]("Lat")
        col4.cellValueFactory = cdf => ObjectProperty(cdf.value.latitude)

        val col5 = new TableColumn[Print, String]("Lon")
        col5.cellValueFactory = cdf => ObjectProperty(cdf.value.longitude)

        val col6 = new TableColumn[Print, String]("YY")
        col6.cellValueFactory = cdf => ObjectProperty(cdf.value.year)

        val col7 = new TableColumn[Print, String]("MM")
        col7.cellValueFactory = cdf => ObjectProperty(cdf.value.month)

        val col8 = new TableColumn[Print, String]("DD")
        col8.cellValueFactory = cdf => ObjectProperty(cdf.value.day)

        val col9 = new TableColumn[Print, String]("h")
        col9.cellValueFactory = cdf => ObjectProperty(cdf.value.hour)

        val col10 = new TableColumn[Print, String]("min")
        col10.cellValueFactory = cdf => ObjectProperty(cdf.value.minute)

        val col11 = new TableColumn[Print, String]("Depth")
        col11.cellValueFactory = cdf => ObjectProperty(cdf.value.depth)

        val col12 = new TableColumn[Print, String]("Mag")
        col12.cellValueFactory = cdf => ObjectProperty(cdf.value.magnitude)

        table.columns ++= List(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12)

        return table
      }

      val txa = new TextField
      val grid = new GridPane
      grid.setHgap(7)
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
        promptText = "750"
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

      var yearCombo = new ComboBox(List.range(1900, 2019))
      var monthCombo = new ComboBox(List.range(1, 13))
      var dayCombo = new ComboBox(List.range(1, 32))
      yearCombo.prefWidth = 100
      monthCombo.prefWidth = 100
      dayCombo.prefWidth = 100

      val keyword = new TextField {
        promptText = "Type keyword"
      }

      val latitude = new TextField {
        prefColumnCount = 7
      }

      val longitude = new TextField {
        prefColumnCount = 7
      }

      val radius = new TextField {
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
      grid.add(new Label("Radius (KM)"),2,4)
      grid.add(radius,2,5)

      grid.add(submit, 3, 4)
      grid.add(reset, 3, 5)
      grid.add(save,3,3)
      grid.add(resetImg, 4,4)
      grid.add(saveImg, 4,5)
      val progress = new ProgressBar
      progress.layoutX = 80
      progress.layoutY = 10

      var size = new Label()
      var info = new Label()
      grid.add(size,3,2)

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
        radius.text = ""

        x_first = 0
        y_first = 0
        x_last = 0
        y_last = 0

        grid.children.-=(yearCombo, monthCombo, dayCombo)
        yearCombo = new ComboBox(List.range(1900, 2019))
        monthCombo = new ComboBox(List.range(1, 13))
        dayCombo = new ComboBox(List.range(1, 32))
        yearCombo.prefWidth = 100
        monthCombo.prefWidth = 100
        dayCombo.prefWidth = 100
        grid.add(yearCombo, 1, 1)
        grid.add(monthCombo, 1, 3)
        grid.add(dayCombo, 1, 5)
      }

      resetImg.onAction = (e : ActionEvent) =>{
        x_first = 0
        y_first = 0
        x_last = 0
        y_last = 0
        val p = new Point2D(0,0)

        val upd = Updater
        upd.update(p, p)
        pane.children = upd.r
      }

      saveImg.onAction = (e : ActionEvent) =>{
        val fileChooser = new FileChooser()
        val selectedFile = fileChooser.showSaveDialog(stage)
        val img2 = SwingFXUtils.fromFXImage(root2.snapshot(new SnapshotParameters(),null), null)
        ImageIO.write(img2, "png", selectedFile)
      }

      save.onAction = (e : ActionEvent) => {
        val fileChooser = new FileChooser
        val selectedFile = fileChooser.showSaveDialog(stage)
        query.saveAsTextFile(selectedFile.getAbsolutePath)
      }

      submit.onAction = (e:ActionEvent) => {
        var ok = false

        anchor.children.-=(info)

        var subQuery = df

        val p = new Point2D(0, 0)
        Updater.update(p, p)

        pane.children = Updater.r

        if (!magMin.getText.equals("") && !magMax.getText.equals("")) {
          if(doubleControl(magMin.getText, info) && doubleControl(magMax.getText, info)) {
            ok = true
            subQuery = Main.searchByMag(subQuery, magMin.getText.toDouble, magMax.getText.toDouble)
          }
        }
        else if (!magMin.getText.equals("")) {
          if (doubleControl(magMin.getText, info)) {
            ok = true
            subQuery = Main.searchByMag(subQuery, minMag = magMin.getText.toDouble)
          }
        }
        else if (!magMax.getText.equals("")) {
          if(doubleControl(magMax.getText, info)){
            ok = true
            subQuery = Main.searchByMag(subQuery, maxMag = magMax.getText.toDouble)
          }
        }

        if (!depthMin.getText.equals("") && !depthMax.getText.equals("")) {
          if (doubleControl(depthMin.getText, info) && doubleControl(depthMax.getText, info)) {
            ok = true
            subQuery = Main.searchByDepth(subQuery, depthMin.getText.toDouble, depthMax.getText.toDouble)
          }
        }
        else if (!depthMin.getText.equals("")) {
          if(doubleControl(depthMin.getText, info)){
            ok = true
            subQuery = Main.searchByDepth(subQuery, minDepth = depthMin.getText.toDouble)
          }
        }
        else if (!depthMax.getText.equals("")) {
          if(doubleControl(depthMax.getText, info)){
            ok = true
            subQuery = Main.searchByDepth(subQuery, maxDepth = depthMax.getText.toDouble)
          }
        }

        if (!yearMin.getText.equals("") && !yearMax.getText.equals("")) {
          if (doubleControl(yearMin.getText, info) && doubleControl(yearMax.getText, info)) {
            ok = true
            subQuery = Main.searchByDateRange(subQuery, minYear = yearMin.getText.toInt, maxYear = yearMax.getText.toInt)
          }
        }
        else if (!yearMin.getText.equals("")) {
          if(doubleControl(yearMin.getText, info)){
            ok = true
            subQuery = Main.searchByDateRange(subQuery, minYear = yearMin.getText.toInt)
          }
        }
        else if (!yearMax.getText.equals("")) {
          if(doubleControl(yearMax.getText, info)){
            ok = true
            subQuery = Main.searchByDateRange(subQuery, maxYear = yearMax.getText.toInt)
          }
        }

        if (!getCombo(yearCombo).equals(0) && !getCombo(monthCombo).equals(0) && !getCombo(dayCombo).equals(0)) {
          ok = true
          subQuery = Main.searchByDate(subQuery, getCombo(yearCombo), getCombo(monthCombo), getCombo(dayCombo))
        }
        else if (!getCombo(yearCombo).equals(0) && !getCombo(monthCombo).equals(0)) {
          ok = true
          subQuery = Main.searchByMonth(Main.searchByYear(subQuery, getCombo(yearCombo)), getCombo(monthCombo))
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
          ok = true
          subQuery = Main.searchByDay(Main.searchByMonth(subQuery, getCombo(monthCombo)), getCombo(dayCombo))
        }
        else if (!getCombo(yearCombo).equals(0)) {
          ok = true
          subQuery = Main.searchByYear(subQuery, getCombo(yearCombo))
        }
        else if (!getCombo(monthCombo).equals(0)) {
          ok = true
          subQuery = Main.searchByMonth(subQuery, getCombo(monthCombo))
        }
        else if (!getCombo(yearCombo).equals(0)) {
          ok = false
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Error Dialog"
            headerText = "Error"
            contentText = "You can't indicate only a day for the query!"
          }.showAndWait()
        }

        if (!keyword.getText.equals("")) {
          ok = true
          subQuery = Main.searchByKey(subQuery, keyword.getText)
        }

        if (!latitude.getText.equals("") && !longitude.getText.equals("")) {
          if (doubleControl(latitude.getText, info) && doubleControl(longitude.getText, info)) {
            ok = true
            subQuery = Main.searchByLatLon(subQuery, latitude.getText.toDouble, longitude.getText.toDouble)
          }
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

        if (!radius.getText.equals("") && radiusQuery) {
          if (doubleControl(radius.getText, info)) {
            ok = true
            subQuery = Main.radiusQuery(subQuery, y_first.toDouble, x_first.toDouble, radius.getText.toDouble)
          }
        }
        else if (!radiusQuery && !radius.getText.equals("")) {
          ok = false
          radiusQuery = false
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Error Dialog"
            headerText = "Error"
            contentText = "You must choose both a location from \nthe map and the radius of the query"
          }.showAndWait()
        }
        else if (radiusQuery && radius.getText.equals("")) {
          ok = false
          radiusQuery = false
          new Alert(AlertType.Error) {
            initOwner(stage)
            title = "Error Dialog"
            headerText = "Error"
            contentText = "You must choose both a location from \nthe map and the radius of the query"
          }.showAndWait()
        }

        if( x_first!= 0 && y_first!= 0 && x_last!= 0 && y_last!= 0){
          ok = true
          subQuery = Main.rectQuery(subQuery,y_first,x_first,y_last,x_last)
          x_first = 0
          y_first = 0
          x_last = 0
          y_last = 0
        }

        if (ok) {
          anchor.children.-=(table)
          table = output(subQuery.collect())
          table.onMouseClicked = (e : MouseEvent) => {
            pane.children -= point2
            val pos = table.getSelectionModel.getSelectedCells.get(0)
            val row = pos.getRow
            val item = table.getItems.get(row)

            var y_p2 = 0.0
            var x_p2 = 0.0
            if(item.getLon() > 0) { //long
              y_p2 = ((item.getLon() * (canvas.width.toDouble / 2)) / 180) + (canvas.width.toDouble / 2)
            }
            else{
              y_p2 = (canvas.width.toDouble/2 - (Math.abs(item.getLon())*(canvas.width.toDouble/2))/180)//+(canvas.width.toDouble/2)
            }

            if(item.getLat() > 0) { //lat
              x_p2 = (item.getLat()*(canvas.height.toDouble/2))/90 + canvas.height.toDouble/2 - 15
            }
            else{
              x_p2 = (canvas.height.toDouble/2 - (Math.abs(item.getLat())*(canvas.height.toDouble/2))/90) //+ (canvas.height.toDouble/2) + 15
            }
            point2 = Rectangle( y_p2, x_p2 + (canvas.height.toDouble - (x_p2*2)), 3, 3 )
            point2.fill = Color.Yellow
            pane.children += point2
          }

          anchor.children.-=(grid)
          AnchorPane.setTopAnchor(table, 5)
          AnchorPane.setBottomAnchor(table, 5)
          AnchorPane.setLeftAnchor(table, img.width.toDouble)
          AnchorPane.setRightAnchor(table, 5)
          anchor.children ++= List(table,grid)
      }
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

      table.columns ++= List(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12)

    val pane = new Pane {
      // Add rectangle that will be updated with user interactions
      children += Updater.r
    }

    // Define handling of mouse events
    var x_first = 0.0
    var y_first = 0.0

    var x_last = 0.0
    var y_last = 0.0

    var rangeQuery = false
    var radiusQuery = false
    pane.handleEvent(MouseEvent.Any) {
      me: MouseEvent => {

        me.eventType match {
          case MouseEvent.MousePressed => {
            if (!radiusQuery && !rangeQuery) {
              radiusQuery = true
              // Reset the shape
              val p = new Point2D(me.x, me.y)
              Updater.update(p, p)
              var tmp11 = (180 * me.y)/img.height.toDouble
              y_first = (180 - tmp11 - 90)+5

              var tmp12 = (360*me.x)/img.width.toDouble
              x_first = tmp12-180
            }
            else if (rangeQuery || radiusQuery) {
              radiusQuery = false
              rangeQuery = false
              val p = new Point2D(0,0)
              val upd = Updater
              upd.update(p, p)
              upd.r.visible = false
              pane.children = upd.r
              x_first = 0
              y_first = 0
              x_last = 0
              y_last = 0
            }
          }
          case MouseEvent.MouseDragged => {
            radiusQuery = false
            rangeQuery = true
            // Adjust the shape
            Updater.update(end = new Point2D(me.x, me.y))

            var tmp21 = (180 * me.y)/img.height.toDouble
            y_last = (180 - tmp21 - 90)+5

            var tmp22 = (360*me.x)/img.width.toDouble
            x_last = tmp22-180
          }
          case _                       => {}
        }
      }

    }

      root2.getChildren.add(pane)

      var anchor = new AnchorPane {
        AnchorPane.setTopAnchor(root2, 5)
        AnchorPane.setLeftAnchor(root2, 5)
        AnchorPane.setBottomAnchor(grid,5)
        AnchorPane.setLeftAnchor(grid, 5)
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
  stage.show()
}