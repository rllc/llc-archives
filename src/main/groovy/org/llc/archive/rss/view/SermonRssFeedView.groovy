package org.llc.archive.rss.view

import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Item
import org.llc.archive.rest.domain.Sermon
import org.llc.archive.rest.repository.SermonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.feed.AbstractRssFeedView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Steven McAdams on 3/3/16.
 */
@Component
class SermonRssFeedView extends AbstractRssFeedView {

    @Autowired
    SermonRepository sermonRepository

    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
        feed.title = 'LLC Archived Sermons'
        feed.description = 'LLC Archived Sermons'
        feed.link = 'https://llc-archives.herokuapp.com'
        super.buildFeedMetadata(model, feed, request)
    }
    
    @Override
    protected List<Item> buildFeedItems(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        def items = []
        model.get('feedContent').each { Sermon sermon ->
            items << new Item(
                    title: "${sermon.minister} - ${sermon.bibleText}",
                    link: sermon.fileUrl,
                    comments: sermon.comments,
                    author: sermon.minister,
                    pubDate: sermon.date
            )
        }

        return items
    }
}
