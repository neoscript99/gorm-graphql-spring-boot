package neo.script.gorm.graphql.demo.graphql.mapping

import neo.script.gorm.graphql.demo.domain.Speaker
import neo.script.gorm.graphql.entity.GraphQLMappingFlag
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping
import org.springframework.stereotype.Component

import java.time.LocalDate
import java.time.Period

@GraphQLMappingFlag(Speaker)
@Component
class SpeakerGraphqlMapping extends GraphQLMapping {
    SpeakerGraphqlMapping() {
        property 'lastName', order: 1 //<1>
        property 'firstName', order: 2
        property 'email', order: 3

        property 'name', deprecationReason: 'To be removed August 1st, 2018' //<3>

        property('bio') { //<4>
            order 4
            dataFetcher { Speaker speaker ->
                speaker.bio ?: "No biography provided"
            }
        }

        add('age', Integer) { //<5>
            dataFetcher { Speaker speaker ->
                Period.between(speaker.birthday, LocalDate.now()).years
            }
            input false
        }
    }
}
