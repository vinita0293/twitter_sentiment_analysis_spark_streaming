package com.gudvin.tsa.spark

import edu.stanford.nlp.pipeline.StanfordCoreNLP

/**
  * Created by vinita on 7/15/16.
  */
object MyCoreNLP {

    @transient lazy val stanfordCoreNLP = new StanfordCoreNLP()
}
