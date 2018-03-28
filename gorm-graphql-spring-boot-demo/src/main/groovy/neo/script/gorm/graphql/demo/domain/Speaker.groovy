package neo.script.gorm.graphql.demo.domain

import grails.gorm.annotation.Entity
import neo.script.gorm.data.initializer.initialize.InitializeDomian
import neo.script.gorm.graphql.demo.mapping.SpeakerMapping
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

import java.time.LocalDate
import java.time.Period

@Entity
@InitializeDomian
class Speaker {
    String id
    String firstName
    String lastName
    LocalDate birthday = LocalDate.of(2000, 1, 1)
    String email
    String bio
    String name

    static hasMany = [talks: Talk]

    static graphql = new SpeakerMapping()

    static constraints = {
        email nullable: true, email: true
        bio nullable: true, maxSize: 128
        birthday nullable: true
    }

    static mapping = {
        bio type: 'text'
        name formula: 'concat(FIRST_NAME,\' \',LAST_NAME)'
        talks sort: 'id'
    }

    static Speaker JEFF = new Speaker(firstName: 'Jeff', lastName: 'Brown', birthday: LocalDate.of(1975, 11, 15), email: 'brownj@objectcomputing.com', bio: 'Jeff Brown is an author and tech lead at OCI')
    static Speaker GRAEME = new Speaker(firstName: 'Graeme', lastName: 'Rocher', birthday: LocalDate.of(1978, 1, 1), email: 'rocherg@objectcomputing.com', bio: 'Graeme Rocher is the tech lead for the Grails project')
    static Speaker ZAK = new Speaker(firstName: 'Zachary', lastName: 'Klein', birthday: LocalDate.of(1989, 2, 23), email: 'kleinz@objectcomputing.com')
    static initList = [JEFF, GRAEME, ZAK]
}