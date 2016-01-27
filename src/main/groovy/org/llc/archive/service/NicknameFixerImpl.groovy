package org.llc.archive.service

import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 1/26/16.
 */
@Component
class NicknameFixerImpl implements NicknameFixer {

    static final Map<String, String> nicknameToRealNameMap = [
            'Art'  : 'Arthur',
            'Bob'  : 'Robert',
            'Jim'  : 'James',
            'Joe'  : 'Joseph',
            'Phil' : 'Phillip',
            'Rocky': 'Rodrick',
            'Stan' : 'Stanley'
    ]

    @Override
    String convertToRealName(String nickname) {
        if (nicknameToRealNameMap.containsKey(nickname)) {
            return nicknameToRealNameMap[nickname]
        }
        return nickname
    }
}
