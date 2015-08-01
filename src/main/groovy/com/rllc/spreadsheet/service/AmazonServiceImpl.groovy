package com.rllc.spreadsheet.service

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.google.common.io.ByteStreams
import com.rllc.spreadsheet.domain.AmazonCredentials
import com.rllc.spreadsheet.domain.RemoteFiles
import com.rllc.spreadsheet.props.CongregationPropertyLoader
import com.rllc.spreadsheet.rest.repository.SyncExecutionRepository
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

    @Autowired
    SyncExecutionRepository syncExecutionRepository

    @Override
    RemoteFiles downloadMetadata(List<String> fileNames, String congregationKey) {
        logger.info("downloadMetadata $congregationKey")
        AmazonCredentials amazonCredentials = congregationPropertyLoader.credentials[congregationKey].amazonCredentials
        AmazonS3 amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(amazonCredentials.accessKey, amazonCredentials.secretKey))
        def mp3Files = []
        def mp3Dir = File.createTempDir()

        fileNames.each { fileName ->
            logger.info "downloading $fileName"

            GetObjectRequest rangeObjectRequest = new GetObjectRequest(
                    amazonCredentials.bucket, fileName)
            rangeObjectRequest.setRange(0, 1024)
            S3Object objectPortion = amazonS3Client.getObject(rangeObjectRequest)
            byte[] buffer = ByteStreams.toByteArray(objectPortion.getObjectContent());

            File targetFile = new File("${mp3Dir.absolutePath}/$fileName")
            targetFile.getParentFile().mkdirs();
            OutputStream outStream = new FileOutputStream(targetFile)
            outStream.write(buffer)
            mp3Files << targetFile
        }

        logger.info "> files : $mp3Files"
        logger.info "> root : $mp3Dir.absolutePath"

        return new RemoteFiles(files: mp3Files, root: mp3Dir.absolutePath)
    }

    @Override
    List<String> listFiles(boolean refreshAll, String congregationKey) {
        Date lastExecution = syncExecutionRepository.findTop1ByOrderByDateDesc()[0]?.date ?: new Date(2000, 0, 1)
        logger.info "> lastExecution : ${lastExecution}"
        AmazonCredentials amazonCredentials = congregationPropertyLoader.credentials[congregationKey].amazonCredentials
        AmazonS3 amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(amazonCredentials.accessKey, amazonCredentials.secretKey))

        def files = []
        ObjectListing listing = amazonS3Client.listObjects(amazonCredentials.bucket);
        while (true) {
            List<S3ObjectSummary> summaries = listing.getObjectSummaries();
            summaries.each { summary ->
                logger.info "> ${summary.key} (size = ${summary.size}, lastModified = ${summary.lastModified})"
                logger.info "> $summary after $lastExecution ? ${summary.lastModified.after(lastExecution)}"
                if (summary.key.endsWith(".mp3") &&
                        (refreshAll || summary.lastModified.after(lastExecution))) {
                    files << summary.key
                }
            }
            listing = amazonS3Client.listNextBatchOfObjects(listing);
            if (!listing.truncated) {
                break
            } else {
                logger.info("listing is truncated.. get next results")
            }
        }
        logger.info("> found ${files.size()} files")

        return files
    }

}
