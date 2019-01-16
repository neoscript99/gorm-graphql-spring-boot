import org.hibernate.cfg.ImprovedNamingStrategy

grails.gorm.default.mapping = {

    /**
     * 类名生成表名
     * 取最后一层包名作为表名前缀
     * 如：a.b.c.User -->  c_user
     * 本处delegate 为 org.grails.orm.hibernate.cfg.HibernateMappingBuilder,处理方法为evaluate
     */
    def names = className.split("\\.");
    table ImprovedNamingStrategy.addUnderscores(names.takeRight(2).join('_'))

    id generator: 'uuid2'
    autoTimestamp true
    tablePerHierarchy false
    //cache true
    /**
     *  version用来做乐观锁，如果是并发修改控制比较松的管理系统，可以不用
     *
     *  由于设置了cascade:"none"，部份在代码中被直接关联的字典表静态对象，需设置version = false
     *  否则会造成Hibernate错误：object references an unsaved transient instance
     *  如：list([eq: [['status', MeetingStatus.ENROLL]]])中的MeetingStatus.ENROLL
     *  @see neo.script.gorm.general.domain.sys.ParamType
     */
    version true
    fetch 'join'
    lazy false
    '*'(cascade:"none")
}

//maxSize : 32在数据库中表现为字符长度，不是字节，如oracle VARCHAR2(32 CHAR)
grails.gorm.default.constraints = {
    '*'(maxSize: 36, blank: false)
}

grails.gorm.failOnError = true
