package org.llc.archive.rss.view

import com.rometools.rome.feed.atom.*
import org.llc.archive.rest.domain.Sermon
import org.llc.archive.rest.repository.SermonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Steven McAdams on 3/3/16.
 */
@Component
class SermonAtomFeedView extends AbstractAtomFeedView {

    @Autowired
    SermonRepository sermonRepository

    @Override
    protected void buildFeedMetadata(Map<String, Object> model, Feed feed, HttpServletRequest request) {
        feed.title = 'LLC Archived Sermons'
        feed.alternateLinks = [
                new Link(href: 'https://llc-archives.herokuapp.com')
        ]
        super.buildFeedMetadata(model, feed, request)
    }

    @Override
    protected List<Entry> buildFeedEntries(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {


        def entries = []

        model.get('feedContent').each { Sermon sermon ->
            Content comments = new Content()
            comments.setValue(sermon.comments)
            comments.setType(Content.TEXT)

            entries << new Entry(
                    title: "${sermon.minister} - ${sermon.bibleText}",
                    alternateLinks: [
                            new Link(href: sermon.fileUrl)
                    ],
                    summary: new Content(
                            value: sermon.comments
                    ),
                    authors: [
                            new Person(
                                    name: sermon.minister
                            )
                    ],
                    created: sermon.date,
                    updated: sermon.date,
                    published: sermon.date
            )
        }

        return entries
    }
}
