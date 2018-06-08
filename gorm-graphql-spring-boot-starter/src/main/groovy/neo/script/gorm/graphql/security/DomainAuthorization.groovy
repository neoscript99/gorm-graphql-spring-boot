package neo.script.gorm.graphql.security

import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.schema.DataFetcher

interface DomainAuthorization {
    /**
     * 根据token，domain和是否写操作进行签权
     * @param stateMap
     * @param domain
     * @param isMutation
     * @return
     */
    boolean isAuthorized(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters, String token)
}
