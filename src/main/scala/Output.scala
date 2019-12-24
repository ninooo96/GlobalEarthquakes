//import scalafx.application.JFXApp
//import scalafx.application.JFXApp.PrimaryStage
//import scalafx.collections.ObservableBuffer
//import scalafx.scene.Scene
//import scalafx.scene.control.TableColumn._
//import scalafx.scene.control.{TableCell, TableColumn, TableView}
//import scalafx.scene.paint.Color
//import scalafx.scene.shape.Circle
//
//object Output extends JFXApp {
//
//
//  stage = new PrimaryStage {
//    title = "TableView with custom color cell"
//    scene = new Scene {
//      content = new TableView[String]() {
//        columns ++= List(
//          new TableColumn[String, _]{
//            text = "First Name"
//            prefWidth = 100
//          },
//          new TableColumn[String, _]{
//            text = "Last Name"
//            prefWidth = 100
//          },
//          new TableColumn[String, _]{
//            text = "Favorite Color"
//            // Render the property value when it changes,
//            // including initial assignment
////            cellFactory = { _ =>
////              new TableCell[Person, Color] {
////                item.onChange { (_, _, newColor) =>
////                  graphic =
////                    if (newColor != null)
////                      new Circle {
////                        fill = newColor
////                        radius = 8
////                      }
////                    else
////                      null
////                }
////              }
////            }
//            prefWidth = 100
//          }
//        )
//      }
//    }
//  }
//}
////}
