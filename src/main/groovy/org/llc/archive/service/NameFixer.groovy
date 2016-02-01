package org.llc.archive.service

/**
 * Created by Steven McAdams on 1/26/16.
 */
interface NameFixer {

    String convertToFormalName(String nickname)

    String correctMisspelledLastName(String lastname)

}