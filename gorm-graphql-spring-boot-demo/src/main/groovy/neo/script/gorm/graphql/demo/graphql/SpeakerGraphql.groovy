package neo.script.gorm.graphql.demo.graphql

import neo.script.gorm.graphql.demo.domain.Speaker
import neo.script.gorm.graphql.fetcher.GeneralQueryDataFetcher
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping

import java.time.LocalDate
import java.time.Period

class SpeakerGraphql extends GraphQLMapping {
    SpeakerGraphql() {
        operations.list.paginate(true)

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
        speakerQuery();
    }

    def speakerQuery() {
        query('speakerQuery', pagedResult(Speaker)) {
            argument('criteria',String)
            dataFetcher(new GeneralQueryDataFetcher(Speaker.gormPersistentEntity))
        }
    }
}
