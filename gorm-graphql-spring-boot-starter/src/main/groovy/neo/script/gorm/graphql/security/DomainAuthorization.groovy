package neo.script.gorm.graphql.security

interface DomainAuthorization {
    /**
     * 根据token，domain和是否写操作进行签权
     * @param stateMap
     * @param domain
     * @param isMutation
     * @return
     */
    boolean authorization(String token, Class domain, boolean isMutation)
}
