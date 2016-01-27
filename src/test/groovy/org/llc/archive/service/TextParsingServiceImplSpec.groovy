package org.llc.archive.service

import org.llc.archive.rest.domain.Minister
import org.llc.archive.rest.repository.MinisterRepository
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Steven McAdams on 4/25/15.
 */
class TextParsingServiceImplSpec extends Specification {

    def ministerRepository = Mock(MinisterRepository)
    TextParsingService textParsingService

    void setup() {
        textParsingService = new TextParsingServiceImpl(
                ministerRepository: ministerRepository,
                nicknameFixer: new NicknameFixerImpl()
        )

        ministerRepository.findAll() >> { v ->
            [
                    new Minister(firstName: 'Philip', lastName: 'Alajoki'),
                    new Minister(firstName: 'David', lastName: 'Anderson'),
                    new Minister(firstName: 'David', lastName: 'Anderson'),
                    new Minister(firstName: 'Todd', lastName: 'Anderson'),
                    new Minister(firstName: 'Jon', lastName: 'Bloomquist'),
                    new Minister(firstName: 'David', lastName: 'Edwards'),
                    new Minister(firstName: 'Andrew', lastName: 'Forstie'),
                    new Minister(firstName: 'James', lastName: 'Frantti'),
                    new Minister(firstName: 'Randall', lastName: 'Haapala'),
                    new Minister(firstName: 'Robert', lastName: 'Haapala'),
                    new Minister(firstName: 'Paul', lastName: 'Haataja'),
                    new Minister(firstName: 'Steven', lastName: 'Haataja'),
                    new Minister(firstName: 'Jouko', lastName: 'Haapsaari'),
                    new Minister(firstName: 'Arthur', lastName: 'Harju'),
                    new Minister(firstName: 'Randall', lastName: 'Herrala'),
                    new Minister(firstName: 'Randy', lastName: 'Hillukka'),
                    new Minister(firstName: 'Jeremy', lastName: 'Honga'),
                    new Minister(firstName: 'Ronald', lastName: 'Honga'),
                    new Minister(firstName: 'Paul', lastName: 'Honkala'),
                    new Minister(firstName: 'Andrew', lastName: 'Hotari'),
                    new Minister(firstName: 'Mauri', lastName: 'Hotari'),
                    new Minister(firstName: 'Petri', lastName: 'Hotari'),
                    new Minister(firstName: 'Thomas', lastName: 'Huhta'),
                    new Minister(firstName: 'Phillip', lastName: 'Huhta'),
                    new Minister(firstName: 'Hannu', lastName: 'Janhunen'),
                    new Minister(firstName: 'Brian', lastName: 'Johnson'),
                    new Minister(firstName: 'Dale', lastName: 'Johnson'),
                    new Minister(firstName: 'Daniel', lastName: 'Jurmu'),
                    new Minister(firstName: 'Eric', lastName: 'Jurmu'),
                    new Minister(firstName: 'James', lastName: 'Jurmu'),
                    new Minister(firstName: 'Steven', lastName: 'Kallinen'),
                    new Minister(firstName: 'Melvin', lastName: 'Kallio'),
                    new Minister(firstName: 'Wayne', lastName: 'Kallio'),
                    new Minister(firstName: 'James', lastName: 'Keplinger'),
                    new Minister(firstName: 'Tommi', lastName: 'Kinnunen'),
                    new Minister(firstName: 'George', lastName: 'Koivukangas'),
                    new Minister(firstName: 'Craig', lastName: 'Kumpula'),
                    new Minister(firstName: 'Eugene', lastName: 'Kumpula'),
                    new Minister(firstName: 'Michael', lastName: 'Kumpula'),
                    new Minister(firstName: 'Reino', lastName: 'Kuoppala'),
                    new Minister(firstName: 'Michael', lastName: 'Kuopus'),
                    new Minister(firstName: 'Peter', lastName: 'Kuopus'),
                    new Minister(firstName: 'Donald', lastName: 'Lahti'),
                    new Minister(firstName: 'Steven', lastName: 'Laiho'),
                    new Minister(firstName: 'Darwin', lastName: 'Lake'),
                    new Minister(firstName: 'Walter', lastName: 'Lampi'),
                    new Minister(firstName: 'Mark', lastName: 'Lee'),
                    new Minister(firstName: 'John', lastName: 'Lehtola'),
                    new Minister(firstName: 'Joseph', lastName: 'Lehtola'),
                    new Minister(firstName: 'Peter', lastName: 'Lever'),
                    new Minister(firstName: 'Markus', lastName: 'Lohi'),
                    new Minister(firstName: 'Blaine', lastName: 'Maki'),
                    new Minister(firstName: 'Eric', lastName: 'Mattila'),
                    new Minister(firstName: 'James', lastName: 'Moll'),
                    new Minister(firstName: 'Keith', lastName: 'Moll'),
                    new Minister(firstName: 'Nathan', lastName: 'Muhonen'),
                    new Minister(firstName: 'Lauri', lastName: 'Nevala'),
                    new Minister(firstName: 'Paul', lastName: 'Nevala'),
                    new Minister(firstName: 'Richard', lastName: 'Nevala'),
                    new Minister(firstName: 'David', lastName: 'Niemi'),
                    new Minister(firstName: 'Rodney', lastName: 'Nikula'),
                    new Minister(firstName: 'Timothy', lastName: 'Niskanen'),
                    new Minister(firstName: 'David', lastName: 'Ojala'),
                    new Minister(firstName: 'Matthew', lastName: 'Ojala'),
                    new Minister(firstName: 'Joseph', lastName: 'Ojalehto'),
                    new Minister(firstName: 'Karl', lastName: 'Ojalehto'),
                    new Minister(firstName: 'Daniel', lastName: 'Parks'),
                    new Minister(firstName: 'Howard', lastName: 'Parks'),
                    new Minister(firstName: 'Mikko', lastName: 'Pasanen'),
                    new Minister(firstName: 'Jari', lastName: 'Pigg'),
                    new Minister(firstName: 'Allen', lastName: 'Pirness'),
                    new Minister(firstName: 'Arvin', lastName: 'Pirness'),
                    new Minister(firstName: 'Martin', lastName: 'Pirness'),
                    new Minister(firstName: 'Roger', lastName: 'Plough'),
                    new Minister(firstName: 'Thomas', lastName: 'Prophet'),
                    new Minister(firstName: 'Martin', lastName: 'Pylvainen'),
                    new Minister(firstName: 'Daniel', lastName: 'Rintamaki'),
                    new Minister(firstName: 'Michael', lastName: 'Riutta'),
                    new Minister(firstName: 'Russell', lastName: 'Roiko'),
                    new Minister(firstName: 'Samuel', lastName: 'Roiko'),
                    new Minister(firstName: 'Kevin', lastName: 'Ruonavaara'),
                    new Minister(firstName: 'Quentin', lastName: 'Ruonavaara'),
                    new Minister(firstName: 'Terry', lastName: 'Ruonavaara'),
                    new Minister(firstName: 'Travis', lastName: 'Ruonavaara'),
                    new Minister(firstName: 'Raimo', lastName: 'Savolainen'),
                    new Minister(firstName: 'Arthur', lastName: 'Simonson'),
                    new Minister(firstName: 'Bryan', lastName: 'Simonson'),
                    new Minister(firstName: 'Carey', lastName: 'Simonson'),
                    new Minister(firstName: 'Curtis', lastName: 'Simonson'),
                    new Minister(firstName: 'Eric', lastName: 'Simonson'),
                    new Minister(firstName: 'Martin', lastName: 'Simonson'),
                    new Minister(firstName: 'Richard', lastName: 'Simonson'),
                    new Minister(firstName: 'Ronald', lastName: 'Simonson'),
                    new Minister(firstName: 'Scott', lastName: 'Simonson'),
                    new Minister(firstName: 'Mark', lastName: 'Smith'),
                    new Minister(firstName: 'Rodrick', lastName: 'Sorvala'),
                    new Minister(firstName: 'Rory', lastName: 'Sorvala'),
                    new Minister(firstName: 'Swen', lastName: 'Sorvala'),
                    new Minister(firstName: 'John', lastName: 'Stewart'),
                    new Minister(firstName: 'Thomas', lastName: 'Stewart'),
                    new Minister(firstName: 'William', lastName: 'Sturos'),
                    new Minister(firstName: 'Olli', lastName: 'Vänskä'),
                    new Minister(firstName: 'Benjamin', lastName: 'Waaraniemi'),
                    new Minister(firstName: 'Keith', lastName: 'Waaraniemi'),
                    new Minister(firstName: 'Neil', lastName: 'Waaraniemi'),
                    new Minister(firstName: 'Ray', lastName: 'Waaraniemi'),
                    new Minister(firstName: 'Jari', lastName: 'Warwaruk'),
                    new Minister(firstName: 'Marvin', lastName: 'Wittenberg'),
                    new Minister(firstName: 'Martin', lastName: 'Wuollet'),
                    new Minister(firstName: 'Kenneth', lastName: 'Wuollet'),
                    new Minister(firstName: 'Troy', lastName: 'Wuollet'),
                    new Minister(firstName: 'Clayton', lastName: 'Ylioja'),
                    new Minister(firstName: 'Stanley', lastName: 'Ylioja'),
            ]
        }
        ministerRepository.findByLastName(_) >> { v ->
            return ministerRepository.findAll().findAll { m -> m.lastName == v }
        }
        ministerRepository.findByFirstNameStartingWithAndLastNameLike(_ as String, _ as String) >> { String firstName, String lastName ->
            return ministerRepository.findAll().findAll { m ->
                m.lastName == lastName && m.firstName.startsWith(firstName)
            }
        }
    }

    @Unroll
    def "parse filename #mp3File"() {
        when: "filelocation is parsed"
        def filename = textParsingService.parseFilename(mp3Directory, mp3File)

        then: "base mp3 directory is stripped"
        filename == parsedFilename

        where:
        mp3Directory                      | mp3File                       || parsedFilename
        'C:\\example\\archives\\rockford' | '2015\\20150405_CKumpula.mp3' || '2015/20150405_CKumpula.mp3'

    }

    @Unroll
    def "Parse Minister #input"() {
        given:

        when: "minister is parsed"
        def parsedMinister = textParsingService.parseMinister(input)

        then: "minister is spelled correctly"
        parsedMinister == ministerName

        where:
        input                                   || ministerName
        ''                                      || ''
        'Ahti Riihimaki'                        || 'Ahti Riihimaki'
        'Annto Paananen'                        || 'Annto Paananen'
        'Arno Haho'                             || 'Arno Haho'
        'Art Simonson'                          || 'Arthur Simonson'
        'Arthur Harju'                          || 'Arthur Harju'
        'Arthur Simonson'                       || 'Arthur Simonson'
        'Bob Haapala'                           || 'Robert Haapala'
        'Brian Johnson-Friendship Presentation' || 'Brian Johnson-friendship Presentation'
        'Bryan Simonson'                        || 'Bryan Simonson'
        'craig kumpala'                         || 'Craig Kumpula'
        'Craig Kumpula'                         || 'Craig Kumpula'
        'Curt Simonson'                         || 'Curtis Simonson'
        'Dale Johnson'                          || 'Dale Johnson'
        'Dan Parks'                             || 'Daniel Parks'
        'Dave Anderson'                         || 'David Anderson'
        'Dave Ojala'                            || 'David Ojala'
        'David Ojala'                           || 'David Ojala'
        'Eric Jurmu'                            || 'Eric Jurmu'
        'George Koivukangas'                    || 'George Koivukangas'
        'Howard Parks'                          || 'Howard Parks'
        'James Frantti'                         || 'James Frantti'
        'Jari Warwaruk'                         || 'Jari Warwaruk'
        'Jim Frantti'                           || 'James Frantti'
        'Jim Jurmu'                             || 'James Jurmu'
        'Jim Keplinger'                         || 'James Keplinger'
        'Jim Moll'                              || 'James Moll'
        'Joe Lehtola'                           || 'Joseph Lehtola'
        'John Lehtola'                          || 'John Lehtola'
        'Jon Bloomquist'                        || 'Jon Bloomquist'
        'Joseph Lehtola'                        || 'Joseph Lehtola'
        'Joukko Haapsaari'                      || 'Jouko Haapsaari'
        'Jouko Haapsaari'                       || 'Jouko Haapsaari'
        'Keijo Nissila'                         || 'Keijo Nissila'
        'Keith Moll'                            || 'Keith Moll'
        'Keith Waaraniemi'                      || 'Keith Waaraniemi'
        'Ken Wuollet'                           || 'Kenneth Wuollet'
        'Lauri Nevala'                          || 'Lauri Nevala'
        'M Heikkinen'                           || 'M Heikkinen'
        'Marcus Lohi'                           || 'Markus Lohi'
        'Mark Lee'                              || 'Mark Lee'
        'Mark Smith'                            || 'Mark Smith'
        'Markus Lohi'                           || 'Markus Lohi'
        'Mel Kallio'                            || 'Melvin Kallio'
        'Mike Riutta'                           || 'Michael Riutta'
        'Mikko Pasanen'                         || 'Mikko Pasanen'
        'Mikko Passanen'                        || 'Mikko Pasanen'
        'Nate Muhonen'                          || 'Nathan Muhonen'
        'Nathan Muhonen'                        || 'Nathan Muhonen'
        'Nathen Muhonen'                        || 'Nathan Muhonen'
        'Pannanen'                              || 'Pannanen'
        'Paul Honkala'                          || 'Paul Honkala'
        'Phil Huhta'                            || 'Phillip Huhta'
        'R Waaraniemi'                          || 'Ray Waaraniemi'
        'Randy Haapala'                         || 'Randall Haapala'
        'Randy Herrala 6:30 service'            || 'Randy Herrala 6:30 Service'
        'Randy Herrala'                         || 'Randall Herrala'
        'Ray Waaraniemi'                        || 'Ray Waaraniemi'
        'Reijo Peura'                           || 'Reijo Peura'
        'Richard Nevala'                        || 'Richard Nevala'
        'Rick Nevala'                           || 'Richard Nevala'
        'Rocky Sorvala'                         || 'Rodrick Sorvala'
        'Rod Nikula'                            || 'Rodney Nikula'
        'Rodney Nikula'                         || 'Rodney Nikula'
        'Rodrick Sorvala'                       || 'Rodrick Sorvala'
        'Ron Honga'                             || 'Ronald Honga'
        'Ron Simonson'                          || 'Ronald Simonson'
        'Rory Sorvala'                          || 'Rory Sorvala'
        'Rory Sorvola'                          || 'Rory Sorvala'
        'Sam Roiko'                             || 'Samuel Roiko'
        'Stan Ylioja'                           || 'Stanley Ylioja'
        'Steven Kallinen'                       || 'Steven Kallinen'
        'Todd Anderson'                         || 'Todd Anderson'
        'Tommi Kinnunen'                        || 'Tommi Kinnunen'
        'Wayne Kallio'                          || 'Wayne Kallio'
    }

    @Unroll
    def "Parse Minister From Filename #filename"() {
        when: "minister name is parsed from filename"
        def parsedMinister = textParsingService.parseMinisterFromFilename(filename)

        then: "minister name is returned"
        parsedMinister == ministerName

        where:
        filename                                                  || ministerName
        '2013/20130101_RNikula.mp3'                               || 'Rodney Nikula'
        '2013/20130106_RHerrala.mp3'                              || 'Randall Herrala'
        '2013/20130106_RNikula.mp3'                               || 'Rodney Nikula'
        '2013/20130113RNevala.mp3'                                || ''
        '2013/20130203_MLee.mp3'                                  || 'Mark Lee'
        '2013/20130623_dojalapm.mp3'                              || 'David Ojala'
        '2013/20130630_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2013/20130630_MLee.mp3'                                  || 'Mark Lee'
        '2014/0112_DJohnson0700PM.mp3'                            || 'Dale Johnson'
        '2014/0112_RSorvala1030.mp3'                              || ''
        '2014/0119_EJurmu700PM.mp3'                               || 'Eric Jurmu'
        '2014/0119_WayneKallio.mp3'                               || 'Wayne Kallio'
        '2014/0126_EJurmu1030AM.mp3'                              || 'E Jurmu1030am'
        '2014/0202_EJurmu1030AM.mp3'                              || 'E Jurmu1030am'
        '2014/0216_WKallio.mp3'                                   || 'Wayne Kallio'
        '2014/030214_Keplinger.mp3'                               || ''
        '2014/0309_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0309_KWuollet.mp3'                                  || 'Kenneth Wuollet'
        '2014/0316_DJohnson.mp3'                                  || 'Dale Johnson'
        '2014/0316_RSorvala.mp3'                                  || ''
        '2014/0323_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0406_MRiutta.mp3'                                   || 'Michael Riutta'
        '2014/0417_RWaaraniemi.mp3'                               || 'Ray Waaraniemi'
        '2014/0418_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0420_DJohnson.mp3'                                  || 'Dale Johnson'
        '2014/0424_MHeikkinen.mp3'                                || ''
        '2014/0427_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0504_JFrantti.mp3'                                  || 'James Frantti'
        '2014/0511__1900EJurmu.mp3'                               || ''
        '2014/0511_KMoll.mp3'                                     || 'Keith Moll'
        '2014/0518__1030RSorvala.mp3'                             || ''
        '2014/0525_DJohnson.mp3'                                  || 'Dale Johnson'
        '2014/0525_MRiutta.mp3'                                   || 'Michael Riutta'
        '2014/0601_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0622_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0622_MRiutta.mp3'                                   || 'Michael Riutta'
        '2014/0629_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2014/0629_RSorvala.mp3'                                  || ''
        '2014/0706_RSorvala1000AM.mp3'                            || ''
        '2014/0713_MRiutta1000AM.mp3'                             || 'Michael Riutta'
        '2014/0720_MRiutta.mp3'                                   || 'Michael Riutta'
        '2014/0727_DJohnson.mp3'                                  || 'Dale Johnson'
        '2014/0810_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0810_MSmith.mp3'                                    || 'Mark Smith'
        '2014/0817_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2014/0817_MRiutta.mp3'                                   || 'Michael Riutta'
        '2014/0824_MRiutta.mp3'                                   || 'Michael Riutta'
        '2014/0831_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0831_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2014/0914_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/0914_RSorvala.mp3'                                  || ''
        '2014/0921_RSorvala.mp3'                                  || ''
        '2014/0928_DJohnson.mp3'                                  || 'Dale Johnson'
        '2014/0928_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/1005_MPassanen.mp3'                                 || 'Mikko Pasanen'
        '2014/1012_HParks.mp3'                                    || 'Howard Parks'
        '2014/1012_HParks0700PM.mp3'                              || 'Howard Parks'
        '2014/1016_RPeura.mp3'                                    || ''
        '2014/1019_DJohnson700.mp3'                               || 'Dale Johnson'
        '2014/1019_MRiutta.mp3'                                   || 'Michael Riutta'
        '2014/1026_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/1031_DJohnson.mp3'                                  || 'Dale Johnson'
        '2014/1102_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/1109_NMuhonen.mp3'                                  || 'Nathan Muhonen'
        '2014/1116_MPassanen1030.mp3'                             || 'Mikko Pasanen'
        '2014/1116_RSorvala700.mp3'                               || ''
        '2014/1123_EJurmu700.mp3'                                 || 'Eric Jurmu'
        '2014/1123_MRiutta1030.mp3'                               || 'Michael Riutta'
        '2014/1127_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/1130_RSorvala.mp3'                                  || ''
        '2014/1205_CKumpula.mp3'                                  || 'Craig Kumpula'
        '2014/1207_PHonkala.mp3'                                  || 'Paul Honkala'
        '2014/1214_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/1221_MRiutta_1030AM.mp3'                            || 'Michael Riutta'
        '2014/1224_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2014/1225_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2014/1228_DJohnson.mp3'                                  || 'Dale Johnson'
        '2014/1228_JFrantti.mp3'                                  || 'James Frantti'
        '2014/1231_RSorvala.mp3'                                  || ''
        '2014/20140118_Friendship_Presentation_Brian_Johnson.mp3' || ''
        '2014/20140119_JLehtola.mp3'                              || ''
        '2014/20140126_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140126_RHerrala.mp3'                              || 'Randall Herrala'
        '2014/20140202_RNikula.mp3'                               || 'Rodney Nikula'
        '2014/20140211_KeijoNissila.mp3'                          || ''
        '2014/20140216_CKumpula.mp3'                              || 'Craig Kumpula'
        '2014/20140216_KWaaraniemi.mp3'                           || 'Keith Waaraniemi'
        '2014/20140223_RNevela.mp3'                               || 'Richard Nevala'
        '2014/20140314_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140314_SRoiko.mp3'                                || 'Samuel Roiko'
        '2014/20140316_HParks.mp3'                                || 'Howard Parks'
        '2014/20140316_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140316_SRoiko.mp3'                                || 'Samuel Roiko'
        '2014/20140323_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140330_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140330_LNevala.mp3'                               || 'Lauri Nevala'
        '2014/20140413_GKoivukangas.mp3'                          || 'George Koivukangas'
        '2014/20140417_CKumpula.mp3'                              || 'Craig Kumpula'
        '2014/20140420_RSorvala.mp3'                              || ''
        '2014/20140427_NMuhonen.mp3'                              || 'Nathan Muhonen'
        '2014/20140504_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140511_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140511_RNikula.mp3'                               || 'Rodney Nikula'
        '2014/20140525_Rnevala.mp3'                               || 'Richard Nevala'
        '2014/20140601_NMuhonen.mp3'                              || 'Nathan Muhonen'
        '2014/20140615_CSimonson.mp3'                             || ''
        '2014/20140615_JAiras.mp3'                                || ''
        '2014/20140620_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140621_RHongaPresentation.mp3'                    || ''
        '2014/20140622_RHonga.mp3'                                || 'Ronald Honga'
        '2014/20140629_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20140629_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2014/20140709_RHerrala.mp3'                              || 'Randall Herrala'
        '2014/20140803_RSorvala.mp3'                              || ''
        '2014/20140803_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2014/20140810_RNevala.mp3'                               || 'Richard Nevala'
        '2014/20140831_JLehtola.mp3'                              || ''
        '2014/20140914_CKumpula.mp3'                              || 'Craig Kumpula'
        '2014/20140921_RNikula.mp3'                               || 'Rodney Nikula'
        '2014/20140928_NMuhonen.mp3'                              || 'Nathan Muhonen'
        '2014/20141012_CKumpula.mp3'                              || 'Craig Kumpula'
        '2014/20141012_DParks.mp3'                                || 'Daniel Parks'
        '2014/20141019_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20141019_RSimonson.mp3'                             || ''
        '2014/20141021_RPeura.mp3'                                || ''
        '2014/20141027_RNevala.mp3'                               || 'Richard Nevala'
        '2014/20141031_RNevala.mp3'                               || 'Richard Nevala'
        '2014/20141109_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20141123_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20141123_MLee.mp3'                                  || 'Mark Lee'
        '2014/20141130_RSorvala.mp3'                              || ''
        '2014/20141207_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2014/20141221_CKumpala.mp3'                              || 'Craig Kumpula'
        '2014/20141221_RNevala.mp3'                               || 'Richard Nevala'
        '2014/20141224_NMuhonen.mp3'                              || 'Nathan Muhonen'
        '2014/20141225_RHerrala.mp3'                              || 'Randall Herrala'
        '2014/20141227_RNevala.mp3'                               || 'Richard Nevala'
        '2014/20141231_RNevala.mp3'                               || 'Richard Nevala'
        '2014/8314_RHonga.mp3'                                    || 'Ronald Honga'
        '2015/0104_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2015/0111_MRiutta_1030.mp3'                              || 'Michael Riutta'
        '2015/0111_RSorvala.mp3'                                  || ''
        '2015/0117_ASimonson.mp3'                                 || 'Arthur Simonson'
        '2015/0125_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2015/0125_PHuhta.mp3'                                    || 'Phillip Huhta'
        '2015/0201_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/0215_WKallio.mp3'                                   || 'Wayne Kallio'
        '2015/0301_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2015/0308_EJurmu_1900.mp3'                               || 'Eric Jurmu'
        '2015/0308_JMoll.mp3'                                     || 'James Moll'
        '2015/0315_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/0322_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2015/0322_RSorvala.mp3'                                  || ''
        '2015/0329_WKallio.mp3'                                   || 'Wayne Kallio'
        '2015/0402_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2015/0403_RNikula.mp3'                                   || 'Rodney Nikula'
        '2015/0405_TKinnunen.mp3'                                 || 'Tommi Kinnunen'
        '2015/0419_WKalio.mp3'                                    || 'Wayne Kallio'
        '2015/0426_EJurmu.mp3'                                    || 'Eric Jurmu'
        '2015/0517_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/0531_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/05_10MSmith.mp3'                                    || 'Mark Smith'
        '2015/0607_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2015/0614_PHuhta.mp3'                                    || 'Phillip Huhta'
        '2015/0621_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/0628_Ejurmu.mp3'                                    || 'Eric Jurmu'
        '2015/0628_MPassanen.1.mp3'                               || 'Mikko Pasanen'
        '2015/0712_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/0712_RSorvala.mp3'                                  || ''
        '2015/0719_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2015/0726_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/0802_RSorvola.mp3'                                  || ''
        '2015/0823_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/0913_KMoll.mp3'                                     || 'Keith Moll'
        '2015/0920 RSorvala.mp3'                                  || ''
        '2015/0920_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2015/0927_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/1004_RSorvala.mp3'                                  || ''
        '2015/1011_DOjala.mp3'                                    || 'David Ojala'
        '2015/1011_DOjala2.mp3'                                   || 'David Ojala'
        '2015/1018_ARiihimaki.mp3'                                || ''
        '2015/1018_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2015/1031_JFranti.mp3'                                   || 'James Frantti'
        '2015/1101_GKoivukangas.mp3'                              || 'George Koivukangas'
        '2015/1108_SRoiko.mp3'                                    || 'Samuel Roiko'
        '2015/1122_DJohnson.mp3'                                  || 'Dale Johnson'
        '2015/1129_RSorvala.mp3'                                  || ''
        '2015/1205_TAnderson.mp3'                                 || 'Todd Anderson'
        '2015/1206_MLohi.mp3'                                     || 'Markus Lohi'
        '2015/1206_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2015/1213_MPasanen.mp3'                                  || 'Mikko Pasanen'
        '2015/20121225_RHerrala.mp3'                              || 'Randall Herrala'
        '2015/20150101_RNikula.mp3'                               || 'Rodney Nikula'
        '2015/20150104_MLee.mp3'                                  || 'Mark Lee'
        '2015/20150115_RHaapala.mp3'                              || ''
        '2015/20150115_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20150118_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150130_APaananen.mp3'                             || ''
        '2015/20150201_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150201_RNikula.mp3'                               || 'Rodney Nikula'
        '2015/20150208_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20150213_APaananen.mp3'                             || ''
        '2015/20150301_RSorvala.mp3'                              || ''
        '2015/20150315_JBloomquist.mp3'                           || 'Jon Bloomquist'
        '2015/20150315_RNevala.mp3'                               || 'Richard Nevala'
        '2015/20150319_RHerrala.mp3'                              || 'Randall Herrala'
        '2015/20150320_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150320_MLohi.mp3'                                 || 'Markus Lohi'
        '2015/20150321JLehtola.mp3'                               || ''
        '2015/20150322_JHaapsari.mp3'                             || 'Jouko Haapsaari'
        '2015/20150322_JJurmu.mp3'                                || 'James Jurmu'
        '2015/20150322_LNevala.mp3'                               || 'Lauri Nevala'
        '2015/20150322_MLee.mp3'                                  || 'Mark Lee'
        '2015/20150322_MLohi.mp3'                                 || 'Markus Lohi'
        '2015/20150329_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150402_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20150403_MLee.mp3'                                  || 'Mark Lee'
        '2015/20150405_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20150410_NMuhonen.mp3'                              || 'Nathan Muhonen'
        '2015/20150412_JLehtola.mp3'                              || ''
        '2015/20150412_RNevala.mp3'                               || 'Richard Nevala'
        '2015/20150503_NMuhonen.mp3'                              || 'Nathan Muhonen'
        '2015/20150503_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20150510_RHerrala.mp3'                              || 'Randall Herrala'
        '2015/20150514_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20150517_JHaapsari.mp3'                             || 'Jouko Haapsaari'
        '2015/20150517_RNevala.mp3'                               || 'Richard Nevala'
        '2015/20150524_MLee.mp3'                                  || 'Mark Lee'
        '2015/20150524_RSovala.mp3'                               || ''
        '2015/20150531_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150607_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20150607_JLehtola.mp3'                              || ''
        '2015/20150614_RNevala.mp3'                               || 'Richard Nevala'
        '2015/20150614_RNikula.mp3'                               || 'Rodney Nikula'
        '2015/20150619_BSimonson.mp3'                             || 'Bryan Simonson'
        '2015/20150619_GKoivukangas.mp3'                          || 'George Koivukangas'
        '2015/20150619_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150620NMuhonen.mp3'                               || 'Nathan Muhonen'
        '2015/20150621_BSimonson,.mp3'                            || 'Bryan Simonson'
        '2015/20150621_GKoivukangas.mp3'                          || 'George Koivukangas'
        '2015/20150621_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150621_RNevala.mp3'                               || 'Richard Nevala'
        '2015/20150628_RodNikula.mp3'                             || 'Rodney Nikula'
        '2015/20150628_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20150712_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20150712_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150719_RNevala.mp3'                               || 'Richard Nevala'
        '2015/20150726_JLehtola.mp3'                              || ''
        '2015/20150726_RNevala.mp3'                               || 'Richard Nevala'
        '2015/20150802_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150802_JLehtola.mp3'                              || ''
        '2015/20150809_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150809_NMuhonen.mp3'                              || 'Nathan Muhonen'
        '2015/20150814_AHarju.mp3'                                || ''
        '2015/20150814_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150815_CSimonson.mp3'                             || ''
        '2015/20150816_AHarju.mp3'                                || ''
        '2015/20150816_EJurmu.mp3'                                || 'Eric Jurmu'
        '2015/20150816_SKallinen.mp3'                             || 'Steven Kallinen'
        '2015/20150823_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20150823_RSorvala.mp3'                              || ''
        '2015/20150830_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20150830_RNikula.mp3'                               || 'Rodney Nikula'
        '2015/20150906_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20150913_AHaho.mp3'                                 || ''
        '2015/20150920_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20150927_JLehtola.mp3'                              || ''
        '2015/20150927_MLee.mp3'                                  || 'Mark Lee'
        '2015/20151004_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20151011_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20151018_RNikula.mp3'                               || 'Rodney Nikula'
        '2015/20151025_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20151031_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20151101_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20151101_RNikula.mp3'                               || 'Rodney Nikula'
        '2015/20151108_JHapsaari.mp3'                             || 'Jouko Haapsaari'
        '2015/20151115_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20151115_KWaaraniemi.mp3'                           || 'Keith Waaraniemi'
        '2015/20151122_RHerrala.mp3'                              || 'Randall Herrala'
        '2015/20151129_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2015/20151205_JLehtola.mp3'                              || ''
        '2015/20151206_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20151213_JHaapsaari.mp3'                            || 'Jouko Haapsaari'
        '2015/20151220_EJurmu.mp3'                                || 'Eric Jurmu'
        '2015/20151220_RHerrala.mp3'                              || 'Randall Herrala'
        '2015/20151224_CKumpula.mp3'                              || 'Craig Kumpula'
        '2015/20151224_RNikula.mp3'                               || 'Rodney Nikula'
        '2015/20151225_EJurmu.mp3'                                || 'Eric Jurmu'
        '2015/20151227_MLee.mp3'                                  || 'Mark Lee'
        '2015/20151227_NMuhonen.mp3'                              || 'Nathan Muhonen'
        '20151115 Arthur Simonson.mp3'                            || 'Arthur Simonson'
        '20151115 Jari Warwaruk.mp3'                              || 'Jari Warwaruk'
        '20151129 Dave Anderson.mp3'                              || 'David Anderson'
        '20151129 Wayne Kallio.mp3'                               || 'Wayne Kallio'
        '20151206 Jari Warwaruk.mp3'                              || 'Jari Warwaruk'
        '20151213 Bob Haapala.mp3'                                || 'Bob Haapala'
        '20151224 Wayne Kallio.mp3'                               || 'Wayne Kallio'
        '20151225 Bob Haapala.mp3'                                || 'Robert Haapala'
        '20151227 Stan Ylioja.mp3'                                || 'Stanley Ylioja'
        '20151231 Mel Kallio.mp3'                                 || 'Melvin Kallio'
        '2016/0110_PHuhta.mp3'                                    || 'Phillip Huhta'
        '2016/0117MPasanen.mp3'                                   || 'Mikko Pasanen'
        '2016/20160101_RSorvala.mp3'                              || ''
        '2016/20160103_RNikula.mp3'                               || 'Rodney Nikula'
        '2016/20160103_RWaaraniemi.mp3'                           || 'Ray Waaraniemi'
        '2016/20160110_EJurmu.mp3'                                || 'Eric Jurmu'
        '2016/20160110_MLee.mp3'                                  || 'Mark Lee'
        '20160103 Jari Warwaruk.mp3'                              || 'Jari Warwaruk'
        '20160103 Stan Ylioja.mp3'                                || 'Stanley Ylioja'
        '20160110 Bob Haapala.mp3'                                || 'Robert Haapala'
    }

    @Unroll
    def "parse bible text #input"() {
        when: "bible text is parsed"
        def bibleText = textParsingService.parseBibleText(input)

        then: "bible text is returned"
        bibleText == output

        where:
        input         || output
        null          || ''
        ''            || ''
        'Acts 2:1-12' || 'Acts 2:1-12'

    }

    @Unroll
    def "Parse Time #input"() {
        when: "date + time parsed"
        def time = textParsingService.parseTime(input)

        then: "time is returned"
        time == output

        where:
        input                 || output
        ''                    || ''
        '01/01/2014'          || ''
        '01/01/2014 10:30'    || '10:30'
        '01/01/2014 10:30 am' || '10:30'
        '01/01/2014 10:30 pm' || '22:30'

    }

    @Unroll
    def "parse date (#inputDate, #fileLocation)"() {
        when: "date is parsed"
        def date = textParsingService.parseDate(inputDate, fileLocation)

        then: "date is returned"
        date == parsedDate

        where:
        inputDate           | fileLocation            || parsedDate
        ''                  | ''                      || ''
        ''                  | 'CKumpula_20150405'     || ''
        ''                  | '20150405_CKumpula.mp3' || '04/05/2015'
        '04/2015'           | '20150405_CKumpula.mp3' || '04/05/2015'
        '04/04/2015'        | '20150405_CKumpula.mp3' || '04/04/2015'
        ' 07/26/2015 10:30' | '20150405_CKumpula.mp3' || '07/26/2015'
        '3/16/14 2 pm'      | '20150405_CKumpula.mp3' || '03/16/2014'

    }

    @Unroll
    def "Format Date (#day, #month, #year)"() {
        when: "format date is called"
        def date = textParsingService.formatDate(day, month, year)

        then: "date is formatted as 'MM/dd/yyyy'"
        date == formattedDate

        where:
        day | month | year || formattedDate
        12  | 12    | 2014 || '12/12/2014'

    }

    @Unroll
    def "Parse Notes #input"() {
        when: 'notes are parsed'
        def notes = textParsingService.parseNotes(input);

        then: 'comments are returned'
        notes == output;

        where:
        input                   || output
        null                    || ''
        ''                      || ''
        'Christmas Eve Service' || 'Christmas Eve Service'

    }
}
