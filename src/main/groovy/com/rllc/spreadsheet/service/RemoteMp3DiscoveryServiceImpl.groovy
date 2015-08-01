package com.rllc.spreadsheet.service

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.rllc.spreadsheet.domain.RemoteFiles
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
    RemoteFiles findMp3Files(boolean refreshAll, String congregationKey) {

        try {
            List<String> s3Files = amazonService.listFiles(refreshAll, congregationKey)
            RemoteFiles remoteFiles = amazonService.downloadMetadata(s3Files, congregationKey)
            return remoteFiles
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
            return new RemoteFiles()
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException, which means" +
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.")
            logger.info("Error Message: " + ace.getMessage())
            return new RemoteFiles()
        }
    }

}
