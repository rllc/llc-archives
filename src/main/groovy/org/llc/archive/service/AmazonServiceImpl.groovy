package org.llc.archive.service

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.ObjectListing
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.google.common.io.ByteStreams
import org.llc.archive.domain.AmazonCredentials
import org.llc.archive.domain.RemoteFile
import org.llc.archive.domain.S3File
import org.llc.archive.props.CongregationPropertyLoader
import org.llc.archive.rest.repository.SermonRepository
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
    SermonRepository sermonRepository

    @Override
    RemoteFile downloadMetadata(String fileName, String congregationKey) {
        logger.info "downloading $fileName"
        def mp3Dir = File.createTempDir()
        AmazonCredentials amazonCredentials = congregationPropertyLoader.credentials[congregationKey].amazonCredentials
        AmazonS3Client amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(amazonCredentials.accessKey, amazonCredentials.secretKey))
        File targetFile
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(
                    amazonCredentials.bucket, fileName)
            S3Object objectPortion = amazonS3Client.getObject(getObjectRequest)
            byte[] buffer = ByteStreams.toByteArray(objectPortion.getObjectContent());

            targetFile = new File("${mp3Dir.absolutePath}/$fileName")
            targetFile.getParentFile().mkdirs();
            OutputStream outStream = new FileOutputStream(targetFile)
            outStream.write(buffer)
        }
        catch (all) {
            logger.warn "error downloading $fileName ${all.printStackTrace()}"
        }

        return new RemoteFile(file: targetFile, root: mp3Dir.absolutePath)
    }

    @Override
    List<S3File> listFiles(String congregationKey) {
        List<S3File> s3Files = []
        AmazonCredentials amazonCredentials = congregationPropertyLoader.credentials[congregationKey].amazonCredentials
        AmazonS3 amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(amazonCredentials.accessKey, amazonCredentials.secretKey))

        ObjectListing listing = amazonS3Client.listObjects(amazonCredentials.bucket);
        while (true) {
            List<S3ObjectSummary> summaries = listing.getObjectSummaries();
            summaries.each { summary ->
                s3Files << new S3File(
                        filename: summary.key,
                        lastModified: summary.lastModified
                )
            }
            listing = amazonS3Client.listNextBatchOfObjects(listing);
            if (!listing.truncated) {
                break
            } else {
                logger.info("listing is truncated.. get next results")
            }
        }
        logger.info("> found ${s3Files.size()} files")
        return s3Files
    }

}
