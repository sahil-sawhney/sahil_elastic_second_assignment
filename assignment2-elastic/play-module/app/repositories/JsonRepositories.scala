package repositories

import java.io.{FileOutputStream, File}
import java.net.InetAddress

import org.elasticsearch.action.admin.indices.delete.{DeleteIndexRequest, DeleteIndexResponse}
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.node.NodeBuilder.nodeBuilder


import scala.io.Source

/**
  * Created by sahil on 4/12/16.
  */
trait JsonTable {

  val port = 9300
  val nodes = List("localhost")
  val addresses = nodes.map { host => new InetSocketTransportAddress(InetAddress.getByName(host), port) }
  lazy val settings = Settings.settingsBuilder()
    .put("threadpool.search.queue_size", -1)
    .put("threadpool.index.queue_size", -1)
    .put("client.transport.ping_timeout", "120s")
    .put("client.transport.nodes_sampler_interval", "60s")
    .put("cluster.name", "sahil55")
    .build()
  val client: Client = TransportClient.builder().settings(settings).build().addTransportAddresses(addresses: _*)
}

class JsonRepo extends JsonTable {

  def fetchJson(filePath:String) = {

    if(client.admin().indices().prepareExists("index_json").execute().actionGet().isExists) {
      client.admin.indices.delete(new DeleteIndexRequest("index_json")).actionGet
    }
    val jsonData = Source.fromFile(filePath).getLines().toList
    val bulkRequest = client.prepareBulk
    for (i <- jsonData.indices) {
      bulkRequest.add(client.prepareIndex("index_json", "type_json", (i + 1).toString).setSource(jsonData(i)))
    }
    bulkRequest.execute.actionGet
    client.admin.indices.prepareRefresh("index_json").get
    Thread.sleep(500)
  }

  def generateJson(folderPath:String) = {

    val pw = new java.io.PrintWriter(new FileOutputStream(new File(folderPath + "/outputJson.json")))
    val arrayy = client.prepareSearch("index_json").setFrom(0).setSize(300).setQuery(matchAllQuery).execute.actionGet.getHits.getHits
    for (i <- 0 until arrayy.length) {
      pw.write(arrayy.apply(i).getSourceAsString + "\n")
    }
    pw.close()
  }
}
