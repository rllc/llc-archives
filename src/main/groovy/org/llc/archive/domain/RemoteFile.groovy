package org.llc.archive.domain

import groovy.transform.Canonical

/**
 * Created by Steven McAdams on 7/31/15.
 */
@Canonical
class RemoteFile {
    File file
    String root
}
