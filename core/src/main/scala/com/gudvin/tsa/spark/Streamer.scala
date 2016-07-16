package com.gudvin.tsa.spark

import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by vinita on 7/12/16.
  */
object Streamer {
  def main(args: Array[String]) {
    val sparkHome = "/usr/local/spark-1.6.1-hadoop2.6-firsttime/"
    val sparkMasterUrl = "spark://vinita-Lenovo-G50-80:7077"

    val conf: SparkConf = new SparkConf()
      .setAppName("Twitter Streaming Sentiment")
      .setMaster("local[6]")
      .setSparkHome(sparkHome)

    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(10))

    val consumerKey = "CyuGlzTbWsD9LNrjyJavvnt88"
    val consumerSecret = "7z4S8LoPJanekLxyzBKD7wfAbm2SgDX1zrJ36Bfcis7uqVfhG0"
    val accessToken = "2217026450-NzTHo0kwcao7swmJLD1retrLT7m4d0ONhQL1n5o"
    val accessTokenSecret = "DbSIq0MP5S2nAcZQOd6MfEGu08zqhRCYTDCS849gEt8Md"

    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

    val stream = TwitterUtils.createStream(ssc, None, Seq("obama"))

    /**
      * stream.foreachRDD(myRdd => myRdd)
      * 1 DSTREAM
      * 2 COLLection[RDD]
      */

    stream.mapPartitions(partition => {
      val pipeline = StreamerUtils.getPipeline()
      partition.map(x => (x.getId, StreamerUtils.detectSentiment(x.getText, pipeline)))
    }).print()

    /** val pipeline = StreamerUtils.getPipeline()
      * stream.map(x => (x.getId, StreamerUtils.detectSentiment(x.getText, pipeline)))
      * stream.filter(_.getText.contains("vini")).print()
      */
    ssc.start()
    ssc.awaitTermination()
  }
}
