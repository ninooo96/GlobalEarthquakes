import multirange.MultiRange
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.AccessibleRole.TextArea
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, ComboBox, Label, TextArea, TextField}
import scalafx.scene.image.{Image, ImageView, WritableImage}
import scalafx.scene.layout.{AnchorPane, BorderPane, GridPane}
import scalafx.{application, stage}

object HelloSBT extends JFXApp {
//  stage = new PrimaryStage {
//    scene = new Scene {
//      root = new BorderPane {
//        padding = Insets(25)
//        center = new Label("Hello SBT")
//      }
//    }
//  }
//}
//
//val anchorPane = AnchorPane
//val label1 = Label("Label1")
//val label2 = Label("Label2")
//val text1 = Label("Text1")
//val text2 = Label("Text2")
//AnchorPane.setLeftAnchor(label1, 20d)
//AnchorPane.setTopAnchor(label1, 10d)
//AnchorPane.setLeftAnchor(text1, 20d)
//AnchorPane.setTopAnchor(text1, 40d)
//AnchorPane.setRightAnchor(label2, 165d)
//AnchorPane.setTopAnchor(label2, 10d)
//AnchorPane.setRightAnchor(text2, 50d)
//AnchorPane.setTopAnchor(text2, 40d)
//anchorPane.(400, 100)
//anchorPane.addAll(label1, label2, text1, text2)
//val scene = new Nothing(anchorPane)
//stage.setScene(scene)
//stage.setResizable(false)

stage = new PrimaryStage{
  title = "Global Earthquakes"
  scene = new Scene(1200,700){
    val txa = new TextField
    val label = new Label("Ciao")
    val grid = new GridPane
//    val gridYear = new Grid
    grid.setHgap(10)
    grid.setVgap(2)

    grid.add(new Label("Magnitude (Max, Min)"),0,0)
    grid.add(new GridPane{
      add(new TextField {
        promptText = "1"
        prefColumnCount = 6},0,0)
      add(new TextField {
        promptText = "8"
        prefColumnCount = 6}, 1,0)
    },0,1)

    grid.add(new Label("Depth (Max, Min)"),0,2)
    grid.add(new GridPane{
      add(new TextField {
        promptText = "0"
        prefColumnCount = 6},0,0)
      add(new TextField {
        promptText = "700"
        prefColumnCount = 6}, 1,0)
    },0,3)

    grid.add(new Label("Year "),0,4)
    grid.add(new GridPane{
      add(new TextField {
        promptText = "1900"
        prefColumnCount = 6},0,0)
      add(new TextField {
        promptText = "2018"
        prefColumnCount = 6}, 1,0)
    },0,5)

    grid.add(new Label("Year"),1,0)
    grid.add( new ComboBox(List.range(1900,2019)),1,1)

    grid.add(new Label("Month"),1,2)
    grid.add( new ComboBox(List.range(1,13)),1,3)

    grid.add(new Label("Day"),1,4)
    grid.add( new ComboBox(List.range(1,32)),1,5)

    grid.add(new Label("Keyword"),2,0)
    grid.add(new TextField{promptText="Type keyword"},2,1)
    grid.add(new Label("Latitude and Longitude"),2,2)
    grid.add(new GridPane{

      add(new TextField{prefColumnCount = 7},0,0)
      add(new TextField{prefColumnCount = 7},1,0)
    },2,3)

    grid.add(new Button {
      text = "SUBMIT"
    },3,4)
    grid.add(new Button {
      text = "RESET"
    },3,5)




    val img = new Image("world.png",3*(width/5).toInt, 5*(height/7).toInt/*3*(height/5).toInt*/,false,false)
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
    root = new AnchorPane {

//      AnchorPane.setBottomAnchor(txa, 10)
      AnchorPane.setTopAnchor(node, 5)
      AnchorPane.setLeftAnchor(node, 5)
//      AnchorPane.setBottomAnchor(grid,5)
      AnchorPane.setTopAnchor(grid, img.height.toDouble+10)
//
      AnchorPane.setLeftAnchor(grid,5)
      children = List(node,grid)


    }
    }
  }



}

