grails.gorm.default.mapping = {
    //cache true
    id generator: 'uuid'
    autoTimestamp true
    /* version用来做乐观锁，如果是并发修改控制比较松的管理系统，可以不用 */
    version true
}

//maxSize : 32在数据库中表现为字符长度，不是字节，如oracle VARCHAR2(32 CHAR)
grails.gorm.default.constraints = {
    '*'(maxSize: 32, blank: true)
}