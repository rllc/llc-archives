package org.llc.archive.service

import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 1/26/16.
 */
@Component
class NameFixerImpl implements NameFixer {

    static final Map<String, String> nicknameToFormalNameMap = [
            'Art'  : 'Arthur',
            'Bob'  : 'Robert',
            'Jim'  : 'James',
            'Joe'  : 'Joseph',
            'Phil' : 'Phillip',
            'Rocky': 'Rodrick',
            'Stan' : 'Stanley'
    ]

    static final Map<String, String> mistypedLastnameToCorrectLastName = [
            'Kumpala': 'Kumpula',
            'Sorvola': 'Sorvala'
    ]

    @Override
    String convertToFormalName(String nickname) {
        if (nickname) {
            if (nicknameToFormalNameMap.containsKey(nickname)) {
                return nicknameToFormalNameMap[nickname]
            }
            return nickname
        }
    }

    @Override
    String correctMisspelledLastName(String lastname) {
        if (lastname) {
            if (mistypedLastnameToCorrectLastName.containsKey(lastname)) {
                return mistypedLastnameToCorrectLastName[lastname]
            }
            return lastname
        }
    }
}
