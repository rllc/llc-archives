package com.rllc.spreadsheet.service

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import com.rllc.spreadsheet.domain.AmazonCredentials
import com.rllc.spreadsheet.props.CongregationPropertyLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 5/25/15.
 */
@Component
class AmazonServiceImpl implements AmazonService {

    private static final Logger logger = LoggerFactory.getLogger(AmazonServiceImpl.class);

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Override
    List<File> downloadMetadata(List<String> fileNames, String congregationKey) {
        AmazonCredentials amazonCredentials = congregationPropertyLoader.credentials[congregationKey]
        AmazonS3 amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(amazonCredentials.accessKey, amazonCredentials.secretKey))
        def mp3Files = []
        def mp3Dir = File.createTempDir()
        fileNames.each { fileName ->

            GetObjectRequest rangeObjectRequest = new GetObjectRequest(
                    amazonCredentials.bucket, fileName)
            rangeObjectRequest.setRange(0, 400)
            S3Object objectPortion = amazonS3Client.getObject(rangeObjectRequest)
            InputStream objectData = objectPortion.getObjectContent()

            byte[] buffer = new byte[objectData.available()]
            objectData.read(buffer)

            File targetFile = new File("${mp3Dir.absolutePath}/$fileName")
            OutputStream outStream = new FileOutputStream(targetFile)
            outStream.write(buffer)
            objectData.close()

            mp3Files << targetFile
        }

        return mp3Files
    }

    @Override
    List<String> listFiles(String congregationKey) {
        AmazonCredentials amazonCredentials = congregationPropertyLoader.credentials[congregationKey]
        AmazonS3 amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(amazonCredentials.accessKey, amazonCredentials.secretKey))

        def files = []
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(amazonCredentials.bucket)

        while (true) {
            ObjectListing objectListing = amazonS3Client.listObjects(listObjectsRequest)
            for (S3ObjectSummary objectSummary :
                    objectListing.getObjectSummaries()) {
                logger.info(" - " + objectSummary.getKey() + "  " +
                        "(size = " + objectSummary.getSize() +
                        ")")
                files << objectSummary.key
            }
            listObjectsRequest.setMarker(objectListing.getNextMarker())
            if (objectListing.isTruncated()) break
        }

        return files
    }

}
