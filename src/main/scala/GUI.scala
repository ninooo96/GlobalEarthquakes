import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.AccessibleRole.TextArea
import scalafx.scene.Scene
import scalafx.scene.control.{Label, TextArea, TextField}
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
  title = "Prova"
  scene = new Scene(600,300){
    val txa = new TextField
    val label = new Label("Ciao")
    val grid = new GridPane
    grid.add(new Label("antn"),0,0)
    grid.add(new Label("gaglio"),1,0)

    root = new AnchorPane {

      AnchorPane.setBottomAnchor(txa, 10)
      AnchorPane.setTopAnchor(label, 10)
      AnchorPane.setRightAnchor(txa, 10)
      AnchorPane.setLeftAnchor(label, 10)
      AnchorPane.setBottomAnchor(grid,10)
      AnchorPane.setLeftAnchor(grid,10)
      children = List(txa, label,grid)


    }
    }
  }



}