package neo.script.gorm.general.graphql

import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters
import graphql.schema.DataFetcher
import neo.script.gorm.general.service.TokenService
import neo.script.gorm.graphql.security.DomainAuthorization
import org.grails.gorm.graphql.fetcher.GraphQLDataFetcherType
import org.grails.gorm.graphql.fetcher.interceptor.InterceptingDataFetcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile('test,prod')
@Component
class DomainAuthorizationImpl implements DomainAuthorization {

    static
    final MUTATION_TYPE_LIST = [GraphQLDataFetcherType.CREATE, GraphQLDataFetcherType.DELETE, GraphQLDataFetcherType.UPDATE]
    static final SKIP_TOKEN_OPERATION = ['logout', 'login']
    @Autowired
    TokenService tokenService

    boolean isAuthorized(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters, String token) {
        //InterceptingDataFetcher为gorm根据domain类自动生成的DataFetcher
        //如果要校验其它操作，需优化这里的处理
        if (InterceptingDataFetcher.isAssignableFrom(dataFetcher.class)) {
            Class clazz;
            GraphQLDataFetcherType fetcherType;
            InterceptingDataFetcher.declaredFields.each {
                it.setAccessible(true)
                switch (it.name) {
                    case 'clazz':
                        clazz = it.get(dataFetcher);
                        break;
                    case 'fetcherType':
                        fetcherType = it.get(dataFetcher);
                        break;
                    default:
                        break;
                }
            };
            return SKIP_TOKEN_OPERATION.contains(parameters.field.name) ||
                    (token && tokenService.validateToken(token).success)
        }
        return true
    }
}