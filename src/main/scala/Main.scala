import scalaj.http._
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

import java.net.URI


object Main {
  val accessKey: String = System.getenv("ACCESS_KEY")
  val secretKey: String = System.getenv("SECRET_KEY")
  val bucket: String = System.getenv("BUCKET")

  val client: S3Client = S3Client.builder
    .region(Region.of("fr-par"))
    .endpointOverride(URI.create("https://s3.fr-par.scw.cloud"))
    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))).build

  def main(args: Array[String]): Unit = {
    val years = List("2015", "2016", "2017", "2018", "2019", "2020", "2021")
    val months = List("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    val days = List("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
    val hours = List("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24")

    for {
      year <- years
      month <- months
      day <- days
      hour <- hours
    } yield {
      val key = s"/data/github-archive/year=$year/month=$month/day=$day/hour=$hour/data.json.gz"
      println(s"upload $key to $bucket s3 bucket...")
      uploadFileToS3(bucket, key, Http(s"https://data.gharchive.org/$year-$month-$day-$hour.json.gz").asBytes.body)
    }
  }

  def uploadFileToS3(bucket: String, key: String, content: Array[Byte]): Unit = {
    val objectRequest = PutObjectRequest.builder.key(key).bucket(bucket).build

    client.putObject(objectRequest, RequestBody.fromBytes(content))
  }
}
