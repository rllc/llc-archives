package org.llc.archive.service

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import org.llc.archive.domain.RemoteFile
import org.llc.archive.domain.S3File
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 5/18/15.
 */
@Component(value = "remoteMp3DiscoveryService")
class RemoteMp3DiscoveryServiceImpl extends AbstractMp3DiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(RemoteMp3DiscoveryServiceImpl.class)

    @Autowired
    AmazonService amazonService

    @Override
    List<S3File> findMp3Files(String congregationKey) {
        try {
            return amazonService.listFiles(congregationKey)
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.")
            logger.info("Error Message:    " + ase.getMessage())
            logger.info("HTTP Status Code: " + ase.getStatusCode())
            logger.info("AWS Error Code:   " + ase.getErrorCode())
            logger.info("Error Type:       " + ase.getErrorType())
            logger.info("Request ID:       " + ase.getRequestId())
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException, which means" +
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.")
            logger.info("Error Message: " + ace.getMessage())
        }
    }

    @Override
    RemoteFile downloadMetadata(S3File s3File, String congregationKey) {
        amazonService.downloadMetadata(s3File.filename, congregationKey)
    }
}
