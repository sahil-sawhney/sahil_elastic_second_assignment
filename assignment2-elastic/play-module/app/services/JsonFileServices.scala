package services

import com.google.inject.Inject
import repositories.JsonRepo

/**
  * Created by sahil on 4/12/16.
  */
class JsonFileServices @Inject()(jsonRepo:JsonRepo) {

  def fetchJson(filePath:String) : String={

    jsonRepo.fetchJson(filePath)
    "success"
  }

  def generateJson(folderPath:String) : String={

    jsonRepo.generateJson(folderPath)
    "success"
  }

}
