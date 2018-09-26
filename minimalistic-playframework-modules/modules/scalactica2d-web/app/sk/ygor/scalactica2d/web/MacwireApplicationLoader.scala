package sk.ygor.scalactica2d.web

import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader}

class MacwireApplicationLoader extends ApplicationLoader {

  def load(context: Context): Application = new Scalactica2dWebComponents(context).application

}

