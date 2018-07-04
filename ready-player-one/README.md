# 开发环境启动
```bash
./gradlew ready-player-one:bootRun
```

# [数据库控制台](http://localhost:8080/h2-console/login.jsp?jsessionid=c5628a6f1c447ab92c815403e6be0454)
- url - *jdbc:h2:mem:devDb*
- user - *sa*
- password - 空

# [Graphql控制台](http://localhost:8080/graphiql)

# Graphql控制台操作步骤
1. 登录获取token
    ```
    mutation login {
      login(username: "admin", password: "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918") {
        success
        error
        token
      }
    }
    ```
1. 复制第一步返回的token
1. 进行普通的数据操作，如：
    ```
    query list {
      userList (criteria:"{max:11}"){
        results {
          name
          account
        }
        totalCount
      }
    }
    ```
1. 需带入token，在网页中下方的**Query Variables**中输入：
    ```
    {
      "token": "ffc56c6f-867f-440c-b693-ccd99cb4435e"
    }
    ``` 