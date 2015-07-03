package com.rllc.spreadsheet.config

import com.rllc.spreadsheet.props.CongregationPropertyLoader
import com.rllc.spreadsheet.rest.domain.AmazonCredentials
import com.rllc.spreadsheet.rest.domain.Book
import com.rllc.spreadsheet.rest.domain.Congregation
import com.rllc.spreadsheet.rest.domain.Sermon
import com.rllc.spreadsheet.rest.repository.*
import com.rllc.spreadsheet.security.CongregationUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

/**
 * Created by Steven McAdams on 6/22/15.
 */
@Component
class Bootstrap {

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    CongregationUserDetailsService congregationUserDetailsService

    @Autowired
    CongregationPropertyLoader congregationPropertyLoader

    @Autowired
    CongregationRepository congregationRepository

    @Autowired
    CongregationCrudRepository congregationCrudRepository

    @Autowired
    BookRepository bookRepository

    @Autowired
    BookCrudRepository bookCrudRepository

    @Autowired
    MinisterCrudRepository ministerCrudRepository

    @Autowired
    MinisterRepository ministerRepository

    def execute() {
        def creds = congregationPropertyLoader.credentials
        if (creds.size() == 0) {
            throw new RuntimeException('no credentials available')
        }

        def key = creds.keySet()[0]
        def user = congregationUserDetailsService.loadUserByUsername(key)
        def password = creds.get(key).password

        def previousAuth = SecurityContextHolder.getContext().authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(user.username, user.password, user.authorities)
        SecurityContextHolder.getContext().setAuthentication(auth);

        populateMinisters()
        populateBooks()
        populateCongregations()

        SecurityContextHolder.getContext().setAuthentication(previousAuth);
    }

    def populateCongregations() {
        if (congregationRepository.findAll().size() == 0) {
            congregationCrudRepository.save(
                    new Congregation(
                            name: 'rllc',
                            fullName: 'Rockford',
                            amazonCredentials: new AmazonCredentials(
                                    accessKey: 'access-key',
                                    secretKey: 'secret-key',
                                    bucket: 'aws-bucket'
                            ),
                            sermons: [
                                    new Sermon(
                                            minister: ministerRepository.findByFirstName('Jouko'),
                                            date: new Date(),
                                            bibleText: 'Matthew 6:27-34',
                                            fileUrl: 'aws-bucket/sermon.mp3'
                                    )
                            ]

                    )
            )


        }
    }

    def populateBooks() {
        if (bookRepository.findAll().size() == 0) {

            bookCrudRepository.save(new Book(name: 'Genesis', abbreviation: 'Gen.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Exodus', abbreviation: 'Exo.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Leviticus', abbreviation: 'Lev.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Numbers', abbreviation: 'Num.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Deuteronomy', abbreviation: 'Deut.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Joshua', abbreviation: 'Josh.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Judges', abbreviation: 'Judg.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Ruth', abbreviation: 'Ruth', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: '1 Samuel', abbreviation: '1 Sam.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: '2 Samuel', abbreviation: '2 Sam.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: '1 Kings', abbreviation: '1 Kgs.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: '2 Kings', abbreviation: '2 Kgs.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: '1 Chronicles', abbreviation: '1 Chron.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: '2 Chronicles', abbreviation: '2 Chron.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Ezra', abbreviation: 'Ezra', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Nehemiah', abbreviation: 'Neh.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Esther', abbreviation: 'Esth.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Job', abbreviation: 'Job', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Psalms', abbreviation: 'Psa.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Proverbs', abbreviation: 'Prov.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Ecclesiastes', abbreviation: 'Eccl.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Song of Solomon', abbreviation: 'S.of S.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Isaiah', abbreviation: 'Isa.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Jeremiah', abbreviation: 'Jer.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Lamentations', abbreviation: 'Lam.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Ezekiel', abbreviation: 'Eze.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Daniel', abbreviation: 'Dan.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Hosea', abbreviation: 'Hos.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Joel', abbreviation: 'Joel', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Amos', abbreviation: 'Amos', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Obadiah', abbreviation: 'Obad.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Jonah', abbreviation: 'Jon.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Micah', abbreviation: 'Mic.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Nahum', abbreviation: 'Nah.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Habakkuk', abbreviation: 'Hab.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Zephaniah', abbreviation: 'Zeph.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Haggai', abbreviation: 'Hag.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Zechariah', abbreviation: 'Zech.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Malachi', abbreviation: 'Mal.', testament: 'Old Testament'))
            bookCrudRepository.save(new Book(name: 'Matthew', abbreviation: 'Matt.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Mark', abbreviation: 'Mark', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Luke', abbreviation: 'Luke', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'John', abbreviation: 'John', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Acts', abbreviation: 'Acts', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Romans', abbreviation: 'Rom.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '1 Corinthians', abbreviation: '1 Cor.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '2 Corinthians', abbreviation: '2 Cor.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Galatians', abbreviation: 'Gal.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Ephesians', abbreviation: 'Eph.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Philippians', abbreviation: 'Phil.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Colossians', abbreviation: 'Col.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '1 Thessalonians', abbreviation: '1 Thes.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '2 Thessalonians', abbreviation: '2 Thes.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '1 Timothy', abbreviation: '1 Tim.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '2 Timothy', abbreviation: '2 Tim.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Titus', abbreviation: 'Tit.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Philemon', abbreviation: 'Phile.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Hebrews', abbreviation: 'Heb.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'James', abbreviation: 'Jas.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '1 Peter', abbreviation: '1 Pet.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '2 Peter', abbreviation: '2 Pet.', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '1 John', abbreviation: '1 John', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '2 John', abbreviation: '2 John', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: '3 John', abbreviation: '3 John', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Jude', abbreviation: 'Jude', testament: 'New Testament'))
            bookCrudRepository.save(new Book(name: 'Revelation', abbreviation: 'Rev.', testament: 'New Testament'))

        }
    }

    def populateMinisters() {
        if (ministerRepository.findAll().size() == 0) {

        }
    }
}
