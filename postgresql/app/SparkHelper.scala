package app.helpers

import org.apache.spark.sql.SparkSession

object SparkHelper {

    def getSpark() = {
        SparkSession
            .builder()
            .appName("database analyzer")
            .getOrCreate()
    }

    def closeSession() = {
        SparkSession
            .builder()
            .appName("database analyzer")
            .getOrCreate()
            .close()
    }
}
